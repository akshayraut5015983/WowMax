package com.swaliya.wowmax.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.adapter.AdapterViewAll;
import com.swaliya.wowmax.adapter.SliderPagerAdapter;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.model.MainMovieListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MovieFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private AdView mAdView, adView;
    private SliderLayout sliderLayout;
    ViewPager viewPager;
    private HashMap<String, Integer> sliderImages;
    private ArrayList<MainMovieListModel> listSlider;
    LinearLayout layComedy, layactCom, layAll, layActDrama, layThrAction, layRomance, layComDrama, lyOther;

    ProgressDialog loading, getLoading;
    InterstitialAd mInterstitialAd;
    AdapterViewAll adapterViewAll;
    RecyclerView recCategory;
    List<MainMovieListModel> listCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_movie, viewGroup, false);
        forAdverties(v);
        listSlider = new ArrayList<>();
        viewPager = v.findViewById(R.id.viewPager);

        mInterstitialAd = new InterstitialAd(getContext());

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.intts_ads_unit));

        AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);
        listCategory = new ArrayList<>();
        recCategory = v.findViewById(R.id.recCate);
        recCategory.setLayoutManager(new GridLayoutManager(getContext(), 3));


        layAll = v.findViewById(R.id.layAll);
        lyOther = v.findViewById(R.id.layOther);
        layComedy = v.findViewById(R.id.layComedy);
        layactCom = v.findViewById(R.id.layActionComedy);
        layComDrama = v.findViewById(R.id.layComedyDrama);
        layActDrama = v.findViewById(R.id.layActionDrama);
        layRomance = v.findViewById(R.id.layRomance);
        layThrAction = v.findViewById(R.id.layThrAction);

        ConnectivityManager cn = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected()) {

            getList();
            getLoading = ProgressDialog.show(getContext(), "Loading Data", "Please Wait...", false, false);
        } else {
            Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_SHORT).show();
        }
        getResponce("All");
        onLayoutClick();

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });
    }

    private void getList() {
        String url = Config.URL + "api/apiurl.aspx?msg=Top10Image";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                addDataToSlider(response);
                Log.e("TAG", "onResponse: " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getLoading.dismiss();
                Toast.makeText(getContext(), "Poor Internet Connection", Toast.LENGTH_LONG).show();
                Log.e("TAG", "onErrorResponse: " + error.getLocalizedMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);

    }

    private void addDataToSlider(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            MainMovieListModel mAinMoviListModel = new MainMovieListModel();
            try {
                JSONObject jsonObject = response.getJSONObject(i);

                mAinMoviListModel.setMovieName(jsonObject.getString("MovieName"));
                mAinMoviListModel.setMovieImage2(jsonObject.getString("Image2List"));

            } catch (Exception e) {
                Log.d("TAG", "setListComedy: " + e);
            }
            listSlider.add(mAinMoviListModel);
            getLoading.dismiss();
            SliderPagerAdapter sliderPagerAdapter = new SliderPagerAdapter(getContext(), listSlider);
            viewPager.setAdapter(sliderPagerAdapter);
            Timer timer = new Timer();
            timer.schedule(new MovieFragment.RemindTask(listSlider.size(), viewPager, getContext()), 2500, 2500);

//            videoItems.add(mAinMoviListModel);
//            videosViewPager.setAdapter(new VideosAdapter(videoItems, this));

        }
    }

    class RemindTask extends TimerTask {
        int no_of_pages;
        ViewPager viewPager;
        int page = 0;
        Context context;

        public RemindTask(int no_of_pages, ViewPager viewPager, Context context) {
            this.no_of_pages = no_of_pages;
            this.viewPager = viewPager;
            this.context = context;
        }

        @Override
        public void run() {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (page > no_of_pages) {
                        viewPager.setCurrentItem(0);
                        page = 0;

                    } else {
                        viewPager.setCurrentItem(page++);
                    }
                }
            });
        }
    }

    private void onLayoutClick() {
        ConnectivityManager cn = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();

        layAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nf != null && nf.isConnected()) {

                    listCategory.clear();
                    getResponce("All");
                }
            }
        });
        layactCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nf != null && nf.isConnected()) {
                    listCategory.clear();
                    getResponce("Action and Comedy");
                }
            }
        });
        layActDrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nf != null && nf.isConnected()) {
                    listCategory.clear();
                    getResponce("Action and Drama");
                }
            }
        });
        layComedy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nf != null && nf.isConnected()) {
                    listCategory.clear();
                    getResponce("Comedy");
                }
            }
        });
        layComDrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nf != null && nf.isConnected()) {
                    listCategory.clear();
                    getResponce("Comedy and Drama");
                }
            }
        });
        layRomance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nf != null && nf.isConnected()) {
                    listCategory.clear();
                    getResponce("Romance");
                }
            }
        });
        layThrAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nf != null && nf.isConnected()) {
                    listCategory.clear();
                    getResponce("Thriller and action");
                }
            }
        });
        lyOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nf != null && nf.isConnected()) {
                    listCategory.clear();
                    getResponce("Movie");
                }
            }
        });
    }


    private void getResponce(String category) {

        String url = Config.URL + "api/apiurl.aspx?msg=GetMovieDetails";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                parseData(response, category);
                Log.d("TAG", "onResponse: " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // loading.dismiss();
                Toast.makeText(getContext(), "Poor Internet Connection", Toast.LENGTH_LONG).show();
                Log.e("TAG", "onErrorResponse: " + error.getLocalizedMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);

    }

    private void parseData(JSONArray response, String category) {

        for (int i = 0; i < response.length(); i++) {
            MainMovieListModel mAinMoviListModel = new MainMovieListModel();

            try {
                JSONObject jsonObject = response.getJSONObject(i);
                if (category.equals("All")) {
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

                    listCategory.add(mAinMoviListModel);
                } else if (jsonObject.getString("CategoryName").equals(category)) {

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

                    listCategory.add(mAinMoviListModel);

                }

                Log.d("list", "parseData: " + jsonObject.getString("MovieTitle"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Collections.reverse(listCategory);
        adapterViewAll = new AdapterViewAll(listCategory, getContext());
        recCategory.setAdapter(adapterViewAll);

    }

    private void forAdverties(View v) {

        try {

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
        } catch (Exception e) {
            Log.d("TAAG", "forAdverties: " + e.getMessage(), e.getCause());
        }
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