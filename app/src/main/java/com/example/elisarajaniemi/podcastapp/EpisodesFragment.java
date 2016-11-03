package com.example.elisarajaniemi.podcastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Elisa Rajaniemi on 31.10.2016.
 */

public class EpisodesFragment extends Fragment {

    private ListView listView;
    private EpisodeListArrayAdapter adapter;
    private HttpGetHelper httpGetHelper;
    private String message;
    private ArrayList<PodcastItem> list, listAll;
    private PodcastItem pi;
    private MainActivity ma;
    private TextView collectionName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pi = (PodcastItem) getArguments().getSerializable("message");
        View view = inflater.inflate(R.layout.single_playlist_layout, container, false);
        httpGetHelper = new HttpGetHelper();

        collectionName = (TextView) view.findViewById(R.id.collectionTitle);
        collectionName.setText(pi.collectionName);

        list = new ArrayList<>();
        listAll = new ArrayList<>();

        if(list != null || listAll != null){
            list.clear();
            listAll.clear();
        }
        System.out.println("List size before FOR: " + list.size());
        listAll = PodcastItems.getInstance().getItems();
        System.out.println("List size before FOR: " + PodcastItems.getInstance().getItems().size());

        //list.clear();

        if(list.size() == 0) {
            for (int i = 0; i < listAll.size(); i++) {
                if (listAll.get(i).collectionName.equals(pi.collectionName) && !list.contains(listAll.get(i))) {
                    list.add(listAll.get(i));
                }
            }
        }

        System.out.println("List size AFTER: " + list.size());

        listView = (ListView) view.findViewById(R.id.single_playlist_list);
        adapter = new EpisodeListArrayAdapter(getContext(), this.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                PodcastItem pi = PodcastItems.getInstance().getItems().get(position);
                Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
                intent.putExtra("episode", pi);
                getActivity().startActivity(intent);
                PodcastItems.getInstance().clearItems();

            }

        });

        PodcastItems.getInstance().clearItems();
        //list.clear();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
