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
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {

    ArrayList<FoodPackage> foodPackages = new ArrayList<>();
    private ApiService apiService ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Ánh xạ ImageView của giỏ hàng
        ImageView gioHangImageView = findViewById(R.id.gioHangImageView);

        // Thêm sự kiện onClickListener cho ImageView
        gioHangImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình OrderedPackage
                Intent intent = new Intent(Home.this, OrderedMeals.class);
                startActivity(intent);
            }
        });

        apiService = RetrofitClient.getApiService(this);

        // Call the API to get the list of FoodPackages
        apiService.getFoodPackages().enqueue(new Callback<FoodPackageResponse>() {
            @Override
            public void onResponse(Call<FoodPackageResponse> call, Response<FoodPackageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API Response", response.body().toString()); // Check the response
                    if (response.body().isSuccess()) {
                        foodPackages.clear(); // Clear the current list
                        List<FoodPackage> servicePackages = response.body().getData().getServicePackages();
                        // Convert and add FoodPackages to the list
                        for (FoodPackage servicePackage : servicePackages) {
                            String imageUrl = "https://res.cloudinary.com/dvxn12n91/image/upload/v1720879125/temp/images/" + servicePackage.mainImage; // Update image path
                            // Check and get SubscriptionID object
                            SubscriptionIDs subscription = servicePackage.subscriptionID; // Ensure correct access

                            foodPackages.add(new FoodPackage(servicePackage._id,servicePackage.name,  servicePackage.price, imageUrl, servicePackage.description, subscription));
                        }

                        setupRecyclerView(); // Call method to set up RecyclerView
                    } else {
                        Toast.makeText(Home.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API Error", "Response code: " + response.code());
                    Toast.makeText(Home.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FoodPackageResponse> call, Throwable t) {
                if (t instanceof java.net.SocketTimeoutException) {
                    // Handle timeout exception
                    Toast.makeText(Home.this, "Lỗi: Kết nối quá lâu", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle other errors
                    Toast.makeText(Home.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView foodPackageRecyclerView = findViewById(R.id.foodPackageRecyclerView);
        FoodPackageAdapter adapter = new FoodPackageAdapter(this, foodPackages);
        foodPackageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodPackageRecyclerView.setAdapter(adapter);
    }
}
