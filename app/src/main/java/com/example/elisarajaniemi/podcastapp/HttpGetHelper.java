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
    public ArrayList<PodcastItem> podcastItems = new ArrayList<>();

    protected void onPreExecute() {
        super.onPreExecute();
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
                Log.d("Response: ", "> " + line);
            }
            result = buffer.toString();

            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {

                    JSONArray finalArray = jArray.getJSONArray(i);
                    for (int j = 0; j < finalArray.length(); j++) {

                        JSONObject jObject = finalArray.getJSONObject(j);

                        PodcastItem podcastItem = new PodcastItem(jObject.getString("Title"), jObject.getString("Download link"), jObject.getString("Description"),
                                jObject.getInt("Length (sec)"), jObject.getString("Tags"), jObject.getString("Tags"), jObject.getString("Collection name"),
                                jObject.getInt("Collection ID"), jObject.getString("Location - longitude"));

                        podcastItems.add(podcastItem);
                    }

                } // End Loop
                System.out.println("PodcastItems array: " + podcastItems.get(2).title.toString());
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)


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

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    public ArrayList<PodcastItem> getResults(){
        return this.podcastItems;
    }

}