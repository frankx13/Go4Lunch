package com.lepanda.studioneopanda.go4lunch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail);
        super.onCreate(savedInstanceState);

        TextView restaurantName = findViewById(R.id.tv_detail_restaurant_name);
        TextView restaurantAddress = findViewById(R.id.tv_detail_restaurant_address);
        ImageView restaurantPhoto = findViewById(R.id.detail_restaurant_image);

        String RName = getIntent().getStringExtra("RName");
        String RAddress = getIntent().getStringExtra("RAddress");

        //bitmap decoding for get photo
        Bitmap bmp;
        byte[] byteArray = getIntent().getByteArrayExtra("RPhoto");
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        restaurantPhoto.setImageBitmap(bmp);
        restaurantName.setText(RName);
        restaurantAddress.setText(RAddress);
    }
}
