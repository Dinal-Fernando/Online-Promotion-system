package com.example.dinalfernando.imageapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

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
import java.util.List;

public class videoScreen extends YouTubeBaseActivity {

    private static final String Tag="MainActivity";
    YouTubePlayerView mYouTubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;





    String videoaddress=" http://tv.shopwsrep.com/api/";

    String newsaddress=" http://tv.shopwsrep.com/api/";

    InputStream is=null;
    String line=null;
    String result=null;



    String[] video;
    String[] newvideo;
    int newvideosize;
    int videosize;

    String[] head;
    String[] body;
    String[] tale;



    String statusofapp="Dealer";
    String category;

    TextView textview;


    int count3;

    private Handler mHandler=new Handler();




    private ArrayList<NavigationItem> mActivityList;
    private ItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_screen);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        getNews();
        getVideo();

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
                    startActivity(new Intent(videoScreen.this,Main2Activity.class));
                }
                if(clickedItemName.equals("Image Screen 1")){
                    startActivity(new Intent(videoScreen.this,imageScreen1.class));
                }
                if(clickedItemName.equals("Image Screen 2")){
                    startActivity(new Intent(videoScreen.this,imageScreen2.class));
                }
                if(clickedItemName.equals("Exit")){
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mYouTubePlayerView =(YouTubePlayerView)findViewById(R.id.youtubeplayer);

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


        mYouTubePlayerView.initialize(youtubeconfig.getApikey(), mOnInitializedListener);




        textview=(TextView)findViewById(R.id.textview1);
        textview.setText(head[0]+" * " +body[0]+" * " +tale[0]+head[0]+" * " +body[0]+" * " +tale[0]);
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
        mActivityList.add(new NavigationItem("Video Screen",R.drawable.ic_video_library_black_24dp));
        mActivityList.add(new NavigationItem("Main Screen",R.drawable.ic_live_tv_black_24dp));
        mActivityList.add(new NavigationItem("Image Screen 1",R.drawable.ic_image_black_24dp));
        mActivityList.add(new NavigationItem("Image Screen 2",R.drawable.ic_image_black_24dp));
        mActivityList.add(new NavigationItem("Exit",R.drawable.ic_exit_to_app_black_24dp));
    }

}
