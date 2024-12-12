package com.example.appfood;

public class CancelMealRequest {

    // Trường dữ liệu
    private String mealID;

    // Constructor
    public CancelMealRequest(String mealID) {
        this.mealID = mealID;
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
        return "CancelMealRequest{" +
                "mealID='" + mealID + '\'' +
                '}';
    }
}
