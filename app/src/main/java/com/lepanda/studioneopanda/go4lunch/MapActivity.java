package com.lepanda.studioneopanda.go4lunch;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    //map
    public static final String TAG = "MapActivity";
    public static final int DEFAULT_ZOOM = 15;
    private static final String FINE_LOCATION = ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    //ui
    private DrawerLayout drawerLayout;

    //widgets
    private ImageView mGps;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            mMap.setMyLocationEnabled(true); // to add a blue dot on us, and icon to go back to our position
            mMap.getUiSettings().setMyLocationButtonEnabled(false); // to disable icon to go back to our position

            init();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        //MAPS
        mGps = findViewById(R.id.ic_gps);

        // Initialize Places.
        Places.initialize(getApplicationContext(), Integer.toString(R.string.google_maps_api_key));

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME);

// Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.builder(placeFields).build();

//Call findCurrentPlace and handle the response (first check that the user has granted permission).
//        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
//            placeResponse.addOnCompleteListener(task -> {
//                if (task.isSuccessful()){
//                    FindCurrentPlaceResponse response = task.getResult();
//                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
//                        Log.i(TAG, String.format("Place '%s' has likelihood: %f",
//                                placeLikelihood.getPlace().getName(),
//                                placeLikelihood.getLikelihood()));
//                    }
//                } else {
//                    Exception exception = task.getException();
//                    if (exception instanceof ApiException) {
//                        ApiException apiException = (ApiException) exception;
//                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
//                    }
//                }
//            });
//        } else {
//            // A local method to request required permissions;
//            // See https://developer.android.com/training/permissions/requesting
//            getLocationPermission();
//        }


        getLocationPermission();
        //UI
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

    private void init() {
        Log.d(TAG, "init: Initializing");

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked gps icon");
                getDeviceLocation();
            }
        });
        hideKeyboard(MapActivity.this);
        //hideSoftKeyboard();
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: Getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {

            if (mLocationPermissionsGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Found location!");
                            Location currentLocation = (Location) task.getResult();

                            if (currentLocation != null) {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM,
                                        "My Location");
                            }
                        } else {
                            Log.d(TAG, "onComplete: Current location is null");
                            Toast.makeText(MapActivity.this, "Unable to get the current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: Moving the camera to: to lat:" + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }

        hideKeyboard(MapActivity.this);
        //hideSoftKeyboard();
    }

    private void initMap() {
        Log.d(TAG, "initMap: Initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: Getting location permissions");
        String[] permissions = {ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: Called.");
        mLocationPermissionsGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: Permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                    mLocationPermissionsGranted = true;
                    //init our map
                    initMap();
                }
            }
        }
    }
}
