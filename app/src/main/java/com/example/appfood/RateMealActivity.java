package com.example.appfood;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateMealActivity extends AppCompatActivity {

    private EditText feedbackEditText;
    private RadioButton radio1, radio2, radio3, radio4, radio5;
    private ApiService apiService;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Initialize views
        feedbackEditText = findViewById(R.id.feedbackEditText);
        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);
        radio4 = findViewById(R.id.radio4);
        radio5 = findViewById(R.id.radio5);

        // Initialize ApiService
        apiService = RetrofitClient.getApiService(this);

        // Handle feedback button click
        findViewById(R.id.sendFeedbackButton).setOnClickListener(v -> submitFeedback());
    }

    // Submit feedback
    private void submitFeedback() {
        String feedback = feedbackEditText.getText().toString();

        // Get the selected star rating
        int selectedStar = getSelectedStar();
        // Lấy id từ SharedPreferences
        SharedPreferences sharedPreferences = this.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        String id = sharedPreferences.getString("id", null);

        String mealID = getIntent().getStringExtra("meal_id");

        // Create the ReviewRequest object
        AddReviewRequest reviewRequest = new AddReviewRequest(selectedStar, feedback,mealID,id);


        if (feedback.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập phản hồi!", Toast.LENGTH_SHORT).show();
        } else if (selectedStar == 0) {
            Toast.makeText(this, "Vui lòng chọn số sao!", Toast.LENGTH_SHORT).show();
        } else {

            // Call the API to submit the review
            apiService.review(reviewRequest).enqueue(new Callback<ReviewResponse>() {

                @Override
                public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                    if (response.isSuccessful()) {
                        // Phản hồi thành công
                        Toast.makeText(RateMealActivity.this, "Phản hồi của bạn đã được gửi!", Toast.LENGTH_SHORT).show();
                        feedbackEditText.setText(""); // Xóa nội dung sau khi gửi
                        finish();
                    } else {
                        // Phản hồi lỗi, in thông tin chi tiết
                        String errorBody = "";
                        try {
                            if (response.errorBody() != null) {
                                errorBody = response.errorBody().string(); // Đọc lỗi từ API
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Log.e("API_ERROR", "Error code: " + response.code() + ", Message: " + errorBody);
                        Toast.makeText(RateMealActivity.this, "Đã có lỗi xảy ra: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<ReviewResponse> call, Throwable t) {
                    // Handle failure
                    Toast.makeText(RateMealActivity.this, "Không thể kết nối đến máy chủ. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Get the selected star rating
    private int getSelectedStar() {
        if (radio1.isChecked()) {
            return 1;
        } else if (radio2.isChecked()) {
            return 2;
        } else if (radio3.isChecked()) {
            return 3;
        } else if (radio4.isChecked()) {
            return 4;
        } else if (radio5.isChecked()) {
            return 5;
        } else {
            return 0; // No star selected
        }
    }
}
