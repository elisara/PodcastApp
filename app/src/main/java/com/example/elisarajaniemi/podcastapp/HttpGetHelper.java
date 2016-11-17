package com.example.elisarajaniemi.podcastapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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
    MainActivity mActivity;

    private String result = "";
    public PodcastItems podcastItems = PodcastItems.getInstance();
    public SerieItems serieItems = SerieItems.getInstance();

    public HttpGetHelper(MainActivity mActivity){
        this.mActivity=mActivity;

    }
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
            }
            result = buffer.toString();
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {

                    JSONArray finalArray = jArray.getJSONArray(i);
                    for (int j = 0; j < finalArray.length(); j++) {

                        JSONObject jObject = finalArray.getJSONObject(j);

                        Bitmap bmp = mActivity.imageLoader.loadImageSync(jObject.getString("Location - longitude"));

                        PodcastItem podcastItem = new PodcastItem(jObject.getString("Title"), jObject.getString("Download link"), jObject.getString("Description"),
                                jObject.getInt("Length (sec)"), jObject.getString("Tags"), jObject.getString("Tags"), jObject.getString("Collection name"),
                                jObject.getInt("Collection ID"), jObject.getString("Location - longitude"), bmp);

                        //podcastItems.addPodcastItem(podcastItem);
                        System.out.println("Added " + podcastItem.title);
                        if (serieItems.getSerieItems().size() == 0) serieItems.addSerieItem(podcastItem);
                        else {
                            boolean idFound = false;
                            for (int k = 0; k < serieItems.getSerieItems().size(); k++) {
                                if (serieItems.getSerieItems().get(k).collectionID == podcastItem.collectionID) idFound = true;
                            }
                            if (idFound == false) serieItems.addSerieItem(podcastItem);

                        }
                        if (podcastItems.getItems().size() == 0) podcastItems.addPodcastItem(podcastItem);
                        else {
                            boolean titleFound = false;
                            for (int k = 0; k < podcastItems.getItems().size(); k++) {
                                if (podcastItems.getItems().get(k).title.equalsIgnoreCase( podcastItem.title)) titleFound = true;
                            }
                            if (titleFound == false) podcastItems.addPodcastItem(podcastItem);
                        }
                    }

                }// End Loop

                //System.out.println("SeriID array size: " + serieItems.getSerieItems().size());

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