package com.example.elisarajaniemi.podcastapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by Elisa Rajaniemi on 31.10.2016.
 */

public class CollectionFragment extends Fragment {

    private final String YLE_API = "app_id=950fdb28&app_key=2acb02a2a89f0d366e569b228320619b";

    private ListView listView;

    private String message;
    private ArrayList<PodcastItem> list;
    private PodcastItem pi;
    private MainActivity ma;
    private AlertDialog alertDialog;
    private LinearLayout headerBox;
    private PlayerFragment pf;
    public PodcastItems podcastItems = PodcastItems.getInstance();
    public PlaylistPodcastItems playlistPodcastItems = PlaylistPodcastItems.getInstance();
    public SerieItems serieItems = SerieItems.getInstance();
    public PodcastIDArray podcastIDArray = PodcastIDArray.getInstance();
    public FavoritePodcastItems favoritePodcastItems = FavoritePodcastItems.getInstance();
    public SearchItems searchItems = SearchItems.getInstance();
    public HistoryPodcastItems historyPodcastItems = HistoryPodcastItems.getInstance();
    public AutoplayItems autoplayItems = AutoplayItems.getInstance();
    public QueueItems queueItems = QueueItems.getInstance();
    public Playlists playlists = Playlists.getInstance();
    private PlaylistsFragment playlistsFragment = new PlaylistsFragment();
    private FirebaseAuth auth;

    private int playlistID = 0;
    private ExpandableListView simpleExpandableListView;
    private ExpandableListViewAdapter listAdapter;
    private Favorites favorites;
    private ImageView imageView;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private TextView textView;
    private TextView collectionName;
    private LinearLayout header;
    private PodcastItem piFromAdapter;
    private int lastExpandedPosition = 0;

    private boolean fromFavorites, fromSearch, fromHistory, fromQueue;
    private History history;
    private int width, height;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collection_layout, container, false);
        pi = (PodcastItem) getArguments().getSerializable("message");
        playlistID = getArguments().getInt("playlistID");
        fromFavorites = getArguments().getBoolean("fromFavorites");
        fromSearch = getArguments().getBoolean("fromSearch");
        fromHistory = getArguments().getBoolean("fromHistory");
        fromQueue = getArguments().getBoolean("fromQueue");

        auth = FirebaseAuth.getInstance();

        history = new History();
        list = new ArrayList<>();

        favorites = new Favorites();

        imageView = (ImageView) view.findViewById(R.id.collectionImage);
        textView = (TextView) view.findViewById(R.id.title);
        header = (LinearLayout) view.findViewById(R.id.headerBox);
        collectionName = (TextView) view.findViewById(R.id.collectionName);

        width = getResources().getDisplayMetrics().widthPixels;
        height = (getResources().getDisplayMetrics().heightPixels)/3;

        header.requestLayout();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        header.setLayoutParams(layoutParams);


        //CHECK FROM WHICH FRAGMENT THE USER IS COMING AND WHAT LIST TO SHOW
        if (playlistID != 0 && fromFavorites == false && fromHistory == false) {
            try {
                new GetYlePodcastHelper((MainActivity) getContext()).execute("https://external.api.yle.fi/v1/programs/items/", ".json?" + YLE_API, "fromplaylist").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else if (playlistID == 0 && fromFavorites == true && fromHistory == false) {
            try {
                new GetYlePodcastHelper((MainActivity) getContext()).execute("https://external.api.yle.fi/v1/programs/items/", ".json?" + YLE_API, "fromfavorites").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        } else if (playlistID == 0 && fromHistory == true && fromFavorites == false) {
            try {
                new GetYlePodcastHelper((MainActivity) getContext()).execute("https://external.api.yle.fi/v1/programs/items/", ".json?" + YLE_API, "fromHistory").get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (pi != null) {
            try {
                new GetYlePodcastHelper((MainActivity) getContext()).execute("https://external.api.yle.fi/v1/programs/", "items.json?"+ YLE_API + "&series=" + pi.serieID, "fromseries").get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        simpleExpandableListView = (ExpandableListView) view.findViewById(R.id.expandable_listview);
        fillList();
        //simpleExpandableListView.deferNotifyDataSetChanged();
        //listAdapter.notifyDataSetChanged();

        //EXPAND THE FIRST ITEM OF THE LIST
        if(list.size() > 0 && list != null) {
            simpleExpandableListView.expandGroup(lastExpandedPosition);
            simpleExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    if (lastExpandedPosition != -1
                            && groupPosition != lastExpandedPosition) {
                        simpleExpandableListView.collapseGroup(lastExpandedPosition);
                    }
                    lastExpandedPosition = groupPosition;
                }
            });
            simpleExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    PodcastItem podcastItem = list.get(groupPosition);
                    return false;
                }
            });

            simpleExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    return true;
                }
            });

            if (fromFavorites == true) {
                simpleExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                        final int favoriteID = position;
                        favorites.deleteFavoritesDialog(getContext(), podcastIDArray, favoriteID, favoritePodcastItems, listAdapter);
                        return true;
                    }
                });
            }
        }

        return view;
    }

    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            simpleExpandableListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            simpleExpandableListView.collapseGroup(i);
        }
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    //GET THE RIGHT LIST DEPENDING ON FROM WHICH FRAGMENT THE USER IS COMING FROM
    public void fillList() {

        if (list.size() != 0 || list != null) {
            list.clear();
        }
        if (list.size() == 0 && playlistID == 0 && !fromFavorites && !fromSearch && !fromHistory && !fromQueue) {
            list = serieItems.getSerieItems();
            if (list != null && list.size() > 0)
                collectionName.setText(serieItems.getSerieItems().get(0).collectionName);

        } else if (list.size() == 0 && playlistID != 0 && !fromFavorites && !fromSearch && !fromHistory && !fromQueue) {
            if(auth.getCurrentUser() != null) playlistsFragment.getPlaylists();
            list = playlistPodcastItems.getItems();
            collectionName.setText("Playlist");

        } else if (list.size() == 0 && playlistID == 0 && fromFavorites && !fromSearch && !fromHistory && !fromQueue) {
            if(auth.getCurrentUser() != null) favorites.getFavorites();
            list = favoritePodcastItems.getItems();
            collectionName.setText("Favorites");

        } else if (list.size() == 0 && playlistID == 0 && fromSearch && !fromFavorites && !fromHistory && !fromQueue) {
            list = searchItems.getSearchItems();
            collectionName.setText("Search results");

        } else if (list.size() == 0 && playlistID == 0 && !fromSearch && !fromFavorites && fromHistory && !fromQueue) {
            if(auth.getCurrentUser() != null) history.getHistory();
            list = historyPodcastItems.getItems();
            collectionName.setText("History");

        } else if (list.size() == 0 && playlistID == 0 && !fromSearch && !fromFavorites && !fromHistory && fromQueue) {
            list = autoplayItems.getItems();
            collectionName.setText("Queue");


        }


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileCount(100)
                .imageDownloader(new BaseImageDownloader(getContext())) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .build();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                //.showStubImage(R.drawable.ic_add_black_24dp)
                //.showImageForEmptyUri(R.drawable.ic_add_black_24dp)
                //.showImageOnFail(R.drawable.ic_add_black_24dp)
                .cacheOnDisc(true)
                .build();

        if(list != null && list.size() > 0) {
            if (!list.get(0).collectionName.contains("Metropolia") && playlistID == 0) {
                imageLoader.displayImage("http://images.cdn.yle.fi/image/upload//w_" + width + ",h_" + height + ",c_fill/" + list.get(0).serieImageURL + ".jpg", imageView, options);            //w_705,h_520,c_fill,g_auto
            } else if(!list.get(0).collectionName.contains("Metropolia") && playlistID != 0){
                for (int i = 0; i < playlists.getPlaylists().size(); i++){
                    if (playlists.getPlaylists().get(i).id == playlistID){
                        collectionName.setText(playlists.getPlaylists().get(i).name);

                    }
                }
            }
            else {
                collectionName.setText("Metropolia");
            }
        } else if (playlistID != 0||fromQueue) {
            collectionName.setText("Empty");

        }

        if(!list.isEmpty() && !list.get(0).collectionName.toLowerCase().contains("metropolia") && pi != null && playlistID  == 0) {
            Collections.sort(list, new Comparator<PodcastItem>() {
                public int compare(PodcastItem pod1, PodcastItem pod2) {
                    return pod2.programID.compareToIgnoreCase(pod1.programID); // To compare string values

                }
            });
        }

        for (int i = 0; i < list.size(); i++) {
        }

        if(list.size() > 0 && list != null) {
            listAdapter = new ExpandableListViewAdapter(getContext(), list);
            listAdapter.notifyDataSetChanged();
            simpleExpandableListView.deferNotifyDataSetChanged();
            simpleExpandableListView.setAdapter(listAdapter);
        }


    }

}

