package com.lepanda.studioneopanda.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MyLunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lunch);

        returnToCentral();
        displayRestName();
    }

    private void goToDetail(String restaurantName) {
        Log.i("FromMyLunchToDetail", "goToDetail: " + restaurantName);
        TextView goToSelectedRest = findViewById(R.id.tv_lunch_name_restaurant);
        goToSelectedRest.setText(restaurantName);
        goToSelectedRest.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra("RestaurantName", "");
            intent.putExtra("RestaurantImage", "");
            intent.putExtra("RestaurantWebsite", "");
            intent.putExtra("RestaurantPhone", "");
            startActivity(intent);
        });
    }

    @Nullable
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private void displayRestName() {
        String model = Objects.requireNonNull(this.getCurrentUser()).getUid();

        //recup nom restau avec Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("selection").document(model);
        Log.i("RESTNAMEINMYLUNCH", model);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null) {
                    if (document.exists()) {
                        Object o = document.get("restaurantName");
                        String restaurantName;
                        if (o != null) {
                            restaurantName = o.toString();
                            Log.i("RESTNAMEINMYLUNCH", restaurantName);
                            goToDetail(restaurantName);
                        }
                    }
                } else {
                    Toast.makeText(this, "You didn't select a restaurant", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //STACKBACK
    private void returnToCentral() {
        Button btnBack = findViewById(R.id.btn_lunch_back);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(MyLunchActivity.this, CentralActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
