package com.lepanda.studioneopanda.go4lunch.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.events.NavToDetailEvent;
import com.lepanda.studioneopanda.go4lunch.events.RefreshRVEvent;
import com.lepanda.studioneopanda.go4lunch.events.SearchPlaceEvent;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    //VARS
    public GoogleMap mMap;

    private static final String TAG = "MapFragment: ";
    private static final String FINE_LOCATION = ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private List<Restaurant> restaurants;
    private Boolean mLocationPermissionsGranted = false;
    private Location location;
    private ImageView mGps;
    private List<String> list;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(List<Restaurant> restaurants, Location location) {
        MapFragment myFragment = new MapFragment();

        //get the Restaurant and Location objects from activity
        Bundle args = new Bundle();
        args.putParcelable("Restaurant", Parcels.wrap(restaurants));
        args.putParcelable("Location", Parcels.wrap(location));
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //open objects received in the NewInstance constructor
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
        //inflating with map view
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        mGps = v.findViewById(R.id.ic_gps);
        getLocationPermission();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //create markers on map, and handling onclick event on it
    public void showOnMap(Restaurant restaurant) {
        if (restaurant.getTypes().contains("RESTAURANT") || restaurant.getTypes().contains("FOOD")) {
            //MarkerOptions.CREATOR
            float iconColor;
            if (list.contains(restaurant.getPlaceId())) {
                iconColor = BitmapDescriptorFactory.HUE_GREEN;
            } else {
                iconColor = BitmapDescriptorFactory.HUE_ORANGE;
            }

            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(restaurant.getLatlng())
                    .title(restaurant.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(iconColor))
                    .visible(true));
            m.setTag(restaurant);


            mMap.setOnMarkerClickListener(marker -> {

                Restaurant r = (Restaurant) marker.getTag();
                EventBus.getDefault().post(new NavToDetailEvent(r));
                return true;
            });
        }

    }

    //Preparing the map and pass it to ShowOnMap
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //recup nom restau avec Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("selection");
        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                list = new ArrayList<>();
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    list.add(document.getString("restaurantID"));
                }

                Toast.makeText(getContext(), "Map is ready", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onMapReady: Map is ready");
                mMap = googleMap;

                if (mLocationPermissionsGranted) {
                    moveCamera(new LatLng(location.getLatitude(), location.getLongitude())
                    );

                    googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    Objects.requireNonNull(getContext()), R.raw.style_json));
                    updateLocationUI();
                    init();
                    for (Restaurant r : restaurants) {
                        showOnMap(r);
                    }
                }

            } else {
                Log.d(TAG, "Get failed with ", task.getException());
            }
        });
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            //to use the button "get back to my position"
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

    // INIT MAPS
    private void init() {
        Log.d(TAG, "init: Initializing");

        mGps.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Clicked gps icon");
            moveCamera(new LatLng(location.getLatitude(), location.getLongitude())
            );
        });
    }

    // CAMERA
    private void moveCamera(LatLng latLng) {
        Log.d(TAG, "moveCamera: Moving the camera to: to lat:" + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 18));

        if (!"My Location".equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title("My Location");
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

    //get the data from the searched position and apply it to the map
    @Subscribe //(threadMode = ThreadMode.MAIN)
    public void onSearchPlaceEvent(SearchPlaceEvent searchPlaceEvent) {

        String nameLocation = searchPlaceEvent.getPlace().getName();
        Place placeLocation = searchPlaceEvent.getPlace();
        double locationLng = Objects.requireNonNull(searchPlaceEvent.getPlace().getLatLng()).longitude;
        double locationLat = searchPlaceEvent.getPlace().getLatLng().latitude;

        // TODO: Get info about the selected place.
        Log.i(TAG, "PlaceAutoComplete: " + searchPlaceEvent.getPlace().getName() + ", " + searchPlaceEvent.getPlace().getId() + ", " + locationLat + ", " + locationLng + ", " + nameLocation);

        LatLng definedLocation = new LatLng(locationLat, locationLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(definedLocation));

        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.PHONE_NUMBER,
                Place.Field.OPENING_HOURS,
                Place.Field.PHOTO_METADATAS,
                Place.Field.WEBSITE_URI);

        Restaurant r = new Restaurant();

        FetchPlaceRequest request = FetchPlaceRequest.builder(Objects.requireNonNull(placeLocation.getId()), placeFields).build();

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            final PlacesClient placesClient = Places.createClient(getActivity());
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
                        r.setPhotos(bitmap);
                    });
                }

                r.setPlaceId((placeLocation.getId()));
                r.setName((placeLocation.getName()));
                r.setTypes(String.valueOf(placeLocation.getTypes()));
                r.setRating(placeLocation.getRating());
                r.setAddress(placeLocation.getAddress());
                r.setLatlng(placeLocation.getLatLng());
                r.setPhoneNumber(place.getPhoneNumber());
                r.setWebsiteURI(String.valueOf(place.getWebsiteUri()));

                if (place.getOpeningHours() != null) {
                    r.setOpeningHours(place.getOpeningHours().getWeekdayText());
                    Log.i(TAG, "onSuccess: " + place.getOpeningHours().getWeekdayText());
                }


                Marker detailMarker = mMap.addMarker(new MarkerOptions().position(definedLocation).title("Marker in " + nameLocation));
                detailMarker.setTag(r);

                EventBus.getDefault().post(new RefreshRVEvent(r));
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //register to eventbus
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //unregister to eventbus
        EventBus.getDefault().unregister(this);
    }
}