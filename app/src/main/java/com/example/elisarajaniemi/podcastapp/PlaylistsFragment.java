package com.example.elisarajaniemi.podcastapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Elisa Rajaniemi on 31.10.2016.
 */

public class PlaylistsFragment extends Fragment {


    private ListView listView;
    private SerieArrayAdapter adapter;
    private MenuFragment mf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlist_layout, container, false);

        mf = new MenuFragment();

        final ArrayList<String> list = new ArrayList<>();
        list.add("playlist1");
        list.add("playlist2");
        list.add("playlist3");
        list.add("playlist4");

        listView = (ListView) view.findViewById(R.id.playlist_list);
        adapter = new SerieArrayAdapter(getContext(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                String value = list.get(position);
                System.out.println(value);

                //getActivity().getSupportFragmentManager().beginTransaction()
                 //       .replace(R.id.frag_container, pf).commit();
            }

        });


        return view;
    }
}