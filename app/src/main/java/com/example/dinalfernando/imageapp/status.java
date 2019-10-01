package com.example.dinalfernando.imageapp;

import android.content.Context;

public class status {
    Context context;
    public status(Context ctx){
 context=ctx;
    }
    private static   String category="Dealer";
    public static String getcategory()
    {
        return category;
    }
    public void setCategory(String newcategory){
        category=newcategory;
    }

    private static   String macid=getmacid();
    public static String getmacid()
    {
        return macid;
    }
    public void setmacid(String newmacid){
        macid=newmacid;
    }
}
