package com.swaliya.wowmax.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.adapter.ShortMoviAdapter;
import com.swaliya.wowmax.model.MyListData;

public class ShortMoviFragment extends Fragment {
    InterstitialAd mInterstitialAd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_short_movi, viewGroup, false);
        mInterstitialAd = new InterstitialAd(getContext());

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.intts_ads_unit));

        AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);
        MyListData[] myListData = new MyListData[]{
                new MyListData("De De pyar De ", R.drawable.dedepyardep),
                new MyListData("Ishq Junoon", R.drawable.ishqjunoonp),
                new MyListData("Bahubali", R.drawable.babubalip),
                new MyListData("Fukrey", R.drawable.fukreyp),
        };

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        ShortMoviAdapter adapter = new ShortMoviAdapter(myListData, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });
        return v;
    }

}