package com.example.appfood;

import java.util.List;

public class OrderCustomerResponse {
    private boolean success;
    private List<OrderItem> data; // Mảng đơn hàng

    public boolean isSuccess() {
        return success;
    }

    public List<OrderItem> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "OrderCustomerResponse{" +
                "success=" + success +
                ", data=" + data +
                '}';
    }
}
