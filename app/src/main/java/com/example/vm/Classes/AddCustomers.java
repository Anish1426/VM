package com.example.vm.Classes;

public class AddCustomers {
    String gstNo,cusName,cusAddress,cusState,cusNumber;

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusAddress() {
        return cusAddress;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }

    public String getCusState() {
        return cusState;
    }

    public void setCusState(String cusState) {
        this.cusState = cusState;
    }

    public String getCusNumber() {
        return cusNumber;
    }

    public void setCusNumber(String cusNumber) {
        this.cusNumber = cusNumber;
    }

    public AddCustomers(String gstNo, String cusName, String cusAddress, String cusState, String cusNumber) {
        this.gstNo = gstNo;
        this.cusName = cusName;
        this.cusAddress = cusAddress;
        this.cusState = cusState;
        this.cusNumber = cusNumber;
    }

    public AddCustomers() {
    }
}
