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

public class DetailWorkmatesAdapter extends FirestoreRecyclerAdapter<Workmate, DetailWorkmateViewHolder> {

    private static final String TAG = "FRSTAdapterDetail: ";

    public DetailWorkmatesAdapter(@NonNull FirestoreRecyclerOptions<Workmate> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailWorkmateViewHolder holder, int position, @NonNull Workmate model) {

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
                        if (restaurantName != null && restaurantName.equals(model.getRestSelection()))
                            Log.i(TAG, "onCompleteFirestore: " + restaurantName);
                        //TEXT
                        holder.setWorkmateText(model.getUsername(), true);
                    } else {
                        Log.d(TAG, "No such document");
                        holder.setWorkmateText(model.getUsername(), false);
                    }
                }
            } else {
                Log.d(TAG, "Get failed with ", task.getException());
            }
        });

        //IMAGE
        holder.setWorkmateImage(model.getUrlPicture());


        //CLICK
        holder.setWorkmateContainer();

        Log.i(TAG, "FIRESTORE NAME : " + model.getUsername());

        //bind workmate to VH
        holder.bindToWorkmate(model);
    }


    @NonNull
    @Override
    public DetailWorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.workmates_view_items, viewGroup, false);


        return new DetailWorkmateViewHolder(v);
    }
}