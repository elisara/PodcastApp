package com.example.elisarajaniemi.podcastapp;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.ListView;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlist_layout, container, false);

        mf = new MenuFragment();
        list = new ArrayList<>();

        ArrayList<PodcastItem> kissat = new ArrayList<>();
        ArrayList<PodcastItem> koirat = new ArrayList<>();

        PlaylistItem playlistItem = new PlaylistItem("kissat", kissat);
        PlaylistItem playlistItem2 = new PlaylistItem("koirat", koirat);

        list.add(playlistItem);
        list.add(playlistItem2);
        getPlaylists();


        listView = (ListView) view.findViewById(R.id.playlist_list);
        adapter = new PlaylistsArrayAdapter(getContext(), list);
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

        // set title
        alertDialogBuilder.setTitle("Create new playlist");

        // set dialog message
        alertDialogBuilder.setMessage("Name of the playlist:");

        //editText in dialog
        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialogBuilder.setView(input);

        //alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                t = new Thread(r);
                t.start();
                playlistName = input.getText().toString();
                //Toast.makeText(getContext(), "Playlist "+ playlistName +" created", Toast.LENGTH_SHORT).show();
                ArrayList<PodcastItem> addedList = new ArrayList<PodcastItem>();
                PlaylistItem addedPlaylistItem = new PlaylistItem(playlistName, addedList);
                //list.add(addedPlaylistItem);

            }
        })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void addToExcistingPlaylist(ArrayList<PodcastItem> lista, PodcastItem podcastItem){
        lista.add(0,podcastItem);
        System.out.println("LISTA: " + lista.get(0).title);
    }

    public ArrayList<PlaylistItem> getPlaylists(){
        //System.out.println("listan ekan nimi: " +list.get(0).name);
        ArrayList<PlaylistItem> lista = new ArrayList<>();

        ArrayList<PodcastItem> kissat = new ArrayList<>();
        ArrayList<PodcastItem> koirat = new ArrayList<>();

        PlaylistItem playlistItem = new PlaylistItem("kissat", kissat);
        PlaylistItem playlistItem2 = new PlaylistItem("koirat", koirat);

        lista.add(playlistItem);
        lista.add(playlistItem2);

        return lista;
    }

    public String getString(){
        String koira = "koira";
        return koira;
    }

    Runnable r = new Runnable() {
        public void run() {
            try {
                URL url = new URL("http://media.mw.metropolia.fi/arsu/playlists?token=" + CurrentUser.getInstance().getCurrentUser().get(0).token);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                String input = "{\"playlist_name\":\"" + playlistName + "\",\"user_id\":\"" + CurrentUser.getInstance().getCurrentUser().get(0).id + "\"}";
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
