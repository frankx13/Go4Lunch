package com.lepanda.studioneopanda.go4lunch.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.events.NavToDetailEvent;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;
import com.lepanda.studioneopanda.go4lunch.models.Workmate;

import org.greenrobot.eventbus.EventBus;

public class WorkmateViewHolder extends RecyclerView.ViewHolder {

    private TextView workmateText;
    private ImageView workmateImage;
    private RelativeLayout workmateContainer;
    private String workmateKey;

    public WorkmateViewHolder(@NonNull View itemView) {
        super(itemView);

        workmateText = itemView.findViewById(R.id.workmate_text);
        workmateImage = itemView.findViewById(R.id.workmate_image);
        workmateContainer = itemView.findViewById(R.id.workmate_container);
    }

    public void setWorkmateText(String workmateName, String restaurantName, Boolean restIsSelected) {
        if (restIsSelected){
            workmateText.setText(workmateName + " mange chez " + restaurantName);
        } else {
            workmateText.setText(workmateName + " n'a pas encore décidé");
            workmateText.setTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
        }

    }

    public void setWorkmateImage(String workmateUrl) {
        //Glide.with(mContext).load(workmateUrl).into(workmateImage); NullPointerException
        Glide.with(itemView.getContext())
                .load(workmateUrl).apply(RequestOptions.circleCropTransform())
                .into(workmateImage);
    }

    public void setWorkmateSelection() {

    }


    public void setWorkmateContainer(Restaurant restaurant) {
        workmateContainer.setOnClickListener(v -> {
            EventBus.getDefault().post(new NavToDetailEvent(restaurant));
        });
    }

    public void bindToWorkmate(Workmate workmate) {
        workmateKey = workmate.getUsername();
    }

    public String getWorkmateKey() {
        return workmateKey;
    }
}
