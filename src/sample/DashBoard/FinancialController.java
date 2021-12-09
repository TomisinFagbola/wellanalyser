package sample.DashBoard;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.hildan.fxgson.FxGson;
import sample.FinancialAspect.FinancialAnalysis;
import sample.FinancialAspect.FinancialAnalysisMain;
import sample.Models.*;
import sample.NewCompanyForm.InitFormController;
import sample.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class FinancialController implements Initializable {
    ArrayList<YearlyProduction> yearlyData;
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

    private void analyseData(Double op, Double wc, Double dr, Double r, Double tr, Double b, Double oc) {
        financialData.add(new FinancialData(String.valueOf(0), Double.parseDouble(String.valueOf(0)), Double.parseDouble(String.valueOf(0)), Double.parseDouble(String.valueOf(0)), Double.parseDouble(String.valueOf(0)), Double.parseDouble(String.valueOf(0)), Double.parseDouble(String.valueOf(0)), -wc, -wc, -wc));

        Double npvSum = 0.00000;
        Double ncfSum = 0.00000;
        for (int i = 0; i < yearlyData.size(); i++) {
            Double revenue = yearlyData.get(i).getProduction() * op;
            FinancialData finData = new FinancialData();
            finData.setYear(yearlyData.get(i).getYear());
            finData.setProduction(yearlyData.get(i).getProduction());
            finData.setRevenue(revenue);
            finData.setTax(revenue * (tr / 100));
            finData.setRoyalty(revenue * (r / 100));
            finData.setOperatingCost(revenue * (oc / 100));
            finData.setBankRate(revenue * (b / 100));
            finData.setWellCost((double) 0);
            Double ncf = revenue - (revenue * (tr / 100)) - (revenue * (r / 100)) - (revenue * (oc / 100)) - (revenue * (b / 100));
            Double npv = ncf / Math.pow((1 + (dr/100)), Double.parseDouble(yearlyData.get(i).getYear()));
            finData.setNCF(ncf);
            System.out.println(ncf + "  "+ dr+"   " +Double.parseDouble(yearlyData.get(i).getYear()));
            finData.setNPD(npv);
            ncfSum +=ncf;
            npvSum += npv;
            System.out.println(new Gson().toJson(finData));
            financialData.add(finData);
        }
        FinancialData finData = new FinancialData();
        finData.setNPD(npvSum);
        finData.setNCF(ncfSum);
        finData.setYear("Total");
        financialData.add(finData);
        setData(financialData);
        new FinancialAnalysisMain().start(new Stage());
    }

    private void setData(ArrayList<FinancialData> financialData) {
        getData();

    }

    ArrayList<Field> fields;
    Field field;
    ArrayList<Well> wells;
    ArrayList<FinancialData> financialDataArrayList;

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
                        if (field.getWells() == null) {
                            wells = new ArrayList<>();
                        } else {
                            wells = field.getWells();
                            for (int k = 0; k < wells.size(); k++) {
                                if (wells.get(k).getWellId().equals(Preferences.userNodeForPackage(InitFormController.class).get("currentWell", "nowell"))) {
                                    wells.get(k).setFinancialData(financialData);
                                    field.setWells(wells);
                                    fields.set(j, field);
                                    companyModels.get(i).setFields(fields);
                                    preference.put("companies", new Gson().toJson(companyModels));
                                }
                            }
                        }
                    }
                }
            }
        }
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
        yearlyData = new ArrayList<>();

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
                        if (field.getWells() == null) {
                            wells = new ArrayList<>();
                        } else {
                            wells = field.getWells();
                            for (int k = 0; k < wells.size(); k++) {
                                if (wells.get(k).getWellId().equals(Preferences.userNodeForPackage(InitFormController.class).get("currentWell", "nowell"))) {
                                    yearlyData = wells.get(k).getYearlyProductions();
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
