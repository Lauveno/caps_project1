package com.example.caps_project1;

public class Board {
    private String title; //for title
    private String content; //for content

    public Board(){}

    public Board(String title, String content){
        this.title = title;
        this.content = content;
    }

    public String getTitle(){return title;}
    public String getContent(){return content;}

    

}
