package com.example.appfood;

import java.util.List;

public class FoodPackageResponse {
    private boolean success;
    private Data data;

    public boolean isSuccess() {
        return success;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private List<FoodPackage> servicePackages;

        public List<FoodPackage> getServicePackages() {
            return servicePackages;
        }
    }
}
