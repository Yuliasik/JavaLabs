import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Map;

public class Dispatcher {

	public static void main(String[] args) {

		FileHandler fh = new FileHandler("src//dani.txt");

		String text = fh.firstLetterCapitalize();
		System.out.println("Початок кожного слова з великої букви:\n" + text);

		text = fh.sortAllByWordsLength();
		System.out.println("\nВсі слова за зростанням довжини:\n" + text);
		System.out.println("--------------------");

		text = fh.sortSentencesByWordsLength();
		System.out.println("\nВсі речення за зростанням кількості слів:\n" + text);
		System.out.println();

		List<Integer> numbers = Stream.generate(() -> (int) (20 - Math.random() * 40)).limit(20)
				.collect(Collectors.toList());
		System.out.println("Ціла колекція");
		numbers.forEach(s -> System.out.print(s + " "));

		List<Integer> negative = numbers.stream().sorted().filter((s) -> s < 0).collect(Collectors.toList());
		System.out.println("\nВід'ємні елементи:");
		negative.stream().forEach(s -> System.out.print(s + " "));

		List<Integer> positive = numbers.stream().sorted().filter((s) -> s > 0).collect(Collectors.toList());
		System.out.println("\nДодатні елементи:");
		positive.stream().forEach(s -> System.out.print(s + " "));
		System.out.println();

	}

}

class FileHandler {
	File file;
	String text = "";
	static String rezult = "";

	FileHandler(String name) {
		this.file = new File(name);
		this.readFile();
	}

	void readFile() {
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(file));
			String line = buffer.readLine();
			while (line != null) {
				this.text += line + "\n";
				line = buffer.readLine();
			}
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	String firstLetterCapitalize() {
		rezult = "";
		List<String> words = separateText(" ", this.text.replace("\n", ""));
		words.stream().map(s -> s.substring(0, 1).toUpperCase() + s.substring(1)).forEach(s -> rezult += s + " ");
		return rezult;
	}

	private List<String> separateText(String separator, String text) {
		Stream<String> streamTextToWords = Stream.of(text);
		List<String> words = streamTextToWords.map(s -> s.split(separator)).flatMap(Arrays::stream)
				.collect(Collectors.toList());
		return words;
	}

	String sortAllByWordsLength() {
		rezult = "";
		List<String> words = separateText("[.,!?]?[\r\n\\s]", this.text);

		words.stream().distinct().sorted(new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.length() - o2.length();
			};
		}).forEach(s -> rezult += s + " ");
		return rezult;
	}

	String sortSentencesByWordsLength() {
		rezult = "";
		List<String> sentences = separateText("(?<=[.?!])[\\s\\n]?", this.text);

		Map<String, Integer> mapSentencesWithLength = sentences.stream()
				.collect(Collectors.toMap(s -> s, s -> separateText(" ", s).size()));
		mapSentencesWithLength.entrySet().stream().sorted((o1, o2) -> o2.getValue() - o1.getValue())
				.forEach(s -> rezult += s.getKey() + " ");

		return rezult;
	}
//		words.stream()
//			.sorted(new Comparator<String>() {
//				public int compare(String o1, String o2) {
//					List<String> words1 = separateText(" ", o1);
//					List<String> words2 = separateText(" ", o2);
//					return words1.size() - words2.size();
//				};
//			})
//			.forEach(s -> rezult += s + " ");		
}