class DecodeYleURL extends AsyncTask<PodcastItem, String, String> {
    //ProgressDialog pdLoading = new ProgressDialog(AsyncExample.this);
    Context context;
    String decryptedURL;
    String resultURL;
    String responseCode;
    MyCrypt myCrypt = new MyCrypt();
    BufferedReader br;

    public DecodeYleURL() {

    }

    public DecodeYleURL(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(PodcastItem... params) {

        try {
            URL url = new URL(params[0].decryptedURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            conn.connect();
            responseCode = "" + conn.getResponseCode();
            if (responseCode.equalsIgnoreCase("200")) {
                BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
                String output;

                while ((output = r.readLine()) != null) {
                    try {
                        JSONObject jObject = new JSONObject(output);
                        JSONArray jArray = jObject.getJSONArray("data");
                        for (int i = 0; i < jArray.length(); i++) {
                            decryptedURL = jArray.getJSONObject(i).getString("url");
                        }
                        resultURL = myCrypt.decryptURL(decryptedURL);

                    } catch (JSONException e) {
                        System.out.println(e);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (
                MalformedURLException e
                ) {
            e.printStackTrace();

        } catch (
                IOException e
                )

        {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        params[0].setURL(resultURL);
        return responseCode;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        System.out.println("RESPONSE CODE: " + result);
        if (!result.equalsIgnoreCase("200")) {
            System.out.println("CONTEXT" + context);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
            //AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.CustomDialog));
            alertDialogBuilder.setTitle("Error");

            LinearLayout lp = new LinearLayout(context);
            lp.setOrientation(LinearLayout.VERTICAL);
            lp.setPadding(30, 0, 30, 30);

            final TextView errorMessage = new TextView(context);
            errorMessage.setText("Unfortunately this podcast cannot be played");
            errorMessage.setTextColor(Color.BLACK);
            errorMessage.setPadding(30, 20, 20, 20);
            errorMessage.setTextSize(20);
            lp.addView(errorMessage);

            alertDialogBuilder.setView(lp);
            final AlertDialog alertDialog = alertDialogBuilder.create();

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
    }


}


