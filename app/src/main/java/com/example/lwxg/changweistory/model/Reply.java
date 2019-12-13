package com.example.lwxg.changweistory.model;

public class Reply {
    private int id;
    private String content;
    private String time;
    private String user_name;
    private String user_day;
    private String user_head_image;

    public Reply() {
    }

    public Reply(int id, String content, String time, String user_name, String user_day, String user_head_image) {
        this.id = id;
        this.content = content;
        this.time = time;
        this.user_name = user_name;
        this.user_day = user_day;
        this.user_head_image = user_head_image;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_day='" + user_day + '\'' +
                ", user_head_image='" + user_head_image + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_day() {
        return user_day;
    }

    public void setUser_day(String user_day) {
        this.user_day = user_day;
    }

    public String getUser_head_image() {
        return user_head_image;
    }

    public void setUser_head_image(String user_head_image) {
        this.user_head_image = user_head_image;
    }
}
