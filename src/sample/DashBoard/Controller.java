package sample.DashBoard;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hildan.fxgson.FxGson;
import sample.Graphs.ZoomableLineChart;
import sample.Models.CompanyModel;
import sample.Models.YearlyProductionModel;
import sample.NewCompanyForm.InitFormController;
import sample.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class Controller implements Initializable {

    @FXML
    private VBox pnItems = null;
    @FXML
    private Label title, start, current;
    @FXML
    private Button add, edit, plot;
    @FXML
    TextField production;

    ArrayList<String> ids;
    @FXML
    private SplitMenuButton startYear;

    ArrayList<javafx.scene.control.MenuItem> menuItems = new ArrayList<>();
    ArrayList<YearlyProductionModel> graphDataModels = new ArrayList<>();

    int startYearN = 0, currentYear = 0;
    String id;
    Node[] nodes;
    Preferences preference;
    Gson gson;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        preference = Preferences.userNodeForPackage(InitFormController.class);
        gson = FxGson.create();
        ids = gson.fromJson(preference.get("ids", ""), new TypeToken<ArrayList<String>>() {
        }.getType());
        if (ids != null) {
            for (int i = 0; i < ids.size(); i++) {
                String cmString = preference.get(ids.get(i), "");
                CompanyModel companyModel = gson.fromJson(cmString, CompanyModel.class);
                if (companyModel.getPrimary()) {
                    title.setText("Table Data (" + companyModel.getCompanyName() + ")");
                    start.setText(String.valueOf(startYearN));
                    current.setText(String.valueOf(currentYear));
                    id = companyModel.getId();
                    //companyName.setText(companyModel.getCompanyName()+"\n\nUser: "+companyModel.getUserName());
                }
            }
        }


        String prodData = preference.get(id + "tableData", "");
        if (prodData.equals("") || prodData.equals(null)) {
            nodes = new Node[currentYear - startYearN];
            for (int i = 0; i < nodes.length; i++) {
                javafx.scene.control.MenuItem menuItem = new javafx.scene.control.MenuItem("" + (i + startYearN));
                menuItems.add(menuItem);
                startYear.getItems().add(menuItem);

            }
            for (int i = 0; i < menuItems.size(); i++) {
                int finalI = i;
                menuItems.get(i).setOnAction((e) -> {
                    startYear.setText(menuItems.get(finalI).getText());
                });

            }
            for (int i = 0; i < nodes.length; i++) {
                try {

                    final int j = i;
                    nodes[i] = FXMLLoader.load(getClass().getResource("TableItem.fxml"));
                    Label year = (Label) nodes[i].lookup("#year");
                    Label prodBarrels = (Label) nodes[i].lookup("#prodBarrels");Label cFLabel = (Label) nodes[i].lookup("#cf");
                    if (cFLabel != null)
                        cFLabel.setText("----");
                    if (year != null)
                        year.setText(String.valueOf((i + startYearN)));
                    if (prodBarrels != null)
                        prodBarrels.setText("----");
                    graphDataModels.add(new YearlyProductionModel(i + startYearN, 0,0));
                    preference.put(id + "tableData", gson.toJson(graphDataModels));
                    //give the items some effect

                    nodes[i].setOnMouseEntered(event -> {
                        nodes[j].setStyle("-fx-background-color : #0A0E3F");
                    });
                    nodes[i].setOnMouseExited(event -> {
                        nodes[j].setStyle("-fx-background-color : #02030A");
                    });
                    pnItems.getChildren().add(nodes[i]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            graphDataModels = gson.fromJson(prodData,new TypeToken<ArrayList<YearlyProductionModel>>() {
            }.getType());
            getCF();
            nodes = new Node[graphDataModels.size()];
            for (int i = 0; i < nodes.length; i++) {
                javafx.scene.control.MenuItem menuItem = new javafx.scene.control.MenuItem("" + (i + startYearN));
                menuItems.add(menuItem);
                startYear.getItems().add(menuItem);

            }
            for (int i = 0; i < menuItems.size(); i++) {
                int finalI = i;
                menuItems.get(i).setOnAction((e) -> {
                    startYear.setText(menuItems.get(finalI).getText());
                });

            }
            for (int i = 0; i < nodes.length; i++) {
                try {

                    final int j = i;
                    nodes[i] = FXMLLoader.load(getClass().getResource("TableItem.fxml"));
                    Label year = (Label) nodes[i].lookup("#year");
                    Label prodBarrels = (Label) nodes[i].lookup("#prodBarrels");
                    Label cFLabel = (Label) nodes[i].lookup("#cf");
                    if (cFLabel != null)
                        cFLabel.setText(String.valueOf(graphDataModels.get(i).getCF()));
                    if (year != null)
                        year.setText(String.valueOf(graphDataModels.get(i).getYear()));
                    if (prodBarrels != null)
                        prodBarrels.setText(String.valueOf(graphDataModels.get(i).getProduction()));
                     //give the items some effect

                    nodes[i].setOnMouseEntered(event -> {
                        nodes[j].setStyle("-fx-background-color : #0A0E3F");
                    });
                    nodes[i].setOnMouseExited(event -> {
                        nodes[j].setStyle("-fx-background-color : #02030A");
                    });
                    pnItems.getChildren().add(nodes[i]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @FXML
    public void handleSignIn(ActionEvent actionEvent) throws Exception {
        if (((Node) actionEvent.getSource()).getId().equals("plot")) {
            ZoomableLineChart zoomableLineChart = new ZoomableLineChart();
            zoomableLineChart.init();
            zoomableLineChart.start(new Stage());
        } else if (((Node) actionEvent.getSource()).getId().equals("edit")) {
        } else if (((Node) actionEvent.getSource()).getId().equals("add")) {
            if (startYear.getText().equals("Select Year") || production.getText().equals("")) {
                Toast.makeText(new Stage(), "Select Year and Input Production Value", 1000, 500, 500);
                return;
            }
            int prod = Integer.parseInt(production.getText());
            int year = Integer.parseInt(startYear.getText());

            for (int i = 0; i < graphDataModels.size(); i++) {
                if (graphDataModels.get(i).getYear() == year) {
                    graphDataModels.set(i, new YearlyProductionModel(year, prod,0));
                    getCF();
                    preference.put(id + "tableData", gson.toJson(graphDataModels));
                    if (graphDataModels.size() == nodes.length) {
                        add(i, year, prod,graphDataModels.get(i).getCF());
                        break;
                    }
                }
            }
        }
    }

    private void add(int i, int yearI, int prod,int cumF) {
        Label year = (Label) nodes[i].lookup("#year");
        Label prodBarrels = (Label) nodes[i].lookup("#prodBarrels");
        Label cFLabel = (Label) nodes[i].lookup("#cf");
        if (year != null)
            year.setText(String.valueOf(yearI));
        if (prodBarrels != null)
            prodBarrels.setText(String.valueOf(prod));
        if (cFLabel != null)
            cFLabel.setText(String.valueOf(cumF));
        Toast.makeText(new Stage(),"Added!",500,500,500);
    }

    private void getCF() {
        int total = 0;
        for(int i=0;i<graphDataModels.size();i++){
            if(i == 0){
                total = graphDataModels.get(i).getProduction();
            }else{
                total+=graphDataModels.get(i).getProduction();
            }
            graphDataModels.get(i).setCF(total);
        }
    }
}
