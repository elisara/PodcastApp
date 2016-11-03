package com.example.elisarajaniemi.podcastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Elisa Rajaniemi on 31.10.2016.
 */

public class EpisodesFragment extends Fragment {

    private ListView listView;
    private EpisodeListArrayAdapter adapter;
    private HttpGetHelper httpGetHelper;
    private String message;
    private ArrayList<PodcastItem> allList, list;
    private PodcastItem pi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pi = (PodcastItem) getArguments().getSerializable("message");
        View view = inflater.inflate(R.layout.single_playlist_layout, container, false);
        httpGetHelper = new HttpGetHelper();

        list = new ArrayList<>();
        allList = new ArrayList<>();
        list.clear();
        allList.clear();
        allList = PodcastItems.getInstance().getItems();
        System.out.println("AllList: " + allList.get(0).title);

        for(int i = 0; i < allList.size(); i++){
            if(allList.get(i).collectionName.equals(pi.collectionName)){
                this.list.add(allList.get(i));
            }

        }

        listView = (ListView) view.findViewById(R.id.single_playlist_list);
        adapter = new EpisodeListArrayAdapter(getContext(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {

                PodcastItem pi = PodcastItems.getInstance().getItems().get(position);

                Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
                intent.putExtra("episode", pi);
                getActivity().startActivity(intent);

            }

        });

        return view;
    }

}
