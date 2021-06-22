import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dispatcher_Collectors {

	public static void main(String[] args) {

		WorkWithIntegerList l = new WorkWithIntegerList(20);
		System.out.println(l);
		l.replaceMaxAndMin();
		System.out.println(l + "\n");

		WorkWithIntegerList l2 = new WorkWithIntegerList();
		l.createList(l2);
		System.out.println("elements greater than average = " + l2 + "\n");

		String text = "Java is a High Level. Class-based. Object-oriented. Programming language."
				+ " That is designed to have as few implementation dependencies as possible.";
		WorkWithSentences s = new WorkWithSentences(text);
		System.out.println(s);
		s.creatMap();

	}

}

class WorkWithIntegerList {
	List<Integer> list;
	int max, min;
	double average;

	public WorkWithIntegerList(int count) {
		createList(count);
	}

	public WorkWithIntegerList() {
	}

	void createList(int count) {
		this.list = Stream.generate(() -> (int) (10 - Math.random() * 20))
				.limit(count).collect(Collectors.toList());
	}

	void findMax() {
		this.max = this.list.stream().collect(Collectors.maxBy(Comparator.naturalOrder())).get();
		System.out.println("max = " + this.max);
	}

	void findMin() {
		this.min = this.list.stream().collect(Collectors.minBy(Comparator.naturalOrder())).get();
		System.out.println("min = " + this.min);
	}

	void replaceMaxAndMin() {
		this.findMin();
		this.findMax();
		this.list = this.list.stream()
				.collect(Collectors
						.mapping(n -> n == this.max ? this.min : n == this.min ? this.max : n, 
								Collectors.toList()));
	}

	void findAvarage() {
		this.average = this.list.stream().collect(Collectors.averagingInt(n -> n));
	}

	void createList(WorkWithIntegerList w1) {
		this.findAvarage();
		System.out.println("average = " + this.average);
		w1.list = this.list.stream().filter(n -> n > average).collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return this.list.toString();
	}

}

class WorkWithSentences {
	List<String> listSentences;

	WorkWithSentences(String text) {
		this.createList(text);
	}

	void createList(String text) {
		this.listSentences = Stream.of(text.split("\\."))
				.map(elem -> new String(elem)).collect(Collectors.toList());
	}

	long countVowelsLetters(String sentence) {
		return this.countLetters(sentence, "[aeiouy]");
	}

	long countConsonantLetters(String sentence) {
		return this.countLetters(sentence, "[bcdfghjklmnpqrstvwxz]");
	}

	private long countLetters(String sentence, String regex) {
		long count = Stream.of(sentence.toLowerCase().split(""))
				.filter(s -> s.matches(regex)).count();
		return count;
	}

	int index(String sentence) {
		return this.listSentences.indexOf(sentence);
	}

	void creatMap() {
		Map<Integer, Long> map = listSentences.stream()
				.collect(Collectors.toMap(k -> index(k), 
						s -> Math.abs(countConsonantLetters(s) - countVowelsLetters(s))));
		map.entrySet().forEach(System.out::println);
		System.out.println();
	}

	@Override
	public String toString() {
		return this.listSentences.toString();
	}

}
