package com.example.caps_project1;

import java.io.Serializable;

public class PharmDTO_object implements Serializable {
    private float latitude;
    private float longitude;
    private String locality; //시군명 --> SIGUN_NM
    private String name; //사업장명 --> BIZPLC_NM / ENTRPS_NM
    private String state; //영업상태 --> BSN_STATE_NM
    private String number; //전화번호 --> LOCPLC_FACLT_TELNO_DTLS / TELNO / ENTRPS_TELNO
    private String address; //주소 --> REFINE_ROADNM_ADDR

    public PharmDTO_object(String locality, String name, String number,
                           String address, float latitude, float longitude) {
        this.locality = locality;
        this.name = name;
        this.number = number;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() { return this.latitude; }

    public float getLongitude() { return this.longitude; }

    public String getLocality() { return this.locality; }

    public String getName() {
        return this.name;
    }

    public String getState() { return this.state; }

    public String getNumber() { return this.number; }

    public String getAddress() {
        return this.address;
    }

}