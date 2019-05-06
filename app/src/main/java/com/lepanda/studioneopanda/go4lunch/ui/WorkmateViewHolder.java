package com.lepanda.studioneopanda.go4lunch.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.models.Workmate;

public class WorkmateViewHolder extends RecyclerView.ViewHolder {

    private TextView workmateText;
    private ImageView workmateImage;
    private String workmateKey;
    private Context mContext;


    public WorkmateViewHolder(@NonNull View itemView) {
        super(itemView);

        workmateText = itemView.findViewById(R.id.workmate_text);
        workmateImage = itemView.findViewById(R.id.workmate_image);
    }

    public void setWorkmateName(String workmateName){
        workmateText.setText(workmateName);
    }

    public void setWorkmateImage(String workmateUrl){
        //Glide.with(mContext).load(workmateUrl).into(workmateImage); NullPointerException
    }

    public void bindToWorkmate(Workmate workmate){
        workmateKey = workmate.getUsername();
    }

    public String getWorkmateKey(){
        return workmateKey;
    }
}
