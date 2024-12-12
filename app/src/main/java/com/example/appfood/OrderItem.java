package com.example.appfood;

public class OrderItem {
    public String _id;
    public String code;
    public ServicePackage servicePackage;
    public double totalPrice;
    public String createdAt;

    // Constructor
    public OrderItem(String _id, String code, ServicePackage servicePackage, double totalPrice, String createdAt) {
        this._id = _id;
        this.code = code;
        this.servicePackage = servicePackage;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    // Getters
    public String getId() {
        return _id;
    }

    public String getCode() {
        return code;
    }

    public ServicePackage getServicePackage() {
        return servicePackage;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // Static nested class for ServicePackage
    public static class ServicePackage {
        private String name;
        private String mainImage;
        private String _id;

        // Constructor
        public ServicePackage(String name, String mainImage) {
            this.name = name;
            this.mainImage = mainImage;
        }

        // Getters
        public String getName() {
            return name;
        }

        public String getMainImage() {
            return mainImage;
        }

        public String getServicePackageID() {
            return _id;
        }

    }
}
