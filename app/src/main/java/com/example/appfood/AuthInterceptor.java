package com.example.appfood;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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

        Request originalRequest = chain.request();

        Log.d("accessTokenaccessTokenaccessToken", "accessTokenaccessTokenaccessTokenaccessToken: " + accessToken);

        // Nếu không có token, gửi request như cũ
        if (accessToken == null) {
            return chain.proceed(originalRequest);
        }

        // Thêm Authorization Header vào request
        Request modifiedRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        return chain.proceed(modifiedRequest);
    }
}
