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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
    private ImageView imageView;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private TextView textView;
    private LinearLayout header;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_playlist_layout, container, false);
        pi = (PodcastItem) getArguments().getSerializable("message");
        playlistID = getArguments().getInt("playlistID");
        fromFavorites = getArguments().getBoolean("fromFavorites");
        fromSearch = getArguments().getBoolean("fromSearch");

        System.out.println("FromSearch value: " + fromSearch);
        System.out.println("FromFavorites value: " + fromFavorites);
        System.out.println("PlaylistID value: " + playlistID);
        favoritesFragment = new FavoritesFragment();

        imageView = (ImageView) view.findViewById(R.id.collectionImage);
        textView = (TextView) view.findViewById(R.id.title);
        header = (LinearLayout) view.findViewById(R.id.headerBox);


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
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = (getResources().getDisplayMetrics().heightPixels)/3;
        //imageLoader.displayImage("http://images.cdn.yle.fi/image/upload//w_"+width+",h_"+height+",c_fill/" + list.get(0).imageURL + ".jpg", imageView);
        //imageLoader.displayImage("http://images.cdn.yle.fi/image/upload//w_1000,h_650,c_fill/" + list.get(position).imageURL + ".jpg", imageView, options);

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
            if (!list.get(0).collectionName.contains("Metropolia")) {
                imageLoader.displayImage("http://images.cdn.yle.fi/image/upload//w_" + width + ",h_" + height + ",c_fill/" + list.get(0).imageURL + ".jpg", imageView, options);            //w_705,h_520,c_fill,g_auto
            } else {
                //imageLoader.displayImage("https://s3.postimg.org/gzeoosubn/kissaholder.jpg", imageView, options);
                textView.setText("Metropolia");
                header.requestLayout();
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
                header.setLayoutParams(layoutParams);
                //imageView.getLayoutParams().height = height;
                //imageView.getLayoutParams().width = width;

            }
        }
        else if(playlistID != 0){
            //imageLoader.displayImage("https://s3.postimg.org/gzeoosubn/kissaholder.jpg", imageView, options);
            textView.setText("Empty");
            header.requestLayout();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            header.setLayoutParams(layoutParams);
            //imageView.getLayoutParams().height = height;
            //imageView.getLayoutParams().width = width;

        }

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
