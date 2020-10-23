package com.swaliya.wowmax.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.swaliya.wowmax.R;

import java.util.HashMap;

public class WebSeriesFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private AdView mAdView, adView;
    private SliderLayout sliderLayout;
    private HashMap<String, Integer> sliderImages;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_web_series, viewGroup, false);

        forAdverties(v);
       forSlider(v);

       /* v.findViewById(R.id.layCircle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Coming Soon...!", Toast.LENGTH_SHORT).show();
            }
        });
        v.findViewById(R.id.main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             *//*   startActivity(new Intent(getContext(), ViewAllVdoActivity.class));
                getActivity().overridePendingTransition(R.anim.enter, R.anim.hold);*//*
            }
        });

        v.findViewById(R.id.ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              *//*  startActivity(new Intent(getContext(), VdoDetailsActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.hold);*//*
            }
        });*/


        return v;

    }

    private void forAdverties(View v) {


        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        mAdView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        adView = v.findViewById(R.id.adVieww);
        AdRequest adRequestt = new AdRequest.Builder().build();
        adView.loadAd(adRequestt);
    }

    private void forSlider(View v) {
        sliderLayout = v.findViewById(R.id.sliderLayout);
        sliderImages = new HashMap<>();
        sliderImages.put("De de pyar de", R.drawable.dedepyardep);
        sliderImages.put("Golmaal again", R.drawable.golmalp);
        sliderImages.put("Gully Boy", R.drawable.gullyboyp);

        for (String name : sliderImages.keySet()) {

            TextSliderView textSliderView = new TextSliderView(getContext());
            textSliderView
                    .description(name)
                    .image(sliderImages.get(name))
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);
            sliderLayout.addSlider(textSliderView);
        }

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        // sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        //  sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(2000);
        sliderLayout.addOnPageChangeListener(this);
        sliderLayout.getPagerIndicator().setVisibility(View.GONE);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //  Toast.makeText(getContext(), slider.getBundle().get("extra") +  " ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}