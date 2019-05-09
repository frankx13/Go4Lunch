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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lepanda.studioneopanda.go4lunch.DetailActivity;
import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.models.Workmate;

public class WorkmateViewHolder extends RecyclerView.ViewHolder {

    private TextView workmateText;
    private ImageView workmateImage;
    private RelativeLayout workmateContainer;
    private String workmateKey;
    private Context mContext;
    public static final String TAG = "WorkmateViewHolder: ";


    public WorkmateViewHolder(@NonNull View itemView) {
        super(itemView);

        workmateText = itemView.findViewById(R.id.workmate_text);
        workmateImage = itemView.findViewById(R.id.workmate_image);
        workmateContainer = itemView.findViewById(R.id.workmate_container);
    }

    public void setWorkmateName(String workmateName){
        workmateText.setText(workmateName);
    }

    public void setWorkmateImage(String workmateUrl){
        //Glide.with(mContext).load(workmateUrl).into(workmateImage); NullPointerException
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            Glide.with(itemView.getContext())
                    .load(firebaseUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform())
                    .into(workmateImage);
    }

    public void setWorkmateContainer(){
        workmateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity detailActivity = new DetailActivity();
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
