package sample.NewFieldForm;

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
import sample.Models.Field;
import sample.Toast;

public class InitFormController implements Initializable {

    @FXML
    private JFXTextField inpName;


    private Boolean validateTextField(JFXTextField textField) {
        if (textField.getText().equals("") || textField.getText().equals(null)) {
            Toast.makeText(new Stage(), "Field Cannot Be Empty!", 1000, 500, 500);
            return false;
        } else {
            return true;
        }
    }

    ArrayList<CompanyModel> companyModels = new ArrayList<>();
    ArrayList<Field> fields;


    @FXML
    public void handleSignIn(ActionEvent actionEvent) throws Exception {
        if (((Node) actionEvent.getSource()).getId().equals("btnSignIn")) {
            if (!validateTextField(inpName)) {
                return;
            }
            Field field = new Field();
            field.setFieldName(inpName.getText());
            field.setFieldId(UUID.randomUUID().toString());
            field.setPrimary(false);

            for (int i = 0; i < companyModels.size(); i++) {
                if (companyModels.get(i).getPrimary()) {
                    if (null == companyModels.get(i).getFields()) {
                        fields = new ArrayList<>();
                    } else {
                        fields = companyModels.get(i).getFields();
                    }
                    fields.add(field);
                    companyModels.get(i).setFields(fields);
                }
            }
            Preferences preferences = Preferences.userNodeForPackage(sample.NewCompanyForm.InitFormController.class);

            Gson gson = FxGson.create();

            preferences.put("companies", gson.toJson(companyModels));
            preferences.flush();
            Stage stage = new Stage();
            new Main().start(stage);
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Preferences preferences = Preferences.userNodeForPackage(sample.NewCompanyForm.InitFormController.class);
        Gson gson = FxGson.create();
        companyModels = gson.fromJson(preferences.get("companies", null), new TypeToken<ArrayList<CompanyModel>>() {
        }.getType());
        if (companyModels == null) {
            companyModels = new ArrayList<>();
        }
    }
}
