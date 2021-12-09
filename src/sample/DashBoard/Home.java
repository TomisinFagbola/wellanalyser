package sample.DashBoard;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Splash.MyPreLoader;

import java.util.prefs.Preferences;

public class Home extends Application {
    private double x, y;
    private static final int COUNT_LIMIT = 10000;

    @Override
    public void init() throws Exception {
        for (int i = 0; i < COUNT_LIMIT; i++) {
            double progress = (100 * i) / COUNT_LIMIT;
            LauncherImpl.notifyPreloader(this,new Preloader.ProgressNotification(progress));
        }
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        primaryStage.setScene(new Scene(root));
        //set stage borderless
        //primaryStage.initStyle(StageStyle.UNDECORATED);

        //drag it here
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {

            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);

        });
        primaryStage.show();
        /*
        Preferences prefs = Preferences.systemRoot().node("projects");

        Alert jfxAlert = new Alert(Alert.AlertType.WARNING);
        jfxAlert.setTitle("Error!");
        jfxAlert.setHeaderText( prefs.get("PModel","null")+" Make Sure All Fields Are Filled");
        jfxAlert.show();*/

    }


    public static void main(String[] args) {
        launch(args);
    }
}
