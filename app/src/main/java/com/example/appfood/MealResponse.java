package com.example.appfood;

import java.util.List;

public class MealResponse {
    private boolean success;
    private Data data;

    // Getter và Setter cho success và data
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    // Lớp Data chứa danh sách meals
    public static class Data {
        private List<Meal> meals;

        public List<Meal> getMeals() {
            return meals;
        }

        public void setMeals(List<Meal> meals) {
            this.meals = meals;
        }
    }
}
