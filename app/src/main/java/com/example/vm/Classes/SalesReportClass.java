package com.example.vm.Classes;

public class SalesReportClass {
    String invoiceDate,invoiceNumber,partyName,customerGst,productName,hsnCode,
            quantity,rate,sgstPercentage,cgstPercentage,sGst,cGst,amount;

    public SalesReportClass() {
    }

    public SalesReportClass(String invoiceNumber, String invoiceDate, String partyName, String customerGst, String productName, String hsnCode, String quantity, String rate, String sgstPercentage, String cgstPercentage, String sGst, String cGst, String amount) {
        this.invoiceDate = invoiceDate;
        this.invoiceNumber = invoiceNumber;
        this.partyName = partyName;
        this.customerGst = customerGst;
        this.productName = productName;
        this.hsnCode = hsnCode;
        this.quantity = quantity;
        this.rate = rate;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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
}
