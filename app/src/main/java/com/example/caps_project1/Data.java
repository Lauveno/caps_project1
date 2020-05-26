package com.example.caps_project1;

public class Data {
    //news data를 위한 class
    private String title; //id : textView_title
    private String content; //id : textView_content (url)

    public Data(){

    }

    public Data(String title, String content){
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}