package com.lepanda.studioneopanda.go4lunch.data;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class RestaurantData extends AppCompatActivity {

    private SharedPreferences prefs;
    public static final String TAG = "RestaurantData";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public String getRestaurantName(){
        prefs = getSharedPreferences("Resto prefs", MODE_PRIVATE);
        String restaurantName = prefs.getString("storedIDs", "");
        //Log.i(TAG, "RestoPlace" + restaurantName);
        return restaurantName;
    }


}