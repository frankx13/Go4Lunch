package com.lepanda.studioneopanda.go4lunch.ui;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.models.Workmate;

public class WorkmatesAdapter extends FirestoreRecyclerAdapter<Workmate, WorkmateViewHolder> {

    private static final String TAG = "FirestoreAdapter: ";

    public WorkmatesAdapter(@NonNull FirestoreRecyclerOptions<Workmate> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateViewHolder holder, int position, @NonNull Workmate model) {

        //recup nom restau avec Firestore
        Log.i("grostest", "onBindViewHolder: " + model.getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("selection").document(model.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null) {
                    if (document.exists()) {
                        Object o = document.get("restaurantName");
                        String restaurantName = null;
                        if (o != null) {
                            restaurantName = o.toString();
                        }
                        Boolean restIsSelected = true;
                        Log.i(TAG, "onCompleteFirestore: " + restaurantName);
                        //TEXT
                        holder.setWorkmateText(model.getUsername(), restaurantName, restIsSelected);
                    } else {
                        Boolean restIsSelected = false;
                        Log.d(TAG, "No such document");
                        holder.setWorkmateText(model.getUsername(), "", restIsSelected);
                    }
                }
            } else {
                Log.d(TAG, "Get failed with ", task.getException());
            }
        });

        //IMAGE
        holder.setWorkmateImage(model.getUrlPicture());


        //CLICK
        //holder.setWorkmateContainer(restaurant);

        Log.i(TAG, "FIRESTORE NAME : " + model.getUsername());

        //bind workmate to VH
        holder.bindToWorkmate(model);
    }


    @NonNull
    @Override
    public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.workmates_view_items, viewGroup, false);
        return new WorkmateViewHolder(v);
    }
}