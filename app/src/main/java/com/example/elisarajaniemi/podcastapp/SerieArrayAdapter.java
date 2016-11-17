package com.example.elisarajaniemi.podcastapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Elisa Rajaniemi on 27.10.2016.
 */

public class SerieArrayAdapter extends ArrayAdapter<PodcastItem> {

    private PodcastItem value;

    public SerieArrayAdapter(Context context, ArrayList<PodcastItem> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        value = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.serie_list_item, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.serieName);
        tv.setText(value.collectionName);
        return convertView;
    }
}
