package com.swaliya.wowmax.activity;

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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.swaliya.wowmax.BuildConfig;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.adapter.MovieListAdapter;
import com.swaliya.wowmax.adapter.TabsAdapter;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.configg.SessionManager;
import com.swaliya.wowmax.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener/*, RewardedVideoAdListener*/, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    SessionManager sessionManager;
    String strName = "", strMob = "", strEmail = "";

    SharedPreferences pref;
    String loginid = "", logemail = "", mobilenumber = "", passwords = "";
    FirebaseAuth mAuth;
    boolean isFromMail = true;
    FirebaseUser user;


    private InterstitialAd mInterstitialAd;
    RewardedVideoAd mAd;

    private List<Movie> listSuperHeroes;
    private MovieListAdapter adapter;
    ProgressDialog loading;
    RecyclerView recyclerView;
    List<Movie> movieList;
    MovieListAdapter recyclerAdapter;

    View layoutMAin, layoutInternet;
    ViewPager viewPager;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        drawerLayout = findViewById(R.id.drawer);
        Toolbar toolbar = findViewById(R.id.toool);
        layoutMAin = findViewById(R.id.layoutMain);
        layoutInternet = findViewById(R.id.layoutInternet);
        navigationView = (NavigationView) findViewById(R.id.nav);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        toggle.syncState();

        toolbar.inflateMenu(R.menu.home_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.one) {
//                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
//                    overridePendingTransition(R.anim.enter, R.anim.hold);
                } else if (item.getItemId() == R.id.two) {
                    Toast.makeText(MainActivity.this, "Two", Toast.LENGTH_SHORT).show();
                } else {

                }
                return false;
            }
        });

        forTab();
        forNavigationClick();

        mAuth = FirebaseAuth.getInstance();
        sessionManager = new SessionManager(this);
        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            loginid = pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_EMAIl)) {
            logemail = pref.getString(Config.KEY_EMAIl, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            mobilenumber = pref.getString(Config.KEY_MOBILE, "");
        }
        if (pref.contains(Config.KEY_PASSWORD)) {
            passwords = pref.getString(Config.KEY_PASSWORD, "");
        }
        //   Toast.makeText(this, loginid + "  " + mobilenumber + "  " + passwords, Toast.LENGTH_LONG).show();
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


        //  navigationView.getMenu().getItem(1).setIcon(R.drawable.ic_wrap_text_black_24dp);
        //    forAdvertise();

        forNotification();
        //  forSlider();


        navigationView.setNavigationItemSelectedListener(this);
        TextView tvId = navigationView.findViewById(R.id.tvUserId);
        tvId.setText(mobilenumber);
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

        //  getResponce();


    }


    private void getData(String url) {
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("Data Exists")) {
                    Toast.makeText(MainActivity.this, "Data  already exist", Toast.LENGTH_LONG).show();

                } else if (response.contains("successfully")) {
                    Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.putExtra("key", "reg");
                    startActivity(intent);
                    sessionManager.createLoginSession(strName, strEmail, strMob, "strPass");
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Something wrong in network  ", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, error.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void forNavigationClick() {
        findViewById(R.id.nav_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                recreate();
               /* if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }*/
            }
        });
        findViewById(R.id.nav_subscription).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected() == true) {
                    Intent i = new Intent(getApplicationContext(), MySubscriptionActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter, R.anim.hold);
                    //  loadRewardedVideoAd();
                } else {
                    Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.nav_wishlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected() == true) {
                    Intent i = new Intent(getApplicationContext(), WishListActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter, R.anim.hold);
                } else {
                    Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        findViewById(R.id.nav_wallet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected() == true) {
                    Intent i = new Intent(getApplicationContext(), WalletActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter, R.anim.hold);
                } else {
                    Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.nav_reff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
        findViewById(R.id.nav_terms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.closeDrawers();
                //  termsCondition(R.string.privacy);
                termsCondition(R.string.terms);
            }
        });
        findViewById(R.id.nav_privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.closeDrawers();
                termsCondition(R.string.privacy);
                //  privacyAlert(2);
            }
        });
        findViewById(R.id.nav_legel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                termsCondition(R.string.legal);
            }
        });
        findViewById(R.id.nav_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                termsCondition(R.string.adddress);
            }
        });
        findViewById(R.id.nav_guide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                termsCondition(R.string.guide);
            }
        });
        findViewById(R.id.nav_faq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                termsCondition(R.string.faqq);
            }
        });
        findViewById(R.id.nav_customer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                termsCondition(R.string.service);
            }
        });
        findViewById(R.id.nav_my_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected() == true) {

                    Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(i);

                } else {
                    Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.nav_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

    }

    private void forTab() {

        tabLayout.addTab(tabLayout.newTab().setText("Just Added"));
        tabLayout.addTab(tabLayout.newTab().setText("Movie"));
        tabLayout.addTab(tabLayout.newTab().setText("Web Series"));
        tabLayout.addTab(tabLayout.newTab().setText("Short Movie"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);

        // viewPager.setCurrentItem(1);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private void forAdvertise() {

        mAd = MobileAds.getRewardedVideoAdInstance(this);
        //  mAd.setRewardedVideoAdListener(this);

        MobileAds.initialize(this, "ca-app-pub-7999232318006976~6141148234");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7999232318006976/4666901574");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });


    }


   /* private void getResponce() {

        loading = ProgressDialog.show(this, "Loading Data", "Please Wait...", false, false);
        loading.dismiss();
        movieList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new HindiAdapter(movieList, MainActivity.this);
        recyclerView.setAdapter(recyclerAdapter);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<MAinMoviListModel>> call = apiService.getMovies();
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

    }*/

    private void forNotification() {

        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("MyNo", "MyNo", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("test")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfull";
                        if (!task.isSuccessful()) {
                            msg = "msg_subscribe_failed";
                        }
                        Log.d(TAG, msg);
                        //  Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
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
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            layoutMAin.setVisibility(View.VISIBLE);
            layoutInternet.setVisibility(View.GONE);
        } else {
            layoutInternet.setVisibility(View.VISIBLE);
            layoutMAin.setVisibility(View.GONE);
        }
        /*if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }*/

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

        /*
        if (id == R.id.nav_home) {
            drawerLayout.closeDrawers();
          *//*  if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }*//*
        } else if (id == R.id.nav_subscription) {

            drawerLayout.closeDrawers();
            ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nf = cn.getActiveNetworkInfo();
            if (nf != null && nf.isConnected() == true) {
                Intent i = new Intent(getApplicationContext(), MySubscriptionActivity.class);
                startActivity(i);
                //   loadRewardedVideoAd();
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
                termsCondition(R.string.terms);

            } else {
                Toast.makeText(MainActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.nav_privacy) {

            drawerLayout.closeDrawers();
            termsCondition(R.string.privacy);
            //   privacyAlert(2);
            return true;
        } else if (id == R.id.nav_legel) {

            drawerLayout.closeDrawers();
            termsCondition(R.string.legal);


            return true;
        } else if (id == R.id.nav_about) {

            drawerLayout.closeDrawers();
            termsCondition(R.string.adddress);

            return true;
        } else if (id == R.id.nav_guide) {

            drawerLayout.closeDrawers();
            termsCondition(R.string.guide);

            return true;
        } else if (id == R.id.nav_faq) {

            drawerLayout.closeDrawers();
            termsCondition(R.string.faqq);

            return true;
        } else if (id == R.id.nav_customer) {

            drawerLayout.closeDrawers();
            termsCondition(R.string.service);

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
        }*/
        return false;
    }


    private void termsCondition(int terms) {
        final Dialog dialog = new Dialog(this, R.style.AlertDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.layout_privacy);

        TextView textView = (TextView) dialog.findViewById(R.id.tvMsg);
        textView.setText(terms);
        Button dialogButton = (Button) dialog.findViewById(R.id.btnOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.hold, R.anim.slide_down);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    String TAG = "TAg";

   /* @Override
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
    }*/

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
