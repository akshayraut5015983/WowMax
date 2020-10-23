package com.swaliya.wowmax.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.swaliya.wowmax.activity.MainActivity;
import com.swaliya.wowmax.adapter.HindiAdapter;
import com.swaliya.wowmax.helper.ApiClient;
import com.swaliya.wowmax.helper.ApiInterface;
import com.swaliya.wowmax.model.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class JustAddedFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private AdView mAdView, adView;
    private SliderLayout sliderLayout;
    private HashMap<String, Integer> sliderImages;

    List<Movie> movieList;
    HindiAdapter recyclerAdapter;
    ProgressDialog loading;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_just_add, viewGroup, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);
        getResponce();
        forAdverties(v);
        forSlider(v);

        /*listSuperHeroes = new ArrayList<>();
        recyclerView = v.findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        loading = ProgressDialog.show(getContext(), "Loading Data", "Please Wait...", false, false);
        getData();
        loading.dismiss();
        adapter = new HindiAdapter(listSuperHeroes, getContext());

        Log.d("tag", String.valueOf(adapter.getItemCount()));
        recyclerView.setAdapter(adapter);*/


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
                startActivity(new Intent(getContext(), VdoDetailsActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.hold);
            }
        });*/


        return v;

    }

    private void getResponce() {

        loading = ProgressDialog.show(getContext(), "Loading Data", "Please Wait...", false, false);
        loading.dismiss();
        movieList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new HindiAdapter(movieList, getActivity());
        recyclerView.setAdapter(recyclerAdapter);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Movie>> call = apiService.getMovies();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, retrofit2.Response<List<Movie>> response) {
                loading.dismiss();
                movieList = response.body();
                Log.d("TAG", "Response = " + movieList);
                recyclerAdapter.setMovieList(movieList);
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                loading.dismiss();
                Log.d("TAG", "Response = " + t.toString());
            }
        });

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
        sliderImages.put("Nawabzade", R.drawable.nawabazadep);
        sliderImages.put("Manmarziyaa", R.drawable.manmarziyaanp);
        sliderImages.put("Fry day", R.drawable.frydayp);

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