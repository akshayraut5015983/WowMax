package com.swaliya.wowmax.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.activity.MainActivity;
import com.swaliya.wowmax.activity.MySubscriptionActivity;
import com.swaliya.wowmax.activity.VdoDetailsActivity;
import com.swaliya.wowmax.adapter.MovieListAdapter;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.model.MainMovieListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class JustAddedFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private AdView mAdView, adView;
    private SliderLayout sliderLayout;
    private HashMap<String, Integer> sliderImages;

    List<MainMovieListModel> movieList;
    MovieListAdapter recyclerAdapter;
    ProgressDialog loading;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_mainjust, viewGroup, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);

        forAdverties(v);

        //   forSlider(v);

        movieList = new ArrayList<>();
        recyclerView = v.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        loading = ProgressDialog.show(getContext(), "Loading Data", "Please Wait...", false, false);

        ConnectivityManager cn = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();

        if (nf != null && nf.isConnected() == true) {

            getResponce();

        } else {

            Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_SHORT).show();

        }


        return v;

    }


    private void getResponce() {

        String url = Config.URL + "api/apiurl.aspx?msg=GetMovieDetails";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                parseData(response);
                Log.d("TAG", "onResponse: " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getContext(), "Poor Internet Connection", Toast.LENGTH_LONG).show();
                Log.d("TAG", "onErrorResponse: " + error.getLocalizedMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);

    }

    private void parseData(JSONArray response) {

        for (int i = 0; i < response.length(); i++) {
            MainMovieListModel mAinMoviListModel = new MainMovieListModel();
            try {

                JSONObject jsonObject = response.getJSONObject(i);

                mAinMoviListModel.setMovieTitle(jsonObject.getString("MovieTitle"));
                mAinMoviListModel.setCategoryName(jsonObject.getString("CategoryName"));
                mAinMoviListModel.setMovieQuality(jsonObject.getString("MovieQuality"));
                mAinMoviListModel.setMovieImage(jsonObject.getString("MovieImage"));
                mAinMoviListModel.setMovieDesc(jsonObject.getString("MovieDesc"));
                mAinMoviListModel.setMovieRelYear(jsonObject.getString("MovieRelYear"));
                mAinMoviListModel.setMovieLanguage(jsonObject.getString("MovieLanguage"));
                mAinMoviListModel.setMovieDuration(jsonObject.getString("MovieDuration"));
                mAinMoviListModel.setMovieAddress(jsonObject.getString("MovieAddress"));
                mAinMoviListModel.setMovieImage2(jsonObject.getString("MovieImage2"));

                Log.d("list", "parseData: " + jsonObject.getString("MovieTitle"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            movieList.add(mAinMoviListModel);
        }


        loading.dismiss();
        Collections.reverse(movieList);
        recyclerAdapter = new MovieListAdapter(movieList, getContext());
        Log.d("tag", String.valueOf(recyclerAdapter.getItemCount()));
        recyclerView.setAdapter(recyclerAdapter);

    }

    private void forAdverties(View v) {

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        PublisherAdView mPublisherAdView = v.findViewById(R.id.adView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest);

        PublisherAdView mPublisherAdVieww = v.findViewById(R.id.adVieww);
        PublisherAdRequest adRequestt = new PublisherAdRequest.Builder().build();
        mPublisherAdVieww.loadAd(adRequestt);


    }

    private void forSlider(View v) {
        sliderLayout = v.findViewById(R.id.sliderLayout);
        sliderImages = new HashMap<>();
        sliderImages.put("Nawabzade", R.drawable.nawabazadep);
        sliderImages.put("Manmarziyaa", R.drawable.manmarziyaanp);


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