package com.example.appfood;

public class AddReviewRequest {

    // Các trường dữ liệu
    private int rating;      // Đánh giá (1-5)
    private String content;  // Nội dung đánh giá
    private String mealID;   // ID bữa ăn
    private String customerID;  // ID khách hàng

    // Constructor
    public AddReviewRequest(int rating, String content, String mealID, String customerID) {
        this.rating = rating;
        this.content = content;
        this.mealID = mealID;
        this.customerID = customerID;
    }

    // Getter và Setter cho rating
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    // Getter và Setter cho content
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getter và Setter cho mealID
    public String getMealID() {
        return mealID;
    }

    public void setMealID(String mealID) {
        this.mealID = mealID;
    }

    // Getter và Setter cho customerID
    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    // Phương thức toString (tùy chọn, giúp dễ dàng in ra thông tin đối tượng)
    @Override
    public String toString() {
        return "AddReviewRequest{" +
                "rating=" + rating +
                ", content='" + content + '\'' +
                ", mealID='" + mealID + '\'' +
                ", customerID='" + customerID + '\'' +
                '}';
    }
}
