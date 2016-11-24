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
    //private PodcastItem value;
    View gridView;

    public GridViewAdapter(Context context, ArrayList<PodcastItem> list) {
        this.context = context;
        this.list = list;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridView = new View(context);
            gridView = inflater.inflate(R.layout.gridview_item, parent, false);

            if (convertView == null) {

                // set value into textview
                TextView textView = (TextView) gridView.findViewById(R.id.grid_item_label);
                textView.setText(list.get(position).title);

                //ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);

            } else {
                gridView = (View)convertView;
            }
        }

        return gridView;
    }

    @Override public int getCount () {
        return list.size();
    }


    @Override public Object getItem ( int position){
        return null;
    }

    @Override public long getItemId ( int position){
        return 0;
    }
}