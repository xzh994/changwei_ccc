package com.example.lwxg.changweistory.model;

public class Messages {
    private String id;
    private String user;
    private String tilte;
    private String content;
    private String time;
    private String type;

    public Messages() {
    }

    public Messages(String id, String user, String tilte, String content, String time, String type) {
        this.id = id;
        this.user = user;
        this.tilte = tilte;
        this.content = content;
        this.time = time;
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTilte() {
        return tilte;
    }

    public void setTilte(String tilte) {
        this.tilte = tilte;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
