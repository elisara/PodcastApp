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
    public ArrayList<PodcastItem> list;
    private PodcastItem pi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pi = (PodcastItem) getArguments().getSerializable("message");
        View view = inflater.inflate(R.layout.single_playlist_layout, container, false);
        httpGetHelper = new HttpGetHelper();

        PodcastItems.getInstance().getEpisodes().clear();

        System.out.println("List size in ef: " + PodcastItems.getInstance().getEpisodes().size());

            for(int i = 0; i < PodcastItems.getInstance().getItems().size(); i++) {
                if (PodcastItems.getInstance().getItems().get(i).collectionName.equals(pi.collectionName)) {
                    PodcastItems.getInstance().addEpisodes(PodcastItems.getInstance().getItems().get(i));
                }
            }



        listView = (ListView) view.findViewById(R.id.single_playlist_list);
        adapter = new EpisodeListArrayAdapter(getContext(), PodcastItems.getInstance().getEpisodes());
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
