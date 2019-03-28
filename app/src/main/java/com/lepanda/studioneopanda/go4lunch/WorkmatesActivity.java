package com.lepanda.studioneopanda.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class WorkmatesActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workmates);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_map:
                        Intent a = new Intent(WorkmatesActivity.this, MapActivity.class);
                        startActivity(a);
                        //item.setIcon(R.drawable.ic_map_black_24dp);
                        break;
                    case R.id.navigation_list:
                        Intent b = new Intent(WorkmatesActivity.this, ListActivity.class);
                        startActivity(b);
                        //item.setIcon(R.drawable.ic_list_black_24dp);
                        break;
                    case R.id.navigation_workmates:
                        break;
                }
                return false;
            }
        });
    }
}