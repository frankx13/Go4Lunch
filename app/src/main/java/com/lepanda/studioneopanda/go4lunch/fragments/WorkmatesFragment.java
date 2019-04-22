package com.lepanda.studioneopanda.go4lunch.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;
import com.lepanda.studioneopanda.go4lunch.models.Workmates;
import com.lepanda.studioneopanda.go4lunch.ui.RecyclerViewAdapterRestaurant;
import com.lepanda.studioneopanda.go4lunch.ui.RecyclerViewAdapterWorkmates;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkmatesFragment extends Fragment {

    public static final String TAG = "ListFragment: ";
    private RecyclerView recyclerView;
    private List<Restaurant> restaurants;
    private List<Workmates> workmates;

    public WorkmatesFragment() {
        // Required empty public constructor
    }

    public static WorkmatesFragment newInstance(List<Restaurant> restaurants, List<Workmates> workmates) {
        WorkmatesFragment myFragment = new WorkmatesFragment();

        Bundle args = new Bundle();
        args.putParcelable("RestaurantWorkmates", Parcels.wrap(restaurants));
        args.putParcelable("WorkmatesWorkmates", Parcels.wrap(workmates));
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurants = Parcels.unwrap(getArguments().getParcelable("RestaurantWorkmates"));
        workmates = Parcels.unwrap(getArguments().getParcelable("WorkmatesWorkmates"));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.workmates_view_items, container, false);
        recyclerView = v.findViewById(R.id.workmates_recyclerview);

        for (Restaurant r : restaurants) {
            for (Workmates w : workmates){
                onDataLoaded(r, w);
            }
        }
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void onDataLoaded(Restaurant restaurant, Workmates workmate) {
        RecyclerViewAdapterWorkmates recyclerAdapter = new RecyclerViewAdapterWorkmates(getActivity().getApplicationContext(), workmates, restaurants);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }
}
