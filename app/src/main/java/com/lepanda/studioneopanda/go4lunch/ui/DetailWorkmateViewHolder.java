package com.lepanda.studioneopanda.go4lunch.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lepanda.studioneopanda.go4lunch.R;

import java.util.List;

public class DetailWorkmateViewHolder extends RecyclerView.ViewHolder {

    private TextView workmateText;
    private ImageView workmateImage;
    private RelativeLayout workmateContainer;

    DetailWorkmateViewHolder(@NonNull View itemView) {
        super(itemView);

        workmateText = itemView.findViewById(R.id.workmate_text);
        workmateImage = itemView.findViewById(R.id.workmate_image);
        workmateContainer = itemView.findViewById(R.id.workmate_container);
    }

    void setWorkmateText(List<String> workmateName, List<String> restName, Boolean restIsSelected) {
        for (int i = 0; i < workmateName.size(); i++) {
            if (restIsSelected) {
                workmateText.setText(workmateName.get(i) + " is joining!");
            } else {
                workmateContainer.setVisibility(View.GONE);
            }
        }
    }

    void setWorkmateImage(String workmateUrl) {
        //Glide.with(mContext).load(workmateUrl).into(workmateImage); NullPointerException
        Glide.with(itemView.getContext())
                .load(workmateUrl).apply(RequestOptions.circleCropTransform())
                .into(workmateImage);
    }


    void setWorkmateContainer() {
        workmateContainer.setOnClickListener(v -> {
            //EventBus.getDefault().post(new NavToDetailEvent()); how to pass a restaurant ?
        });
    }

    void bindToWorkmate() {
    }

}
