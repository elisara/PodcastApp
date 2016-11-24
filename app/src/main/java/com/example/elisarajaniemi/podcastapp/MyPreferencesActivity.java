package com.example.elisarajaniemi.podcastapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
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

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);

            all = sp.getBoolean("all", true);


            listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    CheckBoxPreference allBox = (CheckBoxPreference) findPreference("all");
                    CheckBoxPreference humorBox = (CheckBoxPreference) findPreference("humor");
                    CheckBoxPreference historyBox = (CheckBoxPreference) findPreference("history");
                    CheckBoxPreference natureBox = (CheckBoxPreference) findPreference("nature");
                    CheckBoxPreference entBox = (CheckBoxPreference) findPreference("entertainment");
                    CheckBoxPreference politicsBox = (CheckBoxPreference) findPreference("politics");
                    CheckBoxPreference musicBox = (CheckBoxPreference) findPreference("music");
                    CheckBoxPreference healthBox = (CheckBoxPreference) findPreference("health");
                    CheckBoxPreference ecoBox = (CheckBoxPreference) findPreference("economy");
                    CheckBoxPreference techBox = (CheckBoxPreference) findPreference("technology");

                    if(allBox.isChecked() && allChecked == false && someChecked == false) {
                        humorBox.setChecked(true);
                        historyBox.setChecked(true);
                        humorBox.setChecked(true);
                        natureBox.setChecked(true);
                        ecoBox.setChecked(true);
                        entBox.setChecked(true);
                        politicsBox.setChecked(true);
                        musicBox.setChecked(true);
                        healthBox.setChecked(true);
                        techBox.setChecked(true);
                        allChecked = true;
                        System.out.println("ALL in 1:" + allBox.isChecked() + " AllChecked: "+ allChecked + " someChecked: "+someChecked);
                    }

                    else if(!allBox.isChecked() && allChecked == true){
                        humorBox.setChecked(false);
                        historyBox.setChecked(false);
                        humorBox.setChecked(false);
                        natureBox.setChecked(false);
                        ecoBox.setChecked(false);
                        entBox.setChecked(false);
                        politicsBox.setChecked(false);
                        musicBox.setChecked(false);
                        healthBox.setChecked(false);
                        techBox.setChecked(false);
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
            };


            sp.registerOnSharedPreferenceChangeListener(listener);

        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        }

    }

}
