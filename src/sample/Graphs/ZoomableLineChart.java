package sample.Graphs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.hildan.fxgson.FxGson;
import sample.Models.CompanyModel;
import sample.Models.YearlyProductionModel;
import sample.NewCompanyForm.InitFormController;
import sample.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.prefs.Preferences;

public class ZoomableLineChart extends Application {

    private static final int NUM_DATA_POINTS = 3022;
    ArrayList<String> ids;
    ArrayList<YearlyProductionModel> graphDataModels = new ArrayList<>();
    int lowerBand = 0, upperBand = 0;
    String id;
    Node[] nodes;
    Preferences preference;
    Gson gson;
    CompanyModel companyModel;

    @Override
    public void init() throws Exception {
        preference = Preferences.userNodeForPackage(InitFormController.class);
        gson = FxGson.create();
        ids = gson.fromJson(preference.get("ids", ""), new TypeToken<ArrayList<String>>() {
        }.getType());
        if (ids != null) {
            for (int i = 0; i < ids.size(); i++) {
                String cmString = preference.get(ids.get(i), "");
                companyModel = gson.fromJson(cmString, CompanyModel.class);
                if (companyModel.getPrimary()) {
                    //lowerBand = companyModel.getStartYear();
                    //upperBand = companyModel.getEndYear();
                    id = companyModel.getId();
                    //companyName.setText(companyModel.getCompanyName()+"\n\nUser: "+companyModel.getUserName());
                }
            }
        }


        String prodData = preference.get(id + "tableData", "");
        if (prodData.equals("") || prodData.equals(null)) {
        } else {
            graphDataModels = gson.fromJson(prodData, new TypeToken<ArrayList<YearlyProductionModel>>() {
            }.getType());


        }
    }


    ArrayList<MenuItem> menuItems = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        final LineChart<Number, Number>[] chart = new LineChart[]{createChart()};

        final StackPane chartContainer = new StackPane();
        Label label = new Label();
        label.setText("Select A Year that you wish to extrapolate to:");
        SplitMenuButton splitMenuButton = new SplitMenuButton();
        splitMenuButton.setText("Select Year");
        for (int i = 0; i < 30; i++) {
            //MenuItem menuItem = new MenuItem("" + (i + companyModel.getEndYear()));
            //menuItems.add(menuItem);
            //plitMenuButton.getItems().add(menuItem);

        }

        for (int i = 0; i < menuItems.size(); i++) {
            int finalI = i;
            menuItems.get(i).setOnAction((e) -> {
                int expYear = Integer.parseInt(menuItems.get(finalI).getText());
                //int expLength = expYear - companyModel.getEndYear();
                upperBand = expYear;
                int x1, x2, y1, y2;
                x1 = graphDataModels.get(graphDataModels.size() - 2).getProduction();
                x2 = graphDataModels.get(graphDataModels.size() - 1).getProduction();
                y1 = graphDataModels.get(graphDataModels.size() - 2).getYear();
                y2 = graphDataModels.get(graphDataModels.size() - 1).getYear();


                double[][] d = {{(y1), (x1)},
                        {(y2), (x2)}};

                int x3 = (int) extrapolate(d, expYear);

                chartContainer.getChildren().removeAll();
                graphDataModels.add(new YearlyProductionModel(expYear, x3, 0));
                Stage stage = new Stage();
                start(stage);
                Toast.makeText(stage,"From software analysis in year "+expYear + " production would be: "+x3,1000,500,700);
                graphDataModels.remove(graphDataModels.size()-1);
            });


        }
        chartContainer.getChildren().add(chart[0]);

        final Rectangle zoomRect = new Rectangle();
        zoomRect.setManaged(false);
        zoomRect.setFill(Color.LIGHTSEAGREEN.deriveColor(0, 1, 1, 0.5));
        chartContainer.getChildren().add(zoomRect);

        setUpZooming(zoomRect, chart[0]);

