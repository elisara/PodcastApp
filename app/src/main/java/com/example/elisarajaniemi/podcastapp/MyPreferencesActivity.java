package com.example.elisarajaniemi.podcastapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * Created by Elisa Rajaniemi on 28.10.2016.
 */

public class MyPreferencesActivity extends PreferenceActivity {

    boolean checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_layout);

        getFragmentManager().beginTransaction().replace(R.id.displayPrefs, new MyPreferenceFragment()).commit();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sp1 = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences sp = getSharedPreferences("myCustomSharedPrefs", Activity.MODE_PRIVATE);
        checked = sp1.getBoolean("history", false);
        //String strUsr = sp1.getString("history", "NA");
        System.out.println(checked);

        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if(key.equals("history")){
                    System.out.println("PREF_KEY: " + key);
                }

                System.out.println(checked);
            }
        };

        sp.registerOnSharedPreferenceChangeListener(listener);


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
