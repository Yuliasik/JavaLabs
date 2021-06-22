import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dispatcher {

	public static void main(String[] args) {

		String path = "src\\";
		String files[] = { "file1", "file2", "file3", "file4", "file5", "file6", "file7" };

		for (int i = 0; i < files.length; i++) {
			files[i] = path + files[i];
		}

		new FileHandler(files);
	}
}

class FileHandler {
	String files[];

	int numberWithCount = 0;
	int numberWithEdit = -1;

	boolean workFirst = true;
	boolean workSecond = false;

	ArrayList<Integer> counters = new ArrayList<>();

	FileHandler(String files[]) {
		this.files = files;
		new FirstThread(this);
		new SecondThread(this);
	}

	synchronized void countSpace(int n) {
		int localCount = 0;
		this.numberWithCount = n;

		while (this.workFirst == false) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (this.numberWithCount != 0) {
			this.workSecond = true;
			notify();
		}

		try {
			BufferedReader buffer = new BufferedReader(new FileReader(new File(this.files[n])));
			String line = buffer.readLine();

			while (line != null) {
				List<String> listSymbols = Arrays.asList(line.split(""));

				localCount = (int) listSymbols.stream().filter(s -> s.equals(" ")).count();

				line = buffer.readLine();
			}
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.counters.add(localCount);

		if (this.workSecond == false) {
			this.workSecond = true;
			notify();
		} else {
			this.workFirst = false;
		}

		System.out.println("        Потік 1 опрацював: " + this.files[n]);
	}

	// ---------------------------------------------------------------------------------------------------------

	synchronized void workWithText() {

		Pattern pattern = Pattern.compile("[A-Za-z]{2,}[.,!?)\\s\n\r]");
		HashSet<String> hashset = new HashSet<>();
		int localCount = 0;

		while (this.workSecond == false) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.numberWithEdit++;

		// Якщо другий потік хоче швидше взяти значення ніж перший його туди закинув -
		// чекаємо
		while (true) {
			try {
				localCount = this.counters.get(numberWithEdit);
				break;
			} catch (Exception ignored) {
			}
		}

		try {
			hashset.clear();

			File fileForReading = new File(this.files[this.numberWithEdit]);
			File fileForWritting = new File(this.files[this.numberWithEdit] + "_result");

			BufferedReader buffer = new BufferedReader(new FileReader(fileForReading));
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(fileForWritting), "utf-8"));

			String line = buffer.readLine();
			if (localCount % 2 == 0) {
				while (line != null) {
					Matcher matcher = pattern.matcher(line);
					while (matcher.find())
						hashset.add(matcher.group());

					for (String string : hashset)
						line = line.replace(" " + string,
								" " + string.substring(0, 1).toUpperCase() + string.substring(1));

					writer.write(line + "\n", 0, line.length() + 1);
					line = buffer.readLine();
				}
			} else {
				while (line != null) {
					Matcher matcher = pattern.matcher(line);
					while (matcher.find())
						hashset.add(matcher.group());

					for (String string : hashset) {
						int l = string.length() - 2;
						line = line.replace(string, string.substring(0, l) + string.substring(l).toUpperCase());
					}
					writer.write(line + "\n", 0, line.length() + 1);
					line = buffer.readLine();
				}
			}
			buffer.close();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(
				" ------ Потік 2 опрацював: " + this.files[this.numberWithEdit]);

		if (this.workFirst == false) {
			this.workFirst = true;
			notify();
		}

		this.workSecond = false;
	}
}

class FirstThread implements Runnable {
	FileHandler fileHandler;

	FirstThread(FileHandler fileHandler) {
		this.fileHandler = fileHandler;
		new Thread(this, "FirstThread").start();
	}

	public void run() {
		int i = 0;
		while (i < this.fileHandler.files.length) {
			this.fileHandler.countSpace(i++);
		}
	}
}

class SecondThread implements Runnable {
	FileHandler fileHandler;

	SecondThread(FileHandler fileHandler) {
		this.fileHandler = fileHandler;
		new Thread(this, "SecondThread").start();
	}

	public void run() {
		int i = 0;
		while (i < this.fileHandler.files.length - 1) {
			this.fileHandler.workWithText();
			i++;
		}
		// Останній виклик методу з другого потоку
		this.fileHandler.workSecond = true;
		this.fileHandler.workWithText();
	}
}
