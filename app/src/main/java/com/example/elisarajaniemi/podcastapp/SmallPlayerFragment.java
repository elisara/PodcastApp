package com.example.elisarajaniemi.podcastapp;

import android.media.Image;import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Keni on 2016-11-16.
 */

public class SmallPlayerFragment extends Fragment {
    private ImageView podcastImage, button1, button2;
    private TextView text1, text2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification, container, false);

        podcastImage = (ImageView) view.findViewById(R.id.notifiationImage);
        button1 = (ImageView) view.findViewById(R.id.notificationPlayBtn);
        button2 = (ImageView) view.findViewById(R.id.notificationSkipBtn);

        text1 = (TextView) view.findViewById(R.id.notifiationText1);
        text1 = (TextView) view.findViewById(R.id.notifiationText1);

        podcastImage.setImageResource(R.drawable.podcast_headphones);
        button1.setImageResource(R.drawable.ic_play_arrow_black_50dp);
        button2.setImageResource(R.drawable.ic_skip_next_black_50dp);



        return view;
    }
}
