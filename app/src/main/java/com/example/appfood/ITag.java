package com.example.appfood;

public class ITag {
    private String _id;
    private String iTagName;
    private String color;

    // Getters và setters
    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getITagName() {
        return iTagName;
    }

    public void setITagName(String iTagName) {
        this.iTagName = iTagName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "ITag{" +
                "_id='" + _id + '\'' +
                ", iTagName='" + iTagName + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
