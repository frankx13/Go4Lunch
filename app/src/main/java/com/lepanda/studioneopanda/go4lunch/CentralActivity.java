package com.lepanda.studioneopanda.go4lunch;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.lepanda.studioneopanda.go4lunch.adapter.ViewPagerAdapter;
import com.lepanda.studioneopanda.go4lunch.fragments.ListFragment;
import com.lepanda.studioneopanda.go4lunch.fragments.MapFragment;
import com.lepanda.studioneopanda.go4lunch.fragments.WorkmatesFragment;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class CentralActivity extends AppCompatActivity {

    public static final String TAG = "MapActivity";

    //ui
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);

        //Methods call
        setViewPager();
        setToolbar();
        setNavigationDrawer();
        setBottomNavigation();
    }


    //NAVIGATION DRAWER BUTTON
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //VIEWPAGER
    private void setViewPager() {

        viewPager = findViewById(R.id.viewpager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new MapFragment(), "Map View");
        adapter.AddFragment(new ListFragment(), "List View");
        adapter.AddFragment(new WorkmatesFragment(), "Workmates");
        viewPager.setAdapter(adapter);
    }

    //BOTTOM NAVIGATION
    private void setBottomNavigation() {
        navigation = findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.navigation_map:
                        viewPager.setCurrentItem(0);
                        Toast.makeText(CentralActivity.this, "MapFragment should appears here", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_list:
                        viewPager.setCurrentItem(1);
                        Toast.makeText(CentralActivity.this, "ListFragment should appears here", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_workmates:
                        viewPager.setCurrentItem(2);
                        Toast.makeText(CentralActivity.this, "WorkmatesFragment should appears here", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });
    }

    //NAVIGATION DRAWER
    private void setNavigationDrawer() {
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
                        Intent intent = new Intent(CentralActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.logout:
                        AuthUI.getInstance()
                                .signOut(CentralActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // ...
                                        Intent intent = new Intent(CentralActivity.this, MainActivity.class);
                                        Toast.makeText(CentralActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(intent);
                                    }
                                });
                        break;
                }
                return true;
            }
        });
    }

    //TOOLBAR
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
    }
}