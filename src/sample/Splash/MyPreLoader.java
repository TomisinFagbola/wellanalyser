package sample.Splash;

import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.NewCompanyForm.InitFormController;

import java.util.prefs.Preferences;

public class MyPreLoader extends Preloader {

    private Stage preLoaderStage;
    private Scene scene;

    public MyPreLoader() {
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.preLoaderStage = stage;
        preLoaderStage.setScene(scene);
        preLoaderStage.initStyle(StageStyle.UNDECORATED);
        preLoaderStage.show();
    }

    @Override
    public void init() throws Exception {
        //Preferences preference = Preferences.userNodeForPackage(InitFormController.class);
        //preference.clear();
        Parent root1 = FXMLLoader.load(getClass().getResource("splashScreen.fxml"));
        scene = new Scene(root1);
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        if (info instanceof ProgressNotification) {
            SplashController.label.setText("Loading " + ((ProgressNotification) info).getProgress() + " %");
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
        StateChangeNotification.Type type = stateChangeNotification.getType();
        switch (type) {
            case BEFORE_START:
                preLoaderStage.hide();
                break;
        }
    }
}
