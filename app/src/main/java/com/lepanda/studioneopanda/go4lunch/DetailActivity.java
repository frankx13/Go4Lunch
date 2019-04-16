package com.lepanda.studioneopanda.go4lunch;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;

import java.util.List;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = "DetailActivity: ";
    private static final int REQUEST_PHONE_CALL = 1;
    private FloatingActionButton fab;
    private int isRestaurantSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail);
        super.onCreate(savedInstanceState);

        TextView restaurantName = findViewById(R.id.tv_detail_restaurant_name);
        TextView restaurantAddress = findViewById(R.id.tv_detail_restaurant_address);

        TextView like_detail = findViewById(R.id.like_detail);
        //ImageView restaurantPhoto = findViewById(R.id.detail_restaurant_image);

        //bitmap decoding for get photo
//        Bitmap bmp;
//        byte[] byteArray = getIntent().getByteArrayExtra("RPhoto");
//        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        restaurantPhoto.setImageBitmap(bmp);

        String RName = getIntent().getStringExtra("RName");
        Log.i(TAG, "NameResto: " + RName);
        String RAddress = getIntent().getStringExtra("RAddress");

        restaurantName.setText(RName);
        restaurantAddress.setText(RAddress);

        //Partie Firebase avec token
        like_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        onPhoneCall();
        onWebSite();
        onSelectionBtn();
    }

    private void onSelectionBtn() {
        fab = findViewById(R.id.fab);
        TextView restaurantName = findViewById(R.id.tv_detail_restaurant_name);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRestaurantSelected == 0){
                    Toast.makeText(DetailActivity.this, "You chose this place to eat diner", Toast.LENGTH_SHORT).show();
                    isRestaurantSelected = 1;
                        restaurantName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_star_selected_24dp, 0);
                    //+ have to update Firebase here, by indicating it's the selected restaurant

                }else if (isRestaurantSelected == 1){
                    Toast.makeText(DetailActivity.this, "You cancelled your luncher at this place", Toast.LENGTH_SHORT).show();
                    isRestaurantSelected = 0;
                    restaurantName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_star_24dp, 0);
                    //+ have to update Firebase here, by indicating that this restaurant is unselected
                }
            }
        });
    }

    //call functionnality with number provided
    private void onPhoneCall() {
        TextView call_detail = findViewById(R.id.call_detail);
        String RPhone = getIntent().getStringExtra("RPhone");
        Log.i(TAG, "NameResto: " + RPhone);

        call_detail.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    if (RPhone == null) {
                        Toast.makeText(DetailActivity.this, "This restaurant does not have a phone number", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + RPhone));
                        startActivity(callIntent);
                    }
                }
            }
        });
    }

    private void onWebSite() {
        TextView website_detail = findViewById(R.id.website_detail);
        WebView websiteWebview = findViewById(R.id.websiteWebview);
        Button backBtnWvDetail = findViewById(R.id.back_detail_wv_btn);
        Button backBtnHomeDetail = findViewById(R.id.back_detail_home_btn);
        String RUrl = getIntent().getStringExtra("RUrl");
        //website redirection
        website_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RUrl == null) {
                    Toast.makeText(DetailActivity.this, "This restaurant does not have a website!", Toast.LENGTH_SHORT).show();
                } else {
                    backBtnWvDetail.setVisibility(View.VISIBLE);
                    websiteWebview.setVisibility(View.VISIBLE);
                    backBtnHomeDetail.setVisibility(View.GONE);
                    websiteWebview.loadUrl(RUrl);
                    Log.i(TAG, "WebView URL: " + RUrl);

                    backBtnWvDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backBtnWvDetail.setVisibility(View.GONE);
                            websiteWebview.setVisibility(View.GONE);
                            backBtnHomeDetail.setVisibility(View.VISIBLE);
                        }
                    });

                    backBtnHomeDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //stop acti somehow ?
                            Log.i(TAG, "onClick: ");
                        }
                    });
                }
            }
        });
    }
}
