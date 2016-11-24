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
    private ArrayList<PodcastItem> categoryList;
    private ArrayList<Boolean> prefCategoryList;
    private String[] backendCategories;
    private String sortValue;
    private SharedPreferences prefs;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.serie_layout, container, false);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        addItemsOnSpinner();
        categoryList = new ArrayList<>();
        prefCategoryList = new ArrayList<>();
        sortValue = "";

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefs.edit().putBoolean("all", true);

        categoryBtn = (Button) view.findViewById(R.id.categoryBtn);
        categoryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MyPreferencesActivity.class);
                startActivity(i);
                categoryOpen = true;
            }
        });

        listView = (ListView) view.findViewById(R.id.serieList);
        categoryList = getListByCategories();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                PodcastItem pi = categoryList.get(position);
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



    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void addItemsOnSpinner() {
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sort_array, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = parent.getItemAtPosition(position).toString();
        //categoryList = getListByCategories();
        if (value.contains("NAME")) {

            Collections.sort(categoryList, new Comparator<PodcastItem>() {
                public int compare(PodcastItem pod1, PodcastItem pod2) {
                    return pod1.collectionName.compareToIgnoreCase(pod2.collectionName); // To compare string values
                }
            });
            adapter = new SerieArrayAdapter(getContext(), categoryList);
            System.out.println("SORT: NAME");

        } else if (value.contains("NEW")) {
            Collections.sort(categoryList, new Comparator<PodcastItem>() {
                public int compare(PodcastItem pod1, PodcastItem pod2) {
                    return Integer.valueOf(pod2.collectionID).compareTo(pod1.collectionID);
                }
            });

            adapter = new SerieArrayAdapter(getContext(), categoryList);
            System.out.println("SORT: NEW");
        }
        sortValue = value;
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        categoryList = getListByCategories();
        adapter.notifyDataSetChanged();

    }

    public ArrayList<PodcastItem> getListByCategories() {
        if (categoryList.size() != 0) {
            categoryList.clear();
        }
        if(prefCategoryList.size() != 0){
            prefCategoryList.clear();
        }
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

        prefCategoryList.add(humor);
        prefCategoryList.add(technology);
        prefCategoryList.add(economy);
        prefCategoryList.add(health);
        prefCategoryList.add(politics);
        prefCategoryList.add(nature);
        prefCategoryList.add(music);
        prefCategoryList.add(entertainment);
        prefCategoryList.add(history);

        backendCategories = new String[] {"category:huumori", "category:technology", "category:economy",
                "category:terveys", "category:politics", "category:nature",
                "category:music", "category:entertainment", "category:history"};

        for (int i = 0; i < PodcastItems.getInstance().getItems().size(); i++) {
            for(int u = 0; u < backendCategories.length; u++) {
                getCategory(backendCategories[u], prefCategoryList.get(u), i);
            }
            if (all == true) {
                if (testIfListContains(categoryList, PodcastItems.getInstance().getItems().get(i)) == false) {
                    categoryList.add(0,PodcastItems.getInstance().getItems().get(i));
                }
            }
        }
        if(sortValue.contains("NAME")){
            Collections.sort(categoryList, new Comparator<PodcastItem>() {
                public int compare(PodcastItem pod1, PodcastItem pod2) {
                    return pod1.collectionName.compareToIgnoreCase(pod2.collectionName); // To compare string values
                }
            });
        }
        else if(sortValue.contains("NEW")){
            Collections.sort(categoryList, new Comparator<PodcastItem>() {
                public int compare(PodcastItem pod1, PodcastItem pod2) {
                    return Integer.valueOf(pod2.collectionID).compareTo(pod1.collectionID);
                }
            });
        }

        adapter = new SerieArrayAdapter(getContext(), categoryList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        return categoryList;

    }

    public void getCategory(String categoryInBackend, Boolean prefCategory, int i) {
        if (PodcastItems.getInstance().getItems().get(i).tags.toLowerCase().contains(categoryInBackend) && prefCategory == true) {
            if (testIfListContains(categoryList, PodcastItems.getInstance().getItems().get(i)) == false) {
                categoryList.add(PodcastItems.getInstance().getItems().get(i));
            }
        }
    }

    public boolean testIfListContains(ArrayList<PodcastItem> testList, PodcastItem podcastItem) {
        boolean listContains = false;
        for (int i = 0; i < testList.size(); i++) {
            if (testList.get(i).collectionName.equalsIgnoreCase(podcastItem.collectionName)) {
                listContains = true;
            }
        }
        return listContains;
    }


}
