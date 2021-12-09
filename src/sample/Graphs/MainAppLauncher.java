package sample.Graphs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainAppLauncher extends Application {

	public static void main(String[] args) {
		Application.launch(MainAppLauncher.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("MainApp.fxml"));
			stage.setScene(new Scene(root));
			stage.setTitle("JavaFX Graph Example");
			stage.show();
		} catch (Exception e) {
			System.out.print(e);
		}
	}
}