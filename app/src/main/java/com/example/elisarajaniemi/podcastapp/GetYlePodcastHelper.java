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
    HistoryPodcastItems historyPodcastItems = HistoryPodcastItems.getInstance();
    SerieItems serieItems = SerieItems.getInstance();
    public PodcastIDArray podcastIDArray = PodcastIDArray.getInstance();

    public GetYlePodcastHelper(MainActivity mActivity) {
        this.mActivity = mActivity;

    }

    protected void onPreExecute() {
        super.onPreExecute();
        playlistPodcastItems.clearList();

    }

    @Override
    protected String doInBackground(String... params) {

        try {
            switch (params[2]) {
                case "fromepisodes":
                    result = makeConnection(params[0] + params[1]);
                    System.out.println("Kuunnelluimmat 50 jSon: " + params[0] + params[1]);
                    try {
                        podcastItems.addAll(makePodcastItem(result));
                    } catch (JSONException e) {
                        Log.e("JSONException1", "Error: " + e.toString());
                    }
                    break;
                case "fromplaylist":
                    playlistPodcastItems.clearList();
                    for (int i = 0; i < podcastIDArray.getItems().size(); i++) {
                        result = makeConnection(params[0] + podcastIDArray.getItems().get(i).programID + params[1]);
                        try {
                            playlistPodcastItems.addPodcastItem(getSinglePodcast(new JSONObject(result).getJSONObject("data")));
                        } catch (JSONException e) {
                            Log.e("JSONException2", "Error: " + e.toString());
                        }
                    }
                    break;
                case "fromfavorites":
                    favoritePodcastItems.clearList();
                    for (int i = 0; i < podcastIDArray.getItems().size(); i++) {
                        //System.out.println("PodCastIDArray value: " + podcastIDArray.getItems().get(i));
                        result = makeConnection(params[0] + podcastIDArray.getItems().get(i).programID + params[1]);
                        try {
                            favoritePodcastItems.addPodcastItem(getSinglePodcast(new JSONObject(result).getJSONObject("data")));
                        } catch (JSONException e) {
                            Log.e("JSONException3", "Error: " + e.toString());
                        }
                    }
                    break;
                case "fromHistory":
                    historyPodcastItems.clearList();
                    for (int i = 0; i < podcastIDArray.getItems().size(); i++) {
                        result = makeConnection(params[0] + podcastIDArray.getItems().get(i).programID + params[1]);
                        try {
                            historyPodcastItems.addPodcastItem(getSinglePodcast(new JSONObject(result).getJSONObject("data")));
                        } catch (JSONException e) {
                            Log.e("JSONException4", "Error: " + e.toString());
                        }
                    }
                    break;
                case "fromseries":
                    serieItems.clearList();
                    result = makeConnection(params[0] + params[1]);
                    System.out.println("Koko sarjan jSon: " + params[0] + params[1]);
                    try {
                        serieItems.addAll(makePodcastItem(result));
                    } catch (JSONException e) {
                        Log.e("JSONException1", "Error: " + e.toString());
                    }
                    break;
            }

            /**if (params[2].equalsIgnoreCase("fromepisodes")) {
                result = makeConnection(params[0] + params[1]);
                System.out.println("Koko lista: " + params[0] + params[1]);
                try {
                    podcastItems.addAll(makePodcastItem(result));
                } catch (JSONException e) {
                    Log.e("JSONException1", "Error: " + e.toString());
                }
            } else if (params[2].equalsIgnoreCase("fromplaylist")) {
                System.out.println("From Playlists");

                playlistPodcastItems.clearList();
                for (int i = 0; i < podcastIDArray.getItems().size(); i++) {
                    result = makeConnection(params[0] + podcastIDArray.getItems().get(i).programID + params[1]);
                    try {
                        playlistPodcastItems.addPodcastItem(getSinglePodcast(new JSONObject(result).getJSONObject("data")));
                    } catch (JSONException e) {
                        Log.e("JSONException2", "Error: " + e.toString());
                    }
                }

            } else if (params[2].equalsIgnoreCase("fromfavorites")) {
                System.out.println("From Favorites");
                favoritePodcastItems.clearList();
                for (int i = 0; i < podcastIDArray.getItems().size(); i++) {
                    System.out.println("PodCastIDArray value: " + podcastIDArray.getItems().get(i));
                    result = makeConnection(params[0] + podcastIDArray.getItems().get(i).programID + params[1]);
                    try {
                        favoritePodcastItems.addPodcastItem(getSinglePodcast(new JSONObject(result).getJSONObject("data")));
                    } catch (JSONException e) {
                        Log.e("JSONException3", "Error: " + e.toString());
                    }
                }
            } else if (params[2].equalsIgnoreCase("fromHistory")) {
                System.out.println("From Favorites");
                historyPodcastItems.clearList();
                for (int i = 0; i < podcastIDArray.getItems().size(); i++) {
                    result = makeConnection(params[0] + podcastIDArray.getItems().get(i).programID + params[1]);
                    try {
                        historyPodcastItems.addPodcastItem(getSinglePodcast(new JSONObject(result).getJSONObject("data")));
                    } catch (JSONException e) {
                        Log.e("JSONException4", "Error: " + e.toString());
                    }
                }
            } else if (params[2].equalsIgnoreCase("fromSeries")) {
                System.out.println("From Series");

            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);


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

    public int podcastLength(String pituus) {
        String pituusString = pituus;
        int pituusInt = 0;
        int i = pituusString.indexOf("H");
        if (i >= 0) {
            pituusInt = 3600 * Integer.parseInt(pituusString.substring(0, i));
            pituusString = pituusString.substring(i + 1);
        }
        i = pituusString.indexOf("M");
        if (i >= 0) {
            pituusInt = pituusInt + 60 * Integer.parseInt(pituusString.substring(0, i));
            pituusString = pituusString.substring(i + 1);
        }
        i = pituusString.indexOf("S");
        if (i >= 0) {
            pituusInt = pituusInt + Integer.parseInt(pituusString.substring(0, i));
        }
        return pituusInt;
    }

    public ArrayList<PodcastItem> makePodcastItem(String result) throws JSONException {
        tempPodcastList = new ArrayList<PodcastItem>();

        JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            PodcastItem podcastItem = new PodcastItem();
            //System.out.print(i + ". ");
            podcastItem = getSinglePodcast(jsonArray.getJSONObject(i));


            if (podcastItem.programID != null) {
                if (tempPodcastList.size() == 0)//&& podcastItem.length < 600
                    tempPodcastList.add(podcastItem);
                else {
                    boolean titleFound = false;
                    for (int k = 0; k < tempPodcastList.size(); k++) {
                        if (tempPodcastList.get(k).programID.equalsIgnoreCase(podcastItem.programID)) {
                            titleFound = true;
                        }
                    }
                    if (titleFound == false) { //&& podcastItem.length < 600
                        tempPodcastList.add(0, podcastItem);
                    }
                }
            }
        }

        return tempPodcastList;
    }

    public PodcastItem getSinglePodcast(JSONObject jObject) throws JSONException {

        PodcastItem podcastItem = new PodcastItem();
        JSONArray publicationEventArray = jObject.getJSONArray("publicationEvent");
        String mediaID = "";

        for (int i1 = 0; i1 < publicationEventArray.length(); i1++) {
            JSONObject publicationEventObject = publicationEventArray.getJSONObject(i1);
            if (publicationEventObject.getJSONObject("media").length() > 0) {
                mediaID = publicationEventObject.getJSONObject("media").getString("id");
            }
        }

        JSONArray categoryArray = jObject.getJSONArray("subject");
        ArrayList<String> categorys = new ArrayList<>();

        for (int i2 = 0; i2 < categoryArray.length(); i2++) {
            JSONObject categoryObject = categoryArray.getJSONObject(i2);

            if (categoryObject.getJSONObject("title").has("fi"))
                categorys.add(categoryObject.getJSONObject("title").getString("fi"));

        }


        String encryptedURL = "https://external.api.yle.fi/v1/media/playouts.json?program_id=" + jObject.getString("id") + "&protocol=PMD&media_id=" + mediaID + "&" + YLE_APP_KEY;

        //System.out.println(jObject.getJSONObject("partOfSeries").getJSONObject("title").getString("fi") + ": https://external.api.yle.fi/v1/programs/items.json?id=" + jObject.getString("id") + "&" + YLE_APP_KEY);
        if (jObject.getJSONObject("itemTitle").has("fi"))
            podcastItem.setTitle(jObject.getJSONObject("itemTitle").getString("fi"));
        podcastItem.setURL(encryptedURL);
        if (jObject.getJSONObject("description").has("fi"))
            podcastItem.setDescription(jObject.getJSONObject("description").getString("fi"));
        if (jObject.getJSONObject("partOfSeries").getJSONObject("title").has("fi"))
            podcastItem.setCollectionName(jObject.getJSONObject("partOfSeries").getJSONObject("title").getString("fi"));
        if (jObject.getJSONObject("image").has("id"))
            podcastItem.setImageURL(jObject.getJSONObject("image").getString("id"));
        if (jObject.has("id")) podcastItem.setProgramID(jObject.getString("id"));
        podcastItem.setMediaID(mediaID);
        podcastItem.setCategorys(categorys);
        podcastItem.setLength(podcastLength(jObject.getString("duration").substring(2)));
        if (jObject.getJSONObject("partOfSeries").has("id"))
        podcastItem.setSerieID(jObject.getJSONObject("partOfSeries").getString("id"));
        if (jObject.getJSONObject("partOfSeries").getJSONObject("image").has("id"))
        podcastItem.setSerieImageURL(jObject.getJSONObject("partOfSeries").getJSONObject("image").getString("id"));
        //podcastItem.alterPodcastItem(jObject.getJSONObject("title").getString("fi"), encryptedURL, jObject.getJSONObject("description").getString("fi"), jObject.getJSONObject("partOfSeries").getJSONObject("title").getString("fi"), jObject.getJSONObject("image").getString("id"), jObject.getString("id"), mediaID, categorys, podcastLength(jObject.getString("duration").substring(2)));

        if(!jObject.getJSONObject("itemTitle").has("fi")||!jObject.getJSONObject("description").has("fi")||!jObject.getJSONObject("partOfSeries").getJSONObject("title").has("fi")) podcastItem.setProgramID(null);

        return podcastItem;
    }

}