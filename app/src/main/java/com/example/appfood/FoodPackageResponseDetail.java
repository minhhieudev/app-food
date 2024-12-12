package com.example.appfood;

public class FoodPackageResponseDetail {
    private boolean success; // Trạng thái phản hồi
    private DataDetail data; // Dữ liệu chính của phản hồi

    public boolean isSuccess() {
        return success;
    }

    public DataDetail getData() {
        return data;
    }

    public static class DataDetail {
        private FoodPackageDetail servicePackage; // Đối tượng gói dịch vụ

        public FoodPackageDetail getServicePackage() {
            return servicePackage;
        }

        @Override
        public String toString() {
            return "DataDetail{" +
                    "servicePackage=" + (servicePackage != null ? servicePackage.toString() : "null") +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FoodPackageResponseDetail{" +
                "success=" + success +
                ", data=" + (data != null ? data.toString() : "null") +
                '}';
    }
}
