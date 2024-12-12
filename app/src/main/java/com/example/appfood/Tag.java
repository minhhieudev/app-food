package com.example.appfood;

public class Tag {
    private String id; // ID của tag
    private String iTagName; // Tên của tag
    private String color; // Màu sắc của tag

    // Constructor
    public Tag(String id, String iTagName, String color) {
        this.id = id;
        this.iTagName = iTagName;
        this.color = color;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getiTagName() {
        return iTagName;
    }

    public String getColor() {
        return color;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setiTagName(String iTagName) {
        this.iTagName = iTagName;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
