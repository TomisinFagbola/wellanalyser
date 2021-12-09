package sample.Models;

public class YearlyProductionModel {
    int year,production,CF;

    public YearlyProductionModel(int year, int production, int CF) {
        this.year = year;
        this.production = production;
        this.CF = CF;
    }

    public YearlyProductionModel() {
    }

    public int getCF() {
        return CF;
    }

    public void setCF(int CF) {
        this.CF = CF;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getProduction() {
        return production;
    }

    public void setProduction(int production) {
        this.production = production;
    }
}
