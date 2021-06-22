import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dispatcher {
	public static void main(String[] args) throws InterruptedException {
		String path = "src\\";
		Thread t1 = new Thread(new FileHandler(path + "file1.txt"));
		Thread t2 = new Thread(new FileHandler(path + "file2.txt"));
		Thread t3 = new Thread(new FileHandler(path + "file3.txt"));
		t1.start();
		t2.start();
		t3.start();
		t1.join();
		t2.join();
		t3.join();

		FileHandler.outHashMap();
		FileHandler.outListSet();
	}
}

class FileHandler implements Runnable {
	static ConcurrentHashMap<String, Long> wordsAndCountRepetitions = new ConcurrentHashMap<>();
	static ConcurrentSkipListSet<String> wordsWithSameStartAndEnd = new ConcurrentSkipListSet<>();

	Pattern pattern = Pattern.compile("[A-Za-z]{2,}");
	String fileName;

	FileHandler(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void run() {
		this.readFile();
	}

	void readFile() {
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(new File(this.fileName)));
			String line = buffer.readLine();
			while (line != null) {
				findCountfWords(line);
				line = buffer.readLine();
			}
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void findCountfWords(String line) {
		Matcher matcher = pattern.matcher(line);
		while (matcher.find()) {
			String word = matcher.group().toLowerCase();
			wordsAndCountRepetitions.compute(word, (key, value) -> value == null ? 1 : ++value);
			if (word.charAt(0) == word.charAt(word.length() - 1)) {
				wordsWithSameStartAndEnd.add(word);
			}
		}
	}

 	public static void outHashMap() {
		int count = 0;
		int length = 0;
		String temp;
		line();
		System.out.printf("%63s\n", "Ñëîâà òà ê³ëüê³ñòü ¿õ ïîâòîð³â:");
		line();
		for (Map.Entry<String, Long> element : wordsAndCountRepetitions.entrySet()) {
			temp = element.getKey() + "=" + element.getValue();
			length += temp.length() + 2;
			if (length > 85) {
				length = 0;
				System.out.println();
			}
			if (count == wordsAndCountRepetitions.size() - 1) {
				System.out.print(temp + ".\n");
			}else {
				System.out.print(temp + ", ");
			}
			count++;
		}
		System.out.println();
	}

 	public static void outListSet() {
		int count = 0;
		int length = 0;
		line();
		System.out.printf("%73s\n","Ñëîâà, â ÿêèõ ïåðøà òà îñòàííÿ ë³òåðè ñï³âïàäàþòü:");
		line();
		for (String string : wordsWithSameStartAndEnd) {
			length += string.length() + 2;
			if (length > 90) {
				length = 0;
				System.out.println();
			}
			if (count == wordsWithSameStartAndEnd.size() - 1) {
				System.out.print(string + ".\n");
			} else {
				System.out.print(string + ", ");
			}
			count++;
		}
		System.out.println("\n");
	}
	private static void line() {
		for (int i = 0; i < 100; i++) {
			System.out.print("-");
		}
		System.out.println();
	}
}
