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
 * Created by jari on 28/10/2016.
 */

public class HttpGetHelper extends AsyncTask<String, String, String> {

    String result = "";
    public PodcastItems podcastItems = PodcastItems.getInstance();
    public SerieItems serieItems = SerieItems.getInstance();
    public boolean executed;
    private SerieFragment serieFragment;

    protected void onPreExecute() {
        super.onPreExecute();
        serieFragment = new SerieFragment();
        executed = false;
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
                //Log.d("Response: ", "> " + line);
            }
            result = buffer.toString();

            /**try {
             JSONArray jArray = new JSONArray(result);
             for (int i = 0; i < jArray.length(); i++) {

             JSONArray finalArray = jArray.getJSONArray(i);
             //System.out.println("Final Array info: " + finalArray.toString());
             for (int j = 0; j < finalArray.length(); j++) {

             JSONObject jObject = finalArray.getJSONObject(j);

             PodcastItem podcastItem = new PodcastItem(jObject.getString("Title"), jObject.getString("Download link"), jObject.getString("Description"),
             jObject.getInt("Length (sec)"), jObject.getString("Tags"), jObject.getString("Tags"), jObject.getString("Collection name"),
             jObject.getInt("Collection ID"), jObject.getString("Location - longitude"));

             podcastItems.add(podcastItem);
             System.out.println("Podcast info: " + podcastItem.title);
             }

             } // End Loop
             executed = true;
             System.out.println("Array size: " + podcastItems.size());
             } catch (JSONException e) {
             Log.e("JSONException", "Error: " + e.toString());
             } // catch (JSONException e)*/


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
        //System.out.println("onPostExecute: " + result);
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {

                JSONArray finalArray = jArray.getJSONArray(i);
                for (int j = 0; j < finalArray.length(); j++) {

                    JSONObject jObject = finalArray.getJSONObject(j);

                    PodcastItem podcastItem = new PodcastItem(jObject.getString("Title"), jObject.getString("Download link"), jObject.getString("Description"),
                            jObject.getInt("Length (sec)"), jObject.getString("Tags"), jObject.getString("Tags"), jObject.getString("Collection name"),
                            jObject.getInt("Collection ID"), jObject.getString("Location - longitude"));

                    podcastItems.addPodcastItem(podcastItem);
                    if (serieItems.getSerieItems().size() == 0) serieItems.addSerieItem(podcastItem);
                    else {
                        boolean idFound = false;
                        for (int k = 0; k < serieItems.getSerieItems().size(); k++) {
                            if (serieItems.getSerieItems().get(k).collectionID == podcastItem.collectionID) idFound = true;
                        }
                        if (idFound == false) serieItems.addSerieItem(podcastItem);

                    }
                }

            }// End Loop
            System.out.println("SeriID array size: " + serieItems.getSerieItems().size());

            /**for(int i = 0; i <podcastItems.getItems().size(); i++){
             if (!serieItems.getSerieItems().contains(podcastItems.getItems().get(i).collectionID)){
             serieItems.addSerieItem(podcastItems.getItems().get(i));
             }
             System.out.println("SerieItems array: " + serieItems.getSerieItems().size());
             }*/
        } catch (JSONException e) {
            Log.e("JSONException", "Error: " + e.toString());
        }

    }

}