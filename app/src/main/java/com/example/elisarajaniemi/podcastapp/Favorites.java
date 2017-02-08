package com.example.elisarajaniemi.podcastapp;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * Created by Elisa Rajaniemi on 24.11.2016.
 */

public class Favorites {

    private PodcastItems podcastItems = PodcastItems.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FavoritePodcastIDArray favoritePodcastIDArray = FavoritePodcastIDArray.getInstance();
    private FavoritePodcastItems favoritePodcastItems = FavoritePodcastItems.getInstance();




    public void addToFavorites(final String programID) {

        DatabaseReference myRef = database.getReference("users/").child(user.getUid()).child("favorites");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favoritePodcastIDArray.clearList();
                boolean löyty = false;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String value = postSnapshot.child("programid").getValue(String.class);
                    if (value.equals(programID)) löyty = true;
                }
                if(!löyty) {
                    DatabaseReference myRef = database.getReference("users/").child(user.getUid());
                    myRef.child("favorites").push().child("programid").setValue(programID);
                    getFavorites();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {   }
        });
    }

    public void getFavorites() {
        DatabaseReference myRef = database.getReference("users/").child(user.getUid()).child("favorites");


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favoritePodcastIDArray.clearList();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String value = postSnapshot.child("programid").getValue(String.class);
                    //System.out.println("favorite: " + value);
                    PodcastItem podcastItem = new PodcastItem("0", value);
                    favoritePodcastIDArray.addPodcastID(podcastItem);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void deleteFavorites(final String id) throws ExecutionException, InterruptedException {
        DatabaseReference myRef = database.getReference("users/").child(user.getUid()).child("favorites");


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favoritePodcastIDArray.clearList();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String value = postSnapshot.child("programid").getValue(String.class);

                    if (value.equals(id)) {
                        postSnapshot.child("programid").getRef().removeValue();
                    }
                }
                getFavorites();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void deleteFavoritesDialog(final Context context, final PodcastIDArray podcastIDArray, final int favoriteID, final FavoritePodcastItems favoritePodcastItems, final ExpandableListViewAdapter listViewAdapter) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
        //AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.CustomDialog));
        alertDialogBuilder.setTitle("Delete favorite");

        LinearLayout lp = new LinearLayout(context);
        lp.setOrientation(LinearLayout.VERTICAL);
        lp.setPadding(30, 0, 30, 30);


        final TextView toQueue = new TextView(context);
        toQueue.setText("Do you really want to delete this favorite?");
        toQueue.setTextColor(Color.BLACK);
        toQueue.setPadding(30, 20, 20, 20);
        toQueue.setTextSize(20);
        lp.addView(toQueue);

        alertDialogBuilder.setView(lp);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    deleteFavorites(podcastIDArray.getItems().get(favoriteID - 1).programID);

                    favoritePodcastItems.deletePodcast(favoriteID - 1);
                    listViewAdapter.notifyDataSetChanged();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        alertDialog.show();

    }

}







