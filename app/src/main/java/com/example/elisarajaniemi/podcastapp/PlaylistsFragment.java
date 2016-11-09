package com.example.elisarajaniemi.podcastapp;

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

import java.util.ArrayList;

/**
 * Created by Elisa Rajaniemi on 31.10.2016.
 */

public class PlaylistsFragment extends Fragment {


    private ListView listView;
    private PlaylistsArrayAdapter adapter;
    private MenuFragment mf;
    private String playlistName;
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


        listView = (ListView) view.findViewById(R.id.playlist_list);
        adapter = new PlaylistsArrayAdapter(getContext(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                PlaylistItem value = list.get(position);
                //System.out.println(value);
                System.out.println("Episoden nimi playlistsfragmentissa: " + value.name);

               /** SinglePlaylistFragment splf = new SinglePlaylistFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, splf).addToBackStack( "tag" ).commit();*/
            }

        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewPlaylist();
            }
        });

        return view;
    }

    public void createNewPlaylist(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        // set title
        alertDialogBuilder.setTitle("Create new playlist");

        // set dialog message
        alertDialogBuilder.setMessage("Name of the playlist:");

        //editText in dialog
        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialogBuilder.setView(input);

        //alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                playlistName = input.getText().toString();
                Toast.makeText(getContext(), "Playlist "+ playlistName +" created", Toast.LENGTH_SHORT).show();
                ArrayList<PodcastItem> addedList = new ArrayList<PodcastItem>();
                PlaylistItem addedPlaylistItem = new PlaylistItem(playlistName, addedList);
                list.add(addedPlaylistItem);

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

    public void addToPlaylist(PlaylistItem playlistItem){
        list.add(playlistItem);
    }

    public ArrayList getPlaylists(){
        return this.list;
    }
}
