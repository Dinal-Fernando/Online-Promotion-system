package com.example.dinalfernando.imageapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Handler mHandler= new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (internetConnection()) {
            Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Not Connected", Toast.LENGTH_LONG).show();

        }



      // openLogin();

        status();
        mToastRunnable.run();

    }

    private boolean internetConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }


    public void getLogStatus() {
        final String mac=  new getmacid(this).generateID();

        StringRequest req = new StringRequest(Request.Method.POST, "http://192.168.43.108/ProjectHeshan/public/api/loginstate", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")){
                    Toast.makeText(getApplicationContext(),"logedin",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this,Main2Activity.class)); // user activity
                }else{
                    Toast.makeText(getApplicationContext(),"not logedin",Toast.LENGTH_LONG).show();
                    openLogin();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("maC",mac);


                return params;
            }
        };
        Volley.newRequestQueue(this).add(req);
    }


    public void openDeviceReg(){
        Intent intent=new Intent(this,DeviceRegActivity.class);
        startActivity(intent);
    }

    public void openLogin(){
        startActivity(new Intent(this,LoginActivity.class));

    }


    public void status() {
        final String mac =  new getmacid(this).generateID();

        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.108/ProjectHeshan/public/api/activeORnot", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("3")) {
                    deactivate();
                }else if(response.contains("2")){
                    //pending
                }if(response.contains("1")){
                    //getLogStatus();

                    //act
                }else{
                    //rejected
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("maC", mac);

                return params;
            }

        };
        Volley.newRequestQueue(this).add(request);

    }

    public void deactivate(){
        startActivity(new Intent(getApplicationContext(),Deactivate.class));
    }


    private Runnable mToastRunnable=new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this,60000);

            timeUpdate();
        }
    };


    public void timeUpdate(){

        final String mac= new getmacid(this).generateID();

        StringRequest request= new StringRequest(Request.Method.POST, "http://192.168.43.108/ProjectHeshan/public/api/time", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params= new HashMap<>();
                params.put("maC",mac);



                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(request);

    }



}
