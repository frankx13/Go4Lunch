package com.lepanda.studioneopanda.go4lunch;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //menu.findItem(R.id.navigation_map).setIcon(getDrawable(R.drawable.ic_map_black_24dp));
            //menu.findItem(R.id.navigation_list).setIcon(getDrawable(R.drawable.ic_list_black_24dp));
            //menu.findItem(R.id.navigation_workmates).setIcon(getDrawable(R.drawable.ic_people_black_24dp));
        }

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_map:
                        break;
                    case R.id.navigation_list:
                        Intent a = new Intent(MapActivity.this, ListActivity.class);
                        startActivity(a);
                        //item.setIcon(R.drawable.ic_list_black_24dp);
                        break;
                    case R.id.navigation_workmates:
                        Intent b = new Intent(MapActivity.this,WorkmatesActivity.class);
                        startActivity(b);
                        //item.setIcon(R.drawable.ic_people_black_24dp);
                        break;
                }
                return false;
            }
        });
    }
}
