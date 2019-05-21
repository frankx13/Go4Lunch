package com.lepanda.studioneopanda.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;
import com.lepanda.studioneopanda.go4lunch.models.Workmate;

public class MyLunchActivity extends AppCompatActivity {

    private Restaurant mRestaurant;
    private TextView goToSelectedRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lunch);

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("RestaurantName", "");
        intent.putExtra("RestaurantImage", "");
        intent.putExtra("RestaurantWebsite", "");
        intent.putExtra("RestaurantPhone", "");
        startActivity(intent);

        returnToCentral();
        displayRestName();
        goToDetail();
    }

    private void goToDetail() {
        goToSelectedRest = findViewById(R.id.tv_lunch_name_restaurant);
    }

    private void displayRestName() {
        Workmate model = new Workmate();

        //recup nom restau avec Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Selection").document(model.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null) {
                    if (document.exists()) {
                        Object o = document.get("restaurantName");
                        String restaurantName;
                        if (o != null) {
                            restaurantName = o.toString();
                            goToSelectedRest.setText(restaurantName);
                            Log.i("RESTNAMEINMYLUNCH", restaurantName);
                        }
                    }
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
