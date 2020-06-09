package com.example.caps_project1;

public class Board {
    private String title; //for title
    private String content; //for content
    private String username;

    public Board(){}

    public Board(String title, String content, String username){
        this.title = title;
        this.content = content;
        this.username = username;
    }

    public String getTitle(){return title;}
    public String getContent(){return content;}
    public String getUsername(){return username;}
    public void setTitle(String title){this.title = title;}
    public void setContent(String content){this.title = content;}
    public void setUsername(String username){this.username = username;}

}
