package com.example.elisarajaniemi.podcastapp;

import android.os.AsyncTask;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;

/**
 * Created by jari on 22/11/2016.
 */

public class GetYlePodcastHelper extends AsyncTask<String, String, String> {
    MainActivity mActivity;

    private final String YLE_APP_KEY = "app_key=2acb02a2a89f0d366e569b228320619b&app_id=950fdb28";

    private String result = "";
    public ArrayList<PodcastItem> tempPodcastList;
    public PodcastItems podcastItems = PodcastItems.getInstance();
    PlaylistPodcastItems playlistPodcastItems = PlaylistPodcastItems.getInstance();
    FavoritePodcastItems favoritePodcastItems = FavoritePodcastItems.getInstance();
    public PodcastIDArray podcastIDArray = PodcastIDArray.getInstance();

    public GetYlePodcastHelper(MainActivity mActivity) {
        this.mActivity = mActivity;

    }

    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("onPreExecute");
        playlistPodcastItems.clearList();

    }

    @Override
    protected String doInBackground(String... params) {


        try {
            if (params[2].equalsIgnoreCase("fromepisodes")) {
                result = makeConnection(params[0] + params[1]);
                try {
                    podcastItems.addAll(mekePodcastItem(result));
                } catch (JSONException e) {
                    Log.e("JSONException", "Error: " + e.toString());
                }
            } else if (params[2].equalsIgnoreCase("fromplaylist")) {
                System.out.println("From Playlists");
                for (int i = 0; i < podcastIDArray.getItems().size(); i++) {
                    result = makeConnection(params[0] + podcastIDArray.getItems().get(i) + params[1]);
                    try {
                        playlistPodcastItems.addAll(mekePodcastItem(result));
                    } catch (JSONException e) {
                        Log.e("JSONException", "Error: " + e.toString());
                    }
                }

            } else if (params[2].equalsIgnoreCase("fromfavorites")) {
                System.out.println("From Favorites");
                for (int i = 0; i < podcastIDArray.getItems().size(); i++) {
                    result = makeConnection(params[0] + podcastIDArray.getItems().get(i) + params[1]);
                    try {
                        favoritePodcastItems.addAll(mekePodcastItem(result));
                    } catch (JSONException e) {
                        Log.e("JSONException", "Error: " + e.toString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        System.out.println("onPostExecute");

    }

    public ArrayList<PodcastItem> mekePodcastItem(String result) throws JSONException {
        tempPodcastList = new ArrayList<PodcastItem>();

        JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        ArrayList<String> mediaIDArray = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            PodcastItem podcastItem = new PodcastItem();
            JSONObject jObject = jsonArray.getJSONObject(i);
            JSONArray publicationEventArray = jObject.getJSONArray("publicationEvent");

            for (int i1 = 0; i1 < publicationEventArray.length(); i1++) {
                JSONObject publicationEventObject = publicationEventArray.getJSONObject(i1);
                if (publicationEventObject.getJSONObject("media").length() > 0) {
                    mediaIDArray.add(publicationEventObject.getJSONObject("media").getString("id"));
                }
            }

            JSONArray categoryArray = jObject.getJSONArray("subject");
            ArrayList<String> categorys = new ArrayList<>();

            for (int i2 = 0; i2 < categoryArray.length(); i2++) {
                JSONObject categoryObject = categoryArray.getJSONObject(i2);


                categorys.add(categoryObject.getJSONObject("title").getString("fi"));

            }
            String encryptedURL = "https://external.api.yle.fi/v1/media/playouts.json?program_id=" + jObject.getString("id") + "&protocol=PMD&media_id=" + mediaIDArray.get(i) + "&" + YLE_APP_KEY;
            if (jObject.getJSONObject("partOfSeries").getJSONObject("title").has("fi")) {
                System.out.println(jObject.getJSONObject("partOfSeries").getJSONObject("title").getString("fi") + ": https://external.api.yle.fi/v1/programs/items.json?id=" + jObject.getString("id") + "&" + YLE_APP_KEY);

                podcastItem.alterPodcastItem(jObject.getJSONObject("title").getString("fi"), encryptedURL, jObject.getJSONObject("description").getString("fi"),
                        jObject.getJSONObject("partOfSeries").getJSONObject("title").getString("fi"), jObject.getJSONObject("image").getString("id"), jObject.getString("id"), mediaIDArray.get(i), categorys);


                if (tempPodcastList.size() == 0)
                    tempPodcastList.add(podcastItem);
                else {
                    boolean titleFound = false;
                    for (int k = 0; k < tempPodcastList.size(); k++) {
                        if (tempPodcastList.get(k).programID.equalsIgnoreCase(podcastItem.programID)) {

                            titleFound = true;
                        }

                    }
                    if (titleFound == false) tempPodcastList.add(podcastItem);
                }
            }


        }

        return tempPodcastList;
    }

    public String makeConnection(String urli) throws IOException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        URL url = new URL(urli);
        connection = (HttpURLConnection) url.openConnection();
        if (connection.getResponseCode() == 200) {
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            result = buffer.toString();
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

}