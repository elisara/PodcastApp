package com.example.elisarajaniemi.podcastapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Elisa Rajaniemi on 31.10.2016.
 */

public class EpisodesFragment extends Fragment {

    private ListView listView;
    private EpisodeListArrayAdapter adapter;
    private HttpGetHelper httpGetHelper;
    private String message;
    private ArrayList<PodcastItem> list;
    private PodcastItem pi;
    private MainActivity ma;
    AlertDialog alertDialog;
    private TextView collectionName;
    private LinearLayout headerBox;
    private PlayerFragment pf;
    public PodcastItems podcastItems = PodcastItems.getInstance();
    private ArrayList<PodcastItem> listAll = podcastItems.getItems();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pi = (PodcastItem) getArguments().getSerializable("message");
        View view = inflater.inflate(R.layout.single_playlist_layout, container, false);
        //httpGetHelper = new HttpGetHelper();

        collectionName = (TextView) view.findViewById(R.id.collectionTitle);
        collectionName.setText(pi.collectionName);
        //podcastItems = PodcastItems.getInstance();
        //listAll = podcastItems.getItems();


        listView = (ListView) view.findViewById(R.id.single_playlist_list);
        fillList();
       // adapter = new EpisodeListArrayAdapter(getContext(), this.list);
       // listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                PodcastItem pi = list.get(position);
                //Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
                //intent.putExtra("episode", pi);
                //getActivity().startActivity(intent);
                pf = new PlayerFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("episode", pi);
                pf.setArguments(bundle2);

                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("ef")
                        .add(R.id.menu_frag_container, pf).commit();

                //PodcastItems.getInstance().clearItems();

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

                        sendIntent.putExtra(Intent.EXTRA_TEXT, podcastItem.title + " " + podcastItem.url);
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

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        fillList();
        System.out.println("----------------------RESUMEEE------------");


    }

    public URL createUrl(String urli) throws IOException {
        URL url = new URL(urli);
        return url;
    }

    /**
     public Drawable getDrawableFromURL(String imageUrl) {
     try {
     URL url = new URL(imageUrl);
     HttpURLConnection connection = (HttpURLConnection) url.openConnection();
     connection.setDoInput(true);
     connection.connect();
     InputStream input = connection.getInputStream();
     Drawable d = Drawable.createFromStream(input, "src name");
     //Bitmap myBitmap = BitmapFactory.decodeStream(input);
     return d;
     } catch (IOException e) {
     e.printStackTrace();
     return null;
     }
     }*/



    public void fillList(){
        //PodcastItems podcastItems = PodcastItems.getInstance();

        list = new ArrayList<>();
        //listAll = new ArrayList<>();

       //listAll = podcastItems.getItems();

        if(list.size() == 0) {
            for (int i = 0; i < listAll.size(); i++) {
                if (listAll.get(i).collectionName.equals(pi.collectionName) && !list.contains(listAll.get(i))) {
                    list.add(listAll.get(i));
                }
            }
        }

        System.out.println("LISTALL koko: " + listAll.size());
        System.out.println("LISTAN KOKO: " + list.size());
        adapter = new EpisodeListArrayAdapter(getContext(), list);
        listView.setAdapter(adapter);

    }

}
