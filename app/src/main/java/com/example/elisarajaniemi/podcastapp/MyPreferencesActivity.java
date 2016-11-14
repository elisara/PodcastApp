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

    private boolean humor, technology, economy, health, all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_layout);

        getFragmentManager().beginTransaction().replace(R.id.displayPrefs, new MyPreferenceFragment()).commit();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        //SharedPreferences sp = getSharedPreferences("myCustomSharedPrefs", Activity.MODE_PRIVATE);
        all = sp.getBoolean("all", false);
        humor = sp.getBoolean("humor", false);
        technology = sp.getBoolean("technology", false);
        economy = sp.getBoolean("economy", false);
        health = sp.getBoolean("health", false);

        Button button = (Button) findViewById(R.id.close);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static class MyPreferenceFragment extends PreferenceFragment  {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

        }

    }

}
