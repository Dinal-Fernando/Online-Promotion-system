package com.example.dinalfernando.imageapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TimeUtils;
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
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.Timer;


public class Main2Activity extends YouTubeBaseActivity implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener {


    private static final String Tag="MainActivity";
    YouTubePlayerView mYouTubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;


    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    String imageaddress1=" http://tv.shopwsrep.com/api/imageScreen1";
    String imageaddress2=" http://tv.shopwsrep.com/api/imageScreen2";

    String videoaddress=" http://tv.shopwsrep.com/api/video";

    String newsaddress=" http://tv.shopwsrep.com/api/news";

    String categoryaddress=" http://tv.shopwsrep.com/api/category";


    String mac_id;

    InputStream is=null;
    String line=null;
    String result=null;

    String[] image;
    String[] image2;
    String[] imagename;
    String[] imagename2;
    int imagesize;
    int imagesize2;
    int imagearray;
    int imagearray2;


    String[] video;
    String[] newvideo;
    int newvideosize;
    int videosize;
    int videoarray;

    String[] head;
    String[] body;
    String[] tale;

    String path;

    String statusofapp;
    String category;
    String category2;

    TextView textview;

    int count1;
    int count2;
    int count3;

    int delay=60000;


    SliderLayout sliderLayout,sliderLayout2 ;



    HashMap<String, String> HashMapForURL,HashMapForURL1 ;
    HashMap<String, Integer> HashMapForLocalRes ;

    private ArrayList<NavigationItem> mActivityList;
    private ItemAdapter mAdapter;

    private WifiManager wifiManager;

    private Handler mHandler=new Handler();

    String URL_REGIST=" http://tv.shopwsrep.com/api/connectionerror";
    String URL_REGIST1=" http://tv.shopwsrep.com/api/logout";

    int counttime=0;

    String macid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        macid= new getmacid(Main2Activity.this).generateID();




        initList();
        Spinner spinneritem=findViewById(R.id.spinner);
        mAdapter=new ItemAdapter(this,mActivityList);
        spinneritem.setAdapter(mAdapter);
        spinneritem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NavigationItem clickedItem=(NavigationItem)parent.getItemAtPosition(position);
                String clickedItemName=clickedItem.getActivityName();
                if(clickedItemName.equals("Image Screen 1")){
                    startActivity(new Intent(Main2Activity.this,imageScreen1.class));
                }
                if(clickedItemName.equals("Image Screen 2")){
                    startActivity(new Intent(Main2Activity.this,imageScreen2.class));
                }
                if(clickedItemName.equals("Video Screen")){
                    startActivity(new Intent(Main2Activity.this,videoScreen.class));
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

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        getcategory();


        getNews();
        getVideo();
        getImage();
        getImage2();
        AddImagesUrlOnline();



        imagearray=imagesize;
        imagearray2=imagesize2;
        videoarray=videosize;

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        Log.d(Tag,"onCreate:Starting.");
        sliderLayout = (SliderLayout)findViewById(R.id.slider);
        sliderLayout2=(SliderLayout)findViewById(R.id.slider1);
        textview=(TextView)findViewById(R.id.textview1);

        mYouTubePlayerView =(YouTubePlayerView)findViewById(R.id.youtubeplayer);//for youtube player
        youtubeplayeradvance();//for youtube player
        mYouTubePlayerView.initialize(youtubeconfig.getApikey(), mOnInitializedListener);//for youtube player

        textview.setText("  "+head[0]+" * " +body[0]+" * " +tale[0]+head[0]+" * " +body[0]+" * " +tale[0]);//for news bar

        imageslideshow1();//for the first image slider

        imageslideshow2();//for the second image slider

       // mHandler.postDelayed(mToast,1000);

        mToastRunnable.run();


    }





