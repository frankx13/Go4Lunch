package com.lepanda.studioneopanda.go4lunch.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.data.RestaurantData;

import java.util.List;

public class RecyclerViewAdapterRestaurant extends RecyclerView.Adapter<RecyclerViewAdapterRestaurant.MyViewHolder> {

    private Context mContext;
    private List<RestaurantData> mDataRestaurant;

    //CONSTRUCTOR
    public RecyclerViewAdapterRestaurant(Context mContext, List<RestaurantData> mDataRestaurant) {
        this.mContext = mContext;
        this.mDataRestaurant = mDataRestaurant;
    }

    //VIEWHOLDER
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        //We use this layout to display items inside
        v = LayoutInflater.from(mContext).inflate(R.layout.list_view_items, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.restaurantName.setText(mDataRestaurant.get(position).getRestaurantName()); //return the section name
    }

    // we return the size of the article list
    @Override
    public int getItemCount() {
        return ((mDataRestaurant == null) ? 0 : mDataRestaurant.size());
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        // declaration of UI Elements
        private TextView restaurantName;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // we assign a view to each UI Element
            restaurantName = itemView.findViewById(R.id.list_view_place_name);
        }
    }
}