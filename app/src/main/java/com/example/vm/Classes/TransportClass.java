package com.example.vm.Classes;

public class TransportClass {
    String transCode;
    String transName;
    String transNumber;
    String transMobNumber;

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }

    public String getTransNumber() {
        return transNumber;
    }

    public void setTransNumber(String transNumber) {
        this.transNumber = transNumber;
    }

    public String getTransMobNumber() {
        return transMobNumber;
    }

    public void setTransMobNumber(String transMobNumber) {
        this.transMobNumber = transMobNumber;
    }

    public TransportClass(String transCode, String transName, String transNumber, String transMobNumber) {
        this.transCode = transCode;
        this.transName = transName;
        this.transNumber = transNumber;
        this.transMobNumber = transMobNumber;
    }

}
