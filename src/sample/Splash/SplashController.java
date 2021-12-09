package sample.Splash;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {
    @FXML
    private Label progress;

    public static Label label;
    @FXML
    private void handleButtonAction(ActionEvent event){


    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        label = progress;
    }
}
