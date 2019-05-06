package com.lepanda.studioneopanda.go4lunch.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.models.Workmate;
import com.lepanda.studioneopanda.go4lunch.ui.WorkmateViewHolder;
import com.lepanda.studioneopanda.go4lunch.ui.WorkmatesAdapter;

public class WorkmatesFragment extends Fragment {

    private FirebaseFirestore mDatabaseRef;
    private FirestoreRecyclerAdapter<Workmate, WorkmateViewHolder> mAdapter;
    private Query mWorkmateQuery;
    private RecyclerView mRecycler;


    public WorkmatesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_workmates, container, false);

        mRecycler = v.findViewById(R.id.workmates_recyclerview);
        mRecycler.setHasFixedSize(true);

        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mManager);

        mDatabaseRef = FirebaseFirestore.getInstance();
//        mWorkmateQuery = getQuery(mDatabaseRef);
        mWorkmateQuery = FirebaseFirestore.getInstance()
                .collection("workmates");
                //.orderBy("timestamp")

        FirestoreRecyclerOptions<Workmate> recyclerOptions = new FirestoreRecyclerOptions.Builder<Workmate>()
                .setQuery(mWorkmateQuery, Workmate.class)
                .build();

        mAdapter = new WorkmatesAdapter(recyclerOptions);

        mRecycler.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
