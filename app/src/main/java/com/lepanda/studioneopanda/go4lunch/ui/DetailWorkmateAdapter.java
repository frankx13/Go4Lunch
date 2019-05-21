package com.lepanda.studioneopanda.go4lunch.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.models.Workmate;

import java.util.List;

public class DetailWorkmateAdapter extends RecyclerView.Adapter<DetailWorkmateAdapter.MyViewHolder> {

    private static final String TAG = "RVAdapter: ";
    private Context mContext;
    private List<Workmate> mDataWorkmates;

    //CONSTRUCTOR
    public DetailWorkmateAdapter(Context mContext, List<Workmate> mDataWorkmates) {
        this.mContext = mContext;
        this.mDataWorkmates = mDataWorkmates;
    }

    //VIEWHOLDER
    @NonNull
    @Override
    public DetailWorkmateAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        //We use this layout to display items inside
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.detail_view_items, viewGroup, false);
        return new DetailWorkmateAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailWorkmateAdapter.MyViewHolder holder, int position) {
        Workmate w = mDataWorkmates.get(position);

        String username = w.getUsername();

        if (mDataWorkmates.size() > 0) {
            holder.workmateText.setText(w.getUsername() + "is joining !");
        } else {
            holder.workmateText.setText("");
        }
    }

    // we return the size of the article list
    @Override
    public int getItemCount() {
        return ((mDataWorkmates == null) ? 0 : mDataWorkmates.size());
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        // declaration of UI Elements
        private RelativeLayout detailWorkmateContainer;
        private ImageView workmateImage;
        private TextView workmateText;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // we assign a view to each UI Element
            detailWorkmateContainer = itemView.findViewById(R.id.detail_workmate_container);
            workmateImage = itemView.findViewById(R.id.detail_workmate_image);
            workmateText = itemView.findViewById(R.id.detail_workmate_text);
        }
    }
}