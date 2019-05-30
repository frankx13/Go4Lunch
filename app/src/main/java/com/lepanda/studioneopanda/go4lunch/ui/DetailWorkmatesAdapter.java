package com.lepanda.studioneopanda.go4lunch.ui;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.models.Workmate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailWorkmatesAdapter extends FirestoreRecyclerAdapter<Workmate, DetailWorkmateViewHolder> {

    private static final String TAG = "FRSTAdapterDetail: ";
    private List<String> listRestName;
    private List<String> listUserName;

    public DetailWorkmatesAdapter(@NonNull FirestoreRecyclerOptions<Workmate> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailWorkmateViewHolder holder, int position, @NonNull Workmate model) {

        //get fields restname and username from every user in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("selection");
        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listRestName = new ArrayList<>();
                listUserName = new ArrayList<>();
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    listRestName.add(document.getString("restaurantName"));
                    listUserName.add(document.getString("userSenderName"));

                    holder.setWorkmateText(listUserName, listRestName, true);
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
        holder.bindToWorkmate();
    }


    @NonNull
    @Override
    public DetailWorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.workmates_view_items, viewGroup, false);

        return new DetailWorkmateViewHolder(v);
    }
}