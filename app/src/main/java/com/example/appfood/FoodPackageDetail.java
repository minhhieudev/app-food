package com.example.appfood;

import java.util.List;

public class FoodPackageDetail {
    private String _id;  // ID của gói dịch vụ
    private String name;  // Tên gói dịch vụ
    private String description;  // Mô tả gói dịch vụ
    private List<String> serviceTags;  // Các tag dịch vụ
    private int price;  // Giá gói dịch vụ
    private List<String> images;  // Các hình ảnh của gói dịch vụ
    private String mainImage;  // Hình ảnh chính của gói dịch vụ
    private List<Ingredient> ingredientList;  // Danh sách các nguyên liệu

    // Constructor mặc định (tất cả các trường đều được khởi tạo)
    public FoodPackageDetail(String _id, String name, String description, List<String> serviceTags,
                             int price, List<String> images, String mainImage, List<Ingredient> ingredientList) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.serviceTags = serviceTags;
        this.price = price;
        this.images = images;
        this.mainImage = mainImage;
        this.ingredientList = ingredientList;
    }

    // Constructor chỉ nhận các tham số cần thiết
    public FoodPackageDetail(String _id, String name, int price, String mainImage, String description) {
        this._id = _id;
        this.name = name;
        this.price = price;
        this.mainImage = mainImage;
        this.description = description;

        // Các trường khác để null
        this.serviceTags = null;
        this.images = null;
        this.ingredientList = null;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getServiceTags() {
        return serviceTags;
    }

    public void setServiceTags(List<String> serviceTags) {
        this.serviceTags = serviceTags;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @Override
    public String toString() {
        return "FoodPackageDetail{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", serviceTags=" + serviceTags +
                ", price=" + price +
                ", images=" + images +
                ", mainImage='" + mainImage + '\'' +
                ", ingredientList=" + ingredientList +
                '}';
    }
}
