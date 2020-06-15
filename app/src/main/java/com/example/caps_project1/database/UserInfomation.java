package com.example.caps_project1.database;

public class UserInfomation {

    private String pet;
    private String gender;
    private String birth;
    private String address;

    public UserInfomation(String pet, String gender, String birth, String address) {
        this.pet = pet;
        this.gender = gender;
        this.birth = birth;
        this.address = address;
    }

    public String getPet() {
        return this.pet;
    }
    public void setPet(String pet) {
        this.pet = pet;
    }

    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
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
