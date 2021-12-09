package sample.FieldPages;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hildan.fxgson.FxGson;
import sample.Fruit.Controller.ItemController;
import sample.Fruit.main.Main;
import sample.Fruit.main.MyListener;
import sample.Models.CompanyModel;
import sample.Models.Field;
import sample.NewCompanyForm.InitForm;
import sample.NewCompanyForm.InitFormController;
import sample.tableView.OperationTable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class FieldPageController implements Initializable {
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
    private List<Field> fieldList = new ArrayList<>();
    private MyListener myListener;

    private List<CompanyModel> getData() {
        List<CompanyModel> companyModels = new ArrayList<>();
        Preferences preference = Preferences.userNodeForPackage(InitFormController.class);
        Gson gson = FxGson.create();
        companyModels = gson.fromJson(preference.get("companies", ""), new TypeToken<ArrayList<CompanyModel>>() {
        }.getType());
        return companyModels;
    }

    private void setChosenCompany(CompanyModel companyModel) {
        fieldList = companyModel.getFields();
        if(null == companyModel.getFields()){
            fruitNameLable.setText(companyModel.getCompanyName() + ": null Field(s)");
        }else{
        fruitNameLable.setText(companyModel.getCompanyName() + ": "+companyModel.getFields().size() +" Field(s)");}
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

                }

                @Override
                public void onDeleteListener(CompanyModel companyModel) throws Exception {

                }

                @Override
                public void onClickListener(Field field) throws Exception {
                    field.setPrimary(true);
                    for (int i = 0; i < companyModels.size(); i++) {
                        if (companyModels.get(i).getPrimary()) {
                            ArrayList<Field> fieldArrayList =  companyModels.get(i).getFields();
                            for (int j = 0; j < fieldArrayList.size(); j++) {
                                if(field.getFieldId().equals(fieldArrayList.get(j).getFieldId())){
                                    fieldArrayList.set(j,field);
                                }else {
                                    fieldArrayList.get(j).setPrimary(false);
                                }
                                companyModels.get(i).setFields(fieldArrayList);
                            }
                        }
                    }
                    Preferences preferences = Preferences.userNodeForPackage(InitFormController.class);
                    Gson gson = FxGson.create();

                    preferences.put("companies", gson.toJson(companyModels));
                    System.out.println(gson.toJson(companyModels) + "");
                    preferences.flush();
                    OperationTable initForm = new OperationTable();
                    initForm.start(new Stage());

                }

                @Override
                public void onDeleteListener(Field field) throws Exception {

                }
            };

            createNewCompany.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    sample.NewFieldForm.InitForm initForm = new sample.NewFieldForm.InitForm();
                    initForm.start(new Stage());
                    Stage stage = (Stage) createNewCompany.getScene().getWindow();
                    stage.close();

                }
            });

        }
        int column = 0;
        int row = 1;
        if(null != fieldList){
            try {
                for (int i = 0; i < fieldList.size(); i++) {
                    FXMLLoader fxmlLoader = new FXMLLoader();

                    fxmlLoader.setLocation(getClass().getResource("../Fruit/views/item.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();

                    ItemController itemController = fxmlLoader.getController();
                    itemController.setData(fieldList.get(i), myListener);

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

}
