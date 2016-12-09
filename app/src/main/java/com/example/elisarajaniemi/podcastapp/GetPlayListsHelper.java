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

/**
 * Created by jari on 16/11/2016.
 */

public class GetPlayListsHelper extends AsyncTask<String, String, String> {

    private String result = "";
    Playlists playlists = Playlists.getInstance();

    protected void onPreExecute() {
        super.onPreExecute();
        playlists.clearPlaylists();
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

                    JSONObject jsonObject = jArray.getJSONObject(i);
                    PlaylistItem playlistItem = new PlaylistItem(jsonObject.getInt("id"), jsonObject.getString("playlist_name"));

                    if (playlists.getPlaylists().size() == 0) playlists.addPlaylist(playlistItem);
                    else {
                        boolean idFound = false;
                        for (int k = 0; k < playlists.getPlaylists().size(); k++) {
                            if (playlists.getPlaylists().get(k).id == playlistItem.id)
                                idFound = true;
                        }
                        if (idFound == false) playlists.addPlaylist(playlistItem);
                    }
                }// End Loop

                //System.out.println("Playlists: " + playlists.getPlaylists());
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
        //System.out.println("Users Array: " + result);
        super.onPostExecute(result);
    }

}
