package com.lepanda.studioneopanda.go4lunch.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.bumptech.glide.request.target.Target;
import com.lepanda.studioneopanda.go4lunch.DetailActivity;
import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;

import java.util.Arrays;
import java.util.List;

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

        Restaurant r = mDataRestaurant.get(position);
        //NAME OK
        holder.restaurantName.setText(r.getName());

        //ADDRESS OK
        holder.restaurantAddress.setText(r.getAddress());

        //IMG OK
        Glide.with(mContext).asBitmap()
                .load(r.getPhotos())
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

        //OPENINGHOURS NOT OK

//        if (workingTime != null && !workingTime.equals("null")) {
//            holder.restaurantWorkingTime.setText(workingTime);
//        } else {
//            holder.restaurantWorkingTime.setText(R.string.no_working_time_info);
//        }

        List<String> workingTime = r.getOpeningHours();
        if (workingTime != null){
            Log.i(TAG, Arrays.toString(workingTime.toArray()));
        }


        //VIEW CLICK OK
        holder.restaurantContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Should launch Detail activity here", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        //RATINGS OK
        Double rating = r.getRating();
        Log.i(TAG, "onBindViewHolder: the current RRR is : " + rating + "for " + r.getName());

        if (rating != null && rating >= 1.5 && rating < 3.5){
            holder.oneStar.setVisibility(View.VISIBLE);
        } else if (rating != null && rating >= 3.5 && rating < 4.5){
            holder.oneStar.setVisibility(View.VISIBLE);
            holder.twoStar.setVisibility(View.VISIBLE);
        } else if (rating != null && rating >= 4.5){
            holder.oneStar.setVisibility(View.VISIBLE);
            holder.twoStar.setVisibility(View.VISIBLE);
            holder.threeStar.setVisibility(View.VISIBLE);
        }

        //        DISTANCE TO PLACE NOT OK

        holder.restaurantDistanceFromUser.setText(r.getDistance() + " m");
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