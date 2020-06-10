package com.example.caps_project1;

import java.io.Serializable;

public class PharmDTO_hospital implements Serializable {
    private String locality; //시군명
    private String name; //사업장명
    private String state; //영업상태
    private String number; //전화번호
    private String address; //주소

    public String getLocality() { return locality; }
    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String findIndex(String name) {
        if(this.name.equals(name)) {
            return number;
        }
        else return null;
    }
}