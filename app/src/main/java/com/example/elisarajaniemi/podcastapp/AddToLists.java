package com.example.elisarajaniemi.podcastapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

/**
 * Created by Elisa Rajaniemi on 2.12.2016.
 */

public class AddToLists {

    public void addToListsDialog(final Context context, final PodcastItem podcastItem, final PlaylistsFragment playlistsFragment, final FavoritesFragment favoritesFragment){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
        //AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.CustomDialog));
        alertDialogBuilder.setTitle("Add to");

        String user = PreferenceManager.getDefaultSharedPreferences(context).getString("user", "");

        LinearLayout lp = new LinearLayout(context);
        lp.setOrientation(LinearLayout.VERTICAL);
        lp.setPadding(30,0,30,30);


        final TextView toQueue = new TextView(context);
        toQueue.setText("Queue");
        toQueue.setTextColor(Color.BLACK);
        toQueue.setPadding(30, 20, 20, 20);
        toQueue.setTextSize(20);
        lp.addView(toQueue);


        final TextView toPlaylist = new TextView(context);
        toPlaylist.setText("Playlist");
        toPlaylist.setTextSize(20);
        toPlaylist.setTextColor(Color.BLACK);
        toPlaylist.setPadding(30, 20, 20, 20);
        //lp.addView(toPlaylist);

        final TextView toFavorites = new TextView(context);
        toFavorites.setText("Favorites");
        toFavorites.setTextColor(Color.BLACK);
        toFavorites.setPadding(30, 20, 20, 10);
        toFavorites.setTextSize(20);
        //lp.addView(toFavorites);


        if(!user.equalsIgnoreCase("") ) {
            lp.addView(toPlaylist);
            lp.addView(toFavorites);

        }

        alertDialogBuilder.setView(lp);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });


        toPlaylist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //playlistsFragment = new PlaylistsFragment();
                alertDialog.cancel();
                //addToPlaylist = true;
                //podcastItem = value;
                playlistsFragment.addToPlaylistDialog(podcastItem, context);
            }
        });

        toFavorites.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //favoritesFragment = new FavoritesFragment();
                System.out.println("Clicked on: " + podcastItem.programID + ", CurrentUser: " + PreferenceManager.getDefaultSharedPreferences(context).getString("token", "0"));
                try {
                    favoritesFragment.addToFavorites(podcastItem.programID.replace("-", ""), PreferenceManager.getDefaultSharedPreferences(context).getInt("id", 0),
                            "http://media.mw.metropolia.fi/arsu/favourites?token=", PreferenceManager.getDefaultSharedPreferences(context).getString("token", "0"));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                alertDialog.cancel();
            }
        });

        toQueue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //System.out.println("Clicked to queue");
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }
}
