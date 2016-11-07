package com.example.elisarajaniemi.podcastapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Elisa Rajaniemi on 27.10.2016.
 */

public class EpisodeListArrayAdapter extends ArrayAdapter<PodcastItem> {

    PodcastItem value;

    public EpisodeListArrayAdapter(Context context, ArrayList<PodcastItem> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        value = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.episode_list_item, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.episodeName);
        tv.setText(value.title);
        return convertView;
    }


}
