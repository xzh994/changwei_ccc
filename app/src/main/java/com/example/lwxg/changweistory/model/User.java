package com.example.lwxg.changweistory.model;

public class User {
    private String name;
    private int day;

    public User() {
    }

    public User(String name, int day) {
        this.name = name;
        this.day = day;
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
}
