import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dispatcher_Collectors_2 {

	public static void main(String[] args) {
		
		String text = "You. o? Java is a High Level. Class-based. Object-oriented. Programming language."
				+ " That is designed to have as few implementation dependencies as possible.";
		WorkWithSentences2.differentWithVowelsAndConsonantsLetters(text);

	}
}

class WorkWithSentences2 {
	static int index, count;

	static void differentWithVowelsAndConsonantsLetters(String text) {
		index = 1;
		count = 0;
		List<Integer> lastPunctuation = Arrays.asList(46, 33, 63);
		List<Integer> vowelsLetters = Arrays.asList(97, 101, 105, 111, 117, 121);

		text = text.toLowerCase();
		
		Map<Integer, Integer> map = new HashMap<>();
		text.chars().forEach(n -> {
			if (!lastPunctuation.contains(n)) {
				if (Character.isAlphabetic(n)) 
					count += vowelsLetters.contains(n) ? -1 : 1;
			} else {
				map.put(index, count);
				count = 0;
				index++;
			}
		});
		
		map.entrySet().forEach(System.out::println);
	}

}
