package com.example.elisarajaniemi.podcastapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Elisa Rajaniemi on 9.11.2016.
 */

public class PlaylistsArrayAdapter extends ArrayAdapter<PlaylistItem> {

    PlaylistItem value;

    public PlaylistsArrayAdapter(Context context, ArrayList<PlaylistItem> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        value = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.episode_list_item, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.episodeName);
        title.setText(value.name);

        return convertView;
    }
}