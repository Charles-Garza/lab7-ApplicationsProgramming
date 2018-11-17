package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


public class Main extends Application {
	/** The stage. */
	public static Stage stage;
	/**
	 * Creates the primary base for which our application can run on.
	 *
	 * @param primaryStage Stage
	 */
	@Override
	public void start(Stage primaryStage) 
	{
		stage = primaryStage;
		try 
		{
			/** Loads the initial scene/screen */
        	Pane pane = FXMLLoader.load(Main.class.getResource("/application/view/Main.fxml"));
        	/** Create a scene and set the scene as the pane. */
        	Scene mainScene = new Scene(pane);
        	/** Sets the scene to be displayed */
        	primaryStage.setScene(mainScene);
        	primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts and launches the application.
	 *
	 * @param args String[]
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
