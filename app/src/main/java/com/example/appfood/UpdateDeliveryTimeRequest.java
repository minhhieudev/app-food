package com.example.appfood;

public class UpdateDeliveryTimeRequest {

    // Các trường dữ liệu
    private String estimatedDate;
    private String estimatedTime;
    private String mealID;

    // Constructor
    public UpdateDeliveryTimeRequest(String estimatedDate, String estimatedTime, String mealID) {
        this.estimatedDate = estimatedDate;
        this.estimatedTime = estimatedTime;
        this.mealID = mealID;
    }

    // Getter và Setter cho estimatedDate
    public String getEstimatedDate() {
        return estimatedDate;
    }

    public void setEstimatedDate(String estimatedDate) {
        this.estimatedDate = estimatedDate;
    }

    // Getter và Setter cho estimatedTime
    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    // Getter và Setter cho mealID
    public String getMealID() {
        return mealID;
    }

    public void setMealID(String mealID) {
        this.mealID = mealID;
    }

    // Phương thức toString (tùy chọn, giúp dễ dàng in ra thông tin đối tượng)
    @Override
    public String toString() {
        return "UpdateDeliveryTimeRequest{" +
                "estimatedDate='" + estimatedDate + '\'' +
                ", estimatedTime='" + estimatedTime + '\'' +
                ", mealID='" + mealID + '\'' +
                '}';
    }
}
