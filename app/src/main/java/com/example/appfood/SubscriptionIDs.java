package com.example.appfood;

public class SubscriptionIDs {
    public String _id;  // ID của gói đăng ký
    public String name;  // Tên gói đăng ký
    public int totalDate;  // Số ngày trong gói đăng ký
    public int mealsPerDay;  // Số bữa ăn mỗi ngày
    public int totalSub;  // Tổng số bữa ăn
    public String client_guid;  // Mã khách hàng

    // Constructor
    public SubscriptionIDs(String _id, String name, int totalDate, int mealsPerDay, int totalSub, String client_guid) {
        this._id = _id;
        this.name = name;
        this.totalDate = totalDate;
        this.mealsPerDay = mealsPerDay;
        this.totalSub = totalSub;
        this.client_guid = client_guid;
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

    public int getTotalDate() {
        return totalDate;
    }

    public void setTotalDate(int totalDate) {
        this.totalDate = totalDate;
    }

    public int getMealsPerDay() {
        return mealsPerDay;
    }

    public void setMealsPerDay(int mealsPerDay) {
        this.mealsPerDay = mealsPerDay;
    }

    public int getTotalSub() {
        return totalSub;
    }

    public void setTotalSub(int totalSub) {
        this.totalSub = totalSub;
    }

    public String getClient_guid() {
        return client_guid;
    }

    public void setClient_guid(String client_guid) {
        this.client_guid = client_guid;
    }
}
