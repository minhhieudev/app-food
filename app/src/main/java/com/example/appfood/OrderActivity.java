package com.example.appfood;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    ArrayList<OrderItem> orders = new ArrayList<>();
    private ApiService apiService ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        apiService = RetrofitClient.getApiService(this);

        // Call the API to get the list of FoodPackages
        apiService.getOrder().enqueue(new Callback<OrderCustomerResponse>() {
            @Override
            public void onResponse(Call<OrderCustomerResponse> call, Response<OrderCustomerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API Response", response.body().toString()); // Log dữ liệu trả về

                    if (response.body().isSuccess()) {
                        orders.clear(); // Xóa danh sách cũ

                        // Lấy danh sách đơn hàng từ API
                        List<OrderItem> fetchedOrders = response.body().getData();
                        orders.addAll(fetchedOrders); // Thêm tất cả đơn hàng vào danh sách

                        setupRecyclerView(); // Thiết lập RecyclerView
                    } else {
                        Toast.makeText(OrderActivity.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API Error", "Response code: " + response.code());
                    Toast.makeText(OrderActivity.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onFailure(Call<OrderCustomerResponse> call, Throwable t) {
                if (t instanceof java.net.SocketTimeoutException) {
                    // Handle timeout exception
                    Toast.makeText(OrderActivity.this, "Lỗi: Kết nối quá lâu", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle other errors
                    Toast.makeText(OrderActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView orderRecyclerView = findViewById(R.id.orderRecyclerView);
        if (orderRecyclerView != null) {
            OrderAdapter adapter = new OrderAdapter(this, orders);
            orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            orderRecyclerView.setAdapter(adapter);
        } else {
            Log.e("RecyclerView Error", "RecyclerView is null");
        }
    }


}
