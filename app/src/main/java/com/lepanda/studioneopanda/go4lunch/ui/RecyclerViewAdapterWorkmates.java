package com.lepanda.studioneopanda.go4lunch.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;
import com.lepanda.studioneopanda.go4lunch.models.Workmates;

import java.util.List;

public class RecyclerViewAdapterWorkmates extends RecyclerView.Adapter<RecyclerViewAdapterWorkmates.MyViewHolder> {

    public static final String TAG = "RVAdapter: ";
    private Context mContext;
    private List<Workmates> mDataWorkmates;
    List<Restaurant> mDataRestaurants;

    //CONSTRUCTOR
    public RecyclerViewAdapterWorkmates(Context mContext, List<Workmates> mDataWorkmates, List<Restaurant> mDataRestaurants) {
        this.mContext = mContext;
        this.mDataWorkmates = mDataWorkmates;
        this.mDataRestaurants = mDataRestaurants;
    }

    //VIEWHOLDER
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        //We use this layout to display items inside
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.workmates_view_items, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.workmateChoiceRestaurant.setText("Android is eating at: " + mDataRestaurants.get(position).getName());
    }

    // we return the size of the article list
    @Override
    public int getItemCount() {
        return mDataWorkmates.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        // declaration of UI Elements
        TextView workmateChoiceRestaurant;


        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // we assign a view to each UI Element
            workmateChoiceRestaurant = itemView.findViewById(R.id.workmate_text);
        }
    }
}