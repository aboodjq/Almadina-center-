package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
				
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Quran Memorization Tracking System");
			primaryStage.getIcons().add(new Image("file:///C:/Users/TCC/eclipse-workspace/madina/src/test/شعار%20مركز%20المدينة-04.png"));
			primaryStage.centerOnScreen();
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
