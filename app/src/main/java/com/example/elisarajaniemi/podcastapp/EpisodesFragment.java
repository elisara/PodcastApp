package com.example.elisarajaniemi.podcastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    AlertDialog alertDialog;
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int position, long rowId) {

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Description");

                LinearLayout lp = new LinearLayout(getContext());
                lp.setOrientation(LinearLayout.VERTICAL);
                lp.setPadding(30,30,30,30);

                final TextView description = new TextView(getActivity());
                description.setText(list.get(position).description);
                description.setMovementMethod(new ScrollingMovementMethod());
                lp.addView(description);

                alertDialogBuilder.setView(lp);

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
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
