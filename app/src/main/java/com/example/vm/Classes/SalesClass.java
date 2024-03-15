package com.example.vm.Classes;

public class SalesClass {
        String gstNo;
        String partyName;
        String address;
        String state;

        public String getGstNo() {
            return gstNo;
        }

        public void setGstNo(String gstNo) {
            this.gstNo = gstNo;
        }

        public String getPartyName() {
            return partyName;
        }

        public void setPartyName(String partyName) {
            this.partyName = partyName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        String contact;

        public SalesClass(String gstNo, String partyName, String address, String state, String contact) {
            this.gstNo = gstNo;
            this.partyName = partyName;
            this.address = address;
            this.state = state;
            this.contact = contact;
        }
    }

