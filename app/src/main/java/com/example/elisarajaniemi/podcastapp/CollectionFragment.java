package com.example.elisarajaniemi.podcastapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by Elisa Rajaniemi on 31.10.2016.
 */

public class CollectionFragment extends Fragment {

    private ListView listView;
    private GetMetropoliaPodcastHelper getMetropoliaPodcastHelper;
    private String message;
    private ArrayList<PodcastItem> list;
    private PodcastItem pi;
    private MainActivity ma;
    private AlertDialog alertDialog;
    private LinearLayout headerBox;
    private PlayerFragment pf;
    public PodcastItems podcastItems = PodcastItems.getInstance();
    public PlaylistPodcastItems playlistPodcastItems = PlaylistPodcastItems.getInstance();
    public PodcastIDArray podcastIDArray = PodcastIDArray.getInstance();
    public FavoritePodcastItems favoritePodcastItems = FavoritePodcastItems.getInstance();
    public SearchItems searchItems = SearchItems.getInstance();
    private ArrayList<PodcastItem> listAll = podcastItems.getItems();
    private int playlistID = 0;
    private ExpandableListView simpleExpandableListView;
    private ExpandableListViewAdapter listAdapter;
    private FavoritesFragment favoritesFragment;
    private boolean fromFavorites, fromSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        pi = (PodcastItem) getArguments().getSerializable("message");
        playlistID = getArguments().getInt("playlistID");
        fromFavorites = getArguments().getBoolean("fromFavorites");
        fromSearch = getArguments().getBoolean("fromSearch");

        System.out.println("FromSearch value: " + fromSearch);
        System.out.println("FromFavorites value: " + fromFavorites);
        System.out.println("PlaylistID value: " + playlistID);
        favoritesFragment = new FavoritesFragment();


        if(playlistID != 0 && fromFavorites == false) {
            try {
                new GetYlePodcastHelper((MainActivity) getContext()).execute("https://external.api.yle.fi/v1/programs/items/", ".json?app_key=2acb02a2a89f0d366e569b228320619b&app_id=950fdb28", "fromplaylist").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else if(playlistID == 0 && fromFavorites == true){
            try {
                new GetYlePodcastHelper((MainActivity) getContext()).execute("https://external.api.yle.fi/v1/programs/items/", ".json?app_key=2acb02a2a89f0d366e569b228320619b&app_id=950fdb28", "fromfavorites").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

        View view = inflater.inflate(R.layout.single_playlist_layout, container, false);

        simpleExpandableListView = (ExpandableListView) view.findViewById(R.id.expandable_listview);
        //listView = (ListView) view.findViewById(R.id.single_playlist_list);
        fillList();

        //expandAll();

        // setOnGroupClickListener listener for group heading click
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
                collapseAll();
                return true;
            }
        });

        if (fromFavorites == true){
            simpleExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                    try {
                        favoritesFragment.deleteFavorites("http://media.mw.metropolia.fi/arsu/favourites/", podcastIDArray.getItems().get(position).id, "?token=" + PreferenceManager.getDefaultSharedPreferences(getContext()).getString("token", "0"));
                        favoritePodcastItems.deletePodcast(position);
                        listAdapter.notifyDataSetChanged();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return false;
                }
            });
        }

        /**

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                PodcastItem pi = list.get(position);
                pf = new PlayerFragment();
                Bundle bundle2 = new Bundle();
                System.out.println("FromYLE: " + pi.fromYLE);
                if (pi.fromYLE == true){
                    try {
                        new DecodeYleURL().execute(pi).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
                bundle2.putSerializable("episode", pi);
                pf.setArguments(bundle2);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().addToBackStack("pf")
                        .replace(R.id.frag_container, pf, "pf").commit();

            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, final int position, long rowId) {

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Description");

                LinearLayout lp = new LinearLayout(getContext());
                lp.setOrientation(LinearLayout.VERTICAL);
                lp.setPadding(30, 30, 30, 60);

                final TextView description = new TextView(getActivity());
                final Button shareBtn = new Button(getActivity());
                shareBtn.setText("share");
                shareBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent sendIntent = new Intent();
                        Object obj = list.get(position);
                        PodcastItem podcastItem = (PodcastItem) obj;
                        sendIntent.setAction(Intent.ACTION_SEND);

                        sendIntent.putExtra(Intent.EXTRA_TEXT, podcastItem.title + " " + podcastItem.decryptedURL);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }
                });
                String[] splits = list.get(position).description.split("<a>");
                description.setText(splits[0].replaceAll("<br>", "\n\n"));
                description.setMovementMethod(new ScrollingMovementMethod());
                lp.addView(description);
                lp.addView(shareBtn);

                alertDialogBuilder.setView(lp);

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
            }
        });
         */

        return view;
    }

    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            simpleExpandableListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
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
        fillList();



    }

    public URL createUrl(String urli) throws IOException {
        URL url = new URL(urli);
        return url;
    }


    public void fillList(){
        list = new ArrayList<>();
        if (list.size() != 0){
            list.clear();
        }
        if(list.size() == 0 && playlistID == 0 && !fromFavorites && !fromSearch) {
            System.out.println("From episodes");
            for (int i = 0; i < listAll.size(); i++) {
                if (listAll.get(i).collectionName.equals(pi.collectionName) && !list.contains(listAll.get(i))) {
                    list.add(listAll.get(i));
                }
            }
        } else if(list.size() == 0 && playlistID != 0 && !fromFavorites && !fromSearch){
            System.out.println("PlaylistPodcastItems size: " + playlistPodcastItems.getItems().size());
            list = playlistPodcastItems.getItems();

        } else if(list.size() == 0 && playlistID == 0 && fromFavorites && !fromSearch){
            System.out.println("FavoritePodcastItems array size: " + favoritePodcastItems.getItems().size());
            list = favoritePodcastItems.getItems();
        } else if(list.size() == 0 && playlistID == 0 && fromSearch && !fromFavorites){
            System.out.println("From Search: " + fromSearch + ", search size: " + list.size());
            list = searchItems.getSearchItems();
        }

        //listView.setAdapter(adapter);

        listAdapter = new ExpandableListViewAdapter(getContext(), list);
        simpleExpandableListView.setAdapter(listAdapter);

    }



}

class DecodeYleURL extends AsyncTask<PodcastItem, String, String> {
    //ProgressDialog pdLoading = new ProgressDialog(AsyncExample.this);
    String decryptedURL;
    String resultURL;
    MyCrypt myCrypt = new MyCrypt();
    BufferedReader br;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //this method will be running on UI thread
        //pdLoading.setMessage("\tLoading...");
        //pdLoading.show();
    }
    @Override
    protected String doInBackground(PodcastItem... params) {

        try {
            URL url = new URL(params[0].url);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            conn.connect();
            BufferedReader r  = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
            String output;

            while ((output = r.readLine()) != null) {
                try {
                    JSONObject jObject = new JSONObject(output);
                    JSONArray jArray = jObject.getJSONArray("data");
                    for (int i = 0; i < jArray.length(); i++){
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
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }


}
