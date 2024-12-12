package com.example.appfood;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.text.Html;
import androidx.core.text.HtmlCompat;


public class FoodPackageAdapter extends RecyclerView.Adapter<FoodPackageAdapter.FoodPackageViewHolder> {

    private ArrayList<FoodPackage> foodPackages;
    private Context context; // Context for starting new activities

    // Constructor
    public FoodPackageAdapter(Context context, ArrayList<FoodPackage> foodPackages) {
        this.context = context;
        this.foodPackages = foodPackages;
    }

    @NonNull
    @Override
    public FoodPackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout for each item in RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_package, parent, false);
        return new FoodPackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodPackageViewHolder holder, int position) {
        FoodPackage currentPackage = foodPackages.get(position);

        // Set food name
        holder.foodName.setText(currentPackage.name);

        // Set food description with HTML support
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            holder.foodDescription.setText(HtmlCompat.fromHtml(currentPackage.description, HtmlCompat.FROM_HTML_MODE_LEGACY));
        } else {
            holder.foodDescription.setText(Html.fromHtml(currentPackage.description));
        }

        // Định dạng tiền theo kiểu Việt Nam
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedFoodPrice = currencyFormat.format(currentPackage.price);
        holder.foodPrice.setText(formattedFoodPrice);

        // Set total subscription info
        if (currentPackage.subscriptionID != null) {
            String totalSubText = currentPackage.subscriptionID.totalSub + " người đã đặt";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                holder.totalSub.setText(HtmlCompat.fromHtml(totalSubText, HtmlCompat.FROM_HTML_MODE_LEGACY));
            } else {
                holder.totalSub.setText(Html.fromHtml(totalSubText));
            }

            // Display "mealsPerDay / totalDate" in packageInfo if both values are available
            Integer mealsPerDay = currentPackage.subscriptionID.mealsPerDay;
            Integer totalDate = currentPackage.subscriptionID.totalDate;

            if (mealsPerDay != null && totalDate != null) {
                String packageInfoText = mealsPerDay + " phần / " + totalDate + " ngày";
                holder.packageInfo.setText(packageInfoText);
            } else {
                holder.packageInfo.setText("Thông tin không có"); // Default text if values are missing
            }
        } else {
            holder.totalSub.setText("N/A"); // Handle null subscriptionID case
            holder.packageInfo.setText("Thông tin không có");
        }

        // Load image using URL
        new ImageLoadTask(holder.foodImage).execute(currentPackage.mainImage);

        // Set onClick listener for each item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FoodPackageDetailActivity.class);
            intent.putExtra("name", currentPackage.name);
            intent.putExtra("price", currentPackage.price + "đ");
            intent.putExtra("packageId", currentPackage._id);
            intent.putExtra("subscriptionID", currentPackage.subscriptionID._id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodPackages.size();
    }

    // ViewHolder class to hold each item view
    public static class FoodPackageViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, foodDescription, foodPrice, totalSub, packageInfo;

        public FoodPackageViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.foodImage);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodName = itemView.findViewById(R.id.foodName);
            foodDescription = itemView.findViewById(R.id.foodDescription);
            totalSub = itemView.findViewById(R.id.totalSub);
            packageInfo = itemView.findViewById(R.id.packageInfo); // Added packageInfo reference
        }
    }

    // AsyncTask to load image from URL and display in ImageView
    private static class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public ImageLoadTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap bitmap = null;
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            } else {
                imageView.setImageResource(R.drawable.an);
            }
        }
    }
}
