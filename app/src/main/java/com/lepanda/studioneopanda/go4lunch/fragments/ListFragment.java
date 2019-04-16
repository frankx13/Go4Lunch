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

import com.lepanda.studioneopanda.go4lunch.R;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;
import com.lepanda.studioneopanda.go4lunch.ui.RecyclerViewAdapterRestaurant;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    public static final String TAG = "ListFragment: ";
    private RecyclerView recyclerView;
    private RecyclerViewAdapterRestaurant recyclerAdapter;
    private List<Restaurant> mDataRestaurant;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = v.findViewById(R.id.list_recyclerview);
        mDataRestaurant = new ArrayList<>();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    private void loadData() {

        //get restaurant informations to pass to cv : address name photo openingHours etc
        for (int i = 0; i < mDataRestaurant.size(); i++) {

            Restaurant restaurant = new Restaurant();
            restaurant.name = "Kebab";
            restaurant.address = "Rue Maison";

            mDataRestaurant.add(restaurant);
        }


        onDataLoaded(mDataRestaurant);
    }

    private void onDataLoaded(List<Restaurant> restos) {
        recyclerAdapter = new RecyclerViewAdapterRestaurant(getActivity().getApplicationContext(), restos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(recyclerAdapter);
        //recyclerAdapter.notifyDataSetChanged();
    }
}
