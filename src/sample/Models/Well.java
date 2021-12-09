package sample.Models;

import java.util.ArrayList;

public class Well {
    String wellId;
    String nameOfWell;
    Boolean vertical;
    DeclineModel declineModel;
    ArrayList<YearlyProduction> yearlyProductions;

    public Well() {
    }

    ArrayList<FinancialData> financialData;


    public Well(String wellId, String nameOfWell, Boolean vertical, DeclineModel declineModel, ArrayList<YearlyProduction> yearlyProductions, ArrayList<FinancialData> financialData) {
        this.wellId = wellId;
        this.nameOfWell = nameOfWell;
        this.vertical = vertical;
        this.declineModel = declineModel;
        this.yearlyProductions = yearlyProductions;
        this.financialData = financialData;
    }

    public String getWellId() {
        return wellId;
    }

    public void setWellId(String wellId) {
        this.wellId = wellId;
    }

    public String getNameOfWell() {
        return nameOfWell;
    }

    public void setNameOfWell(String nameOfWell) {
        this.nameOfWell = nameOfWell;
    }

    public Boolean getVertical() {
        return vertical;
    }

    public void setVertical(Boolean vertical) {
        this.vertical = vertical;
    }

    public DeclineModel getDeclineModel() {
        return declineModel;
    }

    public void setDeclineModel(DeclineModel declineModel) {
        this.declineModel = declineModel;
    }

    public ArrayList<YearlyProduction> getYearlyProductions() {
        return yearlyProductions;
    }

    public void setYearlyProductions(ArrayList<YearlyProduction> yearlyProductions) {
        this.yearlyProductions = yearlyProductions;
    }

    public ArrayList<FinancialData> getFinancialData() {
        return financialData;
    }

    public void setFinancialData(ArrayList<FinancialData> financialData) {
        this.financialData = financialData;
    }
}
