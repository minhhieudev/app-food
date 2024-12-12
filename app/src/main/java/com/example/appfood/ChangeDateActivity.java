package com.example.appfood;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeDateActivity extends AppCompatActivity {

    private EditText deliveryDateEditText;
    private Spinner afternoonDeliverySpinner;
    private Button changeDateButton;
    private ImageView iconCalendar;
    private ApiService apiService ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_date);
        apiService = RetrofitClient.getApiService(this);

        String EstimatedDate = getIntent().getStringExtra("EstimatedDate");
        String EstimatedTime = getIntent().getStringExtra("EstimatedTime");

        // Khởi tạo các view
        deliveryDateEditText = findViewById(R.id.deliveryDateEditText);
        afternoonDeliverySpinner = findViewById(R.id.afternoonDeliverySpinner);
        changeDateButton = findViewById(R.id.changeDateButton);
        iconCalendar = findViewById(R.id.iconCalendar);

        // Thiết lập Spinner với danh sách khung giờ
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.delivery_times, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        afternoonDeliverySpinner.setAdapter(adapter);

        // Xử lý sự kiện click vào calendar icon để chọn ngày
        iconCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // Xử lý sự kiện khi nhấn nút Đặt hàng
        changeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy giá trị ngày giao từ EditText
                String selectedDate = deliveryDateEditText.getText().toString();

                // Chuyển đổi ngày về định dạng yyyy-MM-dd
                String formattedDate = "";
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date date = inputFormat.parse(selectedDate);
                    if (date != null) {
                        formattedDate = outputFormat.format(date);
                    }
                } catch (ParseException e) {
                    Toast.makeText(ChangeDateActivity.this, "Lỗi định dạng ngày: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return; // Dừng tiếp tục nếu không thể chuyển đổi ngày
                }

                // Lấy giá trị giờ giao từ Spinner
                String selectedTime = afternoonDeliverySpinner.getSelectedItem().toString();
                String mealID = getIntent().getStringExtra("meal_id");



                // Tạo đối tượng OrderData
                UpdateDeliveryTimeRequest data = new UpdateDeliveryTimeRequest(formattedDate, selectedTime, mealID);

                // Gọi API để cập nhật ngày giao
                apiService.updateDeliveryTime(data).enqueue(new Callback<UpdateDeliveryTimeResponse>() {
                    @Override
                    public void onResponse(Call<UpdateDeliveryTimeResponse> call, Response<UpdateDeliveryTimeResponse> response) {
                        if (response.isSuccessful()) {
                            UpdateDeliveryTimeResponse updateDeliveryTimeResponse = response.body();
                            if (updateDeliveryTimeResponse != null) {
                                Toast.makeText(ChangeDateActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChangeDateActivity.this, "Lỗi khi cập nhật ngày giao", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ChangeDateActivity.this, "Không thể thay đổi ngày giao. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateDeliveryTimeResponse> call, Throwable t) {
                        Toast.makeText(ChangeDateActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if (EstimatedDate != null) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date = inputFormat.parse(EstimatedDate);
                if (date != null) {
                    String formattedDate = outputFormat.format(date);
                    deliveryDateEditText.setText(formattedDate); // Hiển thị EstimatedDate với định dạng đúng
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi định dạng ngày", Toast.LENGTH_SHORT).show();
            }
        }

        if (EstimatedTime != null) {
            EstimatedTime = EstimatedTime.trim(); // Loại bỏ khoảng trắng thừa
            int position = adapter.getPosition(EstimatedTime);
            if (position >= 0) {
                afternoonDeliverySpinner.setSelection(position); // Đặt vị trí được chọn
            } else {
                Toast.makeText(this, "Khung giờ không hợp lệ: " + EstimatedTime, Toast.LENGTH_SHORT).show();
            }
        }




    }

    // Hàm hiển thị DatePickerDialog
    private void showDatePicker() {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth1) -> {
                    // Hiển thị ngày đã chọn vào EditText
                    deliveryDateEditText.setText(dayOfMonth1 + "/" + (month1 + 1) + "/" + year1);
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }
}
