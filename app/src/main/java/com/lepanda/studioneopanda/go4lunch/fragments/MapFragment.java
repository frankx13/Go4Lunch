package com.lepanda.studioneopanda.go4lunch.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.events.NavToDetailEvent;
import com.lepanda.studioneopanda.go4lunch.events.SearchPlaceEvent;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    //map
    public static final String TAG = "MapFragment: ";
    private static final String FINE_LOCATION = ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    public GoogleMap mMap;
//    String nameLocation;
//    double bundlelocationLat;
//    double bundlelocationLng;
    //POJO liste restaurant
    List<Restaurant> restaurants;
    //vars
    private Boolean mLocationPermissionsGranted = false;
    private Location location;

    //widgets
    private ImageView mGps;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(List<Restaurant> restaurants, Location location) {
        MapFragment myFragment = new MapFragment();

        Bundle args = new Bundle();
        args.putParcelable("Restaurant", Parcels.wrap(restaurants));
        args.putParcelable("Location", Parcels.wrap(location));
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            restaurants = Parcels.unwrap(getArguments().getParcelable("Restaurant"));
        }
        if (getArguments() != null) {
            location = Parcels.unwrap(getArguments().getParcelable("Location"));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

//        String nameLocation = getArguments().getString("bundleNameLocation");
//        double bundlelocationLat = getArguments().getDouble("bundlelocationLat");
//        double bundlelocationLng = getArguments().getDouble("bundlelocationLng");
//        placeMarker(nameLocation, bundlelocationLat, bundlelocationLng);
        mGps = v.findViewById(R.id.ic_gps);
        getLocationPermission();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void showOnMap(Restaurant restaurant) {
        //MarkerOptions.CREATOR

        if (restaurant.getTypes().contains("RESTAURANT") || restaurant.getTypes().contains("FOOD")) {
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(restaurant.getLatlng())
                    .title(restaurant.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .visible(true));

            m.setTag(restaurant);

            mMap.setOnMarkerClickListener(marker -> {

                Restaurant r = (Restaurant) marker.getTag();
                EventBus.getDefault().post(new NavToDetailEvent(r));
                return true;
            });
        }
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
            moveCamera(new LatLng(location.getLatitude(), location.getLongitude()),
                    18,
                    "My Location");

            googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            Objects.requireNonNull(getContext()), R.raw.style_json));
            updateLocationUI();
            init();
            for (Restaurant r : restaurants) {
                showOnMap(r);
            }
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

        mGps.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Clicked gps icon");
            moveCamera(new LatLng(location.getLatitude(), location.getLongitude()),
                    18,
                    "My Location");
        });
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
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    //PERMS
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: Getting location permissions");
        String[] permissions = {ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted for FineLocation");
            if (ContextCompat.checkSelfPermission(getContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted for CoarseLocation and FinelLocation");
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                Log.i(TAG, "Permission granted for FineLocation but not CoarseLocation");
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            Log.i(TAG, "Permission refused for FineLocation");
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Subscribe //(threadMode = ThreadMode.MAIN)
    public void onSearchPlaceEvent(SearchPlaceEvent searchPlaceEvent){

        String nameLocation = searchPlaceEvent.getPlace().getName();
        double locationLng = Objects.requireNonNull(searchPlaceEvent.getPlace().getLatLng()).longitude;
        double locationLat = searchPlaceEvent.getPlace().getLatLng().latitude;

        // TODO: Get info about the selected place.
        Log.i(TAG, "PlaceAutoComplete: " + searchPlaceEvent.getPlace().getName() + ", " + searchPlaceEvent.getPlace().getId() + ", " + locationLat + ", " + locationLng + ", " + nameLocation);

        LatLng definedLocation = new LatLng(locationLat, locationLng);
        Marker detailMarker = mMap.addMarker(new MarkerOptions().position(definedLocation).title("Marker in " + nameLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(definedLocation));
        detailMarker.setTag(restaurants);
//        detailMarker.setTag(restaurant);

//        Log.i(TAG, "onSearchPlaceEvent" + searchPlaceEvent.getPlace().getName());
//
//        mMap.setOnMarkerClickListener(marker -> {
//            Restaurant r = (Restaurant) marker.getTag();
////            EventBus.getDefault().post(new NavToDetailEvent(r));
//            Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
//            intent.putExtra("restaurant", Parcels.wrap(r));
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            this.startActivity(intent);
//            getActivity().finish();
//            return true;
//        });
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
}