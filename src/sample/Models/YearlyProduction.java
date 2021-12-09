package sample.Models;

public class YearlyProduction {
    String year;
    Double production;

    public YearlyProduction(String year, Double production) {
        this.year = year;
        this.production = production;
    }

    public YearlyProduction() {
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
}
