package nim;
import java.util.*;

public class Queue {
	private LinkedList<Object> list;
	
	public Queue() {
		list = new LinkedList<Object>();
	}
	
	public boolean isEmpty() {
		return (list.size() == 0);
	}
	
	public void equeue(Object item) {
		list.add(item);
	}
	
	public Object dequeue() {
		Object item = list.get(0);
		list.remove(0);
		return item;
	}
	
	public Object peek() {
		return list.get(1);
	}
	
	public int size() {
		return list.size();
	}
	
	public void removeAll() {
		while(list.isEmpty() == false) {
			list.remove();
		}
	}
}
