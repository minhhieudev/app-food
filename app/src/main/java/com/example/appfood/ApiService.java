// ApiService.java
package com.example.appfood;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("register")
    Call<RegisterResponse> register(@Body RegisterRequest body);

//    @POST("login")
//    Call<LoginResponse> login(@Body LoginRequest body);
}
