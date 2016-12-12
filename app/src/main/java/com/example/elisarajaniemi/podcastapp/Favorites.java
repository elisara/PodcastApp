package com.example.elisarajaniemi.podcastapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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

    PodcastItems podcastItems = PodcastItems.getInstance();



    public void addToFavorites(String programID, int userID, String url, String token) throws ExecutionException, InterruptedException {
        new CreateFavorites().execute(programID, userID, url, token).get();
    }

    public void getFavorites(String url, String token) throws ExecutionException, InterruptedException {
        new GetFavorites().execute(url, token).get();
    }

    public void deleteFavorites(String url, String id, String token) throws ExecutionException, InterruptedException {
        new DeleteFavorites().execute(url, id, token).get();
    }

    public void deleteFavoritesDialog(final Context context, final PodcastIDArray podcastIDArray, final int favoriteID, final FavoritePodcastItems favoritePodcastItems, final ExpandableListViewAdapter listViewAdapter){
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
                    deleteFavorites("http://media.mw.metropolia.fi/arsu/favourites/", podcastIDArray.getItems().get(favoriteID).id, "?token=" + PreferenceManager.getDefaultSharedPreferences(context).getString("token", "0"));
                    favoritePodcastItems.deletePodcast(favoriteID);
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

class CreateFavorites extends AsyncTask<Object, String, String> {
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
            URL url = new URL((String) params[2] + params[3]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            String input = "{\"podcast_id\":\"" + params[0] + "\",\"user_id\":\"" + params[1] + "\"}";
            //input = input.replace("\n", "");
            //System.out.println(input);

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
                    //System.out.println("Database message: " + message);
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

class GetFavorites extends AsyncTask<Object, String, String> {

    private String result = "";
    private PodcastIDArray podcastIDArray = PodcastIDArray.getInstance();

    protected void onPreExecute() {
        super.onPreExecute();
        podcastIDArray.clearList();
    }

    @Override
    protected String doInBackground(Object... params) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL((String) params[0] + params[1]);
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
                JSONArray jsonArray  = new JSONArray(result);

                //System.out.println("Favorites juttuja: " + jsonArray);

                //JSONArray jsonArray = new JSONArray(jObject.getString("content"));
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    PodcastItem podcastItem = new PodcastItem(jsonObject.getString("id"), jsonObject.getString("podcast_id").substring(0,1) + "-" + jsonObject.getString("podcast_id").substring(1, jsonObject.getString("podcast_id").length()));
                    //System.out.println("Playlist podcasts: https://external.api.yle.fi/v1/programs/items/" + podcastID + ".json?app_key=2acb02a2a89f0d366e569b228320619b&app_id=950fdb28");
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

class DeleteFavorites extends AsyncTask<Object, String, String> {
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
                    //System.out.println("Database message: " + message);
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

