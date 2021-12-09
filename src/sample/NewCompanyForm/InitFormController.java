package sample.NewCompanyForm;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.prefs.Preferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.hildan.fxgson.FxGson;
import sample.DashBoard.DashboardController;
import sample.DashBoard.Home;
import sample.Fruit.main.Main;
import sample.Models.CompanyModel;
import sample.Toast;

public class InitFormController implements Initializable {

    @FXML
    private JFXTextField inpName, inpEmail;


    private Boolean validateTextField(JFXTextField textField) {
        if (textField.getText().equals("") || textField.getText().equals(null)) {
            Toast.makeText(new Stage(), "Field Cannot Be Empty!", 1000, 500, 500);
            return false;
        } else {
            return true;
        }
    }

    ArrayList<CompanyModel> companyModels = new ArrayList<>();


    @FXML
    public void handleSignIn(ActionEvent actionEvent) throws Exception {
        if (((Node) actionEvent.getSource()).getId().equals("btnSignIn")) {
            if (!validateTextField(inpEmail) | !validateTextField(inpName)) {
                return;
            }
            String id = UUID.randomUUID().toString();
            CompanyModel companyModel = new CompanyModel();
            companyModel.setId(id);
            companyModel.setCompanyName(inpName.getText());
            companyModel.setUserName(inpEmail.getText());


            Preferences preferences = Preferences.userNodeForPackage(InitFormController.class);
            Boolean pri = preferences.getBoolean("logged", false);
            if(pri){
                companyModel.setPrimary(false);
            }else {
                companyModel.setPrimary(true);
            }
            Gson gson = FxGson.create();
            companyModels.add(companyModel);
            preferences.put("companies", gson.toJson(companyModels));
            preferences.putBoolean("logged", true);
            preferences.flush();
            Stage stage = new Stage();
            new Main().start(stage);
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Preferences preferences = Preferences.userNodeForPackage(InitFormController.class);
        Gson gson = FxGson.create();
        companyModels = gson.fromJson(preferences.get("companies", null), new TypeToken<ArrayList<CompanyModel>>() {
        }.getType());
        if (companyModels == null) {
            companyModels = new ArrayList<>();
        }
    }
}
