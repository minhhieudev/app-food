package com.example.appfood;

public class OrderResponse {
    private boolean success;
    private String message;
    private String orderID;

    // Constructor
    public OrderResponse(boolean success, String message, String orderID) {
        this.success = success;
        this.message = message;
        this.orderID = orderID;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getOrderID() {
        return orderID;
    }

    // Optional: toString method for debugging
    @Override
    public String toString() {
        return "OrderResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", orderID='" + orderID + '\'' +
                '}';
    }
}
