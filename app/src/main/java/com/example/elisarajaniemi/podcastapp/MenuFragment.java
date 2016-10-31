package com.example.elisarajaniemi.podcastapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Elisa Rajaniemi on 26.10.2016.
 */

public class MenuFragment extends Fragment {


    //comment
    private MainActivity ma;
    private FrameLayout fl;
    private TextView playList;
    private PlaylistsFragment plf;
    private MenuFragment mf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.menu_layout, container, false);

        playList = (TextView) view.findViewById(R.id.playlists);
        playList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                plf = new PlaylistsFragment();
                mf = new MenuFragment();
                System.out.println("playlist");


                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(MenuFragment.this).commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                       .replace(R.id.frag_container, plf).addToBackStack( "tag" ).commit();


            }
        });


        fl = (FrameLayout) view.findViewById(R.id.outside);
        fl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("outside");

                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(MenuFragment.this).commit();
            }
        });

        return view;
    }
}


