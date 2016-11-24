package com.example.elisarajaniemi.podcastapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Elisa Rajaniemi on 24.11.2016.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PodcastItem> list;

    public GridViewAdapter(Context context, ArrayList<PodcastItem> list) {
        this.context = context;
        this.list = list;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View myView;
        if (convertView == null) {
            //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myView = layoutInflater.inflate(R.layout.gridview_item, parent, false);

            TextView textView = (TextView) myView.findViewById(R.id.grid_item_label);
            textView.setText(list.get(position).title);

        } else {
            myView = convertView;
        }

        return myView;
    }

    @Override public int getCount () {
        return list.size();
    }


    @Override public Object getItem ( int position){
        return position;
    }

    @Override public long getItemId ( int position){
        return position;
    }
}