package com.example.appfood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodPackageAdapter extends RecyclerView.Adapter<FoodPackageAdapter.FoodPackageViewHolder> {

    private ArrayList<FoodPackage> foodPackages;

    // Constructor
    public FoodPackageAdapter(ArrayList<FoodPackage> foodPackages) {
        this.foodPackages = foodPackages;
    }

    @NonNull
    @Override
    public FoodPackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_package, parent, false);
        return new FoodPackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodPackageViewHolder holder, int position) {
        // Bind data to the views
        FoodPackage currentPackage = foodPackages.get(position);
        holder.foodName.setText(currentPackage.name);
        holder.foodDescription.setText(currentPackage.description);
        holder.foodPrice.setText(currentPackage.price + "đ");
        holder.foodImage.setImageResource(currentPackage.imageRes);
    }

    @Override
    public int getItemCount() {
        return foodPackages.size();
    }

    // ViewHolder class to hold each item view
    public static class FoodPackageViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, foodDescription, foodPrice;

        public FoodPackageViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.foodImage);
            foodName = itemView.findViewById(R.id.foodName);
            foodDescription = itemView.findViewById(R.id.foodDescription);
            foodPrice = itemView.findViewById(R.id.foodPrice);
        }
    }
}
