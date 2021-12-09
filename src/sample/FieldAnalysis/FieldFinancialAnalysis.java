package sample.FieldAnalysis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Models.YearlyProduction;

import java.io.IOException;
import java.util.ArrayList;

public class FieldFinancialAnalysis extends Application {

    Parent root;

    @Override
    public void start(Stage primaryStage) throws IOException {
        root = FXMLLoader.load(getClass().getResource("fieldFinancialAnalysis.fxml"));
        Scene scene = new Scene(root);
        //primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


}
