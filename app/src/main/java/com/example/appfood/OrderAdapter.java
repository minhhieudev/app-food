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
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private ArrayList<OrderItem> orders;

    public OrderAdapter(Context context, ArrayList<OrderItem> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItem order = orders.get(position);

        // Set data for UI components
        holder.code.setText(order.getCode());

        // Format and set createdAt date
        String formattedDate = formatDate(order.getCreatedAt());
        holder.createdAt.setText(formattedDate);

        holder.foodName.setText(order.getServicePackage().getName());

        // Định dạng tiền theo kiểu Việt Nam
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = currencyFormat.format(order.getTotalPrice());
        holder.totalPrice.setText(formattedPrice);

        // Load image using Glide or AsyncTask (if using URL)
        new ImageLoadTask(holder.foodImage).execute(order.getServicePackage().getMainImage());

        // Set click listener for item
        holder.itemView.setOnClickListener(v -> {
            // Get the order ID and send it to OrderedMeals Activity
            String orderId = order.getId(); // Assuming you have a method getId() in OrderItem
            String servicePackageID = order.getServicePackage().getServicePackageID(); // Assuming you have a method getId() in OrderItem

            Intent intent = new Intent(context, OrderedMeals.class);
            intent.putExtra("orderID", orderId); // Pass the order ID to the new Activity
            intent.putExtra("servicePackageID", servicePackageID); // Pass the order ID to the new Activity

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView code, createdAt, foodName, totalPrice;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.foodImage);
            code = itemView.findViewById(R.id.code);
            createdAt = itemView.findViewById(R.id.createdAt);
            foodName = itemView.findViewById(R.id.foodName);
            totalPrice = itemView.findViewById(R.id.totalPrice);
        }
    }

    // Method to format the date
    private String formatDate(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        try {
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
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
                URL urlConnection = new URL("https://res.cloudinary.com/dvxn12n91/image/upload/v1720879125/temp/images/" + url);
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
                imageView.setImageResource(R.drawable.an); // Default image if loading fails
            }
        }
    }
}
