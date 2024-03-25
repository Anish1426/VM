package com.example.vm.Classes;

public class SalesClass {
    String pCode;
    String pName;
    String pHsn;
    String quantity;

    String productKey;

    String pRate;
    String pCgst;
    String pSgst;

    String pAmount;

    public SalesClass() {
    }

    public SalesClass(String pCode, String pName, String pHsn, String quantity, String pRate, String pCgst, String pSgst, String pAmount) {
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    public String getpAmount() {
        return pAmount;
    }

    public void setpAmount(String pAmount) {
        this.pAmount = pAmount;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductKey() {
        return productKey;
    }
}
