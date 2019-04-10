package com.lepanda.studioneopanda.go4lunch;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.lepanda.studioneopanda.go4lunch.adapter.ViewPagerAdapter;
import com.lepanda.studioneopanda.go4lunch.data.RestaurantData;
import com.lepanda.studioneopanda.go4lunch.fragments.ListFragment;
import com.lepanda.studioneopanda.go4lunch.fragments.MapFragment;
import com.lepanda.studioneopanda.go4lunch.fragments.WorkmatesFragment;

import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class CentralActivity extends AppCompatActivity implements OnMapReadyCallback {

    //map
    public static final String TAG = "MapActivity";
    public static final int DEFAULT_ZOOM = 15;
    private static final String FINE_LOCATION = ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    //ui
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private BottomNavigationView navigation;

    //widgets
    private ImageView mGps;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // --------------------
    // KEYBOARD HIDE
    // --------------------
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


    // --------------------
    // ONMAPREADY
    // --------------------

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));
            updateLocationUI();
            init();
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionsGranted) {
                mMap.setMyLocationEnabled(true); // to add a blue dot on us, and icon to go back to our position
                mMap.getUiSettings().setMyLocationButtonEnabled(false); // to disable icon to go back to our position


            } else {
                mMap.setMyLocationEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);

        // Places Core API init
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBplBBfi-OXVS843E016RmFO0zhMTgLkVw");
        }

        //Gmap init
        mGps = findViewById(R.id.ic_gps);

        //Methods call
        getLocationPermission();
        setViewPager();
        setToolbar();
        setNavigationDrawer();
        setBottomNavigation();
        findCurrentPlace();
        fetchCurrentPlaceById();
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

    private void fetchCurrentPlaceById() {
        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.PHONE_NUMBER,
                Place.Field.OPENING_HOURS,
                Place.Field.WEBSITE_URI);

        SharedPreferences preferences = getSharedPreferences("ID prefs", MODE_PRIVATE);
        String storedPreference = preferences.getString("storedIDs", "");

        Log.i(TAG, "PlaceIDReceived: " + Arrays.toString(new String[]{storedPreference}));

        FetchPlaceRequest request = FetchPlaceRequest.builder(Arrays.toString(new String[]{storedPreference}), placeFields).build();

        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            final PlacesClient placesClient = Places.createClient(this);
            placesClient.fetchPlace(request).addOnSuccessListener((response) -> {

                Place place = response.getPlace();

                String[] mPlacePhoneNumber = new String[3];
                String[] mPlaceOpeningHours = new String[3];
                String[] mPlaceIDsWebsiteUri = new String[3];

                for (int i = 0; i < storedPreference.length(); i++) {

                    Log.i(TAG, String.format("Place found from ID is: 1: %s + 2: %s + 3: %s",
                            place.getPhoneNumber(),
                            place.getOpeningHours(),
                            place.getWebsiteUri()));

                    mPlacePhoneNumber[i] = place.getPhoneNumber();
                    mPlaceOpeningHours[i] = String.valueOf(place.getOpeningHours());
                    mPlaceIDsWebsiteUri[i] = String.valueOf(place.getWebsiteUri());

                    //Phone SP
                    SharedPreferences preferencesPhone = getSharedPreferences("Phone prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editorPhone = preferencesPhone.edit();
                    editorPhone.putString("RestoPhone", Arrays.toString(mPlacePhoneNumber));
                    editorPhone.commit();

                    //Hours SP
                    SharedPreferences preferencesOpeningHours = getSharedPreferences("Hours prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editorHours = preferencesOpeningHours.edit();
                    editorHours.putString("RestoHours", Arrays.toString(mPlaceOpeningHours));
                    editorHours.commit();

                    //Uri SP
                    SharedPreferences preferencesURI = getSharedPreferences("URI prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editorURI = preferencesURI.edit();
                    editorURI.putString("RestoURI", Arrays.toString(mPlaceIDsWebsiteUri));
                    editorURI.commit();

                    Log.i(TAG, "PlacePhoneNumber" + Arrays.toString(mPlacePhoneNumber));
                    Log.i(TAG, "PlaceOpeningHour" + Arrays.toString(mPlaceOpeningHours));
                    Log.i(TAG, "PlaceWebsiteURI" + Arrays.toString(mPlaceIDsWebsiteUri));

                    if (i > 2) {
                        break;
                    }
                }
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    int statusCode = apiException.getStatusCode();
                    // Handle error with given status code.
                    Log.e(TAG, "Place not found: " + exception.getMessage());
                }
            });
        }
    }


    //PLACES CURRENTPLACE API
    private void findCurrentPlace() {  //private String[] findCurrentPlace() {
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.NAME,
                Place.Field.TYPES,
                Place.Field.ID,
                Place.Field.RATING,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.builder(placeFields).build();

// Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            final PlacesClient placesClient = Places.createClient(this);
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful()) {

                        FindCurrentPlaceResponse response = task.getResult();

                        //set Max entries for places
                        int M_MAX_ENTRIES = 3;
                        // Set the count, handling cases where less than 3 entries are returned.
                        int count;
                        if (response.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                            count = response.getPlaceLikelihoods().size();
                        } else {
                            count = M_MAX_ENTRIES;
                        }
                        int i = 0;
                        String[] mPlaceNames = new String[count];
                        String[] mPlaceTypes = new String[count];
                        String[] mPlaceIDs = new String[count];
                        Double[] mPlaceRatings = new Double[count];
                        String[] mPlaceAdress = new String[count];
                        LatLng[] mPlaceLatLng = new LatLng[count];

                        for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                            //LOG all getters for types selected
                            Log.i(TAG, String.format("Place '%s' has likelihood: '%f' and is of type: '%s' with ID: '%s' with RATING: '%s' with Address: '%s' with latlong: '%s'",
                                    placeLikelihood.getPlace().getName(),
                                    placeLikelihood.getLikelihood(),
                                    placeLikelihood.getPlace().getTypes(),
                                    placeLikelihood.getPlace().getId(),
                                    placeLikelihood.getPlace().getRating(),
                                    placeLikelihood.getPlace().getAddress(),
                                    placeLikelihood.getPlace().getLatLng()));

                            // Build a list of likely places to show the user.
                            mPlaceNames[i] = placeLikelihood.getPlace().getName();
                            mPlaceTypes[i] = placeLikelihood.getPlace().getAddress();
                            mPlaceIDs[i] = placeLikelihood.getPlace().getId();
                            mPlaceRatings[i] = placeLikelihood.getPlace().getRating();
                            mPlaceAdress[i] = placeLikelihood.getPlace().getAddress();
                            mPlaceLatLng[i] = placeLikelihood.getPlace().getLatLng();

                            //IDS SP
                            SharedPreferences preferences = getSharedPreferences("ID prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("storedIDs", Arrays.toString(mPlaceIDs)); // value to store
                            editor.commit();

                            //Name SP
                            SharedPreferences preferencesRestaurant = getSharedPreferences("Resto prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editorResto = preferencesRestaurant.edit();
                            editorResto.putString("RestoName", Arrays.toString(mPlaceNames));
                            editorResto.commit();

                            //Types SP
                            SharedPreferences preferencesTypes = getSharedPreferences("Types prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editorTypes = preferencesTypes.edit();
                            editorTypes.putString("RestoTypes", Arrays.toString(mPlaceTypes));
                            editorTypes.commit();

                            //Ratings SP
                            SharedPreferences preferencesRatings = getSharedPreferences("Ratings prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editorRatings = preferencesRatings.edit();
                            editorRatings.putString("RestoRatings", Arrays.toString(mPlaceRatings));
                            editorRatings.commit();

                            //Address SP
                            SharedPreferences preferencesAddress = getSharedPreferences("Address prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editorAddress = preferencesAddress.edit();
                            editorAddress.putString("RestoAddress", Arrays.toString(mPlaceAdress));
                            editorAddress.commit();

                            i++;
                            if (i > (count - 1)) {
                                break;
                            }

                            // Log arrays of data about Places
                            Log.i(TAG, "PlaceNames" + Arrays.toString(mPlaceNames));
                            Log.i(TAG, "PlaceTypes" + Arrays.toString(mPlaceTypes));
                            Log.i(TAG, "PlaceIDs" + Arrays.toString(mPlaceIDs));
                            Log.i(TAG, "PlaceRatings" + Arrays.toString(mPlaceRatings));
                            Log.i(TAG, "PlaceAddress" + Arrays.toString(mPlaceAdress));
                            Log.i(TAG, "PlaceLatLng" + Arrays.toString(mPlaceLatLng));
                        }
                    } else {
                        Exception exception = task.getException();
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                        }
                    }
                }
            });
        } else {
            // A local method to request required permissions;
            // See https://developer.android.com/training/permissions/requesting
            //getLocationPermission();
            Toast.makeText(this, "ERROR!ERROR!ERROR!", Toast.LENGTH_SHORT).show();
        }
        //return mPlaceIDs;

    }


    //VIEWPAGER
    private void setViewPager() {
        // --------------------
        // ViewPager
        // --------------------

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
                        return true;

                    case R.id.navigation_list:
                        viewPager.setCurrentItem(1);
                        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        // Replace the contents of the container with the new fragment
                        ft.replace(R.id.content_frame, new ListFragment());
                        ft.commit();
                        Log.i(TAG, "LIST FRAGMENT SHOULD POP HERE");
                        return true;
                    case R.id.navigation_workmates:
                        viewPager.setCurrentItem(2);
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
                        //Toast.makeText(MapActivity.this, "Going to the settings section...", Toast.LENGTH_SHORT).show();
                        //finish();
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

    // --------------------
    // GOOGLE MAPS
    // --------------------


    // INIT MAPS
    private void init() {
        Log.d(TAG, "init: Initializing");

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked gps icon");
                getDeviceLocation();
            }
        });
        hideKeyboard(CentralActivity.this);
        //hideSoftKeyboard();
    }

    // GET USER LOCATION
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
                                        18,
                                        "My Location");
                            }
                        } else {
                            Log.d(TAG, "onComplete: Current location is null");
                            Toast.makeText(CentralActivity.this, "Unable to get the current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    // CAMERA
    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: Moving the camera to: to lat:" + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }

        hideKeyboard(CentralActivity.this);
        //hideSoftKeyboard();
    }

    // INIT
    private void initMap() {
        Log.d(TAG, "initMap: Initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(CentralActivity.this);
    }

    //PERMS
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

    // PERM RESULT
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
