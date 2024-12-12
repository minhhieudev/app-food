package com.example.appfood;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private final Context context;
    private final List<Ingredient> ingredientList;
    private final boolean showCheckbox;

    public IngredientAdapter(Context context, List<Ingredient> ingredientList, boolean showCheckbox) {
        this.context = context;
        this.ingredientList = ingredientList;
        this.showCheckbox = showCheckbox;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);

        // Thiết lập tên món ăn
        holder.foodNameTextView.setText(ingredient.getName());

        // Tải ảnh món ăn
        loadImage(holder.foodImageView, ingredient.getImage());

        // Hiển thị CheckBox (nếu cần)
        setupCheckbox(holder.ingredientCheckbox, ingredient);

        // Hiển thị các tag
        setupTags(holder.tagContainer, ingredient.getITags());
    }

    @Override
    public int getItemCount() {
        return ingredientList != null ? ingredientList.size() : 0;
    }

    // Phương thức tải ảnh sử dụng Glide
    private void loadImage(ImageView imageView, String imageName) {
        String imageUrl = "https://res.cloudinary.com/dvxn12n91/image/upload/v1720879125/temp/images/" + imageName;
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.img_2) // Ảnh placeholder
                .error(R.drawable.ot)          // Ảnh lỗi
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Tải và lưu cache
                .into(imageView);
    }

    // Phương thức thiết lập CheckBox
    private void setupCheckbox(CheckBox checkBox, Ingredient ingredient) {
        if (showCheckbox) {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(ingredient.isChecked());
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> ingredient.setChecked(isChecked));
        } else {
            checkBox.setVisibility(View.GONE);
        }
    }

    // Phương thức hiển thị các tag
    private void setupTags(LinearLayout tagContainer, List<ITag> tags) {
        tagContainer.removeAllViews();

        if (tags != null && !tags.isEmpty()) {
            for (ITag tag : tags) {
                ImageView tagView = new ImageView(context);
                tagView.setBackground(createTagBackground(tag.getColor()));

                // Thiết lập kích thước và khoảng cách cho tag
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60, 60); // Kích thước tag
                params.setMargins(8, 0, 8, 0); // Khoảng cách giữa các tag
                tagView.setLayoutParams(params);

                tagContainer.addView(tagView);
            }
        }
    }

    // Phương thức tạo hình tròn màu cho tag
    private GradientDrawable createTagBackground(String color) {
        GradientDrawable circleDrawable = new GradientDrawable();
        circleDrawable.setShape(GradientDrawable.OVAL);
        try {
            circleDrawable.setColor(Color.parseColor(color)); // Đặt màu tag
        } catch (IllegalArgumentException e) {
            circleDrawable.setColor(Color.GRAY); // Màu mặc định nếu mã màu không hợp lệ
        }
        circleDrawable.setSize(50, 50);
        return circleDrawable;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImageView;
        TextView foodNameTextView;
        LinearLayout tagContainer;
        CheckBox ingredientCheckbox;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImageView = itemView.findViewById(R.id.foodImageView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            tagContainer = itemView.findViewById(R.id.tagContainer);
            ingredientCheckbox = itemView.findViewById(R.id.ingredientCheckbox);
        }
    }
}
