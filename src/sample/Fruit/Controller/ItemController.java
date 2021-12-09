package sample.Fruit.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.hildan.fxgson.FxGson;
import sample.Fruit.main.Main;
import sample.Fruit.main.MyListener;
import sample.Fruit.model.Fruit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import sample.Models.CompanyModel;
import sample.Models.Field;
import sample.NewCompanyForm.InitFormController;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


public class ItemController {
    @FXML
    private Label nameLabel,fieldsLabel,userNameLabel;


    @FXML
    private void click(MouseEvent mouseEvent) throws Exception {
        if(company == null && field != null){
            myListener.onClickListener(field);
        }else{
            myListener.onClickListener(company);
        }
    }
    @FXML
    private void delete(MouseEvent mouseEvent) throws Exception {
        if(company == null && field != null){
            myListener.onDeleteListener(field);
        }else{
            myListener.onDeleteListener(company);
        }

    }

    private CompanyModel company;
    private Field field;
    private MyListener myListener;

    public void setData(CompanyModel companyModel, MyListener myListener) {
        this.company = companyModel;
        this.myListener = myListener;
        nameLabel.setText(companyModel.getCompanyName());
        if(null != companyModel.getFields()){
            fieldsLabel.setText(companyModel.getFields().size()+" Fields");
        }else{
            fieldsLabel.setText("No fields Yet");
        }

        userNameLabel.setText("User Name: "+companyModel.getUserName());
    }

    public void setData(Field field, MyListener myListener) {
        this.field = field;
        this.myListener = myListener;
        nameLabel.setText(field.getFieldName());
        if(null != field.getWells()){
            fieldsLabel.setText(field.getWells().size()+" Well(s)");
        }else{
            fieldsLabel.setText("No Wells Yet");
        }

        userNameLabel.setText("--");
    }
}
