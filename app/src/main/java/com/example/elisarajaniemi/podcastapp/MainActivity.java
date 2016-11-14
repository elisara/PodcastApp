package com.example.elisarajaniemi.podcastapp;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageButton menuBtn, searchBtn;
    private AlertDialog alertDialog;
    private MenuFragment mf;
    private SearchFragment searchFragment;
    private TextView title;
    private boolean categoryOpen, menuOpen;
    private SerieFragment sf;
    private EpisodesFragment ef;
    boolean mIsBound = false;
    private PlayerFragment pf;
    private PodcastItem pi, pi2;
    PlayService pServ;
    public ServiceConnection Scon = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            pServ = ((PlayService.ServiceBinder) service).getService();

        }


        @Override
        public void onServiceDisconnected(ComponentName name) {
            pServ = null;
        }

    };
    private String apiKey;
    public static final String START_SERVICE = "start_service";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;


        Thread t = new Thread(r);
        t.start();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        title = (TextView) toolbar.findViewById(R.id.title);

        menuOpen = false;
        categoryOpen = false;

        mf = new MenuFragment();
        sf = new SerieFragment();
        searchFragment = new SearchFragment();
        pServ = new PlayService("PodcastApp");
        ef = new EpisodesFragment();
        pf = new PlayerFragment();


        Intent playerIntent = new Intent(this, PlayService.class);
        playerIntent.setAction(START_SERVICE);
        doBindService(playerIntent);
        startService(playerIntent);

        onNewIntent(getIntent());

        searchBtn = (ImageButton) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Search");

                LinearLayout lp = new LinearLayout(context);
                lp.setOrientation(LinearLayout.VERTICAL);
                lp.setPadding(30, 30, 30, 60);

                final EditText searchField = new EditText(context);

                lp.addView(searchField);
                alertDialogBuilder.setView(lp);

                alertDialogBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SearchItems.getInstance().getSearchItems().clear();

                        for (int j = 0; j < PodcastItems.getInstance().getItems().size(); j++) {

                            if (PodcastItems.getInstance().getItems().get(j).title.toLowerCase().contains(searchField.getText().toString().toLowerCase())
                                    || PodcastItems.getInstance().getItems().get(j).description.toLowerCase().contains(searchField.getText().toString().toLowerCase())
                                        || PodcastItems.getInstance().getItems().get(j).tags.toLowerCase().contains(searchField.getText().toString().toLowerCase())
                                            || PodcastItems.getInstance().getItems().get(j).category.toLowerCase().contains(searchField.getText().toString().toLowerCase())
                                                || PodcastItems.getInstance().getItems().get(j).collectionName.toLowerCase().contains(searchField.getText().toString().toLowerCase())) {

                                SearchItems.getInstance().addSearchItem(PodcastItems.getInstance().getItems().get(j));
                                System.out.println("Added to list: " + PodcastItems.getInstance().getItems().get(j).title);

                            }
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, searchFragment).commit();
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        menuBtn = (ImageButton) findViewById(R.id.menuBtn);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (menuOpen == false) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.menu_frag_container, mf).commit();
                    menuOpen = true;
                } else {
                    getSupportFragmentManager().beginTransaction().remove(mf).commit();
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





    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName()) && pServ.isStarted()) {

                return true;
            }
        }
        return false;
    }


    @Override
    public void onNewIntent(Intent playerIntent){
        Bundle extras = playerIntent.getExtras();
        boolean reloadFragmentFromNotification = playerIntent.getBooleanExtra("isPlayerFragment",false);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (reloadFragmentFromNotification){
            Fragment fragment = new PlayerFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.frag_container, fragment)
                    .commitAllowingStateLoss();
        } else {

            fragmentManager.beginTransaction()
                    .replace(R.id.frag_container, sf).commit();

        }
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

                while ((output = br.readLine()) != null) {
                    try {
                        JSONObject jObject = new JSONObject(output);
                        apiKey = jObject.getString("api_key");
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


    void doBindService(Intent intent) {
        bindService(intent, Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //doUnbindService();
        //pServ.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Category things
        sf.history = prefs.getBoolean("history", true);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pServ.onDestroy();
        doUnbindService();
        finish();
        //doUnbindService();
        //pServ.onDestroy();

    }


    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0 ) {
            super.onBackPressed();


        } else {
            getSupportFragmentManager().popBackStackImmediate();
        }

    }


}

