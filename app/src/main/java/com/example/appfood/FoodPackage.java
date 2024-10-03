package com.example.appfood;

public class FoodPackage {
    public String name;
    public String type;
    public int price;
    public int imageRes;
    public String description;

    // Constructor
    public FoodPackage(String name, String type, int price, int imageRes, String description) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.imageRes = imageRes;
        this.description = description;
    }
}
