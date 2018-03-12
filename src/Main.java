import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.Notification;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.*;
import org.controlsfx.control.Notifications;

public class Main extends Application {

	Stage window;
	BorderPane layout;
	private Desktop desktop = Desktop.getDesktop();

	public static void main(String[] args) {
		launch(args);
	}

	private static void configureFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("View Pictures");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
	}

	private void openFile(File file) {
		try {
			desktop.open(file);
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Queue<String> queue = new LinkedList<String>();

		FileChooser fileChooser = new FileChooser();
		Label[] labelFile = new Label[2];
		final Label labelRead = new Label();
		final Label labelWrite = new Label();
		window = primaryStage;
		window.setTitle("MulThread");
		// File menu
		Menu fileMenu = new Menu("File");
		MenuItem newFileToRead = new MenuItem("New File To Read...");
		newFileToRead.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				configureFileChooser(fileChooser);
				File file = fileChooser.showOpenDialog(window);
				if (file != null) {
					openFile(file);
					// System.out.println("here");
					labelRead.setText(file.getPath());
				}
			}
		});

		MenuItem newFileToWrite = new MenuItem("New File To Write...");
		newFileToWrite.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				configureFileChooser(fileChooser);
				File file = fileChooser.showOpenDialog(window);
				if (file != null) {
					openFile(file);
					labelWrite.setText(file.getPath());
				}

			}
		});

		fileMenu.getItems().add(newFileToRead);
		fileMenu.getItems().add(newFileToWrite);
		fileMenu.getItems().add(new SeparatorMenuItem());
		MenuItem exit = new MenuItem("Exit...");
		exit.setOnAction(e -> Platform.exit());

		fileMenu.getItems().add(new MenuItem("Exit..."));

		// Edit menu
		Menu threadMenu = new Menu("_Thread");
		ToggleGroup threadToggle = new ToggleGroup();

		RadioMenuItem thread1 = new RadioMenuItem("Thread 1");
		RadioMenuItem thread2 = new RadioMenuItem("Thread 2");
		RadioMenuItem threadParallelism = new RadioMenuItem("Thread Parallelism");

		thread1.setSelected(true);
		thread1.setToggleGroup(threadToggle);
		thread2.setToggleGroup(threadToggle);
		threadParallelism.setToggleGroup(threadToggle);

		threadMenu.getItems().addAll(thread1, thread2, threadParallelism);
		// Difficulty RadioMenuItems
		Menu difficultyMenu = new Menu("Difficulty");
		ToggleGroup difficultyToggle = new ToggleGroup();

		RadioMenuItem synchronize = new RadioMenuItem("Synchronize");
		RadioMenuItem asynchronous = new RadioMenuItem("Asynchronous");
		synchronize.setSelected(true);
		synchronize.setToggleGroup(difficultyToggle);
		asynchronous.setToggleGroup(difficultyToggle);

		difficultyMenu.getItems().addAll(synchronize, asynchronous);
		/*
		 * synchronize.setOnAction(new EventHandler<ActionEvent>() {
		 * 
		 * @Override public void handle(ActionEvent e) {
		 * System.out.println("radio toggled"); } }); asynchronous.setOnAction(new
		 * EventHandler<ActionEvent>() {
		 * 
		 * @Override public void handle(ActionEvent e) { System.out.println(s);
		 * System.out.println("radio toggled"); } });
		 * 
		 */
		MenuItem run = new MenuItem("run...");
		run.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				if (thread1.isSelected()) {
					if (labelRead.getText().compareTo("") == 0) {

						return;
					}
					Thread threadReader = new Thread(new Reader(queue, labelRead.getText()), "cin");
					System.out.println("thread1");
					threadReader.start();
					try {
						threadReader.join();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println("==================");
					return;
				}
				if (thread2.isSelected()) {
					if (labelWrite.getText().compareTo("") == 0) {

						return;
					}
					Thread threadWriter = new Thread(new Writer(queue, labelWrite.getText()), "cout");
					System.out.println("thread2");
					threadWriter.start();
					System.out.println("==================");
					return;
				}
				 Semaphore sem = new Semaphore(0);
				Thread threadReader = new Thread(new Reader(queue, labelRead.getText(), sem), "cin");
				Thread threadWriter = new Thread(new Writer(queue, labelWrite.getText(), sem), "cout");
				if(labelRead.getText().compareTo("")==0 || labelWrite.getText().compareTo("")==0) {
					System.out.println("Loi tai day");
					return;
				}
				
				if(asynchronous.isSelected()) {	
					threadReader.start();
					threadWriter.start();
					try {
						threadReader.join();
						threadWriter.join();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println("==================");
					return;
				}
				System.out.println("start");
				threadReader.start();
				threadWriter.start();
				try {
					threadReader.join();
					threadWriter.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("==================");
				
			}
		});

		fileMenu.getItems().add(run);

		// Main menu bar
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, threadMenu, difficultyMenu);

		layout = new BorderPane();
		layout.setTop(menuBar);
		Scene scene = new Scene(layout, 400, 300);
		window.setScene(scene);
		window.show();

	}

}