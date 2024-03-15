package com.example.vm.Classes;

public class ProductClass {
    String pCode;
    String pName;
    String pHsn;
    String quantity;
    String pRate;
    String pCgst;
    String pSgst;

    String pAmount;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getpAmount() {
        return pAmount;
    }

    public void setpAmount(String pAmount) {
        this.pAmount = pAmount;
    }

    public ProductClass(String pCode, String pName, String pHsn, String quantity, String pRate, String pCgst, String pSgst, String pAmount) {
        this.pCode = pCode;
        this.pName = pName;
        this.pHsn = pHsn;
        this.quantity = quantity;
        this.pRate = pRate;
        this.pCgst = pCgst;
        this.pSgst = pSgst;
        this.pAmount = pAmount;
    }

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpHsn() {
        return pHsn;
    }

    public void setpHsn(String pHsn) {
        this.pHsn = pHsn;
    }

    public String getpRate() {
        return pRate;
    }

    public void setpRate(String pRate) {
        this.pRate = pRate;
    }

    public String getpCgst() {
        return pCgst;
    }

    public void setpCgst(String pCgst) {
        this.pCgst = pCgst;
    }

    public String getpSgst() {
        return pSgst;
    }

    public void setpSgst(String pSgst) {
        this.pSgst = pSgst;
    }

    public ProductClass(String pCode, String pName, String pHsn, String pRate, String pCgst, String pSgst) {
        this.pCode = pCode;
        this.pName = pName;
        this.pHsn = pHsn;
        this.pRate = pRate;
        this.pCgst = pCgst;
        this.pSgst = pSgst;
    }
}
