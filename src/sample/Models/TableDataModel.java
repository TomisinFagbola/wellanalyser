package sample.Models;

import javafx.scene.layout.HBox;

public class TableDataModel {
    String id;
    String wellName;
    Double qo,qf,npd,a,b,tf;
    HBox manageBox;
    String mode;
    String vertical;

    public HBox getManageBox() {
        return manageBox;
    }

    public void setManageBox(HBox manageBox) {
        this.manageBox = manageBox;
    }

    public TableDataModel(String id, String wellName, Double qo, Double qf, Double npd, Double a, Double b, Double tf, HBox manageBox, String mode, String vertical) {
        this.id = id;
        this.wellName = wellName;
        this.qo = qo;
        this.qf = qf;
        this.npd = npd;
        this.a = a;
        this.b = b;
        this.tf = tf;
        this.manageBox = manageBox;
        this.mode = mode;
        this.vertical = vertical;
    }

    public TableDataModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWellName() {
        return wellName;
    }

    public void setWellName(String wellName) {
        this.wellName = wellName;
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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getVertical() {
        return vertical;
    }

    public void setVertical(String vertical) {
        this.vertical = vertical;
    }
}
