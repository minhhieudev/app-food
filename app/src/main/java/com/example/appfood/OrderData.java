package com.example.appfood;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderData {
    private String deliveryDate;
    private String estimatedTime1;
    private String estimatedTime2;
    private List<String> favoriteIngredients; // Lưu danh sách các ID
    private List<String> iTags;
    private String servicePackageID;
    private String subscriptionID;
    private String addressDelivery;
    private String phoneNumber;
    private String customerName;


    // Constructor
    public OrderData(String deliveryDate, String morningDeliveryTime,
                     String afternoonDeliveryTime, List<String> selectedIngredients,
                     List<String> iTags,String subscriptionID, String servicePackageID,String addressDelivery,String phoneNumber,String customerName) {
        this.deliveryDate = deliveryDate;
        this.estimatedTime1 = morningDeliveryTime;
        this.estimatedTime2 = afternoonDeliveryTime;
        this.iTags = iTags;
        this.favoriteIngredients = selectedIngredients;

        this.subscriptionID = subscriptionID;

        this.servicePackageID = servicePackageID;
        this.addressDelivery = addressDelivery;
        this.phoneNumber = phoneNumber;
        this.customerName = customerName;

    }

    // Getters and Setters
    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }


    public String getMorningDeliveryTime() {
        return estimatedTime1;
    }

    public void setMorningDeliveryTime(String morningDeliveryTime) {
        this.estimatedTime1 = morningDeliveryTime;
    }

    public String getAfternoonDeliveryTime() {
        return estimatedTime2;
    }

    public void setAfternoonDeliveryTime(String afternoonDeliveryTime) {
        this.estimatedTime2 = afternoonDeliveryTime;
    }

    public List<String> getSelectedIngredients() {
        return favoriteIngredients;
    }

    public void setSelectedIngredients(List<String> selectedIngredients) {
        this.favoriteIngredients = selectedIngredients;
    }

    public List<String> getSelectedITags() {
        return iTags;
    }

    public void setSelectediTags(List<String> iTags) {
        this.iTags = iTags;
    }

    // Convert to Map
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("deliveryDate", deliveryDate);
        map.put("estimatedTime1", estimatedTime1);
        map.put("estimatedTime2", estimatedTime2);
        map.put("selectedIngredients", favoriteIngredients);
        map.put("iTags", iTags);
        map.put("addressDelivery", addressDelivery);
        map.put("phoneNumber", phoneNumber);
        map.put("customerName", customerName);

        return map;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            String cleanDeliveryDate = deliveryDate.replace("\\/", "/");


            jsonObject.put("estimatedDate", cleanDeliveryDate);  // Sử dụng cleanDeliveryDate nếu cần thiết
            jsonObject.put("estimatedTime1", estimatedTime1);
            jsonObject.put("estimatedTime2", estimatedTime2);
            jsonObject.put("servicePackageID", servicePackageID);
            jsonObject.put("subscriptionID", subscriptionID);
            jsonObject.put("addressDelivery", addressDelivery);
            jsonObject.put("phoneNumber", phoneNumber);
            jsonObject.put("customerName", customerName);

            // Convert selectedIngredients (List<String>) to JSONArray
            JSONArray ingredientsArray = new JSONArray(favoriteIngredients);
            jsonObject.put("favoriteIngredients:", ingredientsArray);

            // Chuyển iTags từ List<String> thành JSONArray
            if (iTags != null) {
                JSONArray tagsArray = new JSONArray(iTags);
                jsonObject.put("iTags:", tagsArray);
            } else {
                // Tránh lỗi nếu iTags chưa được khởi tạo hoặc là null
                jsonObject.put("iTags:", new JSONArray());
            }
        } catch (JSONException e) {
            e.printStackTrace();  // In thông tin lỗi nếu có ngoại lệ
        }
        return jsonObject;
    }


    @Override
    public String toString() {
        return "OrderData{" +
                "deliveryDate='" + deliveryDate + '\'' +
                ", estimatedTime1='" + estimatedTime1 + '\'' +
                ", estimatedTime2='" + estimatedTime2 + '\'' +
                ", selectedIngredients=" + favoriteIngredients +
                '}';
    }
}
