package com.example.appfood;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

    ArrayList<FoodPackage> foodPackages = new ArrayList<>();
    private ApiService apiService;
    private long balance;
    private BroadcastReceiver balanceUpdateReceiver;
    SharedPreferences sharedPreferences2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Khai báo TextView để hiển thị thông tin
        TextView emailTextView = findViewById(R.id.emailTextView);

        // Lấy dữ liệu từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
         sharedPreferences2= getSharedPreferences("WalletPrefs", MODE_PRIVATE);


        String email = sharedPreferences.getString("email", "Chưa có dữ liệu");

        // Register the receiver to listen for the "UPDATE_BALANCE" action
        IntentFilter filter = new IntentFilter("com.example.appfood.UPDATE_BALANCE");
        registerReceiver(balanceUpdateReceiver, filter);


        // Hiển thị dữ liệu trên màn hình
        emailTextView.setText("Email: " + email);


        // Kiểm tra nếu email không có giá trị (nếu có lỗi trong việc lấy thông tin)
        if (email.equals("Chưa có dữ liệu")) {
            Toast.makeText(this, "Không thể lấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
        }

        // Ánh xạ ImageView của giỏ hàng
        ImageView gioHangImageView = findViewById(R.id.gioHangImageView);
        ImageView buttonUser = findViewById(R.id.menuImageView);

        // Thêm sự kiện onClickListener cho ImageView
        buttonUser.setOnClickListener(v -> showUserMenu(v));

        // Thêm sự kiện onClickListener cho ImageView giỏ hàng
        gioHangImageView.setOnClickListener(v -> {
            // Chuyển sang màn hình OrderActivity
            Intent intent = new Intent(Home.this, OrderActivity.class);
            startActivity(intent);
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
                            SubscriptionIDs subscription = servicePackage.subscriptionID; // Ensure correct access

                            foodPackages.add(new FoodPackage(servicePackage._id, servicePackage.name, servicePackage.price, imageUrl, servicePackage.description, subscription));
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

        // Gọi API để lấy thông tin Wallet
        apiService.getWallet().enqueue(new Callback<WalletResponse>() {
            @Override
            public void onResponse(Call<WalletResponse> call, Response<WalletResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WalletResponse walletResponse = response.body();

                    // Log thông tin trả về từ response
                    Log.d("API Response", "WalletResponse: " + walletResponse.toString());

                    // Kiểm tra success
                    if (walletResponse.isSuccess()) {
                        Wallet walletData = walletResponse.getData().getWallet(); // Lấy wallet từ data

                        if (walletData != null) {
                            long balance2 = walletData.getBalance();

                            SharedPreferences.Editor editor = sharedPreferences2.edit();
                            editor.putLong("balance", balance2);
                            editor.apply();

                        } else {
                            Log.e("API Error", "Wallet data is null");
                            Toast.makeText(Home.this, "Không có dữ liệu ví", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("API Error", "API success field is false");
                        Toast.makeText(Home.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API Error", "Response code: " + response.code());
                    Toast.makeText(Home.this, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WalletResponse> call, Throwable t) {
                if (t instanceof java.net.SocketTimeoutException) {
                    // Xử lý lỗi timeout
                    Toast.makeText(Home.this, "Lỗi: Kết nối quá lâu", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý lỗi khác
                    Toast.makeText(Home.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e("API Error", "Failure: " + t.getMessage(), t);
            }
        });

    }

    // Phương thức hiển thị dropdown menu
    private void showUserMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(Home.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.user_menu, popupMenu.getMenu());

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "Chưa có dữ liệu");
        long balance = sharedPreferences2.getLong("balance", 0);

        popupMenu.getMenu().findItem(R.id.menu_item_balance).setTitle("Số dư: " + NumberFormat.getNumberInstance(Locale.US).format(balance) + " VND");
        popupMenu.getMenu().findItem(R.id.menu_item_email).setTitle("Email: " + email);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_item_address) {
                // Chuyển sang Activity mới
                Intent intent = new Intent(Home.this, UpdateContactActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }


    private void setupRecyclerView() {
        RecyclerView foodPackageRecyclerView = findViewById(R.id.foodPackageRecyclerView);
        FoodPackageAdapter adapter = new FoodPackageAdapter(this, foodPackages);
        foodPackageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodPackageRecyclerView.setAdapter(adapter);
    }

    private void showContactDialog(String currentPhone, String currentAddress) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cập nhật thông tin liên lạc");

        // Tạo layout chính
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);

        // Sử dụng TextInputLayout cho ô nhập số điện thoại
        TextInputLayout phoneInputLayout = new TextInputLayout(this);
        phoneInputLayout.setHint("Số điện thoại"); // Label cho trường nhập
        EditText phoneInput = new EditText(this);
        phoneInput.setInputType(InputType.TYPE_CLASS_PHONE);
        phoneInput.setText(currentPhone); // Hiển thị dữ liệu hiện tại
        phoneInputLayout.addView(phoneInput); // Thêm EditText vào TextInputLayout
        layout.addView(phoneInputLayout); // Thêm TextInputLayout vào layout chính

        // Sử dụng TextInputLayout cho ô nhập địa chỉ
        TextInputLayout addressInputLayout = new TextInputLayout(this);
        addressInputLayout.setHint("Địa chỉ"); // Label cho trường nhập
        EditText addressInput = new EditText(this);
        addressInput.setInputType(InputType.TYPE_CLASS_TEXT);
        addressInput.setText(currentAddress); // Hiển thị dữ liệu hiện tại
        addressInputLayout.addView(addressInput); // Thêm EditText vào TextInputLayout
        layout.addView(addressInputLayout); // Thêm TextInputLayout vào layout chính

        builder.setView(layout);

        // Nút Lưu
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newPhone = phoneInput.getText().toString().trim();
            String newAddress = addressInput.getText().toString().trim();

            if (!newPhone.isEmpty() && !newAddress.isEmpty()) {
                // Lưu số điện thoại và địa chỉ vào SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("phoneNumber", newPhone);
                editor.putString("address", newAddress);
                editor.apply();

                Toast.makeText(Home.this, "Thông tin liên lạc đã được cập nhật!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Home.this, "Cả số điện thoại và địa chỉ không được để trống!", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút Hủy
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }



}
