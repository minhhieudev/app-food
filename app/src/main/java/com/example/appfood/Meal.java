package com.example.appfood;

import java.util.List;

public class Meal {
    private String _id;
    private String orderID;
    private String customerID;
    private String estimatedDate;
    private String estimatedTime;
    private String image;
    private String status;
    private List<String> favoriteIngredients;

    // Getter và Setter cho tất cả các trường
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getEstimatedDate() {
        return estimatedDate;
    }

    public void setEstimatedDate(String estimatedDate) {
        this.estimatedDate = estimatedDate;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getFavoriteIngredients() {
        return favoriteIngredients;
    }

    public void setFavoriteIngredients(List<String> favoriteIngredients) {
        this.favoriteIngredients = favoriteIngredients;
    }
}

