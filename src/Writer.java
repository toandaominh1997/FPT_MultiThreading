import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.*;
import java.util.Queue;

public class Writer extends Thread {

	private Queue<String> queue = null;
	String pathName;
	Semaphore sem;
	public Writer(Queue<String> queue, String pathName,   Semaphore sem) {
		this.queue = queue;
		this.pathName = pathName;
		this.sem=sem;
	}
	public Writer(Queue<String> queue, String pathName) {
		this.queue = queue;
		this.pathName = pathName;
	}
	public Writer(Queue<String> queue) {
		this.queue = queue;
	}

	public void setReader(String pathName) {
		this.pathName = pathName;
	}

	@Override
	public void run() {
		PrintWriter cout = null;
		String buffer;
		if(sem.hasQueuedThreads()==true) {
			try {
				sem.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			sem.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			cout = new PrintWriter(new File(pathName));
			while (true) {
				buffer = queue.poll();
				cout.println("Write = " +buffer);
				System.out.println("Write : " + buffer);
				if (queue.isEmpty())
					break;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			cout.close();
		}
		sem.release();
	}

}