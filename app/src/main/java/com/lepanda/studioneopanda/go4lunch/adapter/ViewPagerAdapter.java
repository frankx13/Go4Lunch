package com.lepanda.studioneopanda.go4lunch.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> listFragment = new ArrayList<>();
    private final List<String> listTitles = new ArrayList<>();

    //CONSTRUCTOR
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //Tell the ViewPager which fragment is where
    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    //Tell the ViewPager the size of the list
    @Override
    public int getCount() {
        return listTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTitles.get(position);
    }

    //create method to AddFragment, we will use it in MainActivity
    public void AddFragment(Fragment fragment, String title) {
        listFragment.add(fragment);
        listTitles.add(title);
    }
}