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
import com.lepanda.studioneopanda.go4lunch.ui.RecyclerViewAdapterRestaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListFragment extends Fragment {

    public static final String TAG = "ListFragment: ";
    private RecyclerView recyclerView;
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
        onDataLoaded();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void onDataLoaded() {
        //-----------------
        //DATA TO PASS RV FOR LISTVIEW
        //We need to implement the array in here
        //-----------------
        mDataRestaurant = new ArrayList<>();
        Restaurant restaurant = new Restaurant();
        String[] restName = {"Pizza", "Kebab", "Fromage", "Cheeseburger"};
        String[] restAddress = {"10, avenue Feta", "1, Rue des Chiches", "3, boulevard Craime", "51, traverse de Wendy"};
        restaurant.name = Arrays.toString(restName);
        restaurant.address = Arrays.toString(restAddress);
        mDataRestaurant.add(restaurant);
        Log.i(TAG, "NameResto is: " + String.valueOf(restName));
        Log.i(TAG, "NameAddress is: " + String.valueOf(restAddress));

        RecyclerViewAdapterRestaurant recyclerAdapter = new RecyclerViewAdapterRestaurant(getActivity().getApplicationContext(), mDataRestaurant);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }
}
