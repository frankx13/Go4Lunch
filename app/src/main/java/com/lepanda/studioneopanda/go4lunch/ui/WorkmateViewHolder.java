package com.lepanda.studioneopanda.go4lunch.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;
import com.lepanda.studioneopanda.go4lunch.models.Workmate;

public class WorkmateViewHolder extends RecyclerView.ViewHolder {

    private TextView workmateText;
    private ImageView workmateImage;
    private RelativeLayout workmateContainer;
    private String workmateKey;
    private Restaurant mRestaurant;
    private Context mContext;
    public static final String TAG = "WorkmateViewHolder: ";


    public WorkmateViewHolder(@NonNull View itemView) {
        super(itemView);

        workmateText = itemView.findViewById(R.id.workmate_text);
        workmateImage = itemView.findViewById(R.id.workmate_image);
        workmateContainer = itemView.findViewById(R.id.workmate_container);
    }

    public void setWorkmateText(String workmateName, String restaurantName){
        workmateText.setText(workmateName + "" + restaurantName);
    }

    public void setWorkmateImage(String workmateUrl){
        //Glide.with(mContext).load(workmateUrl).into(workmateImage); NullPointerException
            Glide.with(itemView.getContext())
                    .load(workmateUrl).apply(RequestOptions.circleCropTransform())
                    .into(workmateImage);
    }

    public void setWorkmateSelection(){

    }


    public void setWorkmateContainer(){
        workmateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EventBus.getDefault().post(new NavToDetailEvent()); how to pass a restaurant ?
            }
        });
    }

    public void bindToWorkmate(Workmate workmate){
        workmateKey = workmate.getUsername();
    }

    public String getWorkmateKey(){
        return workmateKey;
    }
}
