package com.example.appfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderedMeals extends AppCompatActivity {

    ArrayList<Meal> meals = new ArrayList<>();
    private ApiService apiService;
    SharedPreferences sharedPreferences2;
    private String servicePackageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_meal);

        apiService = RetrofitClient.getApiService(this);

        // Lấy dữ liệu từ SharedPreferences
        sharedPreferences2= getSharedPreferences("WalletPrefs", MODE_PRIVATE);

        // Nhận orderID từ Intent
        String orderID = getIntent().getStringExtra("orderID");

        servicePackageID = getIntent().getStringExtra("servicePackageID");

        if (orderID != null) {
            // Gọi API để lấy danh sách bữa ăn theo orderID
            getMealsByOrderID(orderID);
        } else {
            Toast.makeText(this, "Không có Order ID", Toast.LENGTH_SHORT).show();
        }

        // Ánh xạ ImageView của giỏ hàng
        ImageView buttonUser = findViewById(R.id.menuImageView);

        // Thêm sự kiện onClickListener cho ImageView
        buttonUser.setOnClickListener(v -> showUserMenu(v));
    }

    // Gọi API lấy danh sách bữa ăn theo orderID
    private void getMealsByOrderID(String orderID) {
        apiService.getMeals(orderID).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                Log.e("OrderedMeals", "Error: " + response);
                if (response.isSuccessful() && response.body() != null) {
                    meals.clear();
                    MealResponse mealResponse = response.body();
                    if (mealResponse.isSuccess() && mealResponse.getData() != null) {
                        meals.addAll(mealResponse.getData().getMeals());  // Lấy danh sách bữa ăn từ response
                        setupRecyclerView(); // Cập nhật RecyclerView với dữ liệu mới
                    } else {
                        Toast.makeText(OrderedMeals.this, "Không có bữa ăn nào", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OrderedMeals.this, "Lấy danh sách bữa ăn thất bại", Toast.LENGTH_SHORT).show();
                    Log.e("OrderedMeals", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                Toast.makeText(OrderedMeals.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("OrderedMeals", "Failure: " + t.getMessage());
            }
        });
    }


    // Thiết lập RecyclerView để hiển thị danh sách bữa ăn
    private void setupRecyclerView() {
        RecyclerView mealRecyclerView = findViewById(R.id.mealRecyclerView);
        MealAdapter adapter = new MealAdapter(this, meals, servicePackageID);
        mealRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mealRecyclerView.setAdapter(adapter);
    }

    // Phương thức hiển thị dropdown menu
    private void showUserMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(OrderedMeals.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.user_menu, popupMenu.getMenu());


        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "Chưa có dữ liệu");
        long balance = sharedPreferences2.getLong("balance", 0);

        popupMenu.getMenu().findItem(R.id.menu_item_balance).setTitle("Số dư: " + NumberFormat.getNumberInstance(Locale.US).format(balance) + " VND");
        popupMenu.getMenu().findItem(R.id.menu_item_email).setTitle("Email: " + email);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_item_address) {
                // Chuyển sang Activity mới
                Intent intent = new Intent(OrderedMeals.this, UpdateContactActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

}

