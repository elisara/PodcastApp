package com.example.elisarajaniemi.podcastapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Elisa Rajaniemi on 27.10.2016.
 */

public class SerieFragment extends Fragment implements AdapterView.OnItemSelectedListener, Serializable {

    private ListView listView;
    private SerieArrayAdapter adapter;
    private ImageButton menuBtn;
    private Spinner spinner;
    private Button categoryBtn;
    private boolean categoryOpen;
    private CategoryFragment cf;
    private PlayerFragment pf;
    private String apiKey;
    public boolean history;
    private EpisodesFragment ef;
    private HttpGetHelper httpGetHelper;
    private boolean itemsAdded;
    private MainActivity ma;
    private ArrayList<PodcastItem> list;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        httpGetHelper = new HttpGetHelper();

        //Sorting stuff
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        history = prefs.getBoolean("history", true);
        System.out.println("History in series onCreateView: " + history);

        View view = inflater.inflate(R.layout.serie_layout, container, false);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        addItemsOnSpinner();

        cf = new CategoryFragment();
        pf = new PlayerFragment();

        categoryBtn = (Button) view.findViewById(R.id.categoryBtn);
        categoryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MyPreferencesActivity.class);
                startActivity(i);
                categoryOpen = true;

            }
        });

        list = PodcastItems.getInstance().getItems();

        listView = (ListView) view.findViewById(R.id.serieList);
        adapter = new SerieArrayAdapter(getContext(), SerieItems.getInstance().getSerieItems());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                PodcastItem pi = SerieItems.getInstance().getSerieItems().get(position);
                Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
                intent.putExtra("message", pi);
                getActivity().startActivity(intent);

            }

        });
        return view;
    }

    public void addItemsOnSpinner() {
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sort_array, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        System.out.println("added items to spinner");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = parent.getItemAtPosition(position).toString();
        if (value.contains("NAME")) {
            System.out.println("SORT: NAME");
        } else if (value.contains("NEW")) {
            System.out.println("SORT: NEW");
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
