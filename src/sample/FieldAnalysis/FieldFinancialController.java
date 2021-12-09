package sample.FieldAnalysis;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.hildan.fxgson.FxGson;
import sample.FinancialAspect.FinancialAnalysisMain;
import sample.Models.*;
import sample.NewCompanyForm.InitFormController;
import sample.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class FieldFinancialController implements Initializable {

    ArrayList<FinancialData> financialData;
    @FXML
    private JFXTextField oilPrice, wellCost, discountRate, royalty, taxRate, bankRate, operatingCost;

    @FXML
    public void handleSignIn(ActionEvent actionEvent) throws Exception {
        if (((Node) actionEvent.getSource()).getId().equals("analyseBtn")) {
            // yearlyProductionModels.clear();
            // result.clear();
            String op, wc, dr, r, tr, b, oc;
            op = oilPrice.getText();
            wc = wellCost.getText();
            dr = discountRate.getText();
            r = taxRate.getText();
            tr = bankRate.getText();
            b = royalty.getText();
            oc = operatingCost.getText();

            if (!isNumberAvailable(op) || !isNumberAvailable(wc) || !isNumberAvailable(dr) || !isNumberAvailable(r) || !isNumberAvailable(tr) || !isNumberAvailable(b) || !isNumberAvailable(oc)) {
                Toast.makeText(new Stage(), "Kindly Fill the form Completely", 2500, 500, 500);
            } else {
                analyseData(Double.parseDouble(op), Double.parseDouble(wc), Double.parseDouble(dr), Double.parseDouble(r), Double.parseDouble(tr), Double.parseDouble(b), Double.parseDouble(oc));
            }
        }

    }

    double totalNpv;
    int discountRateIter;

    private void analyseData(Double op, Double wc, Double dr, Double r, Double tr, Double b, Double oc) {
        financialData.add(new FinancialData(String.valueOf(0), Double.parseDouble(String.valueOf(0)), Double.parseDouble(String.valueOf(0)), Double.parseDouble(String.valueOf(0)), Double.parseDouble(String.valueOf(0)), Double.parseDouble(String.valueOf(0)), Double.parseDouble(String.valueOf(0)), -wc, -wc, -wc));
        //financialDataArrayList.add(new FinancialData(String.valueOf(0), Double.parseDouble(String.valueOf(0)), Double.parseDouble(String.valueOf(0)), Double.parseDouble(String.valueOf(0)), Double.parseDouble(String.valueOf(0)), Double.parseDouble(String.valueOf(0)), Double.parseDouble(String.valueOf(0)), -wc, -wc, -wc));
        Double npvSum = -wc;
        Double ncfSum = -wc;
        Double npvSum1 = -wc;
        for (int i = 0; i < financialDataArrayList.size(); i++) {
            try {
                Double revenue = financialDataArrayList.get(i).getProduction() * op;
                FinancialData finData = new FinancialData();
                finData.setYear(financialDataArrayList.get(i).getYear());
                finData.setProduction(financialDataArrayList.get(i).getProduction());
                finData.setRevenue(revenue);
                finData.setTax(revenue * (tr / 100f));
                finData.setRoyalty(revenue * (r / 100f));
                finData.setOperatingCost(revenue * (oc / 100f));
                finData.setBankRate(revenue * (b / 100f));
                finData.setWellCost((double) 0);
                Double ncf = revenue - (revenue * (tr / 100f)) - (revenue * (r / 100f)) - (revenue * (oc / 100f)) - (revenue * (b / 100f));
                Double npv = ncf / Math.pow((1 + (dr / 100f)), Double.parseDouble(financialDataArrayList.get(i).getYear()));
                finData.setNCF(ncf);
                System.out.println(ncf + "  " + dr + "   " + Double.parseDouble(financialDataArrayList.get(i).getYear()));
                finData.setNPD(npv);
                ncfSum += ncf;
                npvSum += npv;
                System.out.println(new Gson().toJson(finData));
                financialData.add(finData);
            } catch (Exception x) {
            }

        }

        int iir = 0;
        for (int j = 0; j < 100; j++) {
            for (int i = 0; i < financialDataArrayList.size(); i++) {
                try {
                    Double revenue = financialDataArrayList.get(i).getProduction() * op;

                    Double ncf = revenue - (revenue * (tr / 100f)) - (revenue * (r / 100f)) - (revenue * (oc / 100f)) - (revenue * (b / 100f));
                    Double npv = ncf / Math.pow((1 + (j / 100f)), Double.parseDouble(financialDataArrayList.get(i).getYear()));
                    npvSum1 += npv;

                } catch (Exception x) {
                    System.out.println(x.getMessage());
                }
            }
            if (npvSum1 <= 0) {
                iir = j;
                break;
            }
            System.out.println("npvs = " + npvSum1 + " at " + j + " discount rate");
            npvSum1 = -wc;
        }
        double pii = npvSum / wc;
        FieldResults fieldResults = new FieldResults();
        fieldResults.setIir(iir);
        fieldResults.setNpv(npvSum);
        fieldResults.setPi(pii);
        System.out.println("npv = " + npvSum);
        System.out.println("iir = " + iir);
        System.out.println("pi = " + pii);
        if (iir > dr && (npvSum - Math.abs(npvSum)) == 0) {
            fieldResults.setProfitable(true);
        } else {
            fieldResults.setProfitable(false);
        }

        FinancialData finData = new FinancialData();
        finData.setNPD(npvSum);
        finData.setNCF(ncfSum);
        finData.setYear("Total");
        financialData.add(finData);
        setData(financialData,fieldResults);
        new FieldAnalysisStage().start(new Stage());
    }

    private void setData(ArrayList<FinancialData> financialData,FieldResults fieldResults) {
        getData();

        Preferences preference = Preferences.userNodeForPackage(InitFormController.class);
        preference.put("fieldAnalysis", new Gson().toJson(financialData));
        preference.put("fieldResults", new Gson().toJson(fieldResults));


    }

    ArrayList<FinancialData> financialDataArrayList;

    private void getData() {
        Preferences preference = Preferences.userNodeForPackage(InitFormController.class);
        Gson gson = FxGson.create();
        financialDataArrayList = gson.fromJson(preference.get("fieldAnalysis", ""), new TypeToken<ArrayList<FinancialData>>() {
        }.getType());
    }
    private void getDataFR() {
        Preferences preference = Preferences.userNodeForPackage(InitFormController.class);
        Gson gson = FxGson.create();
        financialDataArrayList = gson.fromJson(preference.get("fieldResults", ""), new TypeToken<ArrayList<FinancialData>>() {
        }.getType());
    }

    private Boolean isNumberAvailable(String string) {
        if (string == null || string.equals("")) {
            return false;
        } else {
            try {
                double d = Double.parseDouble(string);
            } catch (NumberFormatException nfe) {
                Toast.makeText(new Stage(), "Invalid Input", 1500, 500, 500);
                return false;
            }
            return true;
        }
    }

    ArrayList<String> ids;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        financialData = new ArrayList<>();

        Preferences preference = Preferences.userNodeForPackage(InitFormController.class);
        Gson gson = FxGson.create();
        financialDataArrayList = gson.fromJson(preference.get("fieldAnalysis", ""), new TypeToken<ArrayList<FinancialData>>() {
        }.getType());


    }
}
