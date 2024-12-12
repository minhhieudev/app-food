package com.example.appfood;

import java.util.List;

public class Ingredient {
    public String _id;  // ID của nguyên liệu
    public String name;  // Tên nguyên liệu
    public String image;  // Hình ảnh của nguyên liệu
    public String description;  // Mô tả nguyên liệu
    public List<ITag> iTags;  // Các tag của nguyên liệu
    public IGroup iGroupID;  // Nhóm của nguyên liệu

    // Constructor
    public Ingredient(String _id, String name, String image, String description, List<ITag> iTags, IGroup iGroupID) {
        this._id = _id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.iTags = iTags;
        this.iGroupID = iGroupID;
    }

    // Getter và Setter
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ITag> getiTags() {
        return iTags;
    }

    public void setiTags(List<ITag> iTags) {
        this.iTags = iTags;
    }

    public IGroup getiGroupID() {
        return iGroupID;
    }

    public void setiGroupID(IGroup iGroupID) {
        this.iGroupID = iGroupID;
    }
}
