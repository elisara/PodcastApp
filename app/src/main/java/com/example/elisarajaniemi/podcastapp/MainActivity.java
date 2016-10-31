package com.example.elisarajaniemi.podcastapp;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.apiKey;

public class MainActivity extends AppCompatActivity {

    private ImageButton menuBtn;
    private MenuFragment mf;
    private TextView title;
    private boolean categoryOpen, menuOpen;
    private CategoryFragment cf;
    private SerieFragment sf;
    private String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread t = new Thread(r);
        t.start();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        title = (TextView) toolbar.findViewById(R.id.title);

        menuOpen = false;
        categoryOpen = false;

        mf = new MenuFragment();
        cf = new CategoryFragment();
        sf = new SerieFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frag_container, sf).commit();

        menuBtn = (ImageButton) findViewById(R.id.menuBtn);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (menuOpen == false) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.menu_frag_container, mf).commit();
                    menuOpen = true;
                }
                else{
                    getSupportFragmentManager().beginTransaction()
                            .remove(mf).commit();
                    menuOpen = false;
                }

                System.out.println("menu clicked");

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Category things
        sf.history = prefs.getBoolean("history", true);
        if (sf.history == false) {
            Toast.makeText(this, "False",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "True",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    Runnable r = new Runnable() {
        public void run() {
            try {
                URL url = new URL("http://dev.mw.metropolia.fi/aanimaisema/plugins/api_auth/auth.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                String input = "{\"username\":\"podcast\",\"password\":\"podcast16\"}";

                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes());
                os.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                System.out.println("Output from Server .... \n");
                while ((output = br.readLine()) != null) {
                    try {
                        JSONObject jObject = new JSONObject(output);
                        apiKey = jObject.getString("api_key");
                        System.out.println(apiKey);
                        new HttpGetHelper().execute("http://dev.mw.metropolia.fi/aanimaisema/plugins/api_audio_search/index.php/?key=" + apiKey + "&category=%20&link=true");
                    } catch (JSONException e) {
                        System.out.println(e);
                    }
                }
                conn.disconnect();
            } catch (
                    MalformedURLException e
                    ) {
                e.printStackTrace();

            } catch (
                    IOException e
                    )

            {
                e.printStackTrace();
            }
        }

    };


}

