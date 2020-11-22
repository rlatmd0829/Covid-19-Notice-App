package com.example.myapplication;

public class Notice {

    String notice;
    String name;
    String data;

    public Notice(String notice, String name, String data) {
        this.notice = notice;
        this.name = name;
        this.data = data;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
