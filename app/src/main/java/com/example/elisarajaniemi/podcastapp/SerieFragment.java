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
    private boolean humor, technology, health, economy, all, music, nature, politics, entertainment, history;
    private EpisodesFragment ef;
    private HttpGetHelper httpGetHelper;
    private boolean itemsAdded;
    private MainActivity ma;
    private ArrayList<PodcastItem> list;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.serie_layout, container, false);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        addItemsOnSpinner();

        categoryBtn = (Button) view.findViewById(R.id.categoryBtn);
        categoryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MyPreferencesActivity.class);
                startActivity(i);
                categoryOpen = true;
            }
        });


        listView = (ListView) view.findViewById(R.id.serieList);
        getListByCategories();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                PodcastItem pi = SerieItems.getInstance().getSerieItems().get(position);
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
        if (value.contains("NAME")) {
            list = getListByCategories();
            Collections.sort(list, new Comparator<PodcastItem>(){
                public int compare(PodcastItem pod1, PodcastItem pod2) {
                    return pod1.collectionName.compareToIgnoreCase(pod2.collectionName); // To compare string values
                }
            });
            adapter = new SerieArrayAdapter(getContext(), list);
            System.out.println("SORT: NAME");

        } else if (value.contains("NEW")) {
            list = getListByCategories();
            Collections.sort(list, new Comparator<PodcastItem>(){
                public int compare(PodcastItem pod1, PodcastItem pod2) {
                    return Integer.valueOf(pod2.collectionID).compareTo(pod1.collectionID);
                }
            });
            adapter = new SerieArrayAdapter(getContext(), list);
            System.out.println("SORT: NEW");
        }
        listView.setAdapter(adapter);
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onResume() {
        super.onResume();
        getListByCategories();
        adapter.notifyDataSetChanged();

    }

    public boolean testIfListContains(ArrayList<PodcastItem> testList, PodcastItem podcastItem){
        boolean listContains = false;
        for (int i = 0; i < testList.size(); i++){
            if(testList.get(i).collectionName.equalsIgnoreCase(podcastItem.collectionName)){
                listContains = true;
            }
        }
        return listContains;
    }

    public ArrayList<PodcastItem> getListByCategories(){
        ArrayList<PodcastItem> categoryList = new ArrayList<>();
        if(categoryList.size()!=0){
            categoryList.clear();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        humor = prefs.getBoolean("humor", true);
        technology = prefs.getBoolean("technology", true);
        economy = prefs.getBoolean("economy", true);
        health = prefs.getBoolean("health", true);
        politics = prefs.getBoolean("politics", true);
        nature = prefs.getBoolean("nature", true);
        music = prefs.getBoolean("music", true);
        entertainment = prefs.getBoolean("entertainment", true);
        history = prefs.getBoolean("history", true);
        all = prefs.getBoolean("all", true);

        System.out.println("Categories: humor:"+humor+ " technology:"+technology+ " economy:"+ economy+ " health:"+health + " all:"+all);

        for(int i = 0; i < PodcastItems.getInstance().getItems().size(); i++) {
            if (PodcastItems.getInstance().getItems().get(i).tags.toLowerCase().contains("category:huumori") && humor == true) {
                if (testIfListContains(categoryList, PodcastItems.getInstance().getItems().get(i)) == false) {
                    categoryList.add(PodcastItems.getInstance().getItems().get(i));
                }
            } else if (PodcastItems.getInstance().getItems().get(i).tags.toLowerCase().contains("category:technology") && technology == true) {
                if (testIfListContains(categoryList, PodcastItems.getInstance().getItems().get(i)) == false) {
                    categoryList.add(PodcastItems.getInstance().getItems().get(i));
                }
            } else if (PodcastItems.getInstance().getItems().get(i).tags.toLowerCase().contains("category:economy") && economy == true) {
                if (testIfListContains(categoryList, PodcastItems.getInstance().getItems().get(i)) == false) {
                    categoryList.add(PodcastItems.getInstance().getItems().get(i));
                }
            } else if (PodcastItems.getInstance().getItems().get(i).tags.toLowerCase().contains("category:terveys") && health == true) {
                if (testIfListContains(categoryList, PodcastItems.getInstance().getItems().get(i)) == false) {
                    categoryList.add(PodcastItems.getInstance().getItems().get(i));
                }
            } else if (PodcastItems.getInstance().getItems().get(i).tags.toLowerCase().contains("category:politics") && politics == true) {
                if (testIfListContains(categoryList, PodcastItems.getInstance().getItems().get(i)) == false) {
                    categoryList.add(PodcastItems.getInstance().getItems().get(i));
                }
            } else if (PodcastItems.getInstance().getItems().get(i).tags.toLowerCase().contains("category:nature") && nature == true) {
                if (testIfListContains(categoryList, PodcastItems.getInstance().getItems().get(i)) == false) {
                    categoryList.add(PodcastItems.getInstance().getItems().get(i));
                }
            } else if (PodcastItems.getInstance().getItems().get(i).tags.toLowerCase().contains("category:music") && music == true) {
                if (testIfListContains(categoryList, PodcastItems.getInstance().getItems().get(i)) == false) {
                    categoryList.add(PodcastItems.getInstance().getItems().get(i));
                }
            } else if (PodcastItems.getInstance().getItems().get(i).tags.toLowerCase().contains("category:history") && history == true) {
                if (testIfListContains(categoryList, PodcastItems.getInstance().getItems().get(i)) == false) {
                    categoryList.add(PodcastItems.getInstance().getItems().get(i));
                }
            } else if (PodcastItems.getInstance().getItems().get(i).tags.toLowerCase().contains("category:entertainment") && entertainment == true) {
                if (testIfListContains(categoryList, PodcastItems.getInstance().getItems().get(i)) == false) {
                    categoryList.add(PodcastItems.getInstance().getItems().get(i));
                }
            }
            else if (all == true) {
                if (testIfListContains(categoryList, PodcastItems.getInstance().getItems().get(i)) == false) {
                    categoryList.add(PodcastItems.getInstance().getItems().get(i));
                }
            }

        }
        adapter = new SerieArrayAdapter(getContext(), categoryList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        return categoryList;

    }
}
