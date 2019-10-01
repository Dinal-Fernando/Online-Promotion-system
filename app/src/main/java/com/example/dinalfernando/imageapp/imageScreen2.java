package com.example.dinalfernando.imageapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class imageScreen2 extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener {

    private static final String Tag="MainActivity";





    String imageaddress1=" http://tv.shopwsrep.com/api/imageScreen2";
    String newsaddress=" http://tv.shopwsrep.com/api/news";
    String URL_REGIST = " http://tv.shopwsrep.com/api/connectionerror";
    String URL_REGIST1 = " http://tv.shopwsrep.com/api/logout";
    String categoryaddress = " http://tv.shopwsrep.com/api/category";

    InputStream is=null;
    String line=null;
    String result=null;
    String[] image;
    String[] imagename;
    int imagesize;
    String[] head;
    String[] body;
    String[] tale;
    String path;
    String statusofapp="Dealer";
    String category;
    TextView textview;
    int count1;
    SliderLayout sliderLayout ;
    HashMap<String, String> HashMapForURL ;
    private ArrayList<NavigationItem> mActivityList;
    private ItemAdapter mAdapter;
    private Handler mHandler=new Handler();
    int counttime=0;
    int imagearray;
    String macid;
    String mac_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_screen2);

        macid = new getmacid(imageScreen2.this).generateID();

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        getcategory();
        getImage();
        getNews();
        AddImagesUrlOnline();

        imagearray=imagesize;


        textview=(TextView)findViewById(R.id.textview1);
        textview.setText(head[0]+" * " +body[0]+" * " +tale[0]+head[0]+" * " +body[0]+" * " +tale[0]);

        initList();
        Spinner spinneritem=findViewById(R.id.spinner);
        mAdapter=new ItemAdapter(this,mActivityList);
        spinneritem.setAdapter(mAdapter);
        spinneritem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NavigationItem clickedItem=(NavigationItem)parent.getItemAtPosition(position);
                String clickedItemName=clickedItem.getActivityName();
                if(clickedItemName.equals("Main Screen")){
                    startActivity(new Intent(imageScreen2.this,Main2Activity.class));
                }
                if(clickedItemName.equals("Image Screen 1")){
                    startActivity(new Intent(imageScreen2.this,imageScreen1.class));
                }
                if(clickedItemName.equals("Video Screen")){
                    startActivity(new Intent(imageScreen2.this,videoScreen.class));
                }
                if(clickedItemName.equals("Exit")){
                    logout();
                    mHandler.postDelayed(closeapp,1000);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sliderLayout = (SliderLayout)findViewById(R.id.slider);


        for(String name : HashMapForURL.keySet()){

            TextSliderView textSliderView = new TextSliderView(imageScreen2.this);

            textSliderView
                    .description(name)
                    .image(HashMapForURL.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            textSliderView.bundle(new Bundle());

            textSliderView.getBundle()
                    .putString("extra",name);

            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        sliderLayout.setCustomAnimation(new DescriptionAnimation());

        sliderLayout.setDuration(3000);

        sliderLayout.addOnPageChangeListener(imageScreen2.this);

        mToastRunnable.run();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {


        logout();
        mHandler.postDelayed(closeapp,1000);
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //  Log.d("hiii","yep");
        //  Log.d("Slider Demo", "Page Changed: " + position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void AddImagesUrlOnline(){



        HashMapForURL = new HashMap<String, String>();


        for(int i=0;i<count1;i++) {
            HashMapForURL.put(imagename[i], image[i]);


        }
    }

    private void getImage()
    {
        try {
            URL url = new URL(imageaddress1);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is=new BufferedInputStream(con.getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuilder sb=new StringBuilder();

            while((line=br.readLine())!=null)
            {
                sb.append(line+"\n");
            }
            is.close();
            result=sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            JSONArray ja=new JSONArray(result);
            JSONObject jo=null;



            image=new String[ja.length()];
            imagename=new String[ja.length()];
            imagesize=ja.length();

            count1=0;

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);

                Log.d(Tag,"inside if");
                category=jo.getString("category");
                if(category.equals(statusofapp)){
                    path = jo.getString("image_path");
                    image[count1] = " http://tv.shopwsrep.com/" + path;
                    count1++;
                }


            }
            count1=0;
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                category=jo.getString("category");
                if(category.equals(statusofapp)){
                    imagename[count1] = jo.getString("id");
                    count1++;
                }
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getNews()
    {
        try {
            URL url = new URL(newsaddress);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is=new BufferedInputStream(con.getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuilder sb=new StringBuilder();

            while((line=br.readLine())!=null)
            {
                sb.append(line+"\n");
            }
            is.close();
            result=sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            JSONArray ja=new JSONArray(result);
            JSONObject jo=null;

            head=new String[ja.length()];
            body=new String[ja.length()];
            tale=new String[ja.length()];

            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                head[i]= jo.getString("head");
                body[i]= jo.getString("body");
                tale[i]= jo.getString("tail");
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initList(){
        mActivityList =new ArrayList<>();
        mActivityList.add(new NavigationItem("Image Screen 2",R.drawable.ic_image_black_24dp));
        mActivityList.add(new NavigationItem("Main Screen",R.drawable.ic_live_tv_black_24dp));
        mActivityList.add(new NavigationItem("Image Screen 1",R.drawable.ic_image_black_24dp));
        mActivityList.add(new NavigationItem("Video Screen",R.drawable.ic_video_library_black_24dp));
        mActivityList.add(new NavigationItem("Exit",R.drawable.ic_exit_to_app_black_24dp));
    }

    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifistateextra=intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,WifiManager.WIFI_STATE_UNKNOWN);

            if(wifistateextra == WifiManager.WIFI_STATE_ENABLED && counttime !=0)
            {

                Toast.makeText(imageScreen2.this, "Wifi is Enabled", Toast.LENGTH_SHORT).show();


                mHandler.postDelayed(mToast,7000);



            }

            else if(wifistateextra == WifiManager.WIFI_STATE_DISABLED)
            {
                Toast.makeText(imageScreen2.this, "Wifi is Disabled", Toast.LENGTH_SHORT).show();
                counttime++;
            }

        }
    };

    private Runnable mToastRunnable=new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this,10000);
            getImage();

            if(imagesize==imagearray+1) {
                Toast.makeText(imageScreen2.this, "Updating", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(imageScreen2.this,updating.class));
                imagearray++;
            }




        }
    };

    private Runnable mToast=new Runnable() {
        @Override
        public void run() {
            Toast.makeText(imageScreen2.this, "Wifi run", Toast.LENGTH_SHORT).show();
            wifichecker();
        }
    };

    private Runnable closeapp=new Runnable() {
        @Override
        public void run() {
            Toast.makeText(imageScreen2.this, "Wifi run", Toast.LENGTH_SHORT).show();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }
    };

    private void logout(){
        final String maC=macid;

        StringRequest stringRequest=new StringRequest(Request.Method.POST,URL_REGIST1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              /*  if( response.contains("1")){

                    Toast.makeText(getApplicationContext(),"Insertion success",Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getApplicationContext(),"cannot insert",Toast.LENGTH_LONG).show();

                }*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(Main2Activity.this,"Register  Error",Toast.LENGTH_SHORT).show();

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("maC",maC);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }











    private void wifichecker(){

        final String maC=macid;

        StringRequest stringRequest=new StringRequest(Request.Method.POST,URL_REGIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                /*if( response.contains("1")){

                    Toast.makeText(getApplicationContext(),"Insertion success",Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getApplicationContext(),"cannot insert",Toast.LENGTH_LONG).show();

                }*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(imageScreen2.this,"Register  Error",Toast.LENGTH_SHORT).show();

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("maC",maC);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }


    private void getcategory()
    {
        try {
            URL url = new URL(categoryaddress);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is=new BufferedInputStream(con.getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuilder sb=new StringBuilder();

            while((line=br.readLine())!=null)
            {
                sb.append(line+"\n");
            }
            is.close();
            result=sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            JSONArray ja=new JSONArray(result);
            JSONObject jo=null;








            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);

                Log.d(Tag,"inside if");
                mac_id=jo.getString("mac_id");
                if(mac_id.equals(macid)){
                    path = jo.getString("category");
                    statusofapp=path;

                }


            }




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
