package sample.DashBoard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Models.YearlyProduction;

import java.io.IOException;
import java.util.ArrayList;

public class FinancialAnalysis extends Application {

    Parent root;
    ArrayList<YearlyProduction> yearlyData;

    public FinancialAnalysis(ArrayList<YearlyProduction> yearlyData) {
        this.yearlyData = yearlyData;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        root = FXMLLoader.load(getClass().getResource("financialAnalysis.fxml"));
        Scene scene = new Scene(root);
        //primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


}
