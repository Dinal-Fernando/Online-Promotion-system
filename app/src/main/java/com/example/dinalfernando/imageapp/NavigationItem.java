package com.example.dinalfernando.imageapp;

public class NavigationItem {

    private String mActivityName;
    private int mIconImage;

    public NavigationItem(String activityName,int iconImage){
        mActivityName=activityName;
        mIconImage=iconImage;
    }

    public String getActivityName(){
        return mActivityName;
    }

    public int getIconImage(){
        return mIconImage;
    }

}
