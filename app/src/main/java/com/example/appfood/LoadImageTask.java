package com.example.appfood;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.appfood.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;

    public LoadImageTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String imageUrl = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(imageUrl).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
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
            imageView.setImageResource(R.drawable.img_1);  // Hiển thị ảnh mặc định nếu tải thất bại
        }
    }
}
