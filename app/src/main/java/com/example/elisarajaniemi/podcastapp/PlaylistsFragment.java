package com.example.elisarajaniemi.podcastapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by Elisa Rajaniemi on 31.10.2016.
 */

public class PlaylistsFragment extends Fragment {


    private Thread t;
    private ListView listView;
    private PlaylistsArrayAdapter adapter;
    private MenuFragment mf;
    private String playlistName, message;
    private ArrayList<PlaylistItem> list;
    CurrentUser currentUser = CurrentUser.getInstance();
    Playlists playlists = Playlists.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlist_layout, container, false);

        mf = new MenuFragment();
        list = new ArrayList<>();

        listView = (ListView) view.findViewById(R.id.playlist_list);
        adapter = new PlaylistsArrayAdapter(getContext(), playlists.getPlaylists());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                PlaylistItem value = list.get(position);
                //System.out.println(value);

                System.out.println("Playlistin nimi playlistsfragmentissa: " + value.name);
                System.out.println("Playlistin pituus playlistsfragmentissa: " + value.list.size());


               /** SinglePlaylistFragment splf = new SinglePlaylistFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, splf).addToBackStack( "tag" ).commit();*/
            }

        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewPlaylist(getContext());
            }
        });

        return view;
    }

    public void createNewPlaylist(Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        System.out.println("Current User Id: " + CurrentUser.getInstance().getCurrentUser().get(0).id);
        alertDialogBuilder.setTitle("Create new playlist");
        alertDialogBuilder.setMessage("Name of the playlist:");

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialogBuilder.setView(input);

        alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                t = new Thread(r);
                t.start();
                playlistName = input.getText().toString();
                ArrayList<PodcastItem> addedList = new ArrayList<PodcastItem>();
            }
        })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void addToExcistingPlaylist(int playlistID, PodcastItem podcastItem){

        //lista.add(0,podcastItem);
        //System.out.println("LISTA: " + lista.get(0).title);
    }

    public String getString(){
        String koira = "koira";
        return koira;
    }

    public void addToPlaylistDialog(final PodcastItem podcastItem, final Context context){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Add to");

        LinearLayout lp = new LinearLayout(context);
        lp.setOrientation(LinearLayout.VERTICAL);
        lp.setPadding(30,30,30,30);

        final ListView toPlaylist = new ListView(context);
        adapter = new PlaylistsArrayAdapter(context, playlists.getPlaylists());

        toPlaylist.setAdapter(adapter);
        //toPlaylist.setText(playlist.name);
        //toPlaylist.setTextSize(20);
        toPlaylist.setPadding(30, 20, 20, 20);
        lp.addView(toPlaylist);

        final ImageButton addPlaylist = new ImageButton(context);
        addPlaylist.setImageResource(R.drawable.ic_add_black_24dp);
        lp.addView(addPlaylist);

        alertDialogBuilder.setView(lp);
        final AlertDialog alertDialog2 = alertDialogBuilder.create();

        //Add podcast to existing playlist
        toPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                alertDialog2.cancel();
                PlaylistItem playlistItem = playlists.getPlaylists().get(position);
                //addToExcistingPlaylist(playlist.list, podcastItem);
                new PutPodcastToPlaylist().execute(podcastItem.programID.replace("-", ""), "http://media.mw.metropolia.fi/arsu/playlists/" + playlistItem.id + "?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                        "eyJpZCI6MywidXNlcm5hbWUiOiJtb2kiLCJwYXNzd29yZCI6ImhlcHMiLCJlbWFpbCI6Im1vaUBleGFtcGxlLmNvbSIsImlzX2FkbWluIjpudWxs" +
                        "LCJkYXRlIjoiMjAxNi0xMS0xNVQxNjowMToyMy4wMDBaIiwiaWF0IjoxNDc5MjgxNDI5LCJleHAiOjE1MTA4MTc0Mjl9.5BTFGggjtGCSh7ssNjWokmM7CAHHR9omvcGCqYXLlso");

            }
        });
        //create a new playlist
        addPlaylist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createNewPlaylist(context);
                alertDialog2.cancel();

            }
        });
        alertDialog2.show();
    }



    Runnable r = new Runnable() {
        public void run() {
            try {
                URL url = new URL("http://media.mw.metropolia.fi/arsu/playlists?token=" + currentUser.getCurrentUser().get(0).token);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                String input = "{\"playlist_name\":\"" + playlistName + "\",\"user_id\":\"" + currentUser.getCurrentUser().get(0).id + "\"}";
                input = input.replace("\n", "");
                System.out.println(input);

                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes());
                os.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;

                while ((output = br.readLine()) != null) {
                    try {
                        JSONObject jObject = new JSONObject(output);
                        message = jObject.getString("message");
                        System.out.println("Database message: " + message);
                        new GetPlayListsHelper().execute("http://media.mw.metropolia.fi/arsu/playlists/user/"+ currentUser.getCurrentUser().get(0).id + "?token=" + currentUser.getCurrentUser().get(0).token);
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


class PutPodcastToPlaylist extends AsyncTask<Object, String, String> {
    //ProgressDialog pdLoading = new ProgressDialog(AsyncExample.this);

    String message;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //this method will be running on UI thread
        //pdLoading.setMessage("\tLoading...");
        //pdLoading.show();
    }
    @Override
    protected String doInBackground(Object... params) {

        try {
            URL url = new URL((String)params[1]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            String input = "{\"podcast_id\":\"" + params[0] + "\"}";
            //input = input.replace("\n", "");
            System.out.println(input);

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;

            while ((output = br.readLine()) != null) {
                try {
                    JSONObject jObject = new JSONObject(output);
                    message = jObject.getString("message");
                    System.out.println("Database message: " + message);
                } catch (JSONException e) {
                    System.out.println(e);
                }
            }

            conn.disconnect();
        } catch (
                IOException e
                )

        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        System.out.println("Result: " + result);

        //this method will be running on UI thread

        //pdLoading.dismiss();
    }

}