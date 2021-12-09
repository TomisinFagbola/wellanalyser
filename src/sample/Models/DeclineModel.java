package sample.Models;

public class DeclineModel {
    Double qo,qf,npd,a,b,tf;
    int mode;

    public DeclineModel(Double qo, Double qf, Double npd, Double a, Double b, Double tf, int mode) {
        this.qo = qo;
        this.qf = qf;
        this.npd = npd;
        this.a = a;
        this.b = b;
        this.tf = tf;
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public DeclineModel() {
    }

    public Double getQo() {
        return qo;
    }

    public void setQo(Double qo) {
        this.qo = qo;
    }

    public Double getQf() {
        return qf;
    }

    public void setQf(Double qf) {
        this.qf = qf;
    }

    public Double getNpd() {
        return npd;
    }

    public void setNpd(Double npd) {
        this.npd = npd;
    }

    public Double getA() {
        return a;
    }

    public void setA(Double a) {
        this.a = a;
    }

    public Double getB() {
        return b;
    }

    public void setB(Double b) {
        this.b = b;
    }

    public Double getTf() {
        return tf;
    }

    public void setTf(Double tf) {
        this.tf = tf;
    }
}
