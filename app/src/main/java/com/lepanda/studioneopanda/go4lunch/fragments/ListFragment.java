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
import com.lepanda.studioneopanda.go4lunch.models.UserLocation;
import com.lepanda.studioneopanda.go4lunch.ui.RecyclerViewAdapterRestaurant;

import org.parceler.Parcels;

import java.util.List;

public class ListFragment extends Fragment {

    public static final String TAG = "ListFragment: ";
    private RecyclerView recyclerView;
    private List<Restaurant> restaurants;
    private List<UserLocation> locations;

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
//        restaurants = Parcels.unwrap(getArguments().getParcelable("RestaurantList"));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = v.findViewById(R.id.list_recyclerview);

        restaurants = Parcels.unwrap(getArguments().getParcelable("RestaurantList"));

        for (Restaurant r : restaurants) {
            onDataLoaded(r);
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


    private void onDataLoaded(Restaurant restaurant) {
        RecyclerViewAdapterRestaurant recyclerAdapter = new RecyclerViewAdapterRestaurant(getActivity().getApplicationContext(), restaurants, locations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }
}
