package com.lepanda.studioneopanda.go4lunch.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.models.Workmate;

import java.util.List;

public class WorkmatesAdapter extends FirestoreRecyclerAdapter<Workmate, WorkmatesAdapter.WorkmatesHolder> {

    private Context mContext;
    private List<Workmate> mDataWorkmate;
    public static final String TAG = "WorkmatesAdapter: ";

    public WorkmatesAdapter(@NonNull FirestoreRecyclerOptions<Workmate> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull WorkmatesHolder holder, int position, @NonNull Workmate model) {

        //String name = model.getUsername();
        String name = mDataWorkmate.get(position).getUsername();

        if (name == null) {
            holder.workmateText.setText(R.string.no_workmates_message);
        } else {
            holder.workmateText.setText(name);
        }

        Log.i(TAG, "onBindViewHolder: " + holder.workmateText.getText());

//          IMAGE
//
//        Glide.with(mContext).asBitmap()
//                .load(mDataRestaurant.get(position).getPhotos())
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

    }

    @NonNull
    @Override
    public WorkmatesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.workmates_view_items, viewGroup, false);

        return new WorkmatesHolder(v);
    }

    class WorkmatesHolder extends RecyclerView.ViewHolder {
        TextView workmateText;
        ImageView workmateImage;

        public WorkmatesHolder(@NonNull View itemView) {
            super(itemView);
            workmateText = itemView.findViewById(R.id.workmate_text);
            workmateImage = itemView.findViewById(R.id.workmate_image);
        }
    }
}
