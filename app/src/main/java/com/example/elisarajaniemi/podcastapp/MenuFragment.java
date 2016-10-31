package com.example.elisarajaniemi.podcastapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Elisa Rajaniemi on 26.10.2016.
 */

public class MenuFragment extends Fragment {


    //comment
    private ListView listView;
    private SerieArrayAdapter adapter;
    private Button closeMenu;
    private MainActivity ma;
    private FrameLayout fl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.menu_layout, container, false);

        fl = (FrameLayout) view.findViewById(R.id.outside);
        fl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("outside");
            }
        });

        return view;
    }
}


