package com.example.elisarajaniemi.podcastapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Elisa Rajaniemi on 26.10.2016.
 */

public class MenuFragment extends Fragment {


    //comment
    private ListView listView;
    private SerieArrayAdapter adapter;
    private Button closeMenu;
    private MainActivity ma;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("item1");
        list.add("item2");
        list.add("item3");
        list.add("item4");
        list.add("item5");

        View view = inflater.inflate(R.layout.menu_layout, container, false);

        ma = new MainActivity();

        listView = (ListView) view.findViewById(R.id.menuListView);
        adapter = new SerieArrayAdapter(getContext(),list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                String value = list.get(position);
                System.out.println(value);
            }

        });

        closeMenu = (Button) view.findViewById(R.id.closeMenu);
        closeMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        return view;
    }
}


