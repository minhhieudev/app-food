package com.example.appfood.Model;

public class Group {
    private String id; // ID của nhóm
    private String name; // Tên của nhóm

    // Constructor
    public Group(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
