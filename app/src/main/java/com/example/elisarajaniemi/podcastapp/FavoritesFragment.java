package com.example.elisarajaniemi.podcastapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by Elisa Rajaniemi on 24.11.2016.
 */

public class FavoritesFragment extends Fragment {

    GridView gridView;
    PodcastItems podcastItems = PodcastItems.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorites_layout, container, false);

        gridView = (GridView) view.findViewById(R.id.gridView1);

        ArrayList<PodcastItem> list = podcastItems.getItems();

        gridView.setAdapter(new GridViewAdapter(getContext(), list));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getContext(), ((TextView) v.findViewById(R.id.grid_item_label)).getText(), Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

}

