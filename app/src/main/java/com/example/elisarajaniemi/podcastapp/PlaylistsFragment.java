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
    private SerieArrayAdapter adapter;
    private MenuFragment mf;
    private String playlistName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlist_layout, container, false);

        mf = new MenuFragment();

        final ArrayList<PodcastItem> list = new ArrayList<>();
        /**list.add("playlist1");
        list.add("playlist2");
        list.add("playlist3");
        list.add("playlist4");
         */

        listView = (ListView) view.findViewById(R.id.playlist_list);
        adapter = new SerieArrayAdapter(getContext(), PodcastItems.getInstance().getItems());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                //String value = list.get(position).toString();
                //System.out.println(value);
                SinglePlaylistFragment splf = new SinglePlaylistFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, splf).addToBackStack( "tag" ).commit();
            }

        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                //list.add(playlistName);

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
        });

        return view;
    }
}
