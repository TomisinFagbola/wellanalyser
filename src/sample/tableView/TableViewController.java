/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.tableView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.hildan.fxgson.FxGson;
import sample.DashBoard.FinancialAnalysis;
import sample.FieldAnalysis.FieldAnalysisStage;
import sample.Models.*;
import sample.NewCompanyForm.InitFormController;

import static sample.DashBoard.DeclineModels.NO_MODE;
import static sample.tableView.DeclineModels.*;

/**
 * FXML Controller class
 *
 * @author hocin
 */
public class TableViewController implements Initializable {

    @FXML
    private TableView<TableDataModel> studentsTable;
    @FXML
    private TableColumn<TableDataModel, String> wellName;
    @FXML
    private TableColumn<TableDataModel, Double> qo;
    @FXML
    private TableColumn<TableDataModel, Double> qf;
    @FXML
    private TableColumn<TableDataModel, Double> npd;
    @FXML
    private TableColumn<TableDataModel, Double> tf;
    @FXML
    private TableColumn<TableDataModel, Double> a;
    @FXML
    private TableColumn<TableDataModel, Double> b;
    @FXML
    private TableColumn<TableDataModel, HBox> manage;
    @FXML
    private TableColumn<TableDataModel, String> mode;
    @FXML
    private TableColumn<TableDataModel, String> wellType;
    @FXML
    private Label fieldNameText;
    /**
     * Initializes the controller class.
     */
    ArrayList<Field> fields;
    Field field;
    ArrayList<Well> wells;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        getData();
        if (field.getWells() == null) {
            wells = new ArrayList<>();
            fieldNameText.setText(field.getFieldName() + ":  null Wells");
        } else {
            wells = field.getWells();
            fieldNameText.setText(field.getFieldName() + ": " + field.getWells().size() + " Well(s)");
        }
        studentsTable.
                setStyle("-fx-selection-bar: white; -fx-selection-bar-non-focused: salmon;");
        wellName.setCellValueFactory(new PropertyValueFactory<>("wellName"));
        qo.setCellValueFactory(new PropertyValueFactory<>("qo"));
        qf.setCellValueFactory(new PropertyValueFactory<>("qf"));
        tf.setCellValueFactory(new PropertyValueFactory<>("tf"));
        npd.setCellValueFactory(new PropertyValueFactory<>("npd"));
        a.setCellValueFactory(new PropertyValueFactory<>("a"));
        b.setCellValueFactory(new PropertyValueFactory<>("b"));
        manage.setCellValueFactory(new PropertyValueFactory<>("manageBox"));
        mode.setCellValueFactory(new PropertyValueFactory<>("mode"));
        wellType.setCellValueFactory(new PropertyValueFactory<>("vertical"));
        if (null == wells) {

        } else {
            for (int i = 0; i < wells.size(); i++) {
                TableDataModel tableDataModel = new TableDataModel();
                tableDataModel.setA(wells.get(i).getDeclineModel().getA());
                tableDataModel.setB(wells.get(i).getDeclineModel().getB());
                tableDataModel.setNpd(wells.get(i).getDeclineModel().getNpd());
                tableDataModel.setQf(wells.get(i).getDeclineModel().getQf());
                tableDataModel.setQo(wells.get(i).getDeclineModel().getQo());
                tableDataModel.setTf(wells.get(i).getDeclineModel().getTf());
                tableDataModel.setWellName(wells.get(i).getNameOfWell());
                tableDataModel.setManageBox(getHox(wells.get(i), wells.get(i).getYearlyProductions()));
                if (!(wells.get(i).getVertical() == null)) {
                    if (wells.get(i).getVertical()) {
                        tableDataModel.setVertical("Vertical");
                    } else {
                        tableDataModel.setVertical("Horizontal");
                    }
                }
                tableDataModel.setMode(getActiveModeText(wells.get(i).getDeclineModel().getMode()));
                studentsTable.getItems().add(tableDataModel);
            }
        }

    }

    private String getActiveModeText(int modeSelected) {

        switch (modeSelected) {
            case NO_MODE:
                //bJtf.setVisible(false);
                return "No Mode Selected";
            case ED_MODE:
                //bJtf.setVisible(false);
                return "Exponential Decline";
            case H_MODE:
                //bJtf.setVisible(false);
                return "Harmonic";
            case GH_MODE:
                //bJtf.setVisible(true);
                return "General Hyperbolic";

            default:
                return "";
        }

    }

    private void getData() {
        List<CompanyModel> companyModels = new ArrayList<>();
        Preferences preference = Preferences.userNodeForPackage(InitFormController.class);
        Gson gson = FxGson.create();
        companyModels = gson.fromJson(preference.get("companies", ""), new TypeToken<ArrayList<CompanyModel>>() {
        }.getType());
        for (int i = 0; i < companyModels.size(); i++) {
            if (companyModels.get(i).getPrimary()) {
                fields = companyModels.get(i).getFields();
                for (int j = 0; j < fields.size(); j++) {
                    if (fields.get(j).getPrimary()) {
                        field = fields.get(j);
                    }
                }
            }
        }
    }

    private HBox getHox(Well well, ArrayList<YearlyProduction> yearlyProductions) {
        FontAwesomeIcon deleteIcon = new FontAwesomeIcon();
        FontAwesomeIcon editIcon = new FontAwesomeIcon();
        deleteIcon.setIcon(FontAwesomeIconName.MONEY);
        editIcon.setIcon(FontAwesomeIconName.EDIT);
        deleteIcon.setIconName("MONEY");
        editIcon.setIconName("EDIT");
        deleteIcon.setSize("35");
        editIcon.setSize("35");
        deleteIcon.setFill(Color.gray(0.4));
        editIcon.setFill(Color.gray(0.4));

        deleteIcon.setOnMouseClicked((MouseEvent event) -> {

        });
        editIcon.setOnMouseClicked((MouseEvent event) -> {
            CustomDialog customDialog = new CustomDialog("Yearly Production", "", yearlyProductions);
            customDialog.openDialog();
            Preferences preferences = Preferences.userNodeForPackage(InitFormController.class);

            preferences.put("currentWell", well.getWellId());

            /*student = studentsTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("addStudent.fxml"));
            try {
                loader.load();
            } catch (IOException ex) {
                Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

            AddStudentController addStudentController = loader.getController();
            addStudentController.setUpdate(true);
            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();*/

        });

        HBox managebtn = new HBox(editIcon, deleteIcon);
        managebtn.setStyle("-fx-alignment:center");
        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

        return managebtn;
    }

    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void getAddView(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("declineModels.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(TableViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void refreshTable() {
        fields.clear();
        wells.clear();
        studentsTable.getItems().clear();
        // TODO
        getData();
        if (field.getWells() == null) {
            wells = new ArrayList<>();
            fieldNameText.setText(field.getFieldName() + ":  null Wells");
        } else {
            wells = field.getWells();
            fieldNameText.setText(field.getFieldName() + ": " + field.getWells().size() + " Well(s)");
        }
        studentsTable.
                setStyle("-fx-selection-bar: white; -fx-selection-bar-non-focused: salmon;");
        wellName.setCellValueFactory(new PropertyValueFactory<>("wellName"));
        qo.setCellValueFactory(new PropertyValueFactory<>("qo"));
        qf.setCellValueFactory(new PropertyValueFactory<>("qf"));
        tf.setCellValueFactory(new PropertyValueFactory<>("tf"));
        npd.setCellValueFactory(new PropertyValueFactory<>("npd"));
        a.setCellValueFactory(new PropertyValueFactory<>("a"));
        b.setCellValueFactory(new PropertyValueFactory<>("b"));
        manage.setCellValueFactory(new PropertyValueFactory<>("manageBox"));
        mode.setCellValueFactory(new PropertyValueFactory<>("mode"));
        wellType.setCellValueFactory(new PropertyValueFactory<>("vertical"));
        if (null == wells) {

        } else {
            for (int i = 0; i < wells.size(); i++) {
                TableDataModel tableDataModel = new TableDataModel();
                tableDataModel.setA(wells.get(i).getDeclineModel().getA());
                tableDataModel.setB(wells.get(i).getDeclineModel().getB());
                tableDataModel.setNpd(wells.get(i).getDeclineModel().getNpd());
                tableDataModel.setQf(wells.get(i).getDeclineModel().getQf());
                tableDataModel.setQo(wells.get(i).getDeclineModel().getQo());
                tableDataModel.setTf(wells.get(i).getDeclineModel().getTf());
                tableDataModel.setWellName(wells.get(i).getNameOfWell());
                tableDataModel.setManageBox(getHox(wells.get(i), wells.get(i).getYearlyProductions()));
                if (!(wells.get(i).getVertical() == null)) {
                    if (wells.get(i).getVertical()) {
                        tableDataModel.setVertical("Vertical");
                    } else {
                        tableDataModel.setVertical("Horizontal");
                    }
                }
                tableDataModel.setMode(getActiveModeText(wells.get(i).getDeclineModel().getMode()));
                studentsTable.getItems().add(tableDataModel);
            }
        }
    }

    private static final Interpolator EXP_IN = new Interpolator() {
        @Override
        protected double curve(double t) {
            return (t == 1.0) ? 1.0 : 1 - Math.pow(2.0, -10 * t);
        }
    };

    private static final Interpolator EXP_OUT = new Interpolator() {
        @Override
        protected double curve(double t) {
            return (t == 0.0) ? 0.0 : Math.pow(2.0, 10 * (t - 1));
        }
    };

    private static class CustomDialog extends Stage {

        private ScaleTransition scale1 = new ScaleTransition();
        private ScaleTransition scale2 = new ScaleTransition();

        private SequentialTransition anim = new SequentialTransition(scale1, scale2);

        ArrayList<YearlyProduction> yearlyData;

        CustomDialog(String header, String content, ArrayList<YearlyProduction> yearlyData) {
            Pane root = new Pane();
            this.yearlyData = yearlyData;
            scale1.setFromX(0.01);
            scale1.setFromY(0.01);
            scale1.setToY(1.0);
            scale1.setDuration(Duration.seconds(0.33));
            scale1.setInterpolator(EXP_IN);
            scale1.setNode(root);

            scale2.setFromX(0.01);
            scale2.setToX(1.0);
            scale2.setDuration(Duration.seconds(0.33));
            scale2.setInterpolator(EXP_OUT);
            scale2.setNode(root);

            initStyle(StageStyle.TRANSPARENT);
            initModality(Modality.APPLICATION_MODAL);

            Rectangle bg = new Rectangle(500, 400, Color.WHITE);
            bg.setStroke(Color.GRAY);
            bg.setStrokeWidth(1.0);

            Text headerText = new Text(header);
            headerText.setFont(Font.font(20));

            Text contentText = new Text(content);
            contentText.setFont(Font.font(16));

            VBox box = new VBox(10,
                    headerText,
                    new Separator(Orientation.HORIZONTAL),
                    contentText
            );
            box.setPadding(new Insets(15));

            Button btn = new Button("OK");
            btn.setTranslateX(bg.getWidth() - 50);
            btn.setTranslateY(bg.getHeight() - 50);
            btn.setOnAction(e -> closeDialog());
            Button btn1 = new Button("Show Yearly Production");
            btn1.setTranslateX(bg.getWidth() - 250);
            btn1.setTranslateY(bg.getHeight() - 50);
            btn1.setOnAction(e -> displayYPR());
            Button btn2 = new Button("Show Financial Analysis");
            btn2.setTranslateX(bg.getWidth() - 400);
            btn2.setTranslateY(bg.getHeight() - 50);
            btn2.setOnAction(e -> {
                try {
                    runFinancialAnalysis();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });

            root.getChildren().addAll(bg, box, btn, btn1, btn2);

            setScene(new Scene(root, null));
        }

        void openDialog() {
            show();

            anim.play();
        }

        void closeDialog() {
            anim.setOnFinished(e -> close());
            anim.setAutoReverse(true);
            anim.setCycleCount(2);
            anim.playFrom(Duration.seconds(0.66));
        }

        private void runFinancialAnalysis() throws IOException {
            Stage stage = new Stage();
            new FinancialAnalysis(yearlyData).start(stage);
            Preferences preferences = Preferences.userNodeForPackage(DeclineModels.class);
            Gson gson = FxGson.create();

            preferences.put("ids", gson.toJson(yearlyData));
            System.out.println("\n\n\n\n" + gson.toJson(yearlyData) + "\n\n\n\n");


            closeDialog();
        }

        void displayYPR() {
            closeDialog();
            String res = "";
            for (int i = 0; i < yearlyData.size(); i++) {
                res += yearlyData.get(i).getYear() + ": Production Per Year ==> " + yearlyData.get(i).getProduction() + "\n";
            }
            CustomDialog customDialog = new CustomDialog("Result", res, yearlyData);
            customDialog.openDialog();
        }
    }

    @FXML
    private void print(MouseEvent event) {
        HashMap<Float, Double> hashMap = new HashMap<>();
        for (int i = 0; i < wells.size(); i++) {
            ArrayList<YearlyProduction> yearlyProductions = wells.get(i).getYearlyProductions();
            for (int j = 0; j < yearlyProductions.size(); j++) {
                YearlyProduction yearlyProduction = yearlyProductions.get(j);
                yearlyProduction.setYear(String.valueOf(Math.ceil(Double.parseDouble(yearlyProduction.getYear()))));
                if (hashMap.containsKey(Float.valueOf(yearlyProduction.getYear()))) {
                    Double prev = hashMap.get(Float.valueOf(yearlyProduction.getYear()));
                    hashMap.put(Float.valueOf(yearlyProduction.getYear()), prev + yearlyProduction.getProduction());
                } else {
                    hashMap.put(Float.valueOf(yearlyProduction.getYear()), yearlyProduction.getProduction());
                }
            }
        }

        sorted(hashMap);

    }

    // Function to sort map by Key
    public static void sorted(HashMap map) {
        ArrayList<Float> sortedKeys = new ArrayList<Float>(map.keySet());

        Collections.sort(sortedKeys);
        ArrayList<FinancialData> financialDataArrayList = new ArrayList<>();

        for (Float x : sortedKeys) {
            System.out.println("Key = " + x + ", Value = " + map.get(x));
            FinancialData financialData = new FinancialData();
            financialData.setYear(String.valueOf(x));
            financialData.setProduction((Double) map.get(x));
            financialDataArrayList.add(financialData);
        }

        Preferences preference = Preferences.userNodeForPackage(InitFormController.class);
        preference.put("fieldAnalysis", new Gson().toJson(financialDataArrayList));

        new FieldAnalysisStage().start(new Stage());
    }



}
