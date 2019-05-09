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
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lepanda.studioneopanda.go4lunch.models.Workmate;
import com.lepanda.studioneopanda.go4lunch.ui.DetailWorkmateAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = "DetailActivity: ";
    private static final int REQUEST_PHONE_CALL = 1;
    private FloatingActionButton fab;
    private int isRestaurantSelected = 0;
    private int isRestaurantLiked = 0;
    private RecyclerView recyclerView;
    private List<Workmate> workmates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail);
        super.onCreate(savedInstanceState);

        recyclerView = findViewById(R.id.detail_workmate_recyclerview);
        TextView restaurantName = findViewById(R.id.tv_detail_restaurant_name);
        TextView restaurantAddress = findViewById(R.id.tv_detail_restaurant_address);
        ImageView restaurantImage = findViewById(R.id.detail_restaurant_image);
        Boolean isReceivedFromMap = getIntent().getBooleanExtra("RCondition", false);
        Boolean isReceivedFromList = false;
        Boolean isReceivedFromWorkmate = false;

        if (isReceivedFromMap) {
            //IMG
            Bitmap RImage;
            byte[] byteArray = getIntent().getByteArrayExtra("RImage");
            RImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            //

            String RName = getIntent().getStringExtra("RName");
            String RAddress = getIntent().getStringExtra("RAddress");
            String RPhone = getIntent().getStringExtra("RPhone");
            String RMail = getIntent().getStringExtra("RMail");
            String RId = getIntent().getStringExtra("RId");


            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            String firebaseUserName = firebaseUser.getDisplayName();

            Log.i(TAG, "NameResto: " + RName + RAddress + RPhone + RMail + RId);

            if (RName != null) {
                restaurantName.setText(RName);
            }

            if (RAddress != null) {
                restaurantAddress.setText(RAddress);
            }

            if (RImage != null) {
                restaurantImage.setImageBitmap(RImage);
            } else {
                Exception e = new Exception();
                e.printStackTrace();
            }

            onSelectionBtn(RName, RId, firebaseUserName);
            onLike(RName, RId, firebaseUserName);

        } else if (isReceivedFromList) {

        } else if (isReceivedFromWorkmate) {

        }

        onBackBtn();
        onPhoneCall();
        onWebSite();
    }

    //OK
    private void onBackBtn() {
        Button backBtnHomeDetail = findViewById(R.id.back_detail_home_btn);
        backBtnHomeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, CentralActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //OK Fbase
    public void onLike(String restName, String restaurantID, String firebaseUserName) {
        TextView likeBtn = findViewById(R.id.like_detail);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailActivity.this, "You liked this restaurant !", Toast.LENGTH_SHORT).show();
                //Store isLiked in Firestore.

                String RId = getIntent().getStringExtra("RId");

                if (isRestaurantLiked == 0) {
                    Toast.makeText(DetailActivity.this, "You liked this restaurant !", Toast.LENGTH_SHORT).show();
                    isRestaurantLiked = 1;
                    likeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_24dp, 0, 0);

                    //+ have to update Firebase here, by indicating it's the selected restaurant and storing the ID
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> data = new HashMap<>();
                    data.put("restaurantID", restaurantID);
                    data.put("restaurantName", restName);
                    data.put("userSender", firebaseUserName);
                    data.put("numberOfLikes", + 1);

                    db.collection("likes").document("Liked by: " + firebaseUserName)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });

                } else if (isRestaurantLiked == 1) {
                    Toast.makeText(DetailActivity.this, "You unliked this restaurant !", Toast.LENGTH_SHORT).show();
                    isRestaurantSelected = 1;
                    isRestaurantLiked = 0;
                    likeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_off_24dp, 0, 0);

                    //+ have to update Firebase here, by indicating that this restaurant is unselected
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("likes").document("Liked by: " + firebaseUserName)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);
                                }
                            });
                }
            }


        });

    }

    // OK
    private void onSelectionBtn(String restName, String restaurantID, String firebaseUserName) {
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

                    // Add a new document with a generated id.
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> data = new HashMap<>();
                    data.put("restaurantID", restaurantID);
                    data.put("restaurantName", restName);
                    data.put("userSender", firebaseUserName);

                    db.collection("selection").document("Choice: " + firebaseUserName)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });

                } else if (isRestaurantSelected == 1) {
                    Toast.makeText(DetailActivity.this, "You cancelled your lunch at this place", Toast.LENGTH_SHORT).show();
                    isRestaurantSelected = 0;
                    restaurantName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_star_24dp, 0);
                    //+ have to update Firebase here, by indicating that this restaurant is unselected

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("selection").document("Choice: " + firebaseUserName)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);
                                }
                            });
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

    //OK
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

    private void onDataLoaded(Workmate workmate) {
        DetailWorkmateAdapter recyclerAdapter = new DetailWorkmateAdapter(this, workmates);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }
}
