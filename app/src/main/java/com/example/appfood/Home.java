package com.example.appfood;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    ArrayList<FoodPackage> foodPackages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Thêm dữ liệu mẫu
        foodPackages.add(new FoodPackage("Gói đồ ăn", "Giao ngay", 10000, R.drawable.img_2, "Món ăn gồm cơm, thịt, rau xanh."));
        foodPackages.add(new FoodPackage("Gói đồ ăn", "Giao ngay", 10000, R.drawable.img_2, "Món ăn gồm cơm, thịt, rau xanh."));
        foodPackages.add(new FoodPackage("Gói đồ ăn", "Theo tuần/ tháng", 10000, R.drawable.img_2, "Món ăn gồm cơm, cá, rau xanh."));
        foodPackages.add(new FoodPackage("Gói đồ ăn", "Theo tuần/ tháng", 10000, R.drawable.img_2, "Món ăn gồm cơm, cá, rau xanh."));
        foodPackages.add(new FoodPackage("Gói đồ ăn", "Theo tuần/ tháng", 10000, R.drawable.img_2, "Món ăn gồm cơm, cá, rau xanh."));
        foodPackages.add(new FoodPackage("Gói đồ ăn", "Theo tuần/ tháng", 10000, R.drawable.img_2, "Món ăn gồm cơm, cá, rau xanh."));
        foodPackages.add(new FoodPackage("Gói đồ ăn", "Theo tuần/ tháng", 10000, R.drawable.img_2, "Món ăn gồm cơm, cá, rau xanh."));
        foodPackages.add(new FoodPackage("Gói đồ ăn", "Theo tuần/ tháng", 10000, R.drawable.img_2, "Món ăn gồm cơm, cá, rau xanh."));
        foodPackages.add(new FoodPackage("Gói đồ ăn", "Theo tuần/ tháng", 10000, R.drawable.img_2, "Món ăn gồm cơm, cá, rau xanh."));

        // Tạo RecyclerView và Adapter
        RecyclerView foodPackageRecyclerView = findViewById(R.id.foodPackageRecyclerView);
        FoodPackageAdapter adapter = new FoodPackageAdapter(foodPackages);

        // Đặt layout cho RecyclerView
        foodPackageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodPackageRecyclerView.setAdapter(adapter);
    }
}
