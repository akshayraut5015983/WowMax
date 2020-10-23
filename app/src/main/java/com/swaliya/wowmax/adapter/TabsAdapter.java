package com.swaliya.wowmax.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.swaliya.wowmax.fragment.JustAddedFragment;
import com.swaliya.wowmax.fragment.MovieFragment;
import com.swaliya.wowmax.fragment.ShortMoviFragment;
import com.swaliya.wowmax.fragment.WebSeriesFragment;

public class TabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public TabsAdapter(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                JustAddedFragment justAddedFragment = new JustAddedFragment();
                return justAddedFragment;
            case 1:
                MovieFragment movieFragment = new MovieFragment();
                return movieFragment;
            case 2:
                WebSeriesFragment webSeriesFragment = new WebSeriesFragment();
                return webSeriesFragment;
            case 3:
                ShortMoviFragment shortMoviFragment = new ShortMoviFragment();
                return shortMoviFragment;
            default:
                return null;
        }
    }
}