        final VBox controls = new VBox(10);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER);
        final HBox extpo = new HBox(10);
        extpo.setPadding(new Insets(10));
        extpo.setAlignment(Pos.CENTER);


        final Button zoomButton = new Button("Zoom");
        final Button resetButton = new Button("Reset");
        zoomButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                doZoom(zoomRect, chart[0]);
            }
        });
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final NumberAxis xAxis = (NumberAxis) chart[0].getXAxis();
                xAxis.setLowerBound(graphDataModels.get(0).getProduction());
                xAxis.setUpperBound(graphDataModels.get(graphDataModels.size() - 1).getProduction());
                final NumberAxis yAxis = (NumberAxis) chart[0].getYAxis();
                yAxis.setLowerBound(lowerBand);
                yAxis.setUpperBound(upperBand);

                zoomRect.setWidth(0);
                zoomRect.setHeight(0);
            }
        });
        final BooleanBinding disableControls =
                zoomRect.widthProperty().lessThan(5)
                        .or(zoomRect.heightProperty().lessThan(5));
        //zoomButton.disableProperty().bind(disableControls);
        extpo.getChildren().addAll(label, splitMenuButton);
        controls.getChildren().addAll(zoomButton, resetButton, extpo);

        final BorderPane root = new BorderPane();
        root.setCenter(chartContainer);
        root.setBottom(controls);

        final Scene scene = new Scene(root, 800, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private LineChart<Number, Number> createChart() {
        final NumberAxis xAxis = createAxisX();
        final NumberAxis yAxis = createAxisY();
        final LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(true);
        chart.setCreateSymbols(true);
        chart.setData(generateChartData());
        return chart;
    }

    private NumberAxis createAxisX() {
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(graphDataModels.get(0).getProduction());
        xAxis.setUpperBound(graphDataModels.get(graphDataModels.size() - 1).getProduction());

        xAxis.setTickUnit(1000);
        return xAxis;
    }

    private NumberAxis createAxisY() {
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(lowerBand);
        xAxis.setUpperBound(upperBand);
        xAxis.setTickUnit(1);
        return xAxis;
    }

    private ObservableList<Series<Number, Number>> generateChartData() {
        final Series<Number, Number> series = new Series<>();
        series.setName("Table of Year against Production for:\n" + companyModel.getCompanyName() + " From: " +  " - " );
        final Random rng = new Random();
        for (int i = 0; i < graphDataModels.size(); i++) {
            Data<Number, Number> dataPoint = new Data<Number, Number>(graphDataModels.get(i).getProduction(), graphDataModels.get(i).getYear());
            series.getData().add(dataPoint);
        }
        return FXCollections.observableArrayList(Collections.singleton(series));
    }

    private void setUpZooming(final Rectangle rect, final Node zoomingNode) {
        final ObjectProperty<Point2D> mouseAnchor = new SimpleObjectProperty<>();
        zoomingNode.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseAnchor.set(new Point2D(event.getX(), event.getY()));
                rect.setWidth(0);
                rect.setHeight(0);
            }
        });
        zoomingNode.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();
                rect.setX(Math.min(x, mouseAnchor.get().getX()));
                rect.setY(Math.min(y, mouseAnchor.get().getY()));
                rect.setWidth(Math.abs(x - mouseAnchor.get().getX()));
                rect.setHeight(Math.abs(y - mouseAnchor.get().getY()));
            }
        });
    }


    /*
    *
    double[][] d = {{ 1.2(x1), 2.7(y1) },
    *               { 1.4(x2), 3.1 (y2)}};
    double x = 2.7;
    System.out.println("Value of y at x = 2.7 : " + extra_polate(d, x));
    * */

    static double extrapolate(double[][] d, double x) {
        double y = d[0][1] + (x - d[0][0]) / (d[1][0] - d[0][0]) * (d[1][1] - d[0][1]);

        return y;
    }


    private void doZoom(Rectangle zoomRect, LineChart<Number, Number> chart) {
        Point2D zoomTopLeft = new Point2D(zoomRect.getX(), zoomRect.getY());
        Point2D zoomBottomRight = new Point2D(zoomRect.getX() + zoomRect.getWidth(), zoomRect.getY() + zoomRect.getHeight());
        final NumberAxis yAxis = (NumberAxis) chart.getYAxis();
        Point2D yAxisInScene = yAxis.localToScene(0, 0);
        final NumberAxis xAxis = (NumberAxis) chart.getXAxis();
        Point2D xAxisInScene = xAxis.localToScene(0, 0);
        double xOffset = zoomTopLeft.getX() - yAxisInScene.getX();
        double yOffset = zoomBottomRight.getY() - xAxisInScene.getY();
        double xAxisScale = xAxis.getScale();
        double yAxisScale = yAxis.getScale();
        xAxis.setLowerBound(xAxis.getLowerBound() + xOffset / xAxisScale);
        xAxis.setUpperBound(xAxis.getLowerBound() + zoomRect.getWidth() / xAxisScale);
        yAxis.setLowerBound(yAxis.getLowerBound() + yOffset / yAxisScale);
        yAxis.setUpperBound(yAxis.getLowerBound() - zoomRect.getHeight() / yAxisScale);
        System.out.println(yAxis.getLowerBound() + " " + yAxis.getUpperBound());
        zoomRect.setWidth(0);
        zoomRect.setHeight(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
