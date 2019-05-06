package com.lepanda.studioneopanda.go4lunch.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.models.Workmate;

public class WorkmatesAdapter extends FirestoreRecyclerAdapter<Workmate, WorkmateViewHolder> {

    public static final String TAG = "WorkmatesAdapter: ";
    private Context mContext;

    public WorkmatesAdapter(@NonNull FirestoreRecyclerOptions<Workmate> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateViewHolder holder, int position, Workmate model) {

        //TEXT
        holder.setWorkmateName(model.getUsername());

        //IMAGE
//        Glide.with(mContext).asBitmap()
//                .load(model.getUrlPicture())
//                .listener(new RequestListener<Bitmap>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                        // resource is your loaded Bitmap
//                        holder.restaurantPhoto.setImageBitmap(resource);
//                        return true;
//                    }
//                }).submit();

        holder.setWorkmateImage(model.getUrlPicture());


        Log.i(TAG, "FIRESTORE NAME : " + model.getUsername());

        //bind workmate to VH
        holder.bindToWorkmate(model);
    }


    @NonNull
    @Override
    public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.workmates_view_items, viewGroup,false);


        return new WorkmateViewHolder(v);
    }
}