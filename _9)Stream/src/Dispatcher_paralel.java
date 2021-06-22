import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dispatcher_paralel {

	public static void main(String[] args) {
			
//		List<Integer> numbers = new ArrayList<>();
//		for (int i = 0; i < 200; i++) {
//			Integer temp = (int) (Math.random() * 30);
//			numbers.add(temp);
//		}
		
		List<Integer> numbers = Stream.generate(() -> (int) (Math.random() * 30)).limit(200)
				.collect(Collectors.toList());
		
		long time1 = System.currentTimeMillis();
		
		Map<Integer, Long> newMap = numbers.stream().filter(n -> n % 2 == 0)
				.collect(Collectors.groupingBy(s -> s , Collectors.counting()));
		
		long time2 = System.currentTimeMillis();
		
		Map<Integer, Long> newMap2 = numbers.parallelStream().filter(n -> n % 2 == 0)
				.collect(Collectors.groupingBy(s -> s, Collectors.counting()));
		
		long time3 = System.currentTimeMillis();
		
		for (Entry<Integer, Long> entry : newMap.entrySet()) {
			System.out.println(entry.getKey() + " -> " + entry.getValue());
		}
		
		System.out.println("         stream time: " + (time2 - time1));
		System.out.println("parallel stream time: " + (time3 - time2));
	}

}

