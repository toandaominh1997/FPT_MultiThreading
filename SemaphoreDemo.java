import java.util.*;
import java.util.concurrent.*;

public class SemaphoreDemo {
	public static void main(String[] args) {
		Queue<String> queue = new LinkedList<String>();
		Thread thread1 = new Thread(new ReaderThread(queue), "cin");
		Thread thread2 = new Thread(new WriterThread(queue), "cout");
		thread1.start();
		thread2.start();

		try {
			thread2.join();
			thread1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}