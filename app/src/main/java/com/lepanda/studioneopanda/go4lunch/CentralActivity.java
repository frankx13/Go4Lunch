package com.lepanda.studioneopanda.go4lunch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lepanda.studioneopanda.go4lunch.adapter.ViewPagerAdapter;
import com.lepanda.studioneopanda.go4lunch.events.NavToDetailEvent;
import com.lepanda.studioneopanda.go4lunch.events.SearchPlaceEvent;
import com.lepanda.studioneopanda.go4lunch.fragments.ListFragment;
import com.lepanda.studioneopanda.go4lunch.fragments.MapFragment;
import com.lepanda.studioneopanda.go4lunch.fragments.WorkmatesFragment;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class CentralActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TAG = "MapActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final String FINE_LOCATION = ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    TextView headerNavDrawName;
    TextView headerNavDrawMail;
    ImageView headerNavDrawImg;
    //POJO liste restaurant
    List<Restaurant> restaurants;
    //ui
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private int countPlaces = 0;
    private int countEntries = 0;
    private FusedLocationProviderClient mFusedLocation;
    private Boolean mLocationPermissionsGranted = false;

    private Location myLocation;
    private GoogleMap mMap;
    private MapFragment mapFragment;
    private AutocompleteSupportFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Places Core API init
        if (!Places.isInitialized()) {
            Places.initialize(this.getApplicationContext(), "AIzaSyBplBBfi-OXVS843E016RmFO0zhMTgLkVw");
        }

        restaurants = new ArrayList();
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);

        //Methods call
        setToolbar();
        setNavigationDrawer();
        setBottomNavigation();
        getDeviceLocation();
        getPlaceAutocomplete(mMap);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    //NAVIGATION DRAWER BUTTON
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //VIEWPAGER
    private void setViewPager() {

        viewPager = findViewById(R.id.viewpager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(MapFragment.newInstance(restaurants, myLocation), "Map View");

        adapter.AddFragment(ListFragment.newInstance(restaurants), "List View");

        adapter.AddFragment(new WorkmatesFragment(), "Workmates");

        viewPager.setAdapter(adapter);
    }

    //BOTTOM NAVIGATION
    private void setBottomNavigation() {
        BottomNavigationView navigation = findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.navigation_map:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_list:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_workmates:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        });
    }

    //NAVIGATION DRAWER
    private void setNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        headerNavDrawName = headerView.findViewById(R.id.header_nd_title);
        headerNavDrawMail = headerView.findViewById(R.id.header_nd_detail);
        headerNavDrawImg = headerView.findViewById(R.id.iv_nd);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        String userNameFirebase = null;
        if (firebaseUser != null) {
            userNameFirebase = firebaseUser.getDisplayName();
        }
        String userMailFirebase = null;
        if (firebaseUser != null) {
            userMailFirebase = firebaseUser.getEmail();
        }

        headerNavDrawName.setText(userNameFirebase);
        headerNavDrawMail.setText(userMailFirebase);
        if (firebaseUser != null) {
            Glide.with(this)
                    .load(firebaseUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform())
                    .into(headerNavDrawImg);
        }

        if (firebaseUser != null) {
            Log.d(TAG, " UserMail: " + userMailFirebase + "Username: " + userNameFirebase + "  " + firebaseUser.getPhotoUrl());
        }


        navigationView.setNavigationItemSelectedListener(menuItem -> {

            // set item as selected to persist highlight
            menuItem.setChecked(true);
            // close drawer when item is tapped
            drawerLayout.closeDrawers();

            switch (menuItem.getItemId()) {

                case R.id.my_lunch:
                    Intent intentLunch = new Intent(CentralActivity.this, MyLunchActivity.class);
                    //intentLunch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentLunch);
                    finish();
                    break;

                case R.id.settings:
                    Intent intentSettings = new Intent(CentralActivity.this, SettingsActivity.class);
                    startActivity(intentSettings);
                    finish();
                    break;

                case R.id.logout:
                    AuthUI.getInstance()
                            .signOut(CentralActivity.this)
                            .addOnCompleteListener(task -> {
                                // ...
                                Intent intent = new Intent(CentralActivity.this, MainActivity.class);
                                Toast.makeText(CentralActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(intent);
                            });
                    break;
            }
            return true;
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

    ////////////////PLACES
    public void fetchCurrentPlaceById(Restaurant restaurant) {

        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.PHONE_NUMBER,
                Place.Field.OPENING_HOURS,
                Place.Field.PHOTO_METADATAS,
                Place.Field.WEBSITE_URI);

        FetchPlaceRequest request = FetchPlaceRequest.builder(restaurant.getPlaceId(), placeFields).build();

        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            final PlacesClient placesClient = Places.createClient(this);
            placesClient.fetchPlace(request).addOnSuccessListener(response -> {

                Place place = response.getPlace();

                // Get the photo metadata.
                if (place.getPhotoMetadatas() != null && place.getPhotoMetadatas().size() > 0) {
                    PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0); // get error ???

                    FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                            .setMaxWidth(150) // Optional.
                            .setMaxHeight(150) // Optional.
                            .build();
                    placesClient.fetchPhoto(photoRequest).addOnSuccessListener(fetchPhotoResponse -> {
                        Bitmap bitmap = fetchPhotoResponse.getBitmap();
                        restaurant.setPhotos(bitmap);
                    });
                }

                restaurant.setPhoneNumber(place.getPhoneNumber());

                if (place.getOpeningHours() != null) {
                    restaurant.setOpeningHours(place.getOpeningHours().getWeekdayText());
                    Log.i(TAG, "onSuccess: " + place.getOpeningHours().getWeekdayText());
                }

                restaurant.setWebsiteURI(String.valueOf(place.getWebsiteUri()));

                countPlaces++;
                if (countEntries == countPlaces) {
                    setViewPager();
                }
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    int statusCode = apiException.getStatusCode();
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
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FindCurrentPlaceResponse response = task.getResult();

                    countEntries = Objects.requireNonNull(response).getPlaceLikelihoods().size();

                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {

                        //if (Objects.requireNonNull(placeLikelihood.getPlace().getTypes()).contains("restaurant") || placeLikelihood.getPlace().getTypes().contains("food")) {

                            Location placeLocation = new Location(LocationManager.GPS_PROVIDER);

                            placeLocation.setLongitude(Objects.requireNonNull(placeLikelihood.getPlace().getLatLng()).longitude);
                            placeLocation.setLatitude(placeLikelihood.getPlace().getLatLng().latitude);

                            Restaurant r = new Restaurant();
                            r.setPlaceId((placeLikelihood.getPlace().getId()));
                            r.setName((placeLikelihood.getPlace().getName()));
                            r.setTypes(String.valueOf(placeLikelihood.getPlace().getTypes()));
                            r.setRating(placeLikelihood.getPlace().getRating());
                            r.setAddress(placeLikelihood.getPlace().getAddress());
                            r.setLatlng(placeLikelihood.getPlace().getLatLng());
                            r.setDistance(myLocation.distanceTo(placeLocation));
                            restaurants.add(r);
                            fetchCurrentPlaceById(r);
//                        }
                    }
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                }
            });

        } else {
            Toast.makeText(this, "Permission not granted for Maps", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //PLACES AUTOCOMPLETE
    private void getPlaceAutocomplete(GoogleMap googleMap) {

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

//        ImageView searchIcon = (ImageView) ((LinearLayout) autocompleteFragment.getView()).getChildAt(0);
//
//        Set the desired icon
//        searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_black_24dp));

        autocompleteFragment.setPlaceFields(Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS,
                Place.Field.PHONE_NUMBER,
                Place.Field.WEBSITE_URI,
                Place.Field.PHOTO_METADATAS));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                EventBus.getDefault().post(new SearchPlaceEvent(place));
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

    }


    // GET USER LOCATION
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: Getting the devices current location");
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();

        try {
            if (mLocationPermissionsGranted) {
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
                final Task location = mFusedLocation.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: Found location!");
                        myLocation = (Location) task.getResult();
                        double currentLat = 0;
                        if (myLocation != null) {
                            currentLat = myLocation.getLatitude();
                        }
                        double currentLon = 0;
                        if (myLocation != null) {
                            currentLon = myLocation.getLongitude();
                        }

                        Log.i(TAG, "onComplete: " + currentLat);
                        Log.i(TAG, "onComplete: " + currentLon);

                        findCurrentPlace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //PERMS
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: Getting location permissions");
        String[] permissions = {ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this,
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted for FineLocation");
            if (ContextCompat.checkSelfPermission(this,
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted for CoarseLocation and FinelLocation");
                mLocationPermissionsGranted = true;
            } else {
                Log.i(TAG, "Permission granted for FineLocation but not CoarseLocation");
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            Log.i(TAG, "Permission refused for FineLocation");
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //Capture l'event avec cette signature
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNavToDetailActivity(NavToDetailEvent event) {

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("restaurant", Parcels.wrap(event.getmRestaurant()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = mapFragment.mMap;
        mMap = googleMap;

        getPlaceAutocomplete(googleMap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_bar, menu);
        return true;
    }
}