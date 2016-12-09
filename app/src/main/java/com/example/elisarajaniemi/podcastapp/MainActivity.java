package com.example.elisarajaniemi.podcastapp;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private ImageButton menuBtn, searchBtn;
    private Search search;
    private AlertDialog alertDialog;
    private MenuFragment mf;
    public SmallPlayerFragment spf;
    private TextView title;
    private boolean categoryOpen, menuOpen;
    private FrontPageFragment frontPageFragment;
    private CollectionFragment collectionFragment;
    boolean mIsBound = false;
    private PlayerFragment pf;
    private PodcastItem pi, pi2;
    private Context context = null;
    private Bitmap pic;
    public PodcastItems podcastItems = PodcastItems.getInstance();
    PlayService pServ;
    ImageLoader imageLoader;
    android.support.v4.app.FragmentManager fragmentManager;
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
        context = this;
        System.out.println("---------CREATE--------------");

        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance();
        fragmentManager = getSupportFragmentManager();

        apiKey = "495i4orWwXCqiW5IuOQUzuAlGmfFeky7BzMPe-X19inh9MRm5RqGhQDUEh5avkZNFjC6mYT6w2xGXdQjm9XfakwHloH027i-tkLX77yFMZJlC3wGWqIjyHIXnvPzvHzW";
        try {
            //new GetMetropoliaPodcastHelper((MainActivity) context).execute("http://dev.mw.metropolia.fi/aanimaisema/plugins/api_audio_search/index.php/?key=" + apiKey + "&category=%20&link=true").get();
            new GetYlePodcastHelper((MainActivity) context).execute("https://external.api.yle.fi/v1/programs/", "items.json?app_id=950fdb28" + "&app_key=2acb02a2a89f0d366e569b228320619b&availability=ondemand&mediaobject=audio&order=playcount.6h:desc&limit=50&type=radioprogram", "fromepisodes").get();
            //new GetYlePodcastHelper((MainActivity) context).execute("https://external.api.yle.fi/v1/programs/", "items.json?app_id=950fdb28&app_key=2acb02a2a89f0d366e569b228320619b&availability=ondemand&mediaobject=audio&order=playcount.24h:desc&limit=100&type=radioprogram", "fromepisodes").get();
            //new GetYlePodcastHelper((MainActivity) context).execute("https://external.api.yle.fi/v1/programs/", "items.json?app_id=950fdb28&app_key=2acb02a2a89f0d366e569b228320619b&availability=ondemand&mediaobject=audio&order=playcount.24h:desc&limit=100&type=radioprogram&offset=100", "fromepisodes").get();
            //new GetYlePodcastHelper((MainActivity) context).execute("https://external.api.yle.fi/v1/programs/", "items.json?app_id=950fdb28&app_key=2acb02a2a89f0d366e569b228320619b&availability=ondemand&mediaobject=audio&order=playcount.24h:desc&limit=100&type=radioprogram&offset=200", "fromepisodes").get();


            new GetPlayListsHelper().execute("http://media.mw.metropolia.fi/arsu/playlists/user/"+ PreferenceManager.getDefaultSharedPreferences(this).getInt("id", 0) + "?token=" + PreferenceManager.getDefaultSharedPreferences(this).getString("token", "0")).get();
        }
        catch (ExecutionException e){
                e.printStackTrace();
        }
        catch (InterruptedException e){
                e.printStackTrace();
        }


        //System.out.println("listassa: " +podcastItems.getItems().size());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        title = (TextView) toolbar.findViewById(R.id.title);

        menuOpen = false;
        categoryOpen = false;

        mf = new MenuFragment();
        frontPageFragment = new FrontPageFragment();
        pServ = new PlayService("PodcastApp");
        collectionFragment = new CollectionFragment();
        pf = new PlayerFragment();
        spf = new SmallPlayerFragment();
        search = new Search();


        Intent playerIntent = new Intent(this, PlayService.class);
        playerIntent.setAction(START_SERVICE);
        doBindService(playerIntent);
        startService(playerIntent);

        onNewIntent(getIntent());

        searchBtn = (ImageButton) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.searchDialog(context, collectionFragment);
            }
        });

        menuBtn = (ImageButton) findViewById(R.id.menuBtn);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment dial = (DialogFragment) Fragment.instantiate(getApplicationContext(), MenuFragment.class.getCanonicalName());
                dial.setStyle( DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog );
                dial.show(getSupportFragmentManager(), "dialog");

            }
        });

        title.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, frontPageFragment).addToBackStack("frontPageFragment").commit();
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
        checkForUpdates();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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
    public void onNewIntent(Intent playerIntent) {
        boolean reloadFragmentFromNotification = playerIntent.getBooleanExtra("isPlayerFragment", false);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (reloadFragmentFromNotification) {
           openPlayer();
        } else {

            ft.replace(R.id.frag_container, frontPageFragment);

            ft.add(R.id.player_frag_container, spf);
            ft.commit();
            hidePlayer();
            showPlayer();



        }
    }
    public void openPlayer(){
        System.out.println("-----open player");
        FragmentTransaction ft = fragmentManager.beginTransaction();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate("pf", 0);
        if (!fragmentPopped) {
            Fragment fragment = new PlayerFragment();
            ft.replace(R.id.frag_container, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack("pf");
            ft.commitAllowingStateLoss();
        }

    }

    public void showPlayer() {
        if(pServ.getStatus()>1)fragmentManager.beginTransaction().show(spf).commit();
    }

    public void hidePlayer() {
        fragmentManager.beginTransaction().hide(spf).commit();
    }

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
    public void onStart() {
        super.onStart();
        System.out.println("---------START--------------");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("---------STOP--------------");

    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("----------Main OnPause");
        unregisterManagers();

    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("----------Main OnResume");
        checkForCrashes();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("----------Main OnDestroy");
        doUnbindService();
        pServ.onDestroy();
        unregisterManagers();



    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            System.out.println("----------COUNT 0----------");
            moveTaskToBack(true);
            //super.onBackPressed();


        } else {
            System.out.println("FManager----- " + getSupportFragmentManager().toString());
            getSupportFragmentManager().popBackStackImmediate();
        }

    }

    public void setFragment(Fragment frag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_container, frag).addToBackStack("collectionFragment").commit();
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }



}

