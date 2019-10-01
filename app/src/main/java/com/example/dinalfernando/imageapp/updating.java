package com.example.dinalfernando.imageapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class updating extends AppCompatActivity {
    private Handler mHandler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updating);

        mHandler.postDelayed(mToastRunnable,3000);
    }

    private Runnable mToastRunnable=new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(updating.this,Main2Activity.class));

        }
    };

}
