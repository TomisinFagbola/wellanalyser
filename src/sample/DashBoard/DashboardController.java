package sample.DashBoard;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.prism.paint.Color;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.hildan.fxgson.FxGson;
import sample.Graphs.MainAppLauncher;
import sample.Graphs.ZoomableLineChart;
import sample.Models.CompanyModel;
import sample.NewCompanyForm.InitFormController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class DashboardController implements Initializable {
    @FXML
    private GridPane gridPane;

    @FXML
    private Button viewProjectBtn;

    @FXML
    private Button tableDataBtn;

    @FXML
    private Button graphsBtn;

    @FXML
    private Button wellAnalysisBtn;

    @FXML
    private Button analysisBtn;

    @FXML
    private Button btnSettings;
    @FXML
    private Button declineModels;
    @FXML
    private Button aboutAppBtn;
    @FXML
    private Pane pnlViewProjects;
    @FXML
    private Pane pnlTableData;
    @FXML
    private Pane pnlGraphs;
    @FXML
    private Pane pnlWellAnalysis;
    @FXML
    private Pane pnlDeclineModels;
    @FXML
    private Pane pnlFinancialAnalysis;
    @FXML
    private Pane pnlSettings;
    @FXML
    private Pane pnlAboutApp;

    ArrayList<String> ids;

    @FXML
    private Label companyName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Node[] nodes = new Node[20];
        Preferences preference = Preferences.userNodeForPackage(InitFormController.class);
        Gson gson = FxGson.create();
        ids = gson.fromJson(preference.get("ids", ""), new TypeToken<ArrayList<String>>() {
        }.getType());
        if (ids != null) {
            for (int i = 0; i < ids.size(); i++) {
                String cmString = preference.get(ids.get(i), "");
                System.out.println(cmString);
                CompanyModel companyModel  = gson.fromJson(cmString,CompanyModel.class);
                if(companyModel.getPrimary()){
                    companyName.setText(companyModel.getCompanyName()+"\n\nUser: "+companyModel.getUserName());
                }
            }
        }
        ListView listView = new ListView();
        Label label = new Label();
        label.setText("Select Company");
        listView.prefHeight(800);
        listView.prefWidth(500);

        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        for (int i = 0; i < nodes.length; i++) {
            try {

                final int j = i;
                nodes[i] = FXMLLoader.load(getClass().getResource("CompaniesCard.fxml"));

                //give the items some effect

                nodes[i].setOnMouseEntered(event -> {
                    nodes[j].setStyle("-fx-background-color : #0A0E3F");

                });
                nodes[i].setOnMouseExited(event -> {
                    nodes[j].setStyle("-fx-background-color : #02030A");
                });
                listView.getItems().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Button button = new Button("Read Selected Value");

        button.setOnAction(event -> {
            ObservableList selectedIndices = listView.getSelectionModel().getSelectedIndices();

            for(Object o : selectedIndices){
                System.out.println("o = " + o + " (" + o.getClass() + ")");
            }
        });
        label.setStyle("-fx-text-fill: #ffffff");
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(2,2,2,2));
        label.setFont(new Font(18));
        listView.setStyle("@/style.css");
        VBox vBox = new VBox(label,listView, button);

        pnlViewProjects.getChildren().add(vBox);
        pnlViewProjects.toFront();

    }


    public void handleClicks(ActionEvent actionEvent) throws Exception {
        if (actionEvent.getSource() == viewProjectBtn) {
            /*Stage initStage = new Stage();
            new MainAppLauncher().start(initStage);
            initStage.setTitle("Table View Sample");
            //initStage.setWidth(450);
            //initStage.setHeight(550);

            //defining the axes
            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Number of Month");
            //creating the chart
            final LineChart<Number, Number> lineChart =
                    new LineChart<Number, Number>(xAxis, yAxis);

            lineChart.setTitle("Stock Monitoring, 2010");
            //defining a series
            XYChart.Series series = new XYChart.Series();
            lineChart.autosize();
            lineChart.applyCss();

            series.setName("My portfolio");
            //populating the series with data
            for(int i = 2020;i<3021;i++){
                series.getData().add(new XYChart.Data(i, i));
            }

            lineChart.getData().add(series);
            Scene scene = new Scene(lineChart, 800, 600);
            lineChart.setStyle("");
            initStage.setScene(scene);
            initStage.show();*/
            pnlViewProjects.setStyle("-fx-background-color : #02030A");
            pnlViewProjects.toFront();
        }
        if (actionEvent.getSource() == tableDataBtn) {
            pnlTableData.getChildren().removeAll();
            pnlTableData.setStyle("-fx-background-color :  #02030A");
            pnlTableData.getChildren().add(FXMLLoader.load(getClass().getResource("Home.fxml")));
            pnlTableData.toFront();
        }
        if (actionEvent.getSource() == declineModels) {
            pnlTableData.getChildren().removeAll();
            pnlDeclineModels.setStyle("-fx-background-color :  #f2f2f2");
            pnlDeclineModels.getChildren().add(FXMLLoader.load(getClass().getResource("declineModels.fxml")));
            pnlDeclineModels.toFront();
        }
        if (actionEvent.getSource() == graphsBtn) {
            pnlGraphs.setStyle("-fx-background-color : #02030A");
            pnlGraphs.toFront();
        }
        if (actionEvent.getSource() == wellAnalysisBtn) {
            pnlWellAnalysis.setStyle("-fx-background-color : #02030A");
            pnlWellAnalysis.toFront();
        }
        if (actionEvent.getSource() == analysisBtn) {
            pnlFinancialAnalysis.setStyle("-fx-background-color : #02030A");
            pnlFinancialAnalysis.toFront();
        }
        if (actionEvent.getSource() == btnSettings) {
            pnlSettings.setStyle("-fx-background-color : #464fff");
            pnlSettings.toFront();
        }
        if (actionEvent.getSource() == aboutAppBtn) {
            pnlAboutApp.setStyle("-fx-background-color : #000000");
            pnlAboutApp.toFront();
        }

    }
}
