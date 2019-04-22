package com.lepanda.studioneopanda.go4lunch.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.lepanda.studioneopanda.go4lunch.DetailActivity;
import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.fragments.MapFragment;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;
import com.lepanda.studioneopanda.go4lunch.models.UserLocation;

import org.w3c.dom.Text;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.provider.SettingsSlicesContract.KEY_LOCATION;

public class RecyclerViewAdapterRestaurant extends RecyclerView.Adapter<RecyclerViewAdapterRestaurant.MyViewHolder> {

    public static final String TAG = "RVAdapter: ";
    private Context mContext;
    private List<Restaurant> mDataRestaurant;
    private List<UserLocation> mDataUserLocation;

    //CONSTRUCTOR
    public RecyclerViewAdapterRestaurant(Context mContext, List<Restaurant> mDataRest, List<UserLocation> mDataLoca) {
        this.mContext = mContext;
        this.mDataRestaurant = mDataRest;
        this.mDataUserLocation = mDataLoca;
    }

    //VIEWHOLDER
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        //We use this layout to display items inside
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_view_items, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.restaurantName.setText(mDataRestaurant.get(position).getName());
        holder.restaurantAddress.setText(mDataRestaurant.get(position).getAddress());

        Glide.with(mContext).asBitmap()
                .load(mDataRestaurant.get(position).getPhotos())
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        // resource is your loaded Bitmap
                        holder.restaurantPhoto.setImageBitmap(resource);
                        return true;
                    }
                }).submit();


        // 1 holder.restaurantDistanceFromUser.setText(mDataRestaurant.get(position).getLatlng().toString());
        //3
        double lat = mDataUserLocation.get(position).getUserLocationLat();
        double lon = mDataUserLocation.get(position).getUserLocationLon();

        Location user_location = new Location("locationA");
        user_location.setLatitude(lat);
        user_location.setLongitude(lon);

        Location place_locations = new Location("locationB");
        place_locations.setLatitude(mDataRestaurant.get(position).getLatlng().latitude);
        place_locations.setLongitude(mDataRestaurant.get(position).getLatlng().longitude);

        double distance = user_location.distanceTo(place_locations);

        Log.i(TAG, "onComplete: " + distance);
        holder.restaurantDistanceFromUser.setText(distance + "m");

//    2    String lat = settings.getString("UserLat", null);
//        String lon = settings.getString("UserLon", null);
//        Location user_location = null;
//
//        if (lat != null && lon != null) {
//
//            user_location = new Location("locationA");
//            user_location.setLatitude(Double.parseDouble(lat));
//            user_location.setLongitude(Double.parseDouble(lon));
//
//            Location place_locations = new Location("locationB");
//            place_locations.setLatitude(mDataRestaurant.get(position).getLatlng().latitude);
//            place_locations.setLongitude(mDataRestaurant.get(position).getLatlng().longitude);
//            double distance = user_location.distanceTo(place_locations);
//            Log.i(TAG, "onComplete: " + distance);
//
//            holder.restaurantDistanceFromUser.setText(distance + "m");
//        }


        String workingTime = mDataRestaurant.get(position).getOpeningHours();
        if (workingTime != null && !workingTime.equals("null")) {
            holder.restaurantWorkingTime.setText(workingTime);
        } else {
            holder.restaurantWorkingTime.setText(R.string.no_working_time_info);
        }
        Log.i(TAG, workingTime);

        //DISTANCE
//        Double currentLatitude = Double.parseDouble(mContext.getSharedPreferences("LocationPreferences", MODE_PRIVATE).getString("Latitude", "0.0"));
//        Double currentLongitude = Double.parseDouble(mContext.getSharedPreferences("LocationPreferences", MODE_PRIVATE).getString("Longitude", "0.0"));
//        Log.i(TAG, "onBindViewHolder: " + currentLatitude + currentLongitude);

        holder.restaurantContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Should launch Detail activity here", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

//        RATINGS
//        if (mDataRestaurant.get(position).getRating() > 2.0) {
//            holder.oneStar.setVisibility(View.VISIBLE);
//        } else if (mDataRestaurant.get(position).getRating() > 3.0) {
//            holder.oneStar.setVisibility(View.VISIBLE);
//            holder.twoStar.setVisibility(View.VISIBLE);
//        } else if (mDataRestaurant.get(position).getRating() > 4.0) {
//            holder.oneStar.setVisibility(View.VISIBLE);
//            holder.twoStar.setVisibility(View.VISIBLE);
//            holder.threeStar.setVisibility(View.VISIBLE);
//        } else mDataRestaurant.get(position).getRating() != null;
    }

    // we return the size of the article list
    @Override
    public int getItemCount() {
        return mDataRestaurant.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        // declaration of UI Elements
        private RelativeLayout restaurantContainer;
        private TextView restaurantName;
        private TextView restaurantAddress;
        private TextView restaurantDistanceFromUser;
        private TextView restaurantWorkingTime;
        private ImageView restaurantPhoto;
        private ImageView oneStar;
        private ImageView twoStar;
        private ImageView threeStar;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // we assign a view to each UI Element
            restaurantContainer = itemView.findViewById(R.id.list_view_items_container_parent);
            restaurantName = itemView.findViewById(R.id.list_view_place_name);
            restaurantAddress = itemView.findViewById(R.id.list_view_place_address);
            restaurantPhoto = itemView.findViewById(R.id.list_view_place_image);
            restaurantDistanceFromUser = itemView.findViewById(R.id.list_view_place_distance);
            restaurantWorkingTime = itemView.findViewById(R.id.list_view_place_schedule);
            oneStar = itemView.findViewById(R.id.star_rating1);
            twoStar = itemView.findViewById(R.id.star_rating2);
            threeStar = itemView.findViewById(R.id.star_rating3);
        }
    }
}