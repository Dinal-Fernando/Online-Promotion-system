package com.example.dinalfernando.imageapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<NavigationItem> {

    public ItemAdapter(Context context, ArrayList<NavigationItem> countryList){
        super(context,0, countryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    private View initView(int position,View convertView,ViewGroup parent){
        if(convertView == null){
            convertView=LayoutInflater.from(getContext()).inflate(R.layout.list_spinner_row,parent,false);
        }

        ImageView imageViewFlag=convertView.findViewById(R.id.image_view);
        TextView textViewName=convertView.findViewById(R.id.text_view);

        NavigationItem currentItem=getItem(position);

        if(currentItem!=null){
            imageViewFlag.setImageResource(currentItem.getIconImage());
            textViewName.setText(currentItem.getActivityName());}
        return convertView;
    }

}
