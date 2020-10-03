package com.swaliya.vidmax.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
/*
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;*/
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.swaliya.vidmax.BuildConfig;
import com.swaliya.vidmax.R;
import com.swaliya.vidmax.adapter.HindiAdapter;
import com.swaliya.vidmax.configg.Config;
import com.swaliya.vidmax.configg.SessionManager;
import com.swaliya.vidmax.helper.ApiClient;
import com.swaliya.vidmax.helper.ApiInterface;
import com.swaliya.vidmax.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RewardedVideoAdListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    SessionManager sessionManager;

    String strName = "", strMob = "", strEmail = "";

    SharedPreferences pref;
    String loginid = "", mobilenumber = "", passwords = "";
    FirebaseAuth mAuth;
    boolean isFromMail = true;
    FirebaseUser user;

    private AdView mAdView, adView;
    private InterstitialAd mInterstitialAd;
    RewardedVideoAd mAd;


    private List<Movie> listSuperHeroes;
    private HindiAdapter adapter;
    ProgressDialog loading;
    RecyclerView recyclerView;
    List<Movie> movieList;
    HindiAdapter recyclerAdapter;
SliderLayout sliderLayout;
    HashMap<String,Integer> sliderImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        forNotify();

        mAuth = FirebaseAuth.getInstance();
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);

        MobileAds.initialize(this, "ca-app-pub-7999232318006976~6141148234");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7999232318006976/4666901574");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        adView = findViewById(R.id.adVieww);
        AdRequest adRequestt = new AdRequest.Builder().build();
        adView.loadAd(adRequestt);
       /* adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Toast.makeText(MainActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Toast.makeText(MainActivity.this, "onAdOpened()", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Toast.makeText(MainActivity.this, "onAdClosed()", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Toast.makeText(MainActivity.this, "onAdFailedToLoad()", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Toast.makeText(MainActivity.this, "onAdLeftApplication()", Toast.LENGTH_SHORT).show();
            }
        });*/
        sessionManager = new SessionManager(this);
        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            loginid = pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            mobilenumber = pref.getString(Config.KEY_MOBILE, "");
        }
        if (pref.contains(Config.KEY_PASSWORD)) {
            passwords = pref.getString(Config.KEY_PASSWORD, "");
        }
        Toast.makeText(this, loginid + "  " + mobilenumber + "  " + passwords, Toast.LENGTH_LONG).show();
        Intent intent = getIntent();
        if (intent != null) {
            String str = intent.getExtras().getString("key");
            if (str.equals("mail")) {
                isFromMail = true;
                user = mAuth.getCurrentUser();
                strName = user.getDisplayName();
                strMob = user.getPhoneNumber();
                strEmail = user.getEmail();

            } else {
                isFromMail = false;
            }
        }

        navigationView = (NavigationView) findViewById(R.id.nav);
        //  navigationView.getMenu().getItem(1).setIcon(R.drawable.ic_wrap_text_black_24dp);
        drawerLayout = findViewById(R.id.drawer);
        Toolbar toolbar = findViewById(R.id.toool);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        toolbar.inflateMenu(R.menu.home_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.one) {
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                } else if (item.getItemId() == R.id.two) {
                    Toast.makeText(MainActivity.this, "Two", Toast.LENGTH_SHORT).show();
                } else {

                }
                return false;
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        findViewById(R.id.main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewAllVdoActivity.class));
            }
        });
        findViewById(R.id.ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), VdoDetailsActivity.class));
            }
        });

       /* listSuperHeroes = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        loading = ProgressDialog.show(this, "Loading Data", "Please Wait...", false, false);
        getData();
        loading.dismiss();
        adapter = new HindiAdapter(listSuperHeroes, this);

        Log.d("tag", String.valueOf(adapter.getItemCount()));
        recyclerView.setAdapter(adapter);*/
        getResponce();

        sliderLayout=findViewById(R.id.sliderLayout);
        sliderImages = new HashMap<>();
        sliderImages.put("Great Indian Deal", R.drawable.img_clo);
        sliderImages.put("New Deal Every Hour",R.drawable.img_bann);
        sliderImages.put("Appliances Sale", R.drawable.img_ban);

        for (String name : sliderImages.keySet()) {

            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(name)
                    .image(sliderImages.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(this);

    }

    private void getResponce() {

        loading = ProgressDialog.show(this, "Loading Data", "Please Wait...", false, false);
        movieList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new HindiAdapter(movieList, MainActivity.this);
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

    private void forNotify() {

        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("MyNo", "MyNo", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("genral")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfull";
                        if (!task.isSuccessful()) {
                            msg = "msg_subscribe_failed";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void getData() {
        String url = "https://reqres.in/api/unknown";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    parseData(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onResponse: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Eoorr", Toast.LENGTH_SHORT).show();
                loading.dismiss();
                Log.d(TAG, "onErrorResponse: ");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjectRequest);


        //http://urbsfhelp.live/API/APIURL.aspx?msg=Income%20sai&mobile=123456789
       /* String MY_URL = Config.URL + "API/APIURL.aspx?msg=Income%20" + loginid + "&mobile=" + mobilenumber;

  try {
                    JSONArray jsonArray = response.getJSONArray("categories");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        JSONArray jsonArray1 = jsonObject.getJSONArray("videos");
                        parseData(jsonArray1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(MY_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response history", String.valueOf(response));
                        parseData(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Log.d("incomreport", error.getMessage());
                        error.getMessage();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonArrayRequest);*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }

    private void parseData(JSONArray array) {
       /* try {
            //  JSONArray array = new JSONArray(aarray);
            for (int i = 0; i < array.length(); i++) {
                Datum vdo = new Datum();
                JSONObject json = null;
                try {
                    json = array.getJSONObject(i);
                    vdo.setName(json.getString("name"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listSuperHeroes.add(vdo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("histcout", String.valueOf(adapter.getItemCount()));
        //  Toast.makeText(this, String.valueOf(adapter.getItemCount()), Toast.LENGTH_SHORT).show();
        if (adapter.getItemCount() == 0) {
            //    Toast.makeText(this, "Data not available", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        loading.dismiss();*/
    }

    @SuppressLint("ResourceAsColor")
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        /*MenuItem searchViewItem = menu.findItem(R.id.one);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setBackgroundColor(R.color.white);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                Toast.makeText(MainActivity.this, "ONClik", Toast.LENGTH_SHORT).show();
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // adapter.getFilter().filter(newText);
                return false;
            }
        });*/
        return true;
    }

    private void loadRewardedVideoAd() {
        mAd.loadAd("ca-app-pub-7999232318006976/5848191117",
                new AdRequest.Builder()
                        .build());
    }

   /* private void removeCart() {
        // http://demo1.swaliyasoftech.com/api/apiurl.aspx?msg=deleterecord%209876543210
        String url = Config.URL + "api/apiurl.aspx?msg=deleterecord%20" + mobilenumber;
        Log.d("vollyadd", url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("vollyaddresponce", response);
                if (response.contains("Record deleted successfully")) {

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Cart Not Clear");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Delete Record", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeCart();
                        }
                    });

                    Dialog dialog = builder.create();
                    dialog.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("inserterror", error.getMessage());
                Toast.makeText(getApplicationContext(), "Error to Remove cart item check connection", Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }*/



    /* @Override
     public void onBackPressed() {
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setMessage("Are you want to close");
         builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
             }
         });
         builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 finishAffinity();
             }
         });
         Dialog dialog = builder.create();
         dialog.show();


     }*/

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
            drawerLayout.closeDrawers();

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        } else if (id == R.id.nav_subscription) {

            drawerLayout.closeDrawers();
            ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nf = cn.getActiveNetworkInfo();
            if (nf != null && nf.isConnected() == true) {
                Intent i = new Intent(getApplicationContext(), MySubscriptionActivity.class);
                startActivity(i);
                loadRewardedVideoAd();
            } else {
                Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
            }

            return true;
        } else if (id == R.id.nav_wishlist) {

            drawerLayout.closeDrawers();
            ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nf = cn.getActiveNetworkInfo();
            if (nf != null && nf.isConnected() == true) {
                Intent i = new Intent(getApplicationContext(), WishListActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
            }

            return true;
        } else if (id == R.id.nav_wallet) {
            drawerLayout.closeDrawers();
            ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nf = cn.getActiveNetworkInfo();
            if (nf != null && nf.isConnected() == true) {
                Intent i = new Intent(getApplicationContext(), WalletActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
            }

            return true;
        } else if (id == R.id.nav_reff) {
            drawerLayout.closeDrawers();

            ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nf = cn.getActiveNetworkInfo();
            if (nf != null && nf.isConnected() == true) {

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Vidmax");
                    String shareMessage = "\nLet me recommend you this application\n\n";

                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    Log.d("TAg", "onNavigationItemSelected: ");
                }
            } else {
                Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
            }

            return true;
        } else if (id == R.id.nav_terms) {

            drawerLayout.closeDrawers();
            ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nf = cn.getActiveNetworkInfo();
            if (nf != null && nf.isConnected() == true) {
                termsCondition();

            } else {
                Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
            }

            return true;
        } else if (id == R.id.nav_my_profile) {
            drawerLayout.closeDrawers();
            ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nf = cn.getActiveNetworkInfo();
            if (nf != null && nf.isConnected() == true) {

                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(i);

            } else {
                Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.nav_logout) {
            drawerLayout.closeDrawers();
            if (isFromMail) {
                mAuth.signOut();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                sessionManager.logoutUser();
            } else {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                sessionManager.logoutUser();
            }
            return true;
        }
        return false;
    }

    private void termsCondition() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_terms_condition);


        Button dialogButton = (Button) dialog.findViewById(R.id.btnOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    String TAG = "TAg";

    @Override
    public void onRewardedVideoAdLoaded() {
        Log.i(TAG, "Rewarded: onRewardedVideoAdLoaded");
        try {
            if (mAd.isLoaded()) {
                mAd.show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.i(TAG, "Rewarded: onRewardedVideoAdOpened");
    }

    @Override
    public void onRewardedVideoStarted() {
        Log.i(TAG, "Rewarded: onRewardedVideoStarted");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Log.i(TAG, "Rewarded: onRewardedVideoAdClosed");

    }

    @Override
    public void onRewarded(com.google.android.gms.ads.reward.RewardItem rewardItem) {
        Log.i(TAG, "Rewarded:  onRewarded! currency: " + rewardItem.getType() + "  amount: " +
                rewardItem.getAmount());

    }


    @Override
    public void onRewardedVideoAdLeftApplication() {
        Log.i(TAG, "Rewarded: onRewardedVideoAdLeftApplication ");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Log.i(TAG, "Rewarded: onRewardedVideoAdFailedToLoad: " + i);
    }

    @Override
    public void onRewardedVideoCompleted() {
        Log.d(TAG, "onRewardedVideoCompleted: ");

    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
     //   Toast.makeText(MainActivity.this, slider.getBundle().get("extra") +  " ", Toast.LENGTH_SHORT).show();
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
