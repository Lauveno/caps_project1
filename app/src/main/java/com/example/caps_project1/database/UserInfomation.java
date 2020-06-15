package com.example.caps_project1.database;

public class UserInfomation {

    private String pet;
    private String phoneNumber;
    private String birth;
    private String address;

    public UserInfomation(String pet, String phoneNumber, String birth, String address) {

        this.pet = pet;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.address = address;
    }

    public String getPet() { return this.pet; }
    public void setPet(String pet) { this.pet = pet; }

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
