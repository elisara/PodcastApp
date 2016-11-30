package com.example.elisarajaniemi.podcastapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
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

public class FavoritesFragment extends Fragment {

    GridView gridView;
    PodcastItems podcastItems = PodcastItems.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorites_layout, container, false);

        gridView = (GridView) view.findViewById(R.id.gridView1);

        ArrayList<PodcastItem> list = podcastItems.getItems();

        gridView.setAdapter(new GridViewAdapter(getContext(), list));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getContext(), ((TextView) v.findViewById(R.id.grid_item_label)).getText(), Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

    public void addToFavorites(String programID, int userID, String url, String token) throws ExecutionException, InterruptedException {
        new CreateFavorites().execute(programID, userID, url, token).get();
    }

    public void getFavorites(String url, String token) throws ExecutionException, InterruptedException {
        new GetFavorites().execute(url, token).get();
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

                System.out.println("Favorites juttuja: " + jsonArray);

                //JSONArray jsonArray = new JSONArray(jObject.getString("content"));
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String podcastID = jsonObject.getString("podcast_id").substring(0,1) + "-" + jsonObject.getString("podcast_id").substring(1, jsonObject.getString("podcast_id").length());
                    //System.out.println("Playlist podcasts: https://external.api.yle.fi/v1/programs/items/" + podcastID + ".json?app_key=2acb02a2a89f0d366e569b228320619b&app_id=950fdb28");
                    podcastIDArray.addPodcastID(podcastID);
                    System.out.println("Added favorite: " + podcastIDArray.getItems().get(i));
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

