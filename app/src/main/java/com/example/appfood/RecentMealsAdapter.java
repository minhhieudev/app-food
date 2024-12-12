package com.example.appfood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class RecentMealsAdapter extends RecyclerView.Adapter<RecentMealsAdapter.ViewHolder> {

    private List<String> recentMealsList;
    private static final String BASE_URL = "https://res.cloudinary.com/dvxn12n91/image/upload/v1720879125/temp/images/";
    private static final String DEFAULT_IMAGE_URL = "https://res.cloudinary.com/dvxn12n91/image/upload/v1720879125/temp/images/pezemcjns5ibxmnph82b.png";

    public RecentMealsAdapter(List<String> recentMealsList) {
        this.recentMealsList = recentMealsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageFilename = recentMealsList.get(position);
        String imageUrl = BASE_URL + imageFilename; // Combine base URL with the image filename

        // Load image using Glide
        Glide.with(holder.recentMealImageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.img_2) // Placeholder image while loading
                .error(R.drawable.img_2)             // Error image if load fails
                .diskCacheStrategy(DiskCacheStrategy.ALL)  // Cache images for faster loading
                .into(holder.recentMealImageView);
    }

    @Override
    public int getItemCount() {
        return recentMealsList != null ? recentMealsList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView recentMealImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recentMealImageView = itemView.findViewById(R.id.recentMealImageView);
        }
    }
}
