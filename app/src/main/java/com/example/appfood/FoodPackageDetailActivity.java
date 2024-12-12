package com.example.appfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.HashSet;
import java.util.Random;

import android.app.DatePickerDialog;

import java.io.Serializable;
import java.util.Calendar;
import android.app.Dialog;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FoodPackageDetailActivity extends AppCompatActivity {

    private ImageView foodImageView;
    private TextView foodNameTextView, foodPriceTextView, peopleOrderedTextView, foodDescriptionTextView;
    private RecyclerView objectivesRecyclerView, recentMealsRecyclerView, ingredientsRecyclerView;
    private ObjectiveAdapter objectiveAdapter;
    private RecentMealsAdapter recentMealsAdapter;
    private IngredientAdapter ingredientAdapter;
    private List<Objective> objectiveList;
    private List<Objective> objectiveList2;
    private List<String> recentMealsList;
    private List<Ingredient> ingredientList;  // Declare the ingredientList

    private Spinner morningDeliverySpinner;
    private Spinner afternoonDeliverySpinner;
    private EditText deliveryDateEditText;
    private String servicePackageID;
    private String subscriptionID;

    private ApiService apiService ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_package_detail);

        apiService = RetrofitClient.getApiService(this);

        // Khởi tạo các view
        foodImageView = findViewById(R.id.foodImageView);
        foodNameTextView = findViewById(R.id.foodNameTextView);
        foodPriceTextView = findViewById(R.id.foodPriceTextView);
        //peopleOrderedTextView = findViewById(R.id.peopleOrderedTextView);
        foodDescriptionTextView = findViewById(R.id.foodDescriptionTextView);
        objectivesRecyclerView = findViewById(R.id.objectivesRecyclerView);
        recentMealsRecyclerView = findViewById(R.id.recentMealsRecyclerView);
        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView);

        // Gán sự kiện cho nút tùy chỉnh
        Button customizeButton = findViewById(R.id.customizeButton);
        customizeButton.setOnClickListener(v -> showCustomizePopup());
        // Lấy dữ liệu từ intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String packageId = extras.getString("packageId");
            subscriptionID = extras.getString("subscriptionID");

            servicePackageID = packageId;
            if (packageId != null) {
                Log.d("FoodPackageDetail", "Package ID: " + packageId);  // Log ra packageId
                // Gọi API để lấy chi tiết gói dịch vụ
                getFoodPackageDetails(packageId);
            } else {
                Log.d("FoodPackageDetail", "Package ID is null");
            }
        }

