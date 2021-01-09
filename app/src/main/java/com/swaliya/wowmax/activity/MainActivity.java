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

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.swaliya.wowmax.BuildConfig;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.adapter.AdapterComedy;
import com.swaliya.wowmax.adapter.TabsAdapter;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.configg.SessionManager;
import com.swaliya.wowmax.model.Movie;

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


    private List<Movie> listSuperHeroes;
    private AdapterComedy adapter;
    ProgressDialog loading;
    RecyclerView recyclerView;
    List<Movie> movieList;
    AdapterComedy recyclerAdapter;

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
                    startActivity(new Intent(MainActivity.this, ViewAllVdoActivity.class).putExtra("key", "All"));
//                    overridePendingTransition(R.anim.enter, R.anim.hold);
                } else if (item.getItemId() == R.id.two) {
                    ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo nf = cn.getActiveNetworkInfo();
                    if (nf != null && nf.isConnected()) {
                        startActivity(new Intent(MainActivity.this, WowsShortListActivity.class));
                    }
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
        findViewById(R.id.layHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        findViewById(R.id.layWowShort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected()) {
                    startActivity(new Intent(MainActivity.this, WowsShortListActivity.class));
                }
            }
        });
        findViewById(R.id.laySong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected()) {
                    startActivity(new Intent(MainActivity.this, SongsActivity.class));
                }
            }
        });
        findViewById(R.id.layShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Wowmax");
                    String shareMessage = "\nLet me recommend you this application\n\n";

                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    Log.d("TAg", "onNavigationItemSelected: ");
                }
            }
        });


    }


    private void forNavigationClick() {
        findViewById(R.id.nav_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();


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
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Wowmax");
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


    private void forNotification() {

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("MyNo", "MyNo", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("group")
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


    }

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
