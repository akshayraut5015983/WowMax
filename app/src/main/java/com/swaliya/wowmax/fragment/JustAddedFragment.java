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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.swaliya.wowmax.activity.ViewAllVdoActivity;
import com.swaliya.wowmax.adapter.AdapterActionComedy;
import com.swaliya.wowmax.adapter.AdapterActionDrama;
import com.swaliya.wowmax.adapter.AdapterComedy;
import com.swaliya.wowmax.adapter.AdapterComedyDrama;
import com.swaliya.wowmax.adapter.AdapterMarathi;
import com.swaliya.wowmax.adapter.AdapterRemAll;
import com.swaliya.wowmax.adapter.AdapterRomance;
import com.swaliya.wowmax.adapter.AdapterThrAction;
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

    List<MainMovieListModel> listComedy, listActComedy, listAll, listActDrama, listThrAction, listRomance, listComDrama,listMarathi;
    RecyclerView.Adapter adpComedy, adpActComedy, adpAll, adpActDrama, adpThrAction, adpRomance, adpComDrama,adpMarathi;
    RecyclerView rcComedy, rcActComedy, rcAll, rcActDrama, rcThrAction, rcRomance, rcComDrama,reMarathi;
    LinearLayout layComedy, layactCom, layAll, layActDrama, layThrAction, layRomance, layComDrama,lyMarathi;

    ProgressDialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_mainjust, viewGroup, false);


        forAdverties(v);

        //   forSlider(v);
        layAll = v.findViewById(R.id.layAll);
        listAll = new ArrayList<>();
        rcAll = v.findViewById(R.id.recAll);
        rcAll.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        layComedy = v.findViewById(R.id.layComedy);
        listComedy = new ArrayList<>();
        rcComedy = v.findViewById(R.id.recyclerComedy);
        rcComedy.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        layactCom = v.findViewById(R.id.layActionComedy);
        listActComedy = new ArrayList<>();
        rcActComedy = v.findViewById(R.id.recActionComedy);
        rcActComedy.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        layComDrama = v.findViewById(R.id.layComedyDrama);
        listComDrama = new ArrayList<>();
        rcComDrama = v.findViewById(R.id.recComedyDrama);
        rcComDrama.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        layActDrama = v.findViewById(R.id.layActionDrama);
        listActDrama = new ArrayList<>();
        rcActDrama = v.findViewById(R.id.recActionDrama);
        rcActDrama.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        lyMarathi = v.findViewById(R.id.layMarathi);
        listMarathi = new ArrayList<>();
        reMarathi = v.findViewById(R.id.recMarathi);
        reMarathi.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        layRomance = v.findViewById(R.id.layRomance);
        listRomance = new ArrayList<>();
        rcRomance = v.findViewById(R.id.recRomance);
        rcRomance.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        layThrAction = v.findViewById(R.id.layThrAction);
        listThrAction = new ArrayList<>();
        rcThrAction = v.findViewById(R.id.recThrAction);
        rcThrAction.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        ConnectivityManager cn = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected()) {
            getResponce();
            loading = ProgressDialog.show(getContext(), "Loading Data", "Please Wait...", false, false);
        } else {
            Toast.makeText(getContext(), "Check internet connection", Toast.LENGTH_SHORT).show();
        }
        onLayoutClick();
        return v;

    }

    private void onLayoutClick() {
        layAll.setOnClickListener(v -> startActivity(new Intent(getContext(), ViewAllVdoActivity.class).putExtra("key", "All")));

        layComedy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class).putExtra("key", "Comedy"));
            }
        });
        layactCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class).putExtra("key", "Action and Comedy"));
            }
        });
        layComDrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class).putExtra("key", "Comedy and Drama"));
            }
        });
        layActDrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class).putExtra("key", "Action and Drama"));
            }
        });
        lyMarathi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class).putExtra("key", "Marathi"));
            }
        });
        layRomance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class).putExtra("key", "Romance"));
            }
        });
        layThrAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewAllVdoActivity.class).putExtra("key", "Thriller and action"));
            }
        });
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
            try {
                JSONObject jsonObject = response.getJSONObject(i);
                if (jsonObject.getString("CategoryName").equals("Comedy")) {
                    setListComedy(jsonObject);
                } else if (jsonObject.getString("CategoryName").equals("Action and Comedy")) {
                    setListActionComedy(jsonObject);
                } else if (jsonObject.getString("CategoryName").equals("Action and Drama")) {
                    setListActionDrama(jsonObject);
                } else if (jsonObject.getString("CategoryName").equals("Romance")) {
                    setListRomance(jsonObject);
                } else if (jsonObject.getString("CategoryName").equals("Marathi")) {
                    setListMarathi(jsonObject);
                }else if (jsonObject.getString("CategoryName").equals("Thriller and action")) {
                    setListThrAction(jsonObject);
                } else if (jsonObject.getString("CategoryName").equals("Comedy and Drama")) {
                    setListComDrama(jsonObject);
                } else {
                    setRemainData(jsonObject);
                }

                Log.d("list", "parseData: " + jsonObject.getString("MovieTitle"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    private void setListMarathi(JSONObject jsonObject) {
        MainMovieListModel mAinMoviListModel = new MainMovieListModel();
        try {

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

        } catch (Exception e) {
            Log.d("TAG", "setListComedy: " + e);
        }
        listMarathi.add(mAinMoviListModel);
        if (listMarathi.size() != 0) {
            lyMarathi.setVisibility(View.VISIBLE);
        } else {
            lyMarathi.setVisibility(View.GONE);
        }
        loading.dismiss();
        Collections.reverse(listMarathi);
        adpComDrama = new AdapterMarathi(listMarathi, getContext());
        reMarathi.setAdapter(adpMarathi);
    }

    private void setListComDrama(JSONObject jsonObject) {
        MainMovieListModel mAinMoviListModel = new MainMovieListModel();
        try {

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

        } catch (Exception e) {
            Log.d("TAG", "setListComedy: " + e);
        }
        listComDrama.add(mAinMoviListModel);
        if (listComDrama.size() != 0) {
            layComDrama.setVisibility(View.VISIBLE);
        } else {
            layComDrama.setVisibility(View.GONE);
        }
        loading.dismiss();
        Collections.reverse(listComDrama);
        adpComDrama = new AdapterComedyDrama(listComDrama, getContext());
        rcComDrama.setAdapter(adpComDrama);
    }

    private void setListRomance(JSONObject jsonObject) {
        MainMovieListModel mAinMoviListModel = new MainMovieListModel();
        try {

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

        } catch (Exception e) {
            Log.d("TAG", "setListComedy: " + e);
        }
        listRomance.add(mAinMoviListModel);
        if (listRomance.size() != 0) {
            layRomance.setVisibility(View.VISIBLE);
        } else {
            layRomance.setVisibility(View.GONE);
        }
        loading.dismiss();
        Collections.reverse(listRomance);
        adpRomance = new AdapterRomance(listRomance, getContext());

        rcRomance.setAdapter(adpRomance);
    }

    private void setListThrAction(JSONObject jsonObject) {
        MainMovieListModel mAinMoviListModel = new MainMovieListModel();
        try {

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

        } catch (Exception e) {
            Log.d("TAG", "setListComedy: " + e);
        }
        listThrAction.add(mAinMoviListModel);
        if (listThrAction.size() != 0) {
            layThrAction.setVisibility(View.VISIBLE);
        } else {
            layThrAction.setVisibility(View.GONE);
        }
        loading.dismiss();
        Collections.reverse(listThrAction);
        adpThrAction = new AdapterThrAction(listThrAction, getContext());
        rcThrAction.setAdapter(adpThrAction);
    }

    private void setRemainData(JSONObject jsonObject) {
        MainMovieListModel mAinMoviListModel = new MainMovieListModel();
        try {

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

        } catch (Exception e) {
            Log.d("TAG", "setListComedy: " + e);
        }
        listAll.add(mAinMoviListModel);
        if (listAll.size() != 0) {
            layAll.setVisibility(View.VISIBLE);
        } else {
            layAll.setVisibility(View.GONE);
        }
        loading.dismiss();
        Collections.reverse(listAll);
        adpAll = new AdapterRemAll(listAll, getContext());
        rcAll.setAdapter(adpAll);
    }

    private void setListActionComedy(JSONObject jsonObject) {
        MainMovieListModel mAinMoviListModel = new MainMovieListModel();
        try {

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

        } catch (Exception e) {
            Log.d("TAG", "setListComedy: " + e);
        }
        listActComedy.add(mAinMoviListModel);
        if (listActComedy.size() != 0) {
            layactCom.setVisibility(View.VISIBLE);
        } else {
            layactCom.setVisibility(View.GONE);
        }
        loading.dismiss();
        Collections.reverse(listActComedy);
        adpActComedy = new AdapterActionComedy(listActComedy, getContext());
        rcActComedy.setAdapter(adpActComedy);
    }

    private void setListActionDrama(JSONObject jsonObject) {
        MainMovieListModel mAinMoviListModel = new MainMovieListModel();
        try {

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

        } catch (Exception e) {
            Log.d("TAG", "setListComedy: " + e);
        }
        listActDrama.add(mAinMoviListModel);
        if (listActDrama.size() != 0) {
            layActDrama.setVisibility(View.VISIBLE);
        } else {
            layActDrama.setVisibility(View.GONE);
        }
        loading.dismiss();
        Collections.reverse(listActDrama);
        adpActDrama = new AdapterActionDrama(listActDrama, getContext());
        rcActDrama.setAdapter(adpActDrama);
    }

    private void setListComedy(JSONObject jsonObject) {
        MainMovieListModel mAinMoviListModel = new MainMovieListModel();
        try {

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

        } catch (Exception e) {
            Log.d("TAG", "setListComedy: " + e);
        }
        listComedy.add(mAinMoviListModel);
        if (listComedy.size() != 0) {
            layComedy.setVisibility(View.VISIBLE);
        } else {
            layComedy.setVisibility(View.GONE);
        }
        loading.dismiss();
        Collections.reverse(listComedy);
        adpComedy = new AdapterComedy(listComedy, getContext());
        rcComedy.setAdapter(adpComedy);
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