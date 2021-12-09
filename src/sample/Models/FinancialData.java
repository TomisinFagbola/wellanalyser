package sample.Models;

public class FinancialData {
    String year;
    Double production, revenue, tax, royalty, operatingCost, bankRate, wellCost, NCF, NPD;

    public FinancialData() {
    }

    public FinancialData(String year, Double production, Double revenue, Double tax, Double royalty, Double operatingCost, Double bankRate, Double wellCost, Double NCF, Double NPD) {
        this.year = year;
        this.production = production;
        this.revenue = revenue;
        this.tax = tax;
        this.royalty = royalty;
        this.operatingCost = operatingCost;
        this.bankRate = bankRate;
        this.wellCost = wellCost;
        this.NCF = NCF;
        this.NPD = NPD;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Double getProduction() {
        return production;
    }

    public void setProduction(Double production) {
        this.production = production;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getRoyalty() {
        return royalty;
    }

    public void setRoyalty(Double royalty) {
        this.royalty = royalty;
    }

    public Double getOperatingCost() {
        return operatingCost;
    }

    public void setOperatingCost(Double operatingCost) {
        this.operatingCost = operatingCost;
    }

    public Double getBankRate() {
        return bankRate;
    }

    public void setBankRate(Double bankRate) {
        this.bankRate = bankRate;
    }

    public Double getWellCost() {
        return wellCost;
    }

    public void setWellCost(Double wellCost) {
        this.wellCost = wellCost;
    }

    public Double getNCF() {
        return NCF;
    }

    public void setNCF(Double NCF) {
        this.NCF = NCF;
    }

    public Double getNPD() {
        return NPD;
    }

    public void setNPD(Double NPD) {
        this.NPD = NPD;
    }
}
