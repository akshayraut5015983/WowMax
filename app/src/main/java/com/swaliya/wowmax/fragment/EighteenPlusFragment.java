package com.swaliya.wowmax.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.Transliterator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.adapter.AdapterEighteenList;
import com.swaliya.wowmax.adapter.AdapterMovieList;
import com.swaliya.wowmax.adapter.AdapterSubCategory;
import com.swaliya.wowmax.adapter.AdapterViewAll;
import com.swaliya.wowmax.adapter.Slider10Adapter;
import com.swaliya.wowmax.adapter.SongsAdapter;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.model.MainMovieListModel;
import com.swaliya.wowmax.model.Slider10Model;
import com.swaliya.wowmax.model.SubCategryModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class EighteenPlusFragment extends Fragment {
    ViewPager viewPager;
    private ArrayList<Slider10Model> listSlider;
    private ArrayList<Slider10Model> listMovie;
    private ProgressDialog getLoading;
    private RecyclerView recCategory;
    RecyclerView recMovie;
    private ArrayList<SubCategryModel> subCategryModelArrayList;
    private RecyclerView.Adapter adapterSubCAt;
    AdapterEighteenList adpListMovi;

    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressdialog);
        // dialog.setMessage(Message);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_18_plus, viewGroup, false);
        listSlider = new ArrayList<>();
        listMovie = new ArrayList<>();
        subCategryModelArrayList = new ArrayList<>();
        viewPager = v.findViewById(R.id.viewPager);
        recCategory = v.findViewById(R.id.recCate);
        recMovie = v.findViewById(R.id.recMovie);
        recCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recMovie.setLayoutManager(new GridLayoutManager(getContext(), 3));

        forAdverties(v);
        ConnectivityManager cn = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected()) {
            getList();

            if (getLoading == null) {
                getLoading = createProgressDialog(getContext());
            }
            getLoading.show();
        } else {
            Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_SHORT).show();
        }
       /* adapterSubCAt = new AdapterSubCategory(subCategryModelArrayList, getContext());
        recCategory.setAdapter(adapterSubCAt);*/
        getData();

        getMovieList("action");
        return v;

    }

    private void getData() {
        String url = Config.URL + "api/apiurl.aspx?msg=SubCategoryName";
        Log.e("TAG", "getData: " + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                parseData(response);
                Log.d("TAG", "onnse: " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Toast.makeText(getContext(), "Poor Internet Connection", Toast.LENGTH_LONG).show();
                Log.d("TAG", "onErrorResponse: " + error.getLocalizedMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);

    }

    private void parseData(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            SubCategryModel mAinMoviListModel = new SubCategryModel();

            try {
                JSONObject jsonObject = response.getJSONObject(i);
                mAinMoviListModel.setSubCategoryName(jsonObject.getString("SubCategoryName"));

            } catch (Exception e) {
                Log.d("TAG", "setListComedy: " + e);
            }
            subCategryModelArrayList.add(mAinMoviListModel);

            AdapterSubCategory adapterSubCategory = new AdapterSubCategory(subCategryModelArrayList, getContext(), new AdapterSubCategory.OnItemClicked() {
                @Override
                public void onItemClick(String position) {

                    getName(position);
                    //  Toast.makeText(getContext(), position, Toast.LENGTH_SHORT).show();
                }
            });
            recCategory.setAdapter(adapterSubCategory);
        }

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    public void getName(String name) {
        listMovie.clear();
        if (name.equals("")) {
            Log.e("TAG", "getName: ");
        } else {
            Log.e("TAG", "getName: " + name);
            // Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
            getLoading.show();
            getMovieList(name);
        }

    }


    private void forAdverties(View v) {

        try {

            MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {

                }
            });
            /*PublisherAdView mPublisherAdView = v.findViewById(R.id.adView);
            PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
            mPublisherAdView.loadAd(adRequest);*/

            PublisherAdView mPublisherAdVieww = v.findViewById(R.id.adVieww);
            PublisherAdRequest adRequestt = new PublisherAdRequest.Builder().build();
            mPublisherAdVieww.loadAd(adRequestt);
        } catch (Exception e) {
            Log.d("TAAG", "forAdverties: " + e.getMessage(), e.getCause());
        }
    }

    private void getList() {
        String url = Config.URL + "api/apiurl.aspx?msg=NewVideotop10List";
        Log.e("TAG", "getList: " + url);
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
        getLoading.dismiss();
        for (int i = 0; i < response.length(); i++) {
            Slider10Model mAinMoviListModel = new Slider10Model();
            try {
                JSONObject jsonObject = response.getJSONObject(i);

                mAinMoviListModel.setVideoName(jsonObject.getString("VideoName"));
                mAinMoviListModel.setTypes(jsonObject.getString("Types"));
                mAinMoviListModel.setCategory(jsonObject.getString("Category"));
                mAinMoviListModel.setLanguage(jsonObject.getString("Language"));
                mAinMoviListModel.setDirectorName(jsonObject.getString("DirectorName"));
                mAinMoviListModel.setDuration(jsonObject.getString("Duration"));
                mAinMoviListModel.setWriter(jsonObject.getString("Writer"));
                mAinMoviListModel.setMusicDirector(jsonObject.getString("MusicDirector"));
                mAinMoviListModel.setDiscription(jsonObject.getString("Discription"));
                mAinMoviListModel.setCast(jsonObject.getString("Cast"));
                mAinMoviListModel.setOneLine(jsonObject.getString("OneLine"));
                mAinMoviListModel.setReleseDate(jsonObject.getString("ReleseDate"));
                mAinMoviListModel.setThumbnail1(jsonObject.getString("Thumbnail1"));
                mAinMoviListModel.setThumbnail2(jsonObject.getString("Thumbnail2"));
                mAinMoviListModel.setVidePath(jsonObject.getString("VidePath"));


            } catch (Exception e) {
                Log.d("TAG", "setListComedy: " + e);
            }
            listSlider.add(mAinMoviListModel);

            Slider10Adapter sliderPagerAdapter = new Slider10Adapter(getContext(), listSlider);
            viewPager.setAdapter(sliderPagerAdapter);
            Timer timer = new Timer();
            timer.schedule(new RemindTask(listSlider.size(), viewPager, getContext()), 2500, 2500);

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
                        viewPager.setCurrentItem(0, true);
                        page = 0;

                    } else {

                        viewPager.setCurrentItem(page++);
                    }
                }
            });
        }
    }

    private void getMovieList(String na) {
        String ss = na.replace(" ", "%20");
        String url = Config.URL + "api/apiurl.aspx?msg=NewVideoList%2018plus%20" + ss;
        Log.e("TAG", "getList: " + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                addData(response);
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

    private void addData(JSONArray response) {
        getLoading.dismiss();

        if (response.isNull(0)) {
            Log.e("TAG", "lst:null " + response);
            Toast.makeText(getContext(), "No data Found", Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < response.length(); i++) {
            Slider10Model mAinMoviListModel = new Slider10Model();
            try {
                JSONObject jsonObject = response.getJSONObject(i);

                mAinMoviListModel.setVideoName(jsonObject.getString("VideoName"));
                mAinMoviListModel.setTypes(jsonObject.getString("Types"));
                mAinMoviListModel.setCategory(jsonObject.getString("Category"));
                mAinMoviListModel.setLanguage(jsonObject.getString("Language"));
                mAinMoviListModel.setDirectorName(jsonObject.getString("DirectorName"));
                mAinMoviListModel.setDuration(jsonObject.getString("Duration"));
                mAinMoviListModel.setWriter(jsonObject.getString("Writer"));
                mAinMoviListModel.setMusicDirector(jsonObject.getString("MusicDirector"));
                mAinMoviListModel.setDiscription(jsonObject.getString("Discription"));
                mAinMoviListModel.setCast(jsonObject.getString("Cast"));
                mAinMoviListModel.setOneLine(jsonObject.getString("OneLine"));
                mAinMoviListModel.setReleseDate(jsonObject.getString("ReleseDate"));
                mAinMoviListModel.setThumbnail1(jsonObject.getString("Thumbnail1"));
                mAinMoviListModel.setThumbnail2(jsonObject.getString("Thumbnail2"));
                mAinMoviListModel.setVidePath(jsonObject.getString("VidePath"));


            } catch (Exception e) {
                Log.d("TAG", "setListComedy: " + e);
            }
            listMovie.add(mAinMoviListModel);
            Collections.reverse(listMovie);
            adpListMovi = new AdapterEighteenList(listMovie, getContext());

            Log.d("taag", String.valueOf(adpListMovi.getItemCount()));
            recMovie.setAdapter(adpListMovi);

        }
    }
}