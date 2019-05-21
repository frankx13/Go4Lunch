package com.lepanda.studioneopanda.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.lepanda.studioneopanda.go4lunch.models.Restaurant;

public class MyLunchActivity extends AppCompatActivity {

    private Restaurant mRestaurant;

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
