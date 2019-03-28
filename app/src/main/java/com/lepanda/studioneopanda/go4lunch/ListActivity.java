package com.lepanda.studioneopanda.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_map:
                        Intent a = new Intent(ListActivity.this, MapActivity.class);
                        startActivity(a);
                        //item.setIcon(R.drawable.ic_map_black_24dp);
                        break;
                    case R.id.navigation_list:
                        break;
                    case R.id.navigation_workmates:
                        Intent b = new Intent(ListActivity.this,WorkmatesActivity.class);
                        startActivity(b);
                        //item.setIcon(R.drawable.ic_people_black_24dp);
                        break;
                }
                return false;
            }
        });
    }
}
