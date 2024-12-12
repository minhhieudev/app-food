package com.example.appfood;

import java.util.List;

public class oodPackage {
    public String _id;  // ID của gói dịch vụ
    public String name;  // Tên gói dịch vụ
    public String description;  // Mô tả gói dịch vụ
    public List<String> serviceTags;  // Các tag dịch vụ (Healthy, Detox, ...)
    public int price;  // Giá gói dịch vụ
    public List<String> images;  // Các hình ảnh của gói dịch vụ
    public String mainImage;  // Hình ảnh chính của gói dịch vụ
    public SubscriptionIDs subscriptionID;  // Đối tượng SubscriptionIDs chứa các thông tin về gói đăng ký
    public List<Ingredient> ingredientList;  // Danh sách các nguyên liệu trong gói dịch vụ
    // Constructor mặc định (tất cả các trường đều được khởi tạo)
    public FoodPackage(String _id, String name, String description, List<String> serviceTags,
                       int price, List<String> images, String mainImage,
                       SubscriptionIDs subscriptionID, List<Ingredient> ingredientList) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.serviceTags = serviceTags;
        this.price = price;
        this.images = images;
        this.mainImage = mainImage;
        this.subscriptionID = subscriptionID;
        this.ingredientList = ingredientList;
    }

    // Constructor mới chỉ nhận các tham số cần thiết
    public FoodPackage(String _id,String name, int price, String mainImage, String description, SubscriptionIDs subscriptionID) {
        this.name = name;
        this._id = _id;
        this.price = price;
        this.mainImage = mainImage;
        this.description = description;
        this.subscriptionID = subscriptionID;

        // Các trường khác có thể để trống hoặc mặc định
        this.serviceTags = null;  // Hoặc mảng rỗng nếu cần
        this.images = null;  // Hoặc mảng rỗng nếu cần
        this.ingredientList = null;  // Hoặc mảng rỗng nếu cần
    }

    // Các getter và setter
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

    public SubscriptionIDs getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(SubscriptionIDs subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }
}
