package com.lepanda.studioneopanda.go4lunch.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lepanda.studioneopanda.go4lunch.DetailActivity;
import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.fragments.MapFragment;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewAdapterRestaurant extends RecyclerView.Adapter<RecyclerViewAdapterRestaurant.MyViewHolder> {

    public static final String TAG = "RVAdapter: ";
    private Context mContext;
    private List<Restaurant> mDataRestaurant;

    //CONSTRUCTOR
    public RecyclerViewAdapterRestaurant(Context mContext, List<Restaurant> mDataRest) {
        this.mContext = mContext;
        this.mDataRestaurant = mDataRest;
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
        holder.restaurantPhoto.setImageBitmap(mDataRestaurant.get(position).getPhotos());
        holder.restaurantDistanceFromUser.setText(mDataRestaurant.get(position).getLatlng().toString());

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
//        if (mDataRestaurant == null) return 0;
//        else return mDataRestaurant.size();
        return mDataRestaurant.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        // declaration of UI Elements
        private RelativeLayout restaurantContainer;
        private TextView restaurantName;
        private TextView restaurantAddress;
        private TextView restaurantDistanceFromUser;
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
            oneStar = itemView.findViewById(R.id.star_rating1);
            twoStar = itemView.findViewById(R.id.star_rating2);
            threeStar = itemView.findViewById(R.id.star_rating3);
        }
    }
}