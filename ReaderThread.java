import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.Queue;

public class ReaderThread extends Thread {
	protected  Queue<String> queue=null;
	public ReaderThread(Queue<String> queue) {
		this.queue =queue;
	}
	@Override
	public void run() {
		BufferedReader br = null;
		String buffer = null;
		File file = new File("D:\\Javademo\\Thread\\src\\in.txt");
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while ((buffer = br.readLine()) != null) {
				queue.add(buffer);
				//System.out.println(buffer);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}