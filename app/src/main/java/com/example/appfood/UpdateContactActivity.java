package com.example.appfood;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateContactActivity extends AppCompatActivity {

    private EditText phoneInput, addressInput, emailInput, firstNameInput, lastNameInput;
    private Spinner genderSpinner;
    private ImageButton saveButton;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);
        apiService = RetrofitClient.getApiService(this);

        // Khởi tạo các trường nhập
        phoneInput = findViewById(R.id.phoneInput);
        addressInput = findViewById(R.id.addressInput);
        emailInput = findViewById(R.id.emailInput);
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        genderSpinner = findViewById(R.id.genderSpinner);
        saveButton = findViewById(R.id.saveButton);

        // Cài đặt adapter cho Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        // Lấy dữ liệu hiện tại từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentPhone = sharedPreferences.getString("phoneNumber", "");
        String currentAddress = sharedPreferences.getString("addressDelivery", "");
        String currentEmail = sharedPreferences.getString("email", "");
        String currentFirstName = sharedPreferences.getString("firstName", "");
        String currentLastName = sharedPreferences.getString("lastName", "");
        String currentGender = sharedPreferences.getString("gender", "male");

        // Hiển thị dữ liệu lên các trường nhập
        phoneInput.setText(currentPhone);
        addressInput.setText(currentAddress);
        emailInput.setText(currentEmail);
        firstNameInput.setText(currentFirstName);
        lastNameInput.setText(currentLastName);

        // Chọn đúng giá trị trong Spinner
        int genderPosition = adapter.getPosition(getGenderLabel(currentGender));
        genderSpinner.setSelection(genderPosition);

        // Xử lý khi nhấn nút Lưu
        saveButton.setOnClickListener(v -> {
            String newPhone = phoneInput.getText().toString().trim();
            String newAddress = addressInput.getText().toString().trim();
            String newEmail = emailInput.getText().toString().trim();
            String newFirstName = firstNameInput.getText().toString().trim();
            String newLastName = lastNameInput.getText().toString().trim();
            String newGender = getGenderValue(genderSpinner.getSelectedItem().toString());

            if (!newPhone.isEmpty() && !newAddress.isEmpty() && !newEmail.isEmpty()
                    && !newFirstName.isEmpty() && !newLastName.isEmpty() && !newGender.isEmpty()) {

                // Lưu thông tin vào SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("phoneNumber", newPhone);
                editor.putString("addressDelivery", newAddress);
                editor.putString("email", newEmail);
                editor.putString("firstName", newFirstName);
                editor.putString("lastName", newLastName);
                editor.putString("gender", newGender);
                editor.apply();

                // Gửi yêu cầu cập nhật thành phần
                UserInfo.Contact contact = new UserInfo.Contact(newPhone, newAddress, newEmail);
                UserInfo.Info info = new UserInfo.Info(newFirstName, newLastName, newGender);
                UserInfo user = new UserInfo(contact, info);

                apiService.updateUserInfo(user).enqueue(new Callback<UserInfoUpdateResponse>() {
                    @Override
                    public void onResponse(Call<UserInfoUpdateResponse> call, Response<UserInfoUpdateResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(UpdateContactActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(UpdateContactActivity.this, "Không thể cập nhật thông tin. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInfoUpdateResponse> call, Throwable t) {
                        Toast.makeText(UpdateContactActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getGenderValue(String label) {
        switch (label) {
            case "Nam": return "male";
            case "Nữ": return "female";
            case "Khác": return "other";
            default: return "";
        }
    }

    private String getGenderLabel(String value) {
        switch (value) {
            case "male": return "Nam";
            case "female": return "Nữ";
            case "other": return "Khác";
            default: return "";
        }
    }
}
