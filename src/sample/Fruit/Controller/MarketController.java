package sample.Fruit.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.hildan.fxgson.FxGson;
import sample.FieldPages.FieldPage;
import sample.Fruit.main.Main;
import sample.Fruit.main.MyListener;
import sample.Fruit.model.Fruit;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import sample.Models.CompanyModel;
import sample.Models.Field;
import sample.NewCompanyForm.InitForm;
import sample.NewCompanyForm.InitFormController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class MarketController implements Initializable {
    @FXML
    private VBox chosenFruitCard;

    @FXML
    private Label fruitNameLable;


    @FXML
    private JFXButton createNewCompany;

    @FXML
    Button viewDetails;

    @FXML
    private ImageView fruitImg;

    @FXML
    private Label fieldsLabel;

    @FXML
    private GridPane grid;

    private List<CompanyModel> companyModels = new ArrayList<>();
    private MyListener myListener;

    private List<CompanyModel> getData() {
        List<CompanyModel> companyModels = new ArrayList<>();
        Preferences preference = Preferences.userNodeForPackage(InitFormController.class);
        Gson gson = FxGson.create();
        companyModels = gson.fromJson(preference.get("companies", ""), new TypeToken<ArrayList<CompanyModel>>() {
        }.getType());
        System.out.println(gson.toJson(companyModels));
        return companyModels;
    }

    private void setChosenCompany(CompanyModel companyModel) {
        fruitNameLable.setText(companyModel.getCompanyName());
        if (null == companyModel.getFields()) {
            fieldsLabel.setText("Fields \n No Fields Added Yet");
        } else {
            ArrayList<Field> fields = companyModel.getFields();
            String values = "Fields\n";
            for (int i = 0; i < fields.size(); i++) {
                if (null == fields.get(i).getWells()) {
                    values = values + fields.get(i).getFieldName() + ": N/A\n";
                } else {
                    values = values + fields.get(i).getFieldName() + ": " + fields.get(i).getWells().size() +" Wells\n";
                }
            }
            fieldsLabel.setText(values);
        }


        chosenFruitCard.setStyle("-fx-background-color: #" + 235434 + ";\n" +
                "    -fx-background-radius: 15;");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        companyModels.addAll(getData());
        if (companyModels.size() > 0) {
            for (int i = 0; i < companyModels.size(); i++) {
                if (companyModels.get(i).getPrimary()) {
                    setChosenCompany(companyModels.get(i));
                }
            }

            myListener = new MyListener() {
                @Override
                public void onClickListener(CompanyModel companyModel) throws BackingStoreException {
                    companyModel.setPrimary(true);
                    for (int i = 0; i < companyModels.size(); i++) {
                        if (!companyModel.getId().equals(companyModels.get(i).getId())) {
                            companyModels.get(i).setPrimary(false);
                        }
                    }
                    Preferences preferences = Preferences.userNodeForPackage(InitFormController.class);
                    Gson gson = FxGson.create();

                    preferences.put("companies", gson.toJson(companyModels));
                    System.out.println(gson.toJson(companyModels) + "");
                    preferences.flush();

                    setChosenCompany(companyModel);
                }

                @Override
                public void onDeleteListener(CompanyModel companyModel) throws Exception {
                    for (int i = 0; i < companyModels.size(); i++) {
                        if (companyModel.getId().equals(companyModels.get(i).getId())) {
                            companyModels.remove(i);
                        }
                    }
                    Preferences preferences = Preferences.userNodeForPackage(InitFormController.class);
                    Gson gson = FxGson.create();

                    preferences.put("companies", gson.toJson(companyModels));
                    System.out.println(gson.toJson(companyModels) + "");
                    preferences.flush();
                    Main initForm = new Main();
                    initForm.start(new Stage());
                    Stage stage = (Stage) createNewCompany.getScene().getWindow();
                    stage.close();

                }

                @Override
                public void onClickListener(Field field) throws BackingStoreException {

                }

                @Override
                public void onDeleteListener(Field field) throws Exception {

                }
            };

            createNewCompany.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    InitForm initForm = new InitForm();
                    initForm.start(new Stage());
                    Stage stage = (Stage) createNewCompany.getScene().getWindow();
                    stage.close();

                }
            });
            viewDetails.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    FieldPage initForm = new FieldPage();
                    try {
                        initForm.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Stage stage = (Stage) createNewCompany.getScene().getWindow();
                    stage.close();

                }
            });
        }
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < companyModels.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();

                fxmlLoader.setLocation(getClass().getResource("../views/item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setData(companyModels.get(i), myListener);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row); //(child,column,row)
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