    private Runnable mToastRunnable=new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this,10000);
            getImage();

            if(imagesize==imagearray+1) {
                Toast.makeText(Main2Activity.this, "Updating", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Main2Activity.this,updating.class));
                imagearray++;
                                         }

                                         getImage2();

           if(imagesize2==imagearray2+1) {
                Toast.makeText(Main2Activity.this, "Updating", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Main2Activity.this,updating.class));
                imagearray2++;
            }

            getVideo();

           if(videosize==videoarray+1) {
                Toast.makeText(Main2Activity.this, "Updating", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Main2Activity.this,updating.class));
                videoarray++;
            }


            }
    };

    private Runnable mToast=new Runnable() {
        @Override
        public void run() {
            Toast.makeText(Main2Activity.this, "Wifi run", Toast.LENGTH_SHORT).show();
            wifichecker();
            }
    };

    private Runnable closeapp=new Runnable() {
        @Override
        public void run() {
            Toast.makeText(Main2Activity.this, "Wifi run", Toast.LENGTH_SHORT).show();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }
    };






    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifistateextra=intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,WifiManager.WIFI_STATE_UNKNOWN);

            if(wifistateextra == WifiManager.WIFI_STATE_ENABLED && counttime !=0)
            {

                Toast.makeText(Main2Activity.this, "Wifi is Enabled", Toast.LENGTH_SHORT).show();


                mHandler.postDelayed(mToast,7000);



            }

            else if(wifistateextra == WifiManager.WIFI_STATE_DISABLED)
            {
                Toast.makeText(Main2Activity.this, "Wifi is Disabled", Toast.LENGTH_SHORT).show();
                counttime++;
            }

        }
    };










        public void youtubeplayeradvance(){

            mOnInitializedListener=new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    Log.d(Tag,"onClick: Done Initializing.");
                    List<String> videolist=new ArrayList<>();
                    for(int i=0;i<newvideosize;i++)
                    {
                        videolist.add(newvideo[i]);
                    }
                    youTubePlayer.loadVideos(videolist);
                    //youTubePlayer.loadPlaylist("PL0HJgC5m4n-Gwk0jjyEiwUU7AWMJlXIA2");

                    // youTubePlayer.loadVideo("rUWxSEwctFU");
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    Log.d(Tag,"onClick: Failed to Initializing.");
                }
            };

        }










        public void imageslideshow1(){

            for(String name : HashMapForURL.keySet()){
                DefaultSliderView defaultSliderView = new DefaultSliderView(Main2Activity.this);
                defaultSliderView .image(HashMapForURL.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
                sliderLayout.addSlider(defaultSliderView);

            }
            sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

            sliderLayout.setCustomAnimation(new DescriptionAnimation());

            sliderLayout.setDuration(3000);

            sliderLayout.addOnPageChangeListener(Main2Activity.this);

        }












        public void imageslideshow2(){


            for(String name : HashMapForURL1.keySet()){

                TextSliderView textSliderView = new TextSliderView(Main2Activity.this);

                DefaultSliderView defaultSliderView = new DefaultSliderView(Main2Activity.this);
                defaultSliderView .image(HashMapForURL1.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
                sliderLayout2.addSlider(defaultSliderView);
            }
            sliderLayout2.setPresetTransformer(SliderLayout.Transformer.DepthPage);

            sliderLayout2.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

            sliderLayout2.setCustomAnimation(new DescriptionAnimation());

            sliderLayout2.setDuration(3000);

            sliderLayout2.addOnPageChangeListener(Main2Activity.this);

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
















    public void AddImagesUrlOnline(){

        HashMapForURL = new HashMap<String, String>();
        HashMapForURL1= new HashMap<String, String>();

        for(int i=0;i<count1;i++) {
            HashMapForURL.put(imagename[i], image[i]);
            }

        for(int j=0;j<count2;j++) {
            HashMapForURL1.put(imagename2[j], image2[j]);
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












    private void getImage2()
    {
        try {
            URL url = new URL(imageaddress2);
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

            image2=new String[ja.length()];
            imagename2=new String[ja.length()];
            imagesize2=ja.length();


            count2=0;
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                category2=jo.getString("category");
                if(category2.equals(statusofapp)){
                path = jo.getString("image_path");
                    image2[count2] = " http://tv.shopwsrep.com/" + path;
                    count2++;
                }
                    }
            count2=0;
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                category2=jo.getString("category");
                if(category2.equals(statusofapp)){
                    imagename2[count2] = jo.getString("id");
                    count2++;
                }
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }












    private void getVideo()
    {
        try {
            URL url = new URL(videoaddress);
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

            video=new String[ja.length()];
            videosize=ja.length();

            count3=0;
            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                category=jo.getString("category");
                if(category.equals(statusofapp)) {
               video[count3] = jo.getString("video_id");
                count3++;
                }
               }

            videosize=count3;
               newvideosize= 10*ja.length()*ja.length()*ja.length();
               newvideo=new String[newvideosize];

               for(int j=0;j<newvideosize;j++)
               {
                   for(int k=0;k<videosize;k++){
                              newvideo[j]= video[k];
                              j++;
                   }
                      j--;
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
        mActivityList.add(new NavigationItem("Main Screen",R.drawable.ic_live_tv_black_24dp));
        mActivityList.add(new NavigationItem("Image Screen 1",R.drawable.ic_image_black_24dp));
        mActivityList.add(new NavigationItem("Image Screen 2",R.drawable.ic_image_black_24dp));
        mActivityList.add(new NavigationItem("Video Screen",R.drawable.ic_video_library_black_24dp));
        mActivityList.add(new NavigationItem("Exit",R.drawable.ic_exit_to_app_black_24dp));
        }













    @Override
    public void onSliderClick(BaseSliderView slider) {

        logout();

        mHandler.postDelayed(closeapp,1000);

        //Toast.makeText(this,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Main2Activity.this,"Register  Error",Toast.LENGTH_SHORT).show();

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
