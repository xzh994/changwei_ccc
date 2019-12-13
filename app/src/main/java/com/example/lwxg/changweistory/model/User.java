package com.example.lwxg.changweistory.model;

public class User {
    private String name;
    private int day;
    private String head_image;

    public User() {
    }

    public User(String name, int day) {
        this.name = name;
        this.day = day;
    }

    public User(String name, int day, String head_image) {
        this.name = name;
        this.day = day;
        this.head_image = head_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", day=" + day +
                ", head_image='" + head_image + '\'' +
                '}';
    }
}
