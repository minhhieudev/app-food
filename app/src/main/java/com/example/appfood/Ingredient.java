package com.example.appfood;

import java.util.List;

public class Ingredient {
    private String _id;
    private String name;
    private String image;
    private String description;
    private List<ITag> iTags;
    private IGroup iGroupID;
    private boolean isChecked;  // Thêm thuộc tính để theo dõi trạng thái checkbox

    // Constructor
    public Ingredient(String _id, String name, String image, String description, List<ITag> iTags, IGroup iGroupID) {
        this._id = _id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.iTags = iTags;
        this.iGroupID = iGroupID;
        this.isChecked = false;  // Mặc định là chưa được chọn
    }

    // Getters và Setters
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

    public List<ITag> getITags() {
        return iTags;
    }

    public void setITags(List<ITag> iTags) {
        this.iTags = iTags;
    }

    public IGroup getIGroupID() {
        return iGroupID;
    }

    public void setIGroupID(IGroup iGroupID) {
        this.iGroupID = iGroupID;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", iTags=" + iTags +
                ", iGroupID=" + iGroupID +
                '}';
    }
}
