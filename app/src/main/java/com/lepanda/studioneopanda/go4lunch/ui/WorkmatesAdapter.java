package com.lepanda.studioneopanda.go4lunch.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.models.Workmate;

public class WorkmatesAdapter extends FirestoreRecyclerAdapter<Workmate, WorkmateViewHolder> {

    public static final String TAG = "FirestoreAdapter: ";
    private Context mContext;

    public WorkmatesAdapter(@NonNull FirestoreRecyclerOptions<Workmate> options) {
        super(options);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateViewHolder holder, int position, Workmate model) {

        //recup nom restau avec Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Selection").document(model.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Object o = document.get("restaurantName");
                        String restaurantName = o.toString();

                        Log.i(TAG, "onCompleteFirestore: " + restaurantName);
                        //TEXT
                        holder.setWorkmateText(model.getUsername(), restaurantName);
                    }
                    else {
                        Log.d(TAG, "No such document");
                        holder.setWorkmateText(model.getUsername(), "");
                    }
                } else {
                    Log.d(TAG, "Get failed with ", task.getException());
                    //holder.setWorkmateName(model.getUsername(), "");
                }
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
    public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.workmates_view_items, viewGroup,false);


        return new WorkmateViewHolder(v);
    }
}