//        objectiveAdapter = new ObjectiveAdapter(objectiveList, false);
//        objectivesRecyclerView.setAdapter(objectiveAdapter);
//        objectivesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Khởi tạo danh sách ảnh bữa ăn gần đây
        recentMealsList = new ArrayList<>();

        recentMealsAdapter = new RecentMealsAdapter(recentMealsList);
        recentMealsRecyclerView.setAdapter(recentMealsAdapter);
        recentMealsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        ingredientsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        // Khởi tạo danh sách ingredients
        ingredientList = new ArrayList<>();
        ingredientAdapter = new IngredientAdapter(this, ingredientList, false);  // Assuming 'false' for showCheckbox, change as needed
        ingredientsRecyclerView.setAdapter(ingredientAdapter);
        //ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getFoodPackageDetails(String packageId) {

        apiService.getFoodPackageDetails(packageId).enqueue(new Callback<FoodPackageResponseDetail>() {
        @Override
        public void onResponse(Call<FoodPackageResponseDetail> call, Response<FoodPackageResponseDetail> response) {
            if (response.isSuccessful() && response.body() != null) {
                FoodPackageResponseDetail packageResponse = response.body();

                // Log the full response for debugging
                Log.d("FoodPackageDetail", "Full Response: " + packageResponse.toString());

                // Kiểm tra xem dữ liệu có thực sự có mặt không
                if (packageResponse.getData() != null && packageResponse.getData().getServicePackage() != null) {
                    // Trường hợp có dữ liệu
                    FoodPackageDetail servicePackage = packageResponse.getData().getServicePackage();
                    if (servicePackage == null) {
                        Log.d("FoodPackageDetail", "No service packages available.");
                        Toast.makeText(FoodPackageDetailActivity.this, "Không có gói dịch vụ nào.", Toast.LENGTH_SHORT).show();
                    } else {
                        updateUIWithServicePackage(servicePackage);  // Update UI with the package data
                    }
                } else {
                    // Nếu không có cả servicePackages
                    Log.d("FoodPackageDetail", "Service package detail is null");
                    Toast.makeText(FoodPackageDetailActivity.this, "Dữ liệu gói dịch vụ không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("FoodPackageDetail", "Failed response: " + response.code());
                Toast.makeText(FoodPackageDetailActivity.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<FoodPackageResponseDetail> call, Throwable t) {
            // Xử lý lỗi kết nối hoặc lỗi khác
            Log.e("FoodPackageDetail", "Error: " + t.getMessage());
            Toast.makeText(FoodPackageDetailActivity.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
        }
    });
}

    private void updateUIWithServicePackage(FoodPackageDetail servicePackage) {
        // Cập nhật thông tin về gói dịch vụ
        foodNameTextView.setText(servicePackage.getName());
        foodPriceTextView.setText(String.format("%,d VNĐ", servicePackage.getPrice()));  // Định dạng giá trị
        foodDescriptionTextView.setText(Html.fromHtml(servicePackage.getDescription()));  // Hiển thị mô tả có HTML

        // Cập nhật danh sách ingredients
        ingredientList.clear();
        if (servicePackage.getIngredientList() != null) {
            ingredientList.addAll(servicePackage.getIngredientList());  // Thêm Ingredient vào danh sách
            ingredientAdapter.notifyDataSetChanged();  // Cập nhật lại adapter cho RecyclerView
        }

        recentMealsList.clear();
        if (servicePackage.getImages() != null) {
            recentMealsList.addAll(servicePackage.getImages());  // Thêm dữ liệu vào danh sách
            Log.d("RecentMealsList", "Danh sách ảnh gần đây: " + recentMealsList.toString()); // Log sau khi thêm dữ liệu
            recentMealsAdapter.notifyDataSetChanged();
        }


        // Cập nhật danh sách mục tiêu (objectives) từ iTags của ingredients
        List<Objective> dynamicObjectiveList = new ArrayList<>();
        objectiveList = dynamicObjectiveList;

        if (servicePackage.getIngredientList() != null) {
            for (Ingredient ingredient : servicePackage.getIngredientList()) {
                if (ingredient.getITags() != null) {
                    for (ITag iTag : ingredient.getITags()) {
                        // Tạo một Objective từ iTag
                        Objective objective = new Objective(
                                iTag.getId(),            // ID từ iTag
                                iTag.getITagName(),      // Tên của mục tiêu từ iTag
                                Color.parseColor(iTag.getColor())  // Màu sắc từ iTag
                        );
                        // Kiểm tra xem objective đã có chưa, nếu chưa thì thêm vào danh sách
                        if (!dynamicObjectiveList.contains(objective)) {
                            dynamicObjectiveList.add(objective);
                        }
                    }
                }
            }
        }

        // List mục tiêu của gói ăn để chọn
        // Tập hợp để lưu các ID đã sử dụng (kiểu String)
        Set<String> usedIds = new HashSet<>();
        Random random = new Random();

        // Cập nhật danh sách mục tiêu (objectives) từ iTags của ingredients
        List<Objective> dynamicObjectiveList2 = new ArrayList<>();
        objectiveList2 = dynamicObjectiveList2;

        if (servicePackage.getServiceTags() != null) {
            for (String item : servicePackage.getServiceTags()) {
                // Sinh một số ngẫu nhiên không trùng và ép kiểu thành chuỗi
                String uniqueId;
                do {
                    uniqueId = String.valueOf(random.nextInt(1000000)); // Sinh số ngẫu nhiên từ 0 đến 999999
                } while (usedIds.contains(uniqueId));
                usedIds.add(uniqueId);

                // Sinh mã màu ngẫu nhiên
                String randomColor = String.format("#%06X", random.nextInt(0xFFFFFF + 1)); // Giá trị HEX từ 0x000000 đến 0xFFFFFF

                // Tạo đối tượng Objective
                Objective objective = new Objective(
                        uniqueId,                 // ID ngẫu nhiên dạng String
                        item,                     // Tên mục tiêu
                        Color.parseColor(randomColor) // Mã màu ngẫu nhiên
                );

                // Kiểm tra xem objective đã có chưa, nếu chưa thì thêm vào danh sách
                if (!dynamicObjectiveList2.contains(objective)) {
                    dynamicObjectiveList2.add(objective);
                }
            }
        }

        // Set lại adapter cho RecyclerView mục tiêu
        objectiveAdapter = new ObjectiveAdapter(dynamicObjectiveList, false);
        objectivesRecyclerView.setAdapter(objectiveAdapter);
        objectivesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        // Set lại adapter cho RecyclerView mục tiêu
        objectiveAdapter = new ObjectiveAdapter(dynamicObjectiveList, false);
        objectivesRecyclerView.setAdapter(objectiveAdapter);
        objectivesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Tải ảnh chính của gói dịch vụ
        String imageUrl = servicePackage.getMainImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            new LoadImageTask(foodImageView).execute("https://res.cloudinary.com/dvxn12n91/image/upload/v1720879125/temp/images/" + imageUrl);  // Cập nhật URL gốc của ảnh
        }
    }


    // LoadImageTask sẽ tải ảnh từ URL vào ImageView (thay thế Glide)
    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }



    // Hàm hiển thị Popup tùy chỉnh đơn hàng
    private void showCustomizePopup() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_customize_order);

        // Điều chỉnh kích thước popup
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.98),
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }

        // Liên kết các thành phần trong Popup
        ImageView closeButton = dialog.findViewById(R.id.closeButton);  // Nút đóng X
        RecyclerView ingredientsRecyclerView = dialog.findViewById(R.id.ingredientsRecyclerView);
        RecyclerView objectivesRecyclerView = dialog.findViewById(R.id.objectivesRecyclerView);
        morningDeliverySpinner = dialog.findViewById(R.id.morningDeliverySpinner);
        afternoonDeliverySpinner = dialog.findViewById(R.id.afternoonDeliverySpinner);
        deliveryDateEditText = dialog.findViewById(R.id.deliveryDateEditText);

        // Thiết lập adapter cho Spinner khung giờ giao buổi sáng
        ArrayAdapter<String> morningAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"6:00 - 8:00", "8:00 - 10:00", "10:00 - 12:00"});
        morningAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        morningDeliverySpinner.setAdapter(morningAdapter);
// Đặt mục mặc định (ví dụ: 6:00 - 8:00)
        morningDeliverySpinner.setSelection(0);

// Thiết lập adapter cho Spinner khung giờ giao buổi chiều
        ArrayAdapter<String> afternoonAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"12:00 - 14:00", "14:00 - 16:00", "16:00 - 18:00","18:00 - 20:00"});
        afternoonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        afternoonDeliverySpinner.setAdapter(afternoonAdapter);
// Đặt mục mặc định (ví dụ: 13:00 - 15:00)
        afternoonDeliverySpinner.setSelection(0);

        // Cài đặt Adapter và LayoutManager cho RecyclerView
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        objectivesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        IngredientAdapter ingredientAdapter = new IngredientAdapter(this, ingredientList, true);
        ObjectiveAdapter objectiveAdapter = new ObjectiveAdapter(objectiveList2, true);

        ingredientsRecyclerView.setAdapter(ingredientAdapter);
        objectivesRecyclerView.setAdapter(objectiveAdapter);

        // Mặc định chọn tất cả các mục trong danh sách Ingredients
        for (Ingredient ingredient : ingredientList) {
            ingredient.setChecked(true); // Đánh dấu tất cả ingredient là đã chọn
        }
        ingredientAdapter.notifyDataSetChanged();  // Cập nhật lại trạng thái checkbox



        // Xử lý sự kiện bấm vào deliveryDateEditText để hiển thị DatePickerDialog
        deliveryDateEditText.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Show DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
                String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                deliveryDateEditText.setText(selectedDate);
            }, year, month, day);

            datePickerDialog.show();
        });

        // Xử lý sự kiện bấm vào nút X để đóng popup
        closeButton.setOnClickListener(v -> dialog.dismiss());

        Button orderButton = dialog.findViewById(R.id.orderButton); // Nút Đặt hàng
        orderButton.setOnClickListener(v -> {
            // Lấy dữ liệu từ các trường
            String deliveryDate = deliveryDateEditText.getText().toString();
            String morningDeliveryTime = morningDeliverySpinner.getSelectedItem().toString();
            String afternoonDeliveryTime = afternoonDeliverySpinner.getSelectedItem().toString();

            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            String addressDelivery = sharedPreferences.getString("addressDelivery", "Chưa có dữ liệu");
            String phoneNumber = sharedPreferences.getString("phoneNumber", "Chưa có dữ liệu");
            String customerName = sharedPreferences.getString("customerName", "Chưa có dữ liệu");

            // Kiểm tra nếu chưa có addressDelivery
            if (addressDelivery == null ||customerName == null || addressDelivery.trim().isEmpty() || addressDelivery.equals("Chưa có dữ liệu")) {
                Toast.makeText(FoodPackageDetailActivity.this, "Vui lòng cập nhật địa chỉ giao hàng trước khi đặt hàng!", Toast.LENGTH_SHORT).show();
                return; // Ngăn không cho tiếp tục đặt hàng
            }

            // Lọc danh sách nguyên liệu đã chọn
            List<String> selectedIngredients = new ArrayList<>();
            for (Ingredient ingredient : ingredientList) {
                if (ingredient.isChecked()) { // Chỉ lấy các mục đã chọn
                    selectedIngredients.add(ingredient.getId());
                }
            }



            // Lọc danh sách mục tiêu đã chọn
            List<String> selectedObjectives = new ArrayList<>();
            for (Objective objective : objectiveList2) {
                if (objective.isChecked()) {
                    selectedObjectives.add(objective.getName());
                }
            }

            // Nếu không có mục tiêu nào được chọn, lấy tất cả mục tiêu
            if (selectedObjectives.isEmpty()) {
                for (Objective objective : objectiveList2) {
                    selectedObjectives.add(objective.getName());
                }
            }

            // Tạo dữ liệu đơn hàng
            OrderData orderData = new OrderData(
                    deliveryDate,
                    morningDeliveryTime,
                    afternoonDeliveryTime,
                    selectedIngredients,
                    selectedObjectives,
                    subscriptionID,
                    servicePackageID,
                    addressDelivery,
                    phoneNumber,
                    customerName
            );

            // Gọi API đặt hàng
            apiService.order(orderData).enqueue(new Callback<OrderResponse>() {
                @Override
                public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Lấy orderID từ phản hồi
                        String orderID = response.body().getOrderID();

                        Toast.makeText(FoodPackageDetailActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                        Log.d("OrderAPI", "Order ID: " + orderID);

                        updateBalance();
                        // Chuyển sang màn hình OrderedMeals và truyền orderID
                        Intent intent = new Intent(FoodPackageDetailActivity.this, OrderedMeals.class);
                        intent.putExtra("orderID", orderID); // Truyền orderID qua Intent
                        intent.putExtra("servicePackageID", servicePackageID);

                        startActivity(intent);

                        dialog.dismiss(); // Đóng popup nếu cần
                    } else {
                        // Xử lý lỗi từ server
                        Toast.makeText(FoodPackageDetailActivity.this, "Đặt hàng thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                        Log.e("OrderAPI", "Error: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<OrderResponse> call, Throwable t) {
                    // Xử lý lỗi kết nối hoặc lỗi khác
                    Toast.makeText(FoodPackageDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("OrderAPI", "Failure: " + t.getMessage());
                }
            });
        });



        // Hiển thị popup
        dialog.show();
    }


    private void handleOrder() {
        // Lấy dữ liệu từ Spinner
        String morningDeliveryTime = morningDeliverySpinner.getSelectedItem().toString();
        String afternoonDeliveryTime = afternoonDeliverySpinner.getSelectedItem().toString();

        // Lấy dữ liệu từ EditText
        String deliveryDate = deliveryDateEditText.getText().toString();
        //String voucherCode = voucherEditText != null ? voucherEditText.getText().toString() : "";

        // Lấy danh sách nguyên liệu đã chọn
        List<Ingredient> selectedIngredients = new ArrayList<>();
        for (Ingredient ingredient : ingredientList) {
            if (ingredient.isChecked()) { // Kiểm tra xem ingredient có được chọn không
                selectedIngredients.add(ingredient);
            }
        }

        // Ghi log hoặc hiển thị Toast để kiểm tra
        Log.d("OrderInfo", "Morning Delivery Time: " + morningDeliveryTime);
        Log.d("OrderInfo", "Afternoon Delivery Time: " + afternoonDeliveryTime);
        Log.d("OrderInfo", "Delivery Date: " + deliveryDate);
        //Log.d("OrderInfo", "Voucher Code: " + voucherCode);
        Log.d("OrderInfo", "Selected Ingredients: " + selectedIngredients.toString());

        // Hiển thị thông báo tóm tắt đặt hàng
        Toast.makeText(
                this,
                "Đặt hàng thành công!\nThời gian giao: " + deliveryDate + "\n" +
                        "Buổi sáng: " + morningDeliveryTime + "\n" +
                        "Buổi chiều: " + afternoonDeliveryTime + "\n" +
                        "Số nguyên liệu: " + selectedIngredients.size(),
                Toast.LENGTH_LONG
        ).show();
    }


    private void updateBalance() {
        apiService.getWallet().enqueue(new Callback<WalletResponse>() {
            @Override
            public void onResponse(Call<WalletResponse> call, Response<WalletResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WalletResponse walletResponse = response.body();
                    if (walletResponse.isSuccess()) {
                        Wallet walletData = walletResponse.getData().getWallet();
                        if (walletData != null) {
                            // Lưu số dư ví vào SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("WalletPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putLong("balance", walletData.getBalance());
                            editor.apply();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<WalletResponse> call, Throwable t) {
                // Xử lý lỗi
            }
        });
    }




}
