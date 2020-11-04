package com.swaliya.wowmax.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.configg.SessionManager;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "coding";

    EditText edMobile, edPass;
    String strMob = "", strPass = "";
    SessionManager sessionManager;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        edMobile = findViewById(R.id.edMob);

        sessionManager = new SessionManager(getApplicationContext());
        edPass = findViewById(R.id.edPass);


        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        findViewById(R.id.imgShow).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        edPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        return true;
                    case MotionEvent.ACTION_UP:
                        edPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        return true;
                }
                return false;
            }
        });


        findViewById(R.id.tvForgotPas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forForgotPassword();

            }
        });
        findViewById(R.id.btnLlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strMob = edMobile.getText().toString();
                strPass = edPass.getText().toString();
                if (strMob.equals("")) {
                    Toast.makeText(LoginActivity.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                } else if (strMob.length() != 10) {
                    Toast.makeText(LoginActivity.this, "Enter the valid mobile number", Toast.LENGTH_SHORT).show();
                } else if (strPass.equals("")) {
                    Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                } else {
                    doLogin(strMob, strPass);

                }

            }
        });

    }

    private void forForgotPassword() {

        final Dialog dialog = new Dialog(this, R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.layout_forgot_pass);
        final EditText edNumber = (EditText) dialog.findViewById(R.id.edMob);

        Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strNo = edNumber.getText().toString();
                if (strNo.equals("")) {
                    Toast toast = Toast.makeText(LoginActivity.this, "Enter Valid Details", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (strNo.length() != 10) {
                    Toast toast = Toast.makeText(LoginActivity.this, "Enter Valid Details", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    overridePendingTransition(R.anim.hold, R.anim.slide_down);
                    sendForgotMsg(strNo);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void sendForgotMsg(String strNo) {
        String url = Config.URL + "API/APIURL.aspx?msg=Forgotpass%20" + strNo;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("successful")) {
                    Toast.makeText(LoginActivity.this, "Password will be send to Your Mobile Number", Toast.LENGTH_LONG).show();

                } else if (response.contains("Not Available")) {
                    Toast.makeText(LoginActivity.this, "Your Mobile Not Registered ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Error In Network", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Error in sending message.", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void doLogin(String strMob, String strPass) {


        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            // http://urbsfhelp.live/API/APIlogin.aspx?uid=sai&pass=123&mbl=123456789
            // http://wowmaxmovies.com/API/APIURL.aspx?msg=SelectMobile%209637542885%20123asf
            String url = Config.URL + "API/APIURL.aspx?msg=SelectMobile%20" + strMob + "%20" + strPass;
            getData(url);
            Log.d(TAG, "url: " + url);
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Please Wait Login Processing...");
            pDialog.setCancelable(false);
            pDialog.show();

        } else {
            Toast.makeText(this, "Check internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getData(String url) {
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("welcome")) {
                    Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("key", "reg");
                    startActivity(intent);
                    sessionManager.createLoginSession("name", " ", strMob, strPass);
                    edMobile.setText("");
                    edPass.setText("");
                    pDialog.dismiss();
                } else if (response.contains("unsuccessful")) {
                    pDialog.dismiss();
                    edMobile.setText("");
                    edPass.setText("");
                    Toast.makeText(LoginActivity.this, " Mobile and Password Does Not Match", Toast.LENGTH_SHORT).show();
                } else {
                    pDialog.dismiss();
                    edMobile.setText("");
                    edPass.setText("");
                    Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(LoginActivity.this, error.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
       /* if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, MainActivity.class).putExtra("key", "mail"));
        }

        if (sessionManager.isLoggedIn() == true) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("key", "reg"));

            //  overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }*/

    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you want to close");
        builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));


            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

}
