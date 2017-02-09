package com.example.elisarajaniemi.podcastapp;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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


public class PlaylistsFragment extends Fragment {

    private final String TOKEN = "";

    private Thread t;
    private ListView listView;
    private PlaylistsArrayAdapter adapter;
    private MenuFragment mf;
    private String playlistName, message;
    private ArrayList<PlaylistItem> list;
    Playlists playlists = Playlists.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private boolean toExistingPlaylist = false;
    private PodcastItem playListPodcastItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlist_layout, container, false);

        mf = new MenuFragment();
        list = new ArrayList<>();

        playListPodcastItem = new PodcastItem();

        listView = (ListView) view.findViewById(R.id.playlist_list);
        adapter = new PlaylistsArrayAdapter(getContext(), playlists.getPlaylists());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                PlaylistItem value = playlists.getPlaylists().get(position);

                try {
                    new GetPlaylistPodcasts().execute("http://media.mw.metropolia.fi/arsu/playlists/"+value.id +
                            "?token=" + PreferenceManager.getDefaultSharedPreferences(getContext()).getString("token", "0")).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


                CollectionFragment collectionFragment = new CollectionFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("playlistID", value.id);
                collectionFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, collectionFragment).addToBackStack( "playlistFragment" ).commit();
            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                final PlaylistItem value = playlists.getPlaylists().get(position);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
                alertDialogBuilder.setTitle("Delete Playlist");

                System.out.println("PlaylistID: " + position);

                String user = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("user", "");

                LinearLayout lp = new LinearLayout(getContext());
                lp.setOrientation(LinearLayout.VERTICAL);
                lp.setPadding(30,0,30,30);


                final TextView toQueue = new TextView(getContext());
                toQueue.setText("Do you really want to delete this playlist?");
                toQueue.setTextColor(Color.BLACK);
                toQueue.setPadding(30, 20, 20, 20);
                toQueue.setTextSize(20);
                lp.addView(toQueue);

                alertDialogBuilder.setView(lp);
                final AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            new DeletePlaylist().execute("http://media.mw.metropolia.fi/arsu/playlists/", value.id , "?token=" + PreferenceManager.getDefaultSharedPreferences(getContext()).getString("token", "")).get();
                            playlists.deletePlaylist(position);
                            adapter.notifyDataSetChanged();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
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

                return true;
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


    public void createNewPlaylist(final Context context){
        AlertDialog.Builder alertDialogBuilder =  new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));

        alertDialogBuilder.setTitle("Create new playlist");
        alertDialogBuilder.setMessage("Name of the playlist:");

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialogBuilder.setView(input);


        alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {

                playlistName = input.getText().toString();


                DatabaseReference myRef = database.getReference("users/").child(user.getUid());

                if (toExistingPlaylist == true){
                    myRef.child("playlists").child(playlistName).push().setValue(playListPodcastItem.programID);
                } else{
                    myRef.child("playlists").child(playlistName).child("0").setValue("tyhjää");
                }


                ArrayList<PodcastItem> addedList = new ArrayList<PodcastItem>();
            }
        })
                .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void addToPlaylistDialog(final PodcastItem podcastItem, final Context context){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
        alertDialogBuilder.setTitle("Add to playlist");

        LinearLayout lp = new LinearLayout(context);
        lp.setOrientation(LinearLayout.VERTICAL);
        lp.setPadding(30,30,30,30);

        final ImageButton addPlaylist = new ImageButton(context);
        addPlaylist.setImageResource(R.drawable.ic_add_black_24dp);
        lp.addView(addPlaylist);

        final ListView toPlaylist = new ListView(context);
        adapter = new PlaylistsArrayAdapter(context, playlists.getPlaylists());
        toPlaylist.setAdapter(adapter);
        toPlaylist.setPadding(30, 20, 20, 20);
        lp.addView(toPlaylist);

        alertDialogBuilder.setView(lp);
        final AlertDialog alertDialog2 = alertDialogBuilder.create();

        //Add podcast to existing playlist
        toPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View v, int position, long rowId) {
                alertDialog2.cancel();
                PlaylistItem playlistItem = playlists.getPlaylists().get(position);

                DatabaseReference myRef = database.getReference("users/").child(user.getUid());
                myRef.child("playlists").child(playlistItem.name).push().setValue(podcastItem.programID);


                //new PutPodcastToPlaylist().execute(podcastItem.programID.replace("-", ""), "http://media.mw.metropolia.fi/arsu/playlists/" + playlistItem.id + "?token=" + TOKEN);

            }
        });
        //create a new playlist
        addPlaylist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toExistingPlaylist = true;
                playListPodcastItem = podcastItem;
                createNewPlaylist(context);
                alertDialog2.cancel();


            }
        });
        alertDialog2.show();
    }
}




/**class CreateNewPlaylist extends AsyncTask<Object, String, String> {


    String message;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
    @Override
    protected String doInBackground(Object... params) {


        try {
            URL url = new URL("http://media.mw.metropolia.fi/arsu/playlists?token=" + params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            String input = "{\"playlist_name\":\"" + params[1] + "\",\"user_id\":\"" + params[2] + "\"}";
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
        return null;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        System.out.println("Result: " + result);
    }
}*/


class PutPodcastToPlaylist extends AsyncTask<Object, String, String> {
    //ProgressDialog pdLoading = new ProgressDialog(AsyncExample.this);

    String message;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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


            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

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

    }


}


class GetPlaylistPodcasts extends AsyncTask<String, String, String> {


    private String result = "";
    private PodcastIDArray podcastIDArray = PodcastIDArray.getInstance();


    protected void onPreExecute() {
        super.onPreExecute();
        podcastIDArray.clearList();
    }


    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";


            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            result = buffer.toString();
            try {
                JSONObject jObject = new JSONObject(result);

                JSONArray jsonArray = new JSONArray(jObject.getString("content"));
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    PodcastItem podcastItem = new PodcastItem( jsonObject.getString("podcast_id").substring(0,1) + "-" + jsonObject.getString("podcast_id").substring(1, jsonObject.getString("podcast_id").length()));
                    podcastIDArray.addPodcastID(podcastItem);
                }


            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }


}

class DeletePlaylist extends AsyncTask<Object, String, String> {
    String message;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Object... params) {

        try {
            URL url = new URL((String) params[0] + params[1] + params[2]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json");

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
}






