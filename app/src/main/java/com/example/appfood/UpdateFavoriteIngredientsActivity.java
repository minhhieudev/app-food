package com.example.appfood;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateFavoriteIngredientsActivity extends AppCompatActivity {

    private RecyclerView ingredientsRecyclerView;
    private ImageButton updateButton;
    private ApiService apiService;
    private String servicePackageID;
    private List<String> favoriteIngredients;
    private List<Ingredient> ingredientList = new ArrayList<>();
    private IngredientAdapter ingredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_ingredient);
        servicePackageID = getIntent().getStringExtra("servicePackageID");

        // Khởi tạo RecyclerView và nút Update
        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView);
        updateButton = findViewById(R.id.updateButton);

        // Đặt layout cho RecyclerView (1 cột)
        ingredientsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        // Khởi tạo ApiService
        apiService = RetrofitClient.getApiService(this);

        // Xử lý sự kiện khi nhấn nút cập nhật
        updateButton.setOnClickListener(v -> updateIngredients());

        // Lấy dữ liệu yêu thích từ Intent
        favoriteIngredients = (List<String>) getIntent().getSerializableExtra("favoriteIngredients");

        // Gọi API để lấy chi tiết gói dịch vụ
        getFoodPackageDetails(servicePackageID);
    }

    private void getFoodPackageDetails(String packageId) {
        apiService.getFoodPackageDetails(packageId).enqueue(new Callback<FoodPackageResponseDetail>() {
            @Override
            public void onResponse(Call<FoodPackageResponseDetail> call, Response<FoodPackageResponseDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FoodPackageResponseDetail packageResponse = response.body();

                    if (packageResponse.getData() != null && packageResponse.getData().getServicePackage() != null) {
                        FoodPackageDetail servicePackage = packageResponse.getData().getServicePackage();
                        if (servicePackage != null) {
                            ingredientList = servicePackage.getIngredientList();
                            Log.d("FoodPackageDetail99999999999999", "Danh sách thành phần: " + ingredientList.toString());

                            // Initialize the adapter only after the ingredient list is populated
                            ingredientAdapter = new IngredientAdapter(UpdateFavoriteIngredientsActivity.this, ingredientList, true);
                            ingredientsRecyclerView.setAdapter(ingredientAdapter);

                            // Mặc định chọn tất cả các mục trong danh sách Ingredients
                            for (Ingredient ingredient : ingredientList) {
                                if (favoriteIngredients.contains(ingredient.getId())) {
                                    ingredient.setChecked(true); // Đánh dấu các thành phần yêu thích là đã chọn
                                }
                            }

                            // Notify the adapter that the data has changed
                            ingredientAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(UpdateFavoriteIngredientsActivity.this, "Dữ liệu gói dịch vụ không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("FoodPackageDetail444444444444444444444", "Failed response: " + response.code());
                    Toast.makeText(UpdateFavoriteIngredientsActivity.this, "Lỗi khi tải dữ liệu cho update ingredient", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FoodPackageResponseDetail> call, Throwable t) {
                Log.e("FoodPackageDetail77777777777777", "Error: " + t.getMessage());
                Toast.makeText(UpdateFavoriteIngredientsActivity.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateIngredients() {
        String mealID = getIntent().getStringExtra("meal_id");

        List<String> updatedFavoriteIngredients = new ArrayList<>();

        for (Ingredient ingredient : ingredientList) {
            if (ingredient.isChecked()) { // Kiểm tra nếu nguyên liệu này được chọn
                updatedFavoriteIngredients.add(ingredient.getId()); // Thêm id vào danh sách yêu thích
            }
        }

        UpdateFavoriteIngredientsRequest data = new UpdateFavoriteIngredientsRequest(updatedFavoriteIngredients, mealID);

        // Gửi yêu cầu cập nhật thành phần
        apiService.updateFavoriteIngredients(data).enqueue(new Callback<UpdateFavoriteIngredientsRespone>() {
            @Override
            public void onResponse(Call<UpdateFavoriteIngredientsRespone> call, Response<UpdateFavoriteIngredientsRespone> response) {
                if (response.isSuccessful()) {
                    UpdateFavoriteIngredientsRespone updateDeliveryTimeResponse = response.body();
                    if (updateDeliveryTimeResponse != null) {

                        Toast.makeText(UpdateFavoriteIngredientsActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UpdateFavoriteIngredientsActivity.this, "Lỗi khi cập nhật thành phần", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UpdateFavoriteIngredientsActivity.this, "Không thể cập nhật thành phần. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateFavoriteIngredientsRespone> call, Throwable t) {
                Toast.makeText(UpdateFavoriteIngredientsActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
