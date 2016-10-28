package com.example.elisarajaniemi.podcastapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Kade on 28.10.2016.
 */

public class PlayerFragment extends Fragment {
    private ImageView playButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.play_screen, container, false);

        playButton = (ImageView) view.findViewById(R.id.playBtn);

        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Painettu");
            }
        });


        return view;

    }
}
