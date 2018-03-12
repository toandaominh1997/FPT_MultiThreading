import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.Queue;

public class Reader extends Thread {
	protected Queue<String> queue = null;
	String pathName;
	Semaphore sem;

	public Reader(Queue<String> queue) {
		this.queue = queue;
	}
	public Reader(Queue<String> queue, String pathName) {
		this.queue = queue;
		this.pathName = pathName;
	}

	public Reader(Queue<String> queue, String pathName, Semaphore sem) {
		this.queue = queue;
		this.pathName = pathName;
		this.sem=sem;
	}

	public void setReader(String pathName) {
		this.pathName = pathName;
	}

	@Override
	public void run() {
		BufferedReader br = null;
		String buffer = null;
		File file = new File(pathName);
	
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while ((buffer = br.readLine()) != null) {
				queue.add(buffer);
				System.out.println(buffer);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sem.release();

	}
}