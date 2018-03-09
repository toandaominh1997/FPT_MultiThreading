import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.*;
import java.util.Queue;
public class WriterThread extends Thread{

  private Queue<String> queue = null;

  public WriterThread(Queue<String> queue){
    this.queue = queue;     
  }

  @Override
  public void run() {
    PrintWriter cout = null;
    String buffer;
    try {
        cout = new PrintWriter(new File("D:\\Javademo\\Thread\\src\\out.txt"));
        while(true){
        	buffer = queue.poll();
        	if(queue.isEmpty())
        		break;
            cout.println(buffer);
        }               
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }finally{
        cout.close();
    } 
  }

}