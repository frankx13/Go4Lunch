package com.lepanda.studioneopanda.go4lunch.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.lepanda.studioneopanda.go4lunch.CentralActivity;
import com.lepanda.studioneopanda.go4lunch.DetailActivity;
import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    //map
    public static final String TAG = "MapFragment: ";
    public static final int DEFAULT_ZOOM = 15;
    private static final String FINE_LOCATION = ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    //POJO liste restaurant
    List<Restaurant> restaurants;
    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    //widgets
    private ImageView mGps;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        // Places Core API init
        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), "AIzaSyBplBBfi-OXVS843E016RmFO0zhMTgLkVw");
        }
        //restaurant list
        restaurants = new ArrayList();
        mGps = v.findViewById(R.id.ic_gps);
        getLocationPermission();
        findCurrentPlace();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void fetchCurrentPlaceById(Restaurant restaurant) {

        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.PHONE_NUMBER,
                Place.Field.OPENING_HOURS,
                Place.Field.PHOTO_METADATAS,
                Place.Field.WEBSITE_URI);

        FetchPlaceRequest request = FetchPlaceRequest.builder(restaurant.getPlaceId(), placeFields).build();

        if (ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            final PlacesClient placesClient = Places.createClient(getContext());
            placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                @Override
                public void onSuccess(FetchPlaceResponse response) {

                    Place place = response.getPlace();

                    // Get the photo metadata.
//                    PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);
//
//                    // Create a FetchPhotoRequest.
//                    FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
//                            .setMaxWidth(500) // Optional.
//                            .setMaxHeight(300) // Optional.
//                            .build();
//                    placesClient.fetchPhoto(photoRequest).addOnSuccessListener(new OnSuccessListener<FetchPhotoResponse>() {
//                        @Override
//                        public void onSuccess(FetchPhotoResponse fetchPhotoResponse) {
//                            Bitmap bitmap = fetchPhotoResponse.getBitmap();
//                            restaurant.setPhotos(bitmap);
//                        }
//
//                    }).addOnFailureListener((exception) -> {
//                        if (exception instanceof ApiException) {
//                            ApiException apiException = (ApiException) exception;
//                            int statusCode = apiException.getStatusCode();
//                            // Handle error with given status code.
//                            Log.e(TAG, "Place not found: " + exception.getMessage());
//                        }
//                    });


                    restaurant.setPhoneNumber(place.getPhoneNumber());
                    restaurant.setOpeningHours(String.valueOf(place.getOpeningHours()));
                    restaurant.setWebsiteURI(String.valueOf(place.getWebsiteUri()));
                    Log.i(TAG, "WEBSITE: " + String.valueOf(place.getWebsiteUri()));


                    MapFragment.this.showOnMap(restaurant);

                    for (int i = 0; i < restaurant.getPlaceId().length(); i++) {

                        Log.i(TAG, String.format("Place found from ID is: 1: %s + 2: %s + 3: %s",
                                place.getPhoneNumber(),
                                place.getOpeningHours(),
                                place.getWebsiteUri()));
                        //place.getPhotoMetadatas(),

                        if (i > 8) {
                            break;
                        }
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

    public void showOnMap(Restaurant restaurant) {
        //MarkerOptions.CREATOR
        //zIndex avec la distance de l'user ?

        if (restaurant.getTypes().contains("RESTAURANT") || restaurant.getTypes().contains("FOOD")) { // this condition doesnt apply ?? PROBLEM
            for (int i = 0; i < restaurants.size(); i++) {
                mMap.addMarker(new MarkerOptions()
                        .position(restaurants.get(i).getLatlng())
                        .title(restaurants.get(i).getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        .visible(true));

                int finalI = i;

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        //Compressing BitMap
                        //ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                        //restaurants.get(finalI).getPhotos().compress(Bitmap.CompressFormat.PNG, 100, bStream);
                        //byte[] byteArray = bStream.toByteArray();

                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        String nameRestaurant = marker.getTitle(); //restaurant name

                        intent.putExtra("RAddress", restaurants.get(finalI).getAddress()); //address
                        intent.putExtra("RName", nameRestaurant);
                        intent.putExtra("RPhone", restaurants.get(finalI).getPhoneNumber());
                        intent.putExtra("RUrl", restaurant.getWebsiteURI());

                        //intent.putExtra("RPhoto", byteArray); //photo

                        startActivity(intent);
                        return true;
                    }
                });
            }
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
        if (ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            final PlacesClient placesClient = Places.createClient(getContext());
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful()) {

                        FindCurrentPlaceResponse response = task.getResult();

                        //set Max entries for places
                        int M_MAX_ENTRIES = 8;
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

                            Restaurant r = new Restaurant();
                            r.setPlaceId((placeLikelihood.getPlace().getId()));
                            r.setName((placeLikelihood.getPlace().getName()));
                            r.setTypes(String.valueOf(placeLikelihood.getPlace().getTypes()));
                            r.setRating(placeLikelihood.getPlace().getRating());
                            r.setAddress(placeLikelihood.getPlace().getAddress());
                            r.setLatlng(placeLikelihood.getPlace().getLatLng());
                            restaurants.add(r);
                            fetchCurrentPlaceById(r);

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
            Toast.makeText(getContext(), "ERROR!ERROR!ERROR!", Toast.LENGTH_SHORT).show();
        }
        //return mPlaceIDs;

    }


    // --------------------
    // ONMAPREADY
    // --------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getContext(), "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.style_json));
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
    }

    // GET USER LOCATION
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: Getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

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
                            } else {
                                Log.i(TAG, "Couldn't place camera on loc");
                            }
                        } else {
                            Log.d(TAG, "onComplete: Current location is null");
                            Toast.makeText(getContext(), "Unable to get the current location", Toast.LENGTH_SHORT).show();
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
    }

    // INIT
    private void initMap() {
        Log.d(TAG, "initMap: Initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //PERMS
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: Getting location permissions");
        String[] permissions = {ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted for FineLocation");
            if (ContextCompat.checkSelfPermission(getContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted for CoarseLocation and FinelLocation");
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                Log.i(TAG, "Permission granted for FineLocation but not CoarseLocation");
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            Log.i(TAG, "Permission refused for FineLocation");
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
}