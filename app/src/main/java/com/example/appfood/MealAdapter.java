package com.example.appfood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private ArrayList<Meal> meals;
    private Context context; // Context for starting new activities
    private ApiService apiService ;
    private String servicePackageID ;

    // Constructor
    public MealAdapter(Context context, ArrayList<Meal> meals, String servicePackageID) {
        this.context = context;
        this.meals = meals;
        this.apiService = RetrofitClient.getApiService(context);
        this.servicePackageID = servicePackageID;

    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout for each item in RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal currentMeal = meals.get(position);

        // Định dạng lại ngày
        try {
            String rawDate = currentMeal.getEstimatedDate();
            SimpleDateFormat rawFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = displayFormat.format(rawFormat.parse(rawDate));
            holder.tvDate.setText(formattedDate);
        } catch (Exception e) {
            holder.tvDate.setText(currentMeal.getEstimatedDate()); // fallback
        }

        // Khung giờ giao với màu vàng
        holder.tvTime.setText("Khung giờ giao: " + currentMeal.getEstimatedTime());
        holder.tvTime.setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_light));

        // Hiển thị trạng thái với tiếng Việt và màu sắc tương ứng
        String status = currentMeal.getStatus();
        String statusText;
        int statusColor;

        // Set status text and color
        switch (status) {
            case "pending":
                statusText = "Chờ xử lý";
                statusColor = ContextCompat.getColor(context, android.R.color.holo_orange_light); // Màu cam
                holder.btnChangeDate.setVisibility(View.VISIBLE); // Show btnChangeDate when pending
                holder.btnDanhGia.setVisibility(View.GONE); // Hide btnDanhGia when pending
                holder.btnMore.setVisibility(View.VISIBLE); // Optionally hide btnMore or keep it visible
                holder.btnViewDetails.setVisibility(View.VISIBLE); // Show btnXoa when not cancelled
                break;
            case "done":
                statusText = "Hoàn thành";
                statusColor = ContextCompat.getColor(context, android.R.color.holo_green_dark); // Màu xanh lục
                holder.btnChangeDate.setVisibility(View.GONE); // Hide btnChangeDate when done
                holder.btnDanhGia.setVisibility(View.VISIBLE); // Show btnDanhGia when done
                holder.btnMore.setVisibility(View.GONE); // Optionally hide btnMore or keep it visible
                holder.btnViewDetails.setVisibility(View.VISIBLE); // Show btnXoa when not cancelled
                break;
            case "cancelled":
                statusText = "Đã hủy";
                statusColor = ContextCompat.getColor(context, android.R.color.holo_red_dark); // Màu đỏ
                holder.btnChangeDate.setVisibility(View.GONE); // Hide btnChangeDate when cancelled
                holder.btnDanhGia.setVisibility(View.GONE); // Hide btnDanhGia when cancelled
                holder.btnMore.setVisibility(View.GONE); // Optionally hide btnMore or keep it visible
                holder.btnViewDetails.setVisibility(View.GONE); // Hide btnXoa when cancelled
                break;
            case "inprogress":
                statusText = "Đang xử lý";
                statusColor = ContextCompat.getColor(context, android.R.color.holo_blue_dark); // Màu xanh dương
                holder.btnChangeDate.setVisibility(View.VISIBLE); // Show btnChangeDate when in progress
                holder.btnDanhGia.setVisibility(View.GONE); // Hide btnDanhGia when in progress
                holder.btnMore.setVisibility(View.VISIBLE); // Optionally show btnMore
                holder.btnViewDetails.setVisibility(View.VISIBLE); // Show btnXoa when not cancelled
                break;
            default:
                statusText = "Không xác định";
                statusColor = ContextCompat.getColor(context, android.R.color.darker_gray); // Màu xám
                holder.btnChangeDate.setVisibility(View.GONE); // Hide btnChangeDate
                holder.btnDanhGia.setVisibility(View.GONE); // Hide btnDanhGia
                holder.btnMore.setVisibility(View.GONE); // Hide btnMore
                holder.btnViewDetails.setVisibility(View.VISIBLE); // Show btnXoa
                break;
        }

        // Set status text and color
        holder.tvStatus.setText(statusText);
        holder.tvStatus.setTextColor(statusColor);

        /////////////////////////////////
        // Set sự kiện click cho các button
        holder.btnChangeDate.setOnClickListener(v -> {
            // Sự kiện khi nhấn vào "Đổi ngày giao"
            // Ví dụ: Mở một activity mới, hoặc hiển thị một dialog
            Intent intent = new Intent(context, ChangeDateActivity.class);
            intent.putExtra("meal_id", currentMeal.get_id()); // Truyền _id
            intent.putExtra("EstimatedDate", currentMeal.getEstimatedDate());
            intent.putExtra("EstimatedTime", currentMeal.getEstimatedTime());

            context.startActivity(intent);
        });
