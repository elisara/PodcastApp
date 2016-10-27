package com.example.elisarajaniemi.podcastapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Elisa Rajaniemi on 26.10.2016.
 */

public class CategoryFragment extends Fragment {

    private Button okBtn;
    private SerieFragment sf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_layout, container, false);

        sf = new SerieFragment();

        okBtn = (Button) view.findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             getActivity().getSupportFragmentManager().beginTransaction()
                     .replace(R.id.frag_container, sf).commit();

            }
        });

        return view;
    }

}
