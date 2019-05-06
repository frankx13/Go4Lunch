package com.lepanda.studioneopanda.go4lunch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lepanda.studioneopanda.go4lunch.models.Restaurant;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = "DetailActivity: ";
    private static final int REQUEST_PHONE_CALL = 1;
    private FloatingActionButton fab;
    private int isRestaurantSelected = 0;
    private int isRestaurantLiked = 0;
    private List<Restaurant> restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail);
        super.onCreate(savedInstanceState);

        TextView restaurantName = findViewById(R.id.tv_detail_restaurant_name);
        TextView restaurantAddress = findViewById(R.id.tv_detail_restaurant_address);
        TextView like_detail = findViewById(R.id.like_detail);
        ImageView restaurantImage = findViewById(R.id.detail_restaurant_image);
        Boolean isReceivedFromMap = getIntent().getBooleanExtra("RCondition", false);
        Boolean isReceivedFromList = false;

        if (isReceivedFromMap){
            //IMG
            Bitmap RImage;
            byte[] byteArray = getIntent().getByteArrayExtra("RImage");
            RImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            //

            String RName = getIntent().getStringExtra("RName");
            String RAddress = getIntent().getStringExtra("RAddress");
            String RPhone = getIntent().getStringExtra("RPhone");
            String RMail = getIntent().getStringExtra("RMail");

            Log.i(TAG, "NameResto: " + RName + RAddress + RPhone + RMail);

            if (RName != null) {
                restaurantName.setText(RName);
            }

            if (RAddress != null) {
                restaurantAddress.setText(RAddress);
            }

            if (RImage != null){
                restaurantImage.setImageBitmap(RImage);
            } else {
                Exception e = new Exception();
                e.printStackTrace();
            }
        } else if (isReceivedFromList){

        }


        onBackBtn();
        onSelectionBtn();
        onPhoneCall();
        onLike();
        onWebSite();
    }

    //OK
    private void onBackBtn() {
        Button backBtnHomeDetail = findViewById(R.id.back_detail_home_btn);
        backBtnHomeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stop acti somehow ?
                Intent intent = new Intent(DetailActivity.this, CentralActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //OK Fbase
    private void onLike() {
        TextView likeBtn = findViewById(R.id.like_detail);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailActivity.this, "You liked this restaurant !", Toast.LENGTH_SHORT).show();
                //Store isLiked in Firestore.

                if (isRestaurantLiked == 0) {
                    Toast.makeText(DetailActivity.this, "You liked this restaurant !", Toast.LENGTH_SHORT).show();
                    isRestaurantLiked = 1;
                    likeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_24dp, 0, 0);
                    //+ have to update Firebase here, by indicating it's the selected restaurant

                } else if (isRestaurantLiked == 1) {
                    Toast.makeText(DetailActivity.this, "You unliked this restaurant !", Toast.LENGTH_SHORT).show();                    isRestaurantSelected = 1;
                    isRestaurantLiked = 0;
                    likeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_off_24dp, 0, 0);
                    //+ have to update Firebase here, by indicating that this restaurant is unselected
                }
            }


        });

    }

    // OK Fbase
    private void onSelectionBtn() {
        fab = findViewById(R.id.fab);
        TextView restaurantName = findViewById(R.id.tv_detail_restaurant_name);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRestaurantSelected == 0) {
                    Toast.makeText(DetailActivity.this, "You chose this place to eat diner", Toast.LENGTH_SHORT).show();
                    isRestaurantSelected = 1;
                    restaurantName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_star_selected_24dp, 0);
                    //+ have to update Firebase here, by indicating it's the selected restaurant

                } else if (isRestaurantSelected == 1) {
                    Toast.makeText(DetailActivity.this, "You cancelled your lunch at this place", Toast.LENGTH_SHORT).show();
                    isRestaurantSelected = 0;
                    restaurantName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_star_24dp, 0);
                    //+ have to update Firebase here, by indicating that this restaurant is unselected
                }
            }
        });
    }

    //OK
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

    //OK -fab doesnt go invisible
    private void onWebSite() {
        TextView website_detail = findViewById(R.id.website_detail);
        WebView websiteWebview = findViewById(R.id.websiteWebview);
        Button backBtnWvDetail = findViewById(R.id.back_detail_wv_btn);
        Button backBtnHomeDetail = findViewById(R.id.back_detail_home_btn);
        FloatingActionButton fab = findViewById(R.id.fab);
        String RUrl = getIntent().getStringExtra("RMail");
        website_detail.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (RUrl == null) {
                    Toast.makeText(DetailActivity.this, "This restaurant does not have a website!", Toast.LENGTH_SHORT).show();
                } else {
                    fab.setVisibility(View.GONE);
                    backBtnHomeDetail.setVisibility(View.GONE);
                    backBtnWvDetail.setVisibility(View.VISIBLE);
                    websiteWebview.setVisibility(View.VISIBLE);
                    websiteWebview.loadUrl(RUrl);
                    Log.i(TAG, "WebView URL: " + RUrl);

                    backBtnWvDetail.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("RestrictedApi")
                        @Override
                        public void onClick(View v) {
                            backBtnWvDetail.setVisibility(View.GONE);
                            websiteWebview.setVisibility(View.GONE);
                            backBtnHomeDetail.setVisibility(View.VISIBLE);
                            fab.setVisibility(View.VISIBLE);
                            websiteWebview.goBack();
                        }
                    });
                }
            }
        });
    }
}
