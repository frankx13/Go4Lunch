package com.lepanda.studioneopanda.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                // set item as selected to persist highlight
                menuItem.setChecked(true);
                // close drawer when item is tapped
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {

                    case R.id.my_lunch:
                        break;

                    case R.id.settings:
                        Intent intent = new Intent(MapActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        //Toast.makeText(MapActivity.this, "Going to the settings section...", Toast.LENGTH_SHORT).show();
                        //finish();
                        break;

                    case R.id.logout:
                        AuthUI.getInstance()
                                .signOut(MapActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // ...
                                        Intent intent = new Intent(MapActivity.this, MainActivity.class);
                                        Toast.makeText(MapActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(intent);
                                    }
                                });
                        break;

                }


                return true;
            }
        });

        //Menu menu = navigation.getMenu();
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //menu.findItem(R.id.navigation_map).setIcon(getDrawable(R.drawable.ic_map_black_24dp));
        //menu.findItem(R.id.navigation_list).setIcon(getDrawable(R.drawable.ic_list_black_24dp));
        //menu.findItem(R.id.navigation_workmates).setIcon(getDrawable(R.drawable.ic_people_black_24dp));
        //}

        BottomNavigationView navigation = findViewById(R.id.navigation);
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
                        Intent b = new Intent(MapActivity.this, WorkmatesActivity.class);
                        startActivity(b);
                        //item.setIcon(R.drawable.ic_people_black_24dp);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
