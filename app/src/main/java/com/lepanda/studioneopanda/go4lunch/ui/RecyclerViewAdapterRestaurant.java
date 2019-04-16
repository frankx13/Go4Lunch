package com.lepanda.studioneopanda.go4lunch.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lepanda.studioneopanda.go4lunch.DetailActivity;
import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.fragments.MapFragment;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;

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
        holder.restaurantName.setText(mDataRestaurant.get(position).getName()); //return the section name
        holder.restaurantAddress.setText(mDataRestaurant.get(position).getAddress()); //return the section name
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
        private TextView restaurantName;
        private TextView restaurantAddress;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // we assign a view to each UI Element
            restaurantName = itemView.findViewById(R.id.list_view_place_name);
            restaurantAddress = itemView.findViewById(R.id.list_view_place_address);
        }
    }
}