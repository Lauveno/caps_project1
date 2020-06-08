package com.example.caps_project1;

public class PharmDTO_hospital {
    private String locality; //시군명
    private String name; //사업장명
    private String state; //영업상태
    private String number; //전화번호
    private String address; //주소

    public String getLocality() {
        return locality;
    }
    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}