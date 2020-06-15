package com.example.caps_project1.database;

public class WriteInfo {
    private String title;
    private String Contents;
    private String publisher;

    public WriteInfo(String title, String Contents, String publisher) {
        this.title = title;
        this.Contents = Contents;
        this.publisher = publisher;
    }


    public String getTitle() {
        return this.title;
    }
    public void getTitle(String title) {
        this.title = title;
    }


    public String getContents() {
        return this.Contents;
    }
    public void getContents(String Contents) {
        this.Contents = Contents;
    }

    public String getPublisher() {
        return this.publisher;
    }
    public void getPublisher(String publisher) {
        this.publisher = publisher;
    }

}
