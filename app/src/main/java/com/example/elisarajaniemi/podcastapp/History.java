package com.example.elisarajaniemi.podcastapp;

import android.os.AsyncTask;
import android.util.Log;

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
import java.util.concurrent.ExecutionException;

/**
 * Created by jari on 07/12/2016.
 */

public class History {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private HistoryPodcastIDArray historyPodcastIDArray = HistoryPodcastIDArray.getInstance();

    public void getHistory()  {
        DatabaseReference myRef = database.getReference("users/").child(user.getUid()).child("history");


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                historyPodcastIDArray.clearList();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String value = postSnapshot.child("programid").getValue(String.class);
                    //System.out.println("favorite: " + value);
                    PodcastItem podcastItem = new PodcastItem("0", value);
                    historyPodcastIDArray.addPodcastID(podcastItem);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void addToHistory(final String programID) {


        DatabaseReference myRef = database.getReference("users/").child(user.getUid()).child("history");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                historyPodcastIDArray.clearList();
                boolean löyty = false;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String value = postSnapshot.child("programid").getValue(String.class);
                    if (value.equals(programID)) löyty = true;
                }
                if(!löyty) {
                    DatabaseReference myRef = database.getReference("users/").child(user.getUid());
                    myRef.child("history").push().child("programid").setValue(programID);
                    getHistory();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {   }
        });

    }

    public void deleteHistory(final String id)  {
        DatabaseReference myRef = database.getReference("users/").child(user.getUid()).child("history");


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                historyPodcastIDArray.clearList();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String value = postSnapshot.child("programid").getValue(String.class);

                    if (value.equals(id)) {
                        postSnapshot.child("programid").getRef().removeValue();
                    }
                }
                getHistory();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

