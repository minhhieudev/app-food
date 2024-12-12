package com.example.appfood;

public class WalletResponse {
    private boolean success;
    private WalletData data;

    // Getter và Setter
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public WalletData getData() {
        return data;
    }

    public void setData(WalletData data) {
        this.data = data;
    }
}
