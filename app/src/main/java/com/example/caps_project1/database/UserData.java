package com.example.caps_project1.database;

public class UserData {
    private String Name;
    private String Email;
    private String Password;
    private String PhoneNumber;
    private String Users_uid;

    public UserData(String Name, String Email, String Password, String PhoneNumber, String Users_uid) {
        this.Name = Name;
        this.Email = Email;
        this.Password = Password;
        this.PhoneNumber = PhoneNumber;
        this.Users_uid = Users_uid;
    }

    public String getName() {
        return this.Name;
    }
    public void getName(String Name) {
        this.Name = Name;
    }

    public String getEmail() { return this.Email; }
    public void getEmail(String Email) { this.Email = Email; }

    public String getPassword() {
        return this.Password;
    }
    public void getPassword(String Password) { this.Password = Password; }

    public String getPhoneNumber() { return this.PhoneNumber; }
    public void getPhoneNumber(String PhoneNumber) { this.PhoneNumber = PhoneNumber; }

    public String getUsers_uid() { return this.Users_uid; }
    public void getUsers_uid(String Users_uid) { this.Users_uid = Users_uid; }
}