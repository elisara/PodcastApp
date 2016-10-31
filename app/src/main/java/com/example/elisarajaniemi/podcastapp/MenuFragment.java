package com.example.elisarajaniemi.podcastapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Elisa Rajaniemi on 26.10.2016.
 */

public class MenuFragment extends Fragment implements View.OnClickListener {

    private MainActivity ma;
    private FrameLayout fl;
    private TextView playList, favorite, queue, history, continuePlay, signIn;
    private PlaylistsFragment plf;
    private SinglePlaylistFragment splf;
    private MenuFragment mf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.menu_layout, container, false);

        playList = (TextView) view.findViewById(R.id.playlists);
        favorite = (TextView) view.findViewById(R.id.favorites);
        queue = (TextView) view.findViewById(R.id.queue);
        history = (TextView) view.findViewById(R.id.history);
        continuePlay = (TextView) view.findViewById(R.id.continuePlaying);
        signIn = (TextView) view.findViewById(R.id.signIn);
        fl = (FrameLayout) view.findViewById(R.id.outside);


        playList.setOnClickListener(this);
        favorite.setOnClickListener(this);
        queue.setOnClickListener(this);
        history.setOnClickListener(this);
        continuePlay.setOnClickListener(this);
        signIn.setOnClickListener(this);
        fl.setOnClickListener(this);

        plf = new PlaylistsFragment();
        splf = new SinglePlaylistFragment();
        mf = new MenuFragment();

        return view;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.playlists:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, plf).addToBackStack( "tag" ).commit();
                break;

            case R.id.queue:
                System.out.println("QUEUE");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, splf).addToBackStack( "tag" ).commit();
                break;

            case R.id.history:
                System.out.println("HISTORY");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, splf).addToBackStack( "tag" ).commit();
                break;

            case R.id.continuePlaying:
                System.out.println("CONTINUE");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, splf).addToBackStack( "tag" ).commit();
                break;

            case R.id.favorites:
                System.out.println("FAVS");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, splf).addToBackStack( "tag" ).commit();
                break;

            case R.id.signIn:
                System.out.println("SIGNIN");
                Dialog dialog = new Dialog(getActivity());
                dialog.setTitle("Custom Dialog");
                dialog.show();

                break;

            case R.id.outside:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                break;
        }

    }
}


