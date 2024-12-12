package com.example.appfood;

public class IGroup {
    private String _id;
    private String name;

    // Getters và setters
    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "IGroup{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
