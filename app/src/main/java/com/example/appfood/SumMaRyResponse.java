package com.example.appfood;

public class SumMaRyResponse {
    private boolean success;  // Phản ánh trạng thái thành công
    private Summary summary;  // Chi tiết về tóm tắt thanh toán

    // Constructor
    public SumMaRyResponse(boolean success, Summary summary) {
        this.success = success;
        this.summary = summary;
    }

    // Getters và Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "SumMaRyResponse{" +
                "success=" + success +
                ", summary=" + summary +
                '}';
    }

    // Lớp phụ để đại diện cho "summary"
    public static class Summary {
        private int subtotal;        // Tổng số tiền trước giảm giá
        private int discount;        // Số tiền giảm giá
        private int shippingAmount;  // Chi phí vận chuyển
        private int grandTotal;      // Tổng tiền thanh toán

        // Constructor
        public Summary(int subtotal, int discount, int shippingAmount, int grandTotal) {
            this.subtotal = subtotal;
            this.discount = discount;
            this.shippingAmount = shippingAmount;
            this.grandTotal = grandTotal;
        }

        // Getters và Setters
        public int getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(int subtotal) {
            this.subtotal = subtotal;
        }

        public int getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }

        public int getShippingAmount() {
            return shippingAmount;
        }

        public void setShippingAmount(int shippingAmount) {
            this.shippingAmount = shippingAmount;
        }

        public int getGrandTotal() {
            return grandTotal;
        }

        public void setGrandTotal(int grandTotal) {
            this.grandTotal = grandTotal;
        }

        @Override
        public String toString() {
            return "Summary{" +
                    "subtotal=" + subtotal +
                    ", discount=" + discount +
                    ", shippingAmount=" + shippingAmount +
                    ", grandTotal=" + grandTotal +
                    '}';
        }
    }
}
