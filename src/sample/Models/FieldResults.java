package sample.Models;

public class FieldResults {
    double npv,pi;
    int iir;
    Boolean profitable;

    public FieldResults(double npv, double pi, int iir, Boolean profitable) {
        this.npv = npv;
        this.pi = pi;
        this.iir = iir;
        this.profitable = profitable;
    }

    public FieldResults() {
    }

    public double getNpv() {
        return npv;
    }

    public void setNpv(double npv) {
        this.npv = npv;
    }

    public double getPi() {
        return pi;
    }

    public void setPi(double pi) {
        this.pi = pi;
    }

    public int getIir() {
        return iir;
    }

    public void setIir(int iir) {
        this.iir = iir;
    }

    public Boolean getProfitable() {
        return profitable;
    }

    public void setProfitable(Boolean profitable) {
        this.profitable = profitable;
    }
}
