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
import com.lepanda.studioneopanda.go4lunch.events.RefreshRVEvent;
import com.lepanda.studioneopanda.go4lunch.models.Restaurant;
import com.lepanda.studioneopanda.go4lunch.ui.RecyclerViewAdapterRestaurant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.List;
import java.util.Objects;

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Restaurant> restaurants;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance(List<Restaurant> restaurants) {
        ListFragment myFragment = new ListFragment();

        Bundle args = new Bundle();
        args.putParcelable("RestaurantList", Parcels.wrap(restaurants));
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = v.findViewById(R.id.list_recyclerview);

        if (getArguments() != null) {
            restaurants = Parcels.unwrap(getArguments().getParcelable("RestaurantList"));
        }

        for (Restaurant r : restaurants) {
            if (r.getTypes().contains("RESTAURANT") || r.getTypes().contains("FOOD")) {
                Log.i("VERIF", "onCreateView: " + r.getTypes());
                onDataLoaded(restaurants);
            }
        }


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void onDataLoaded(List<Restaurant> restaurantList) {
        RecyclerViewAdapterRestaurant recyclerAdapter = new RecyclerViewAdapterRestaurant(Objects.requireNonNull(getActivity()).getApplicationContext(), restaurantList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void receiveInfoFromSearch(RefreshRVEvent refreshRVEvent) {
        restaurants.clear();
        Restaurant restaurant = refreshRVEvent.getRestaurant();
        restaurants.add(restaurant);
        onDataLoaded(restaurants);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
