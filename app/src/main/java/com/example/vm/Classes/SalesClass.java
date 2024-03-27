package com.example.vm.Classes;

public class SalesClass {

    String invoiceDate;
    String invoiceNumber;
    String partyName;
    String customerGst;
    String productName;
    String hsnCode;
    String salesQuantity;
    String salesRate;
    String sgstPercentage;
    String cgstPercentage;
    String sGst;

    public SalesClass(String invoiceDate, String invoiceNumber, String partyName, String customerGst, String productName, String hsnCode, String salesQuantity, String salesRate, String sgstPercentage, String cgstPercentage, String sGst, String cGst, String amount) {
        this.invoiceDate = invoiceDate;
        this.invoiceNumber = invoiceNumber;
        this.partyName = partyName;
        this.customerGst = customerGst;
        this.productName = productName;
        this.hsnCode = hsnCode;
        this.salesQuantity = salesQuantity;
        this.salesRate = salesRate;
        this.sgstPercentage = sgstPercentage;
        this.cgstPercentage = cgstPercentage;
        this.sGst = sGst;
        this.cGst = cGst;
        this.amount = amount;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getCustomerGst() {
        return customerGst;
    }

    public void setCustomerGst(String customerGst) {
        this.customerGst = customerGst;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getSalesQuantity() {
        return salesQuantity;
    }

    public void setSalesQuantity(String salesQuantity) {
        this.salesQuantity = salesQuantity;
    }

    public String getSalesRate() {
        return salesRate;
    }

    public void setSalesRate(String salesRate) {
        this.salesRate = salesRate;
    }

    public String getSgstPercentage() {
        return sgstPercentage;
    }

    public void setSgstPercentage(String sgstPercentage) {
        this.sgstPercentage = sgstPercentage;
    }

    public String getCgstPercentage() {
        return cgstPercentage;
    }

    public void setCgstPercentage(String cgstPercentage) {
        this.cgstPercentage = cgstPercentage;
    }

    public String getsGst() {
        return sGst;
    }

    public void setsGst(String sGst) {
        this.sGst = sGst;
    }

    public String getcGst() {
        return cGst;
    }

    public void setcGst(String cGst) {
        this.cGst = cGst;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    String cGst;
    String amount;
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
