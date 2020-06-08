package com.example.caps_project1.database;

public class UserInfomation {
    private String name;
    private String phoneNumber;
    private String birth;
    private String address;

    public UserInfomation(String name, String phoneNumber, String birth, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.address = address;
    }


    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getBirth() {
        return this.birth;
    }
    public void setBirth(String birth) {
        this.birth = birth;
    }


    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
