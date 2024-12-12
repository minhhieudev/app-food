package com.example.appfood;

import java.util.List;

public class UpdateFavoriteIngredientsRequest {

    // Các trường dữ liệu
    private List<String> favoriteIngredients;
    private String mealID;

    // Constructor
    public UpdateFavoriteIngredientsRequest(List<String> favoriteIngredients, String mealID) {
        this.favoriteIngredients = favoriteIngredients;
        this.mealID = mealID;
    }

    // Getter và Setter cho favoriteIngredients
    public List<String> getFavoriteIngredients() {
        return favoriteIngredients;
    }

    public void setFavoriteIngredients(List<String> favoriteIngredients) {
        this.favoriteIngredients = favoriteIngredients;
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
        return "UpdateFavoriteIngredientsRequest{" +
                "favoriteIngredients=" + favoriteIngredients +
                ", mealID='" + mealID + '\'' +
                '}';
    }
}
