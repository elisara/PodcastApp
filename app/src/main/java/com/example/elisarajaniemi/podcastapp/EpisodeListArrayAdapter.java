package com.example.elisarajaniemi.podcastapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Elisa Rajaniemi on 27.10.2016.
 */

public class EpisodeListArrayAdapter extends ArrayAdapter<PodcastItem> {

    PodcastItem value, podcastItem;
    PlaylistItem playlistItem;
    PlaylistsFragment playlistsFragment;
    boolean addToPlaylist = false;

    public EpisodeListArrayAdapter(Context context, ArrayList<PodcastItem> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        playlistsFragment = new PlaylistsFragment();

        value = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.episode_list_item, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.episodeName);
        tv.setText(value.title);

        ImageView iv = (ImageView) convertView.findViewById(R.id.episodeIcon);
        iv.setImageBitmap(value.picture);

        ImageButton button = (ImageButton) convertView.findViewById(R.id.itemMenu);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                System.out.println("item imagebutton clicked");
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Add to");

                LinearLayout lp = new LinearLayout(getContext());
                lp.setOrientation(LinearLayout.VERTICAL);
                lp.setPadding(30,30,30,30);


                final TextView toPlaylist = new TextView(getContext());
                toPlaylist.setText("Playlist");
                toPlaylist.setTextSize(20);
                toPlaylist.setPadding(30, 20, 20, 20);
                lp.addView(toPlaylist);

                final TextView toQueue = new TextView(getContext());
                toQueue.setText("Queue");
                toQueue.setPadding(30, 20, 20, 20);
                toQueue.setTextSize(20);
                lp.addView(toQueue);

                final TextView toFavorites = new TextView(getContext());
                toFavorites.setText("Favorites");
                toFavorites.setPadding(30, 20, 20, 10);
                toFavorites.setTextSize(20);
                lp.addView(toFavorites);

                alertDialogBuilder.setView(lp);
                final AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                toPlaylist.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        alertDialog.cancel();
                        addToPlaylist = true;
                        podcastItem = value;
                        playlistsFragment.addToPlaylistDialog(podcastItem, getContext());
                    }
                });

                toQueue.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        System.out.println("Clicked to queue");
                        alertDialog.cancel();
                    }
                });

                toFavorites.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        System.out.println("Clicked to queue");
                        alertDialog.cancel();
                    }
                });

                alertDialog.show();

            }
        });
        return convertView;
    }


}