//
        holder.btnViewDetails.setOnClickListener(v -> {
            // Sự kiện khi nhấn vào "Xem chi tiết"
            // Ví dụ: Mở một activity hiển thị chi tiết của meal
            Intent intent = new Intent(context, UpdateFavoriteIngredientsActivity.class);
            intent.putExtra("meal_id", currentMeal.get_id());
            intent.putExtra("servicePackageID", servicePackageID);
            intent.putExtra("favoriteIngredients", new ArrayList<>(currentMeal.getFavoriteIngredients()));

            context.startActivity(intent);
        });
//
        holder.btnMore.setOnClickListener(v -> {
            // Sự kiện khi nhấn vào "Xóa"
            // Ví dụ: Mở một dialog xác nhận xóa
            // Bạn có thể tùy chỉnh theo nhu cầu của mình
            // Ví dụ: gọi một phương thức xóa hoặc hiển thị dialog xác nhận
            showDeleteConfirmationDialog(currentMeal);
        });
//
        holder.btnDanhGia.setOnClickListener(v -> {
            // Sự kiện khi nhấn vào "Đánh giá"
            // Ví dụ: Mở một activity cho phép đánh giá món ăn
            Intent intent = new Intent(context, RateMealActivity.class);
            intent.putExtra("meal_id", currentMeal.get_id()); // Truyền ID của meal để đánh giá
            context.startActivity(intent);
        });
    }




    @Override
    public int getItemCount() {
        return meals.size();
    }

    // ViewHolder class to hold each item view
    public static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTime, tvStatus, tvDescription;
        ImageButton btnChangeDate, btnViewDetails, btnMore,btnDanhGia;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnChangeDate = itemView.findViewById(R.id.btn_change_date);
            btnViewDetails = itemView.findViewById(R.id.btn_view_details);
            btnMore = itemView.findViewById(R.id.btn_xoa);
            btnDanhGia = itemView.findViewById(R.id.btn_danhgia);
        }
    }

    private void showDeleteConfirmationDialog(Meal meal) {
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận hủy bữa ăn")
                .setMessage("Bạn có chắc chắn muốn hủy bữa ăn này không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Xử lý sự kiện xóa
                    deleteMeal(meal);
                })
                .setNegativeButton("Không", (dialog, which) -> {
                    // Đóng dialog
                    dialog.dismiss();
                })
                .create()
                .show();
    }
    private void deleteMeal(Meal meal) {
        CancelMealRequest data = new CancelMealRequest(meal.get_id());

        // Gọi API để hủy item trên server
        apiService.cancelMeal(data) // Thay meal.getId() bằng phương thức lấy ID phù hợp
                .enqueue(new Callback<CancelMealResponse>() {
                    @Override
                    public void onResponse(Call<CancelMealResponse> call, Response<CancelMealResponse> response) {
                        if (response.isSuccessful()) {
                            // Cập nhật trạng thái của bữa ăn trong danh sách
                            for (Meal m : meals) {
                                if (m.get_id().equals(meal.get_id())) {
                                    m.setStatus("cancelled"); // Thay đổi trạng thái của bữa ăn
                                    break;
                                }
                            }
                            // Cập nhật lại giao diện
                            notifyDataSetChanged();  // Cập nhật RecyclerView
                            Toast.makeText(context, "Đã hủy bữa ăn!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Không thể hủy bữa ăn. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CancelMealResponse> call, Throwable t) {
                        Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }




}

