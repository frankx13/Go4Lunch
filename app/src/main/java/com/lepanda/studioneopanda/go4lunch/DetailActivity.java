package com.lepanda.studioneopanda.go4lunch;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;
import com.lepanda.studioneopanda.go4lunch.models.Workmate;
import com.lepanda.studioneopanda.go4lunch.ui.DetailWorkmateViewHolder;
import com.lepanda.studioneopanda.go4lunch.ui.DetailWorkmatesAdapter;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    //VARS
    public static final String TAG = "DetailActivity: ";
    private static final int REQUEST_PHONE_CALL = 1;
    private FloatingActionButton fab;
    private int isRestaurantSelected = 0;
    private int isRestaurantLiked = 0;
    private FirestoreRecyclerAdapter<Workmate, DetailWorkmateViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail);
        super.onCreate(savedInstanceState);

        Restaurant mRestaurant = Parcels.unwrap(getIntent().getParcelableExtra("restaurant"));
        TextView restaurantName = findViewById(R.id.tv_detail_restaurant_name);
        TextView restaurantAddress = findViewById(R.id.tv_detail_restaurant_address);
        ImageView restaurantImage = findViewById(R.id.detail_restaurant_image);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String firebaseUID = null;
        if (firebaseUser != null) {
            firebaseUID = firebaseUser.getUid();
        }
        String firebaseUserName = null;
        if (firebaseUser != null) {
            firebaseUserName = firebaseUser.getDisplayName();
        }

        String RName = mRestaurant.getName();
        String RAddress = mRestaurant.getAddress();
        String RId = mRestaurant.getPlaceId();
        String RPhone = mRestaurant.getPhoneNumber();
        String RWebsite = mRestaurant.getWebsiteURI();
        Bitmap RImage = mRestaurant.getPhotos();

        if (RName != null) {
            restaurantName.setText(RName);
        }

        if (RAddress != null) {
            restaurantAddress.setText(RAddress);
        }

        if (RImage != null) {
            restaurantImage.setImageBitmap(RImage);
        }

        //Method calls
        onSelectionBtn(RName, RId, firebaseUID, firebaseUserName);
        onLike(RName, RId, firebaseUID, firebaseUserName);
        onPhoneCall(RPhone);
        onWebSite(RWebsite);
        onBackBtn();

        onPhoneCall(RPhone);
        onWebSite(RWebsite);
        onBackBtn();

        onDataLoaded();
    }

    //OK
    private void onBackBtn() {
        Button backBtnHomeDetail = findViewById(R.id.back_detail_home_btn);
        backBtnHomeDetail.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, CentralActivity.class);
            startActivity(intent);
            finish();
        });
    }

    //OK Fbase
    public void onLike(String restName, String restaurantID, String firebaseUID, String firebaseUserName) {
        TextView likeBtn = findViewById(R.id.like_detail);
        likeBtn.setOnClickListener(v -> {
            Toast.makeText(DetailActivity.this, "You liked this restaurant !", Toast.LENGTH_SHORT).show();

            //Store isLiked in Firestore.
            if (isRestaurantLiked == 0) {
                Toast.makeText(DetailActivity.this, "You liked this restaurant !", Toast.LENGTH_SHORT).show();
                isRestaurantLiked = 1;
                likeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_24dp, 0, 0);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> data = new HashMap<>();
                data.put("restaurantID", restaurantID);
                data.put("restaurantName", restName);
                data.put("userSenderID", firebaseUID);
                data.put("userSenderName", firebaseUserName);
                data.put("numberOfLikes", +1);

                db.collection("likes").document("likesdoc " + firebaseUID)
                        .set(data)
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                        .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));

            } else if (isRestaurantLiked == 1) {
                Toast.makeText(DetailActivity.this, "You unliked this restaurant !", Toast.LENGTH_SHORT).show();
                isRestaurantSelected = 1;
                isRestaurantLiked = 0;
                likeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_off_24dp, 0, 0);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("likes").document("likesdocs" + firebaseUID)
                        .delete()
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                        .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
            }
        });

    }

    private void onSelectionBtn(String restName, String restaurantID, String firebaseUID, String firebaseUserName) {
        fab = findViewById(R.id.fab);
        TextView restaurantName = findViewById(R.id.tv_detail_restaurant_name);
        fab.setOnClickListener(v -> {
            if (isRestaurantSelected == 0) {
                Toast.makeText(DetailActivity.this, "You chose this place to eat diner", Toast.LENGTH_SHORT).show();
                isRestaurantSelected = 1;
                restaurantName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_star_selected_24dp, 0);

                fab.setRippleColor(ColorStateList.valueOf(Color.parseColor("#00FF00")));
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00FF00")));

                // Add a new document with a generated id.
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> data = new HashMap<>();
                data.put("restaurantID", restaurantID);
                data.put("restaurantName", restName);
                data.put("userSenderID", firebaseUID);
                data.put("userSenderName", firebaseUserName);

                db.collection("selection").document(firebaseUID)
                        .set(data)
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                        .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));

            } else if (isRestaurantSelected == 1) {
                Toast.makeText(DetailActivity.this, "You cancelled your lunch at this place", Toast.LENGTH_SHORT).show();
                isRestaurantSelected = 0;
                restaurantName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_star_24dp, 0);


                fab.setRippleColor(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_light)));
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFF000")));

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("selection").document(firebaseUID)
                        .delete()
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                        .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
            }
        });
    }

    private void onPhoneCall(String RPhone) {
        TextView call_detail = findViewById(R.id.call_detail);
        Log.i(TAG, "NameResto: " + RPhone);

        call_detail.setOnClickListener(v -> {

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
        });
    }

    @SuppressLint("RestrictedApi")
    private void onWebSite(String RWebsite) {
        TextView website_detail = findViewById(R.id.website_detail);
        WebView websiteWebview = findViewById(R.id.websiteWebview);
        Button backBtnWvDetail = findViewById(R.id.back_detail_wv_btn);
        Button backBtnHomeDetail = findViewById(R.id.back_detail_home_btn);
        FloatingActionButton fab = findViewById(R.id.fab);
        website_detail.setOnClickListener(v -> {
            if (RWebsite == null) {
                Toast.makeText(DetailActivity.this, "This restaurant does not have a website!", Toast.LENGTH_SHORT).show();
            } else {
                fab.setVisibility(View.GONE);
                backBtnHomeDetail.setVisibility(View.GONE);
                backBtnWvDetail.setVisibility(View.VISIBLE);
                websiteWebview.setVisibility(View.VISIBLE);
                websiteWebview.loadUrl(RWebsite);
                Log.i(TAG, "WebView URL: " + RWebsite);

                backBtnWvDetail.setOnClickListener(v1 -> {
                    backBtnWvDetail.setVisibility(View.GONE);
                    websiteWebview.setVisibility(View.GONE);
                    backBtnHomeDetail.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);
                    websiteWebview.goBack();
                });
            }
        });
    }

    private void onDataLoaded() {
        RecyclerView mRecycler = findViewById(R.id.detail_workmate_recyclerview);
        mRecycler.setHasFixedSize(true);

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mManager);

        FirebaseFirestore mDatabaseRef = FirebaseFirestore.getInstance();
        Query mWorkmateQuery = mDatabaseRef
                .collection("workmates");

        FirestoreRecyclerOptions<Workmate> recyclerOptions = new FirestoreRecyclerOptions.Builder<Workmate>()
                .setQuery(mWorkmateQuery, Workmate.class)
                .build();

        mAdapter = new DetailWorkmatesAdapter(recyclerOptions);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}