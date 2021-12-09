package sample.FieldAnalysis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

public class FieldAnalysis implements Initializable {
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
        Preferences preference = Preferences.userNodeForPackage(InitFormController.class);
        Gson gson = FxGson.create();
        financialDataArrayList = gson.fromJson(preference.get("fieldAnalysis", ""), new TypeToken<ArrayList<FinancialData>>() {
        }.getType());
        Double yearlyProductionSum = 0.00;
        for (int i = 0; i < financialDataArrayList.size(); i++) {
            try {
                yearlyProductionSum += financialDataArrayList.get(i).getProduction();
            } catch (Exception e) {

            }
        }
        FinancialData nfinancialData = new FinancialData();
        nfinancialData.setYear("Total Yearly Production");
        nfinancialData.setProduction(yearlyProductionSum);
        financialDataArrayList.add(nfinancialData);
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
                try {
                    if (Float.parseFloat(financialData.getYear()) % 1 == 0) {
                        studentsTable.getItems().add(financialData);
                    }
                } catch (Exception e) {
                    studentsTable.getItems().add(financialData);
                }


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
        String res = "NPV : " + String.format("$ %,f", getDataFR().getNpv()) + "\n IIR : " + getDataFR().getIir() + "% \n PI : " + getDataFR().getPi() + "\n\n";
        String profitable;
        if (getDataFR().getProfitable()) {
            profitable = "Field is Profitable";
        } else {
            profitable = "Field is Not Profitable";
        }
        res = res + profitable;
        CustomDialog customDialog = new CustomDialog("Field Results", res);
        customDialog.show();
    }

    private FieldResults getDataFR() {
        Preferences preference = Preferences.userNodeForPackage(InitFormController.class);
        Gson gson = FxGson.create();
        return gson.fromJson(preference.get("fieldResults", ""), new TypeToken<FieldResults>() {
        }.getType());
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

        CustomDialog(String header, String content) {
            Pane root = new Pane();
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


            root.getChildren().addAll(bg, box, btn);

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

    }


    @FXML
    private void print(MouseEvent event) throws IOException {
        new FieldFinancialAnalysis().start(new Stage());
        ((Stage) (fieldNameText.getScene().getWindow())).close();
    }


}
