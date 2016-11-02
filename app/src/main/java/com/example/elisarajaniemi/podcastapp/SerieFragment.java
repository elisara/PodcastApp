package com.example.elisarajaniemi.podcastapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
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
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Elisa Rajaniemi on 27.10.2016.
 */

public class SerieFragment extends Fragment implements AdapterView.OnItemSelectedListener {

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
    private SingleSerieFragment ssf;
    private HttpGetHelper httpGetHelper;
    private boolean itemsAdded;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Thread t = new Thread(r);
        //t.start();

        itemsAdded = false;

        httpGetHelper = new HttpGetHelper();

        //new HttpGetHelper().execute("http://dev.mw.metropolia.fi/aanimaisema/plugins/api_auth/auth.php");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        history = prefs.getBoolean("history", true);
        System.out.println("History in series onCreateView: " + history);


        /**final ArrayList<String> list = new ArrayList<>();

        list.add("kissat");
        list.add("koira");
        list.add("item2");
        list.add("item3");
        list.add("item4");
        list.add("item5");
        System.out.println("Array size SerieFragment: " + httpGetHelper.getResults().size());
        for (int i = 0; i < httpGetHelper.getResults().size(); i++) {
            list.add(httpGetHelper.getResults().get(i).title);
            System.out.println(httpGetHelper.executed);
        }*/
        //itemsAdded = true;
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

        listView = (ListView) view.findViewById(R.id.serieList);
        adapter = new SerieArrayAdapter(getContext(), httpGetHelper.getResults());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                String value = httpGetHelper.getResults().get(position).title;
                System.out.println(value);
                ssf = new SingleSerieFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, ssf).addToBackStack("tag").commit();
            }

        });

        System.out.println("PodcastItems: " + httpGetHelper.podcastItems);
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
