package sample.FinancialAspect;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
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
import sample.Models.*;
import sample.NewCompanyForm.InitFormController;
import sample.tableView.DeclineModels;
import sample.tableView.TableViewController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import static sample.DashBoard.DeclineModels.NO_MODE;
import static sample.tableView.DeclineModels.*;

public class FinancialAnalysis implements Initializable {
    @FXML
    private TableView<FinancialData> studentsTable;
    @FXML
    private TableColumn<FinancialData, String> year;
    @FXML
    private TableColumn<FinancialData, Double> production;
    @FXML
    private TableColumn<FinancialData, Double> revenue;
    @FXML
    private TableColumn<FinancialData, Double> tax;
    @FXML
    private TableColumn<FinancialData, Double> royalty;
    @FXML
    private TableColumn<FinancialData, Double> operatingCost;
    @FXML
    private TableColumn<FinancialData, Double> bankRate;
    @FXML
    private TableColumn<FinancialData, HBox> wellCost;
    @FXML
    private TableColumn<FinancialData, String> NCF;
    @FXML
    private TableColumn<FinancialData, String> NPD;
    @FXML
    private Label fieldNameText;
    /**
     * Initializes the controller class.
     */
    ArrayList<Field> fields;
    Field field;
    ArrayList<Well> wells;
    ArrayList<FinancialData> financialDataArrayList;

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
            for (int i = 0; i < wells.size(); i++) {
                if (wells.get(i).getWellId().equals(Preferences.userNodeForPackage(InitFormController.class).get("currentWell","nowell"))) {
                    financialDataArrayList = wells.get(i).getFinancialData();
                }
            }
        }
        studentsTable.
                setStyle("-fx-selection-bar: white; -fx-selection-bar-non-focused: salmon;");
        year.setCellValueFactory(new PropertyValueFactory<>("year"));
        production.setCellValueFactory(new PropertyValueFactory<>("production"));
        revenue.setCellValueFactory(new PropertyValueFactory<>("revenue"));
        royalty.setCellValueFactory(new PropertyValueFactory<>("royalty"));
        tax.setCellValueFactory(new PropertyValueFactory<>("tax"));
        operatingCost.setCellValueFactory(new PropertyValueFactory<>("operatingCost"));
        bankRate.setCellValueFactory(new PropertyValueFactory<>("bankRate"));
        wellCost.setCellValueFactory(new PropertyValueFactory<>("wellCost"));
        NCF.setCellValueFactory(new PropertyValueFactory<>("NCF"));
        NPD.setCellValueFactory(new PropertyValueFactory<>("NPD"));
        if (null == financialDataArrayList) {

        } else {
            for (int i = 0; i < financialDataArrayList.size(); i++) {
                FinancialData financialData = financialDataArrayList.get(i);
                studentsTable.getItems().add(financialData);
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
        financialDataArrayList.clear();
        studentsTable.getItems().clear();
        // TODO
        getData();
        if (field.getWells() == null) {
            wells = new ArrayList<>();
            fieldNameText.setText(field.getFieldName() + ":  null Wells");
        } else {
            wells = field.getWells();
            fieldNameText.setText(field.getFieldName() + ": " + field.getWells().size() + " Well(s)");
            for (int i = 0; i < wells.size(); i++) {
                if (wells.get(i).getWellId().equals(Preferences.userNodeForPackage(InitFormController.class).get("currentWell","nowell"))) {
                    financialDataArrayList = wells.get(i).getFinancialData();
                }
            }
        }
        studentsTable.
                setStyle("-fx-selection-bar: white; -fx-selection-bar-non-focused: salmon;");
        year.setCellValueFactory(new PropertyValueFactory<>("year"));
        production.setCellValueFactory(new PropertyValueFactory<>("production"));
        revenue.setCellValueFactory(new PropertyValueFactory<>("revenue"));
        royalty.setCellValueFactory(new PropertyValueFactory<>("royalty"));
        tax.setCellValueFactory(new PropertyValueFactory<>("tax"));
        operatingCost.setCellValueFactory(new PropertyValueFactory<>("operatingCost"));
        bankRate.setCellValueFactory(new PropertyValueFactory<>("bankRate"));
        wellCost.setCellValueFactory(new PropertyValueFactory<>("wellCost"));
        NCF.setCellValueFactory(new PropertyValueFactory<>("NCF"));
        NPD.setCellValueFactory(new PropertyValueFactory<>("NPD"));
        if (null == financialDataArrayList) {

        } else {
            for (int i = 0; i < financialDataArrayList.size(); i++) {
                FinancialData financialData = financialDataArrayList.get(i);
                studentsTable.getItems().add(financialData);
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

            Rectangle bg = new Rectangle(500, 400, Color.WHITESMOKE);
            bg.setStroke(Color.BLACK);
            bg.setStrokeWidth(1.5);

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
            new sample.DashBoard.FinancialAnalysis(yearlyData).start(stage);
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
    }

    private void loadDate() {

    }

}
