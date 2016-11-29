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
    public PodcastItems podcastItems = PodcastItems.getInstance();
    PlaylistPodcastItems playlistPodcastItems = PlaylistPodcastItems.getInstance();
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


        HttpURLConnection connection = null;
        BufferedReader reader = null;


        try {
            if (params[2].equalsIgnoreCase("false")) {
                System.out.println("GetYlePodcastHelper IF");
                URL url = new URL(params[0] + params[1]);
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
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    ArrayList<String> mediaIDArray = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jObject = jsonArray.getJSONObject(i);
                        JSONArray publicationEventArray = jObject.getJSONArray("publicationEvent");

                        for (int i1 = 0; i1 < publicationEventArray.length(); i1++) {
                            JSONObject publicationEventObject = publicationEventArray.getJSONObject(i1);
                            if (publicationEventObject.getJSONObject("media").length() > 0) {
                                mediaIDArray.add(publicationEventObject.getJSONObject("media").getString("id"));
                            }
                        }

                        String encryptedURL = "https://external.api.yle.fi/v1/media/playouts.json?program_id=" + jObject.getString("id") + "&protocol=PMD&media_id=" + mediaIDArray.get(i) + "&" + YLE_APP_KEY;
                        System.out.println(jObject.getJSONObject("partOfSeries").getJSONObject("title").getString("fi") + ": https://external.api.yle.fi/v1/programs/items.json?id=" + jObject.getString("id") + "&" + YLE_APP_KEY);

                        PodcastItem podcastItem = new PodcastItem(jObject.getJSONObject("title").getString("fi"), encryptedURL, jObject.getJSONObject("description").getString("fi"),
                                jObject.getJSONObject("partOfSeries").getJSONObject("title").getString("fi"), jObject.getJSONObject("image").getString("id"), jObject.getString("id"), mediaIDArray.get(i));
                        //System.out.println("Yle podcast collection name: " + jObject.getJSONObject("partOfSeries").getJSONObject("title").getString("fi"));
                        if (podcastItems.getItems().size() == 0)
                            podcastItems.addPodcastItem(podcastItem);
                        else {
                            boolean titleFound = false;
                            for (int k = 0; k < podcastItems.getItems().size(); k++) {
                                if (podcastItems.getItems().get(k).programID.equalsIgnoreCase(podcastItem.programID))
                                    titleFound = true;
                            }
                            if (titleFound == false) podcastItems.addPodcastItem(podcastItem);
                        }
                    }// End Loop

                } catch (JSONException e) {
                    Log.e("JSONException", "Error: " + e.toString());
                }
            } else {
                System.out.println("GetYlePodcastHelper ELSE, boolean arvo: " + params[2]);
                for (int i = 0; i < podcastIDArray.getItems().size(); i++) {
                    System.out.println("GetYlePodcastHelper FOR, I arvo: " + i);
                    URL url = new URL(params[0] + podcastIDArray.getItems().get(i) + params[1]);
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

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONObject jObject = new JSONObject(jsonObject.getString("data"));
                            ArrayList<String> mediaIDArray = new ArrayList<>();
                            //JSONArray jsonArray = jsonObject.getJSONArray("data");
                            //ArrayList<String> mediaIDArray = new ArrayList<>();

                            //JSONObject jObject = jsonArray.getJSONObject(i);
                            JSONArray publicationEventArray = jObject.getJSONArray("publicationEvent");

                            for (int i1 = 0; i1 < publicationEventArray.length(); i1++) {
                                JSONObject publicationEventObject = publicationEventArray.getJSONObject(i1);
                                if (publicationEventObject.getJSONObject("media").length() > 0) {
                                    mediaIDArray.add(0, publicationEventObject.getJSONObject("media").getString("id"));
                                }
                            }

                            String encryptedURL = "https://external.api.yle.fi/v1/media/playouts.json?program_id=" + jObject.getString("id") + "&protocol=PMD&media_id=" + mediaIDArray.get(0) + "&" + YLE_APP_KEY;
                            PodcastItem podcastItem = new PodcastItem(jObject.getJSONObject("title").getString("fi"), encryptedURL, jObject.getJSONObject("description").getString("fi"),
                                    jObject.getJSONObject("partOfSeries").getJSONObject("title").getString("fi"), jObject.getJSONObject("image").getString("id"), jObject.getString("id"), mediaIDArray.get(0));
                            //System.out.println("Yle podcast collection name: " + jObject.getJSONObject("partOfSeries").getJSONObject("title").getString("fi"));
                            if (playlistPodcastItems.getItems().size() == 0)
                                playlistPodcastItems.addPodcastItem(podcastItem);
                            else {
                                boolean titleFound = false;
                                for (int k = 0; k < playlistPodcastItems.getItems().size(); k++) {
                                    if (playlistPodcastItems.getItems().get(k).programID.equalsIgnoreCase(podcastItem.programID))
                                        titleFound = true;
                                }
                                if (titleFound == false)
                                    playlistPodcastItems.addPodcastItem(podcastItem);
                            }

                        } catch (JSONException e) {
                            Log.e("JSONException", "Error: " + e.toString());
                        }
                    }
                }
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
        System.out.println("onPostExecute");

    }

}