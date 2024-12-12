// RegisterRequest.java
package com.example.appfood;

public class RegisterRequest {
    private String email;
    private String password;

    public RegisterRequest(String username, String password) {
        this.email = username;
        this.password = password;
    }

    // Getter và Setter nếu cần
    public String getUsername() {
        return email;
    }

    public void setUsername(String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
