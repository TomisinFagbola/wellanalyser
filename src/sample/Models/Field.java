package sample.Models;

import java.util.ArrayList;

public class Field {
    String fieldId;
    String fieldName;
    Boolean primary;
    ArrayList<Well> wells;
    ArrayList<YearlyProduction> yearlyProductions;
    ArrayList<FinancialData> financialData;

    public Field(String fieldId, String fieldName, Boolean primary, ArrayList<Well> wells, ArrayList<YearlyProduction> yearlyProductions, ArrayList<FinancialData> financialData) {
        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.primary = primary;
        this.wells = wells;
        this.yearlyProductions = yearlyProductions;
        this.financialData = financialData;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public Field() {
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public ArrayList<Well> getWells() {
        return wells;
    }

    public void setWells(ArrayList<Well> wells) {
        this.wells = wells;
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
