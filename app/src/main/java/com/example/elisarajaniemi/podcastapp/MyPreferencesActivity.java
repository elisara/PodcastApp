package com.example.elisarajaniemi.podcastapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

/**
 * Created by Elisa Rajaniemi on 28.10.2016.
 */

public class MyPreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_layout);
        getFragmentManager().beginTransaction().replace(R.id.displayPrefs, new MyPreferenceFragment()).commit();

        Button button = (Button) findViewById(R.id.close);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }


    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        SharedPreferences sp;
        boolean all;
        boolean allChecked, someChecked;
        private SharedPreferences.OnSharedPreferenceChangeListener listener;
        CheckBoxPreference allBox, viihdeBox, musiikkiBox, draamaBox, asiaBox, kulttuuriBox, historiaBox, luontoBox, hartaudetBox, lapsetBox, ajankohtBox, uutisetBox, urheiluBox, metroBox;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);

            allBox = (CheckBoxPreference) findPreference("all");
            viihdeBox = (CheckBoxPreference) findPreference("viihde");
            musiikkiBox = (CheckBoxPreference) findPreference("musiikki");
            draamaBox = (CheckBoxPreference) findPreference("draama");
            asiaBox = (CheckBoxPreference) findPreference("asia");
            kulttuuriBox = (CheckBoxPreference) findPreference("kulttuuri");
            historiaBox = (CheckBoxPreference) findPreference("historia");
            luontoBox = (CheckBoxPreference) findPreference("luonto");
            hartaudetBox = (CheckBoxPreference) findPreference("hartaudet");
            lapsetBox = (CheckBoxPreference) findPreference("lapset");
            ajankohtBox = (CheckBoxPreference) findPreference("ajankohtaisohjelmat");
            uutisetBox = (CheckBoxPreference) findPreference("uutiset");
            urheiluBox = (CheckBoxPreference) findPreference("urheilu");
            metroBox = (CheckBoxPreference) findPreference("metropolia");


            sp.edit().putBoolean("all", true);
            selectCheckboxes();

            listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    selectCheckboxes();
                }
            };
            sp.registerOnSharedPreferenceChangeListener(listener);
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        }

        public void selectCheckboxes(){
            if(allBox.isChecked() && allChecked == false && someChecked == false) {
                viihdeBox.setChecked(true);
                musiikkiBox.setChecked(true);
                viihdeBox.setChecked(true);
                draamaBox.setChecked(true);
                hartaudetBox.setChecked(true);
                asiaBox.setChecked(true);
                kulttuuriBox.setChecked(true);
                historiaBox.setChecked(true);
                luontoBox.setChecked(true);
                lapsetBox.setChecked(true);
                ajankohtBox.setChecked(true);
                uutisetBox.setChecked(true);
                urheiluBox.setChecked(true);
                metroBox.setChecked(true);
                allChecked = true;
                System.out.println("ALL in 1:" + allBox.isChecked() + " AllChecked: "+ allChecked + " someChecked: "+someChecked);
            }

            else if(!allBox.isChecked() && allChecked == true){
                viihdeBox.setChecked(false);
                musiikkiBox.setChecked(false);
                viihdeBox.setChecked(false);
                draamaBox.setChecked(false);
                hartaudetBox.setChecked(false);
                asiaBox.setChecked(false);
                kulttuuriBox.setChecked(false);
                historiaBox.setChecked(false);
                luontoBox.setChecked(false);
                lapsetBox.setChecked(false);
                ajankohtBox.setChecked(false);
                uutisetBox.setChecked(false);
                urheiluBox.setChecked(false);
                metroBox.setChecked(false);
                allChecked = false;
                someChecked = false;
                System.out.println("ALL in 2:" + allBox.isChecked() + " AllChecked: "+ allChecked + " someChecked: "+someChecked);
            }

            else if(allBox.isChecked() && allChecked == true && someChecked == false){
                allChecked = false;
                someChecked = true;
                allBox.setChecked(false);
                System.out.println("ALL in 3:" + allBox.isChecked() + " AllChecked: "+ allChecked + " someChecked: "+someChecked);
            }

            else if(!allBox.isChecked() && someChecked == true && allChecked == false){
                someChecked = false;
                System.out.println("ALL in 4:" + allBox.isChecked() + " AllChecked: "+ allChecked + " someChecked: "+someChecked);
            }
            else{
                System.out.println("Went to ELSE ---- ALL in 4:" + allBox.isChecked() + " AllChecked: "+ allChecked + " someChecked: "+someChecked);
            }


        }

    }

}
