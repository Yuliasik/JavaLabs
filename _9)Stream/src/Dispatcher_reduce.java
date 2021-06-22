import java.util.ArrayList;
import java.util.stream.Stream;

public class Dispatcher_reduce {

	public static void main(String[] args) {
		ListHandler list1 = new ListHandler((int) (1 + Math.random() * 20));
		ListHandler list2 = new ListHandler((int) (1 + Math.random() * 20));

		System.out.println("list1 = " + list1);
		System.out.println("list2 = " + list2);

		ListHandler newlist = list1.concatList(list2.getList());
		System.out.println("newList = " + newlist + "\n");

		double average = newlist.findAverage();
		System.out.println("average = " + average);

		long count = newlist.findCountBiggerThanAverage(average);
		System.out.println("count bigger than average = " + count + "\n");

		System.out.println("list before: " + newlist);
		newlist.setList(newlist.removeDublicateMinAndMax());
		System.out.println("list after:  " + newlist);

	}

}

class ListHandler {
	private ArrayList<Integer> list = new ArrayList<>();
	private static boolean isFirstMin = false;
	private static boolean isFirstMax = false;

	ListHandler(int length) {
		this.list = createList(length);
	}

	ListHandler() {}

	ArrayList<Integer> createList(int length) {
		for (int i = 0; i < length; i++) {
			this.list.add((int) (Math.random() * 10));
		}
		return this.list;
	}

	public ArrayList<Integer> getList() {
		return list;
	}

	public void setList(ArrayList<Integer> list) {
		this.list = list;
	}
	
	ListHandler concatList(ArrayList<Integer> list2) {
		ListHandler lh = new ListHandler();
		Stream.concat(Stream.of(this.list), Stream.of(list2)).forEach(val -> lh.list.addAll(val));
		return lh;
	}

	double findAverage() {
		return this.list.stream().mapToInt(Integer::intValue).average().getAsDouble();
	}

	long findCountBiggerThanAverage(double average) {
		return this.list.stream().filter(n -> n > average).count();
	}

	ArrayList<Integer> removeDublicateMinAndMax() {
		isFirstMin = false;
		isFirstMax = false;

		int min = (this.list.stream().mapToInt(Integer::intValue).min()).getAsInt();
		int max = (this.list.stream().mapToInt(Integer::intValue).max()).getAsInt();
		System.out.println("min = " + min);
		System.out.println("max = " + max);

		ArrayList<Integer> list = new ArrayList<>();
		this.list.stream().forEach((n) -> {
			if (n != min && n != max) {
				list.add(n);
			} else if (n == max && !isFirstMax) {
				isFirstMax = true;
				list.add(n);
			} else if (n == min && !isFirstMin) {
				isFirstMin = true;
				list.add(n);
			}
		});
		return list;
	}

	@Override
	public String toString() {
		return this.list.toString();
	}

}
