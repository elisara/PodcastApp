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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Elisa Rajaniemi on 27.10.2016.
 */

public class SerieFragment extends Fragment implements AdapterView.OnItemSelectedListener, Serializable {

    private ListView listView;
    private SerieArrayAdapter adapter;
    private EpisodeListArrayAdapter episodeAdapter;
    private ImageButton menuBtn;
    private Spinner spinner;
    private Button categoryBtn;
    private boolean categoryOpen;
    private PlayerFragment pf;
    private String apiKey;
    private boolean humor, techonology, health, economy, all;
    private EpisodesFragment ef;
    private HttpGetHelper httpGetHelper;
    private boolean itemsAdded;
    private MainActivity ma;
    private ArrayList<PodcastItem> list, list2;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //httpGetHelper = new HttpGetHelper();

        View view = inflater.inflate(R.layout.serie_layout, container, false);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        addItemsOnSpinner();

        //pf = new PlayerFragment();

        categoryBtn = (Button) view.findViewById(R.id.categoryBtn);
        categoryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MyPreferencesActivity.class);
                startActivity(i);
                categoryOpen = true;

            }
        });

        list = new ArrayList<PodcastItem>();
        list2 = new ArrayList<PodcastItem>();


        listView = (ListView) view.findViewById(R.id.serieList);
        adapter = new SerieArrayAdapter(getContext(), SerieItems.getInstance().getSerieItems());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                PodcastItem pi = SerieItems.getInstance().getSerieItems().get(position);
                //Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
                //intent.putExtra("message", pi);
                //getActivity().startActivity(intent);
                ef = new EpisodesFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("message", pi);
                ef.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("sf")
                        .replace(R.id.frag_container, ef).commit();

            }

        });
        return view;
    }

    public void addItemsOnSpinner() {
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sort_array, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = parent.getItemAtPosition(position).toString();
        //list.clear();
        if (value.contains("NAME")) {
            list = SerieItems.getInstance().getSerieItems();
            Collections.sort(list, new Comparator<PodcastItem>(){
                public int compare(PodcastItem pod1, PodcastItem pod2) {
                    return pod1.collectionName.compareToIgnoreCase(pod2.collectionName); // To compare string values
                }
            });
            adapter = new SerieArrayAdapter(getContext(), list);
            listView.setAdapter(adapter);
            System.out.println("SORT: NAME");

        } else if (value.contains("NEW")) {
            list2 = SerieItems.getInstance().getSerieItems();
            Collections.sort(list2, new Comparator<PodcastItem>(){
                public int compare(PodcastItem pod1, PodcastItem pod2) {
                    return Integer.valueOf(pod2.collectionID).compareTo(pod1.collectionID);
                }
            });
            adapter = new SerieArrayAdapter(getContext(), list2);
            listView.setAdapter(adapter);
            System.out.println("SORT: NEW");
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void refreshLists(){
        episodeAdapter = new EpisodeListArrayAdapter(getContext() ,PodcastItems.getInstance().getItems());
        listView.setAdapter(episodeAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                PodcastItem pi = PodcastItems.getInstance().getItems().get(position);
                //Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
                //intent.putExtra("message", pi);
                //getActivity().startActivity(intent);
                pf = new PlayerFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("message", pi);
                pf.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("sf")
                        .replace(R.id.frag_container, pf).commit();

            }

        });
        System.out.println("Searchin jälkeen: " + SerieItems.getInstance().getSerieItems());
    }

    @Override
    public void onResume() {
        super.onResume();

        //TÄMÄ KESKEN
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        humor = prefs.getBoolean("humor", true);
        techonology = prefs.getBoolean("technology", true);
        economy = prefs.getBoolean("economy", true);
        health = prefs.getBoolean("health", true);
        all = prefs.getBoolean("all", true);

        System.out.println("Categories: humor: "+humor+ " techonoly: "+techonology+ " economy: "+ economy+ " health: "+health + " all: "+all);
        list.clear();

        for(int i = 0; i < SerieItems.getInstance().getSerieItems().size(); i++){
            if(all == false) {
                if (SerieItems.getInstance().getSerieItems().get(i).tags.toLowerCase().contains("category:huumori") && humor == true) {
                    list.add(SerieItems.getInstance().getSerieItems().get(i));
                } else if (SerieItems.getInstance().getSerieItems().get(i).tags.toLowerCase().contains("category:technology") && techonology == true) {
                    list.add(SerieItems.getInstance().getSerieItems().get(i));
                } else if (SerieItems.getInstance().getSerieItems().get(i).tags.toLowerCase().contains("category:economy") && economy == true) {
                    list.add(SerieItems.getInstance().getSerieItems().get(i));
                } else if (SerieItems.getInstance().getSerieItems().get(i).tags.toLowerCase().contains("category:terveys") && health == true) {
                    list.add(SerieItems.getInstance().getSerieItems().get(i));
                }
            }
            else{
                list = SerieItems.getInstance().getSerieItems();
            }
        }

        System.out.println("LIST in resume: "+list.size());
        adapter = new SerieArrayAdapter(getContext(), list);
        listView.setAdapter(adapter);
        System.out.println("CATEGORY: humor");

        System.out.println("Humor in series onCreateView: " + humor);

    }
}
