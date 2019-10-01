package com.example.dinalfernando.imageapp;

import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class getmacid extends AppCompatActivity {


    Context context;
    public getmacid(){}
    public getmacid(Context ctx){ context=ctx;}

    public String generateID() {

        String deviceId = Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        if(deviceId==null){

            // deviceId= ((TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            Random tmpRand=new Random();
            deviceId= String.valueOf(tmpRand.nextLong());
        }
        return getHash(deviceId);
    }

    private String getHash(String stringToHash) {

        MessageDigest digest=null;

        try {
            digest=MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] result=null;

        try {
            result= digest.digest(stringToHash.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder sb=new StringBuilder();

        for(byte b: result){
            sb.append(String.format("%02X",b));
        }

        String messageDigest= sb.toString();

        return messageDigest;
    }

}
