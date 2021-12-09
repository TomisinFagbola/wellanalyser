package sample.NewCompanyForm;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.DashBoard.Home;
import sample.Fruit.main.Main;
import sample.Splash.MyPreLoader;

import java.util.prefs.Preferences;

public class InitForm extends Application {
    Parent root;
    double xOffset, yOffset;

    @Override
    public void start(Stage primaryStage) {
        try {
            root = FXMLLoader.load(getClass().getResource("InitForm.fxml"));
            Scene scene = new Scene(root);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.show();
            scene.setFill(Color.TRANSPARENT);
            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    primaryStage.setX(event.getScreenX() - xOffset);
                    primaryStage.setY(event.getScreenY() - yOffset);
                }
            });
        } catch (Exception e) {
        }
    }

    private static final int COUNT_LIMIT = 85000;

    @Override
    public void init() throws Exception {

        for (int i = 0; i < COUNT_LIMIT; i++) {
            double progress = (100 * i) / COUNT_LIMIT;
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
        }
    }

    public static void main(String[] args) {
        Preferences preferences = Preferences.userNodeForPackage(InitFormController.class);
        Boolean logged = preferences.getBoolean("logged", false);
        if (logged) {
            LauncherImpl.launchApplication(Main.class, MyPreLoader.class, args);
        } else {
            LauncherImpl.launchApplication(InitForm.class, MyPreLoader.class, args);
        }
    }
}
