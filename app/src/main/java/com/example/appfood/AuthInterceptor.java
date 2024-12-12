package com.example.appfood;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // Lấy token từ SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken", null);

        // Thêm Authorization Header nếu token tồn tại
        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder();

        if (accessToken != null) {
            builder.addHeader("Authorization", "Bearer " + accessToken);
        }

        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}
