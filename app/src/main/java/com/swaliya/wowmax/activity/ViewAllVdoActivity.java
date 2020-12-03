package com.swaliya.wowmax.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.util.Strings;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.adapter.AdapterRomance;
import com.swaliya.wowmax.adapter.AdapterViewAll;
import com.swaliya.wowmax.adapter.MyListAdapter;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.configg.SessionManager;
import com.swaliya.wowmax.model.MainMovieListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewAllVdoActivity extends AppCompatActivity {
    SessionManager session;
    SharedPreferences pref;
    String loginid = "", mobilenumber = "", passwords = "";
    String strCat = "", strSearch = "";
    TextView tvCat;

    List<MainMovieListModel> listAll;
    RecyclerView.Adapter adpAll;
    RecyclerView rcViewAll;

    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_vdo);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvCat = findViewById(R.id.tvCat);
        session = new SessionManager(this);
        //  https://gist.github.com/jsturgis/3b19447b304616f18657
        session = new SessionManager(getApplicationContext());
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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            strCat = bundle.getString("key");
            tvCat.setText(strCat);
        }
        rcViewAll = (RecyclerView) findViewById(R.id.recViewAll);
        listAll = new ArrayList<>();
        adpAll = new AdapterViewAll(listAll, this);
        rcViewAll.setLayoutManager(new GridLayoutManager(this, 3));

        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected()) {
            getResponce();
            loading = ProgressDialog.show(this, "Loading Data", "Please Wait...", false, false);
        } else {
            Toast.makeText(this, "Check internet connection", Toast.LENGTH_SHORT).show();
        }
        LinearLayout laySearch = findViewById(R.id.layoutSearch);
        LinearLayout laylist = findViewById(R.id.layList);
        EditText edSearch = findViewById(R.id.edSearch);
        findViewById(R.id.imgSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listAll.clear();
                strSearch = "";
                laylist.setVisibility(View.GONE);
                laySearch.setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strSearch = edSearch.getText().toString().trim();
                edSearch.setText("");
                if (strSearch.equals("")) {
                    Toast.makeText(ViewAllVdoActivity.this, "Enter Details", Toast.LENGTH_SHORT).show();
                } else {
                    getResponce();
                    laySearch.setVisibility(View.GONE);
                    laylist.setVisibility(View.VISIBLE);
                }
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
                Toast.makeText(ViewAllVdoActivity.this, "Poor Internet Connection", Toast.LENGTH_LONG).show();
                Log.d("TAG", "onErrorResponse: " + error.getLocalizedMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void parseData(JSONArray response) {

        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonObject = response.getJSONObject(i);
                if (strCat.equals("All")) {
                    if (strSearch.equals("")) {
                        setListRomance(jsonObject);
                    } else {
                        String sss = jsonObject.getString("MovieTitle");
                        if (sss.toLowerCase().contains(strSearch.toLowerCase())) {
                            setListRomance(jsonObject);
                        }
                    }
                }
                if (strCat.equals(jsonObject.getString("CategoryName"))) {
                    if (strSearch.equals("")) {
                        setListRomance(jsonObject);
                    } else {
                        String sss = jsonObject.getString("MovieTitle");
                        if (sss.toLowerCase().contains(strSearch.toLowerCase())) {
                            setListRomance(jsonObject);
                        }
                    }
                }
                Log.d("list", "parseData: " + jsonObject.getString("MovieTitle"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (listAll.isEmpty()) {
            Toast.makeText(this, "Not Match Found " + strSearch, Toast.LENGTH_SHORT).show();
        }
    }

    private void setListRomance(JSONObject jsonObject) {
        loading.dismiss();

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

        Collections.reverse(listAll);
        adpAll = new AdapterViewAll(listAll, this);

        Log.d("taag", String.valueOf(adpAll.getItemCount()));
        rcViewAll.setAdapter(adpAll);

    }

}
