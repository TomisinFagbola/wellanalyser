package sample.Models;

import java.util.ArrayList;

public class CompanyModel {
    String id;
    String companyName,userName;
    Boolean primary;
    ArrayList<Field> fields;

    public CompanyModel(String id, String companyName, String userName, Boolean primary, ArrayList<Field> fields) {
        this.id = id;
        this.companyName = companyName;
        this.userName = userName;
        this.primary = primary;
        this.fields = fields;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public CompanyModel() {

    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
