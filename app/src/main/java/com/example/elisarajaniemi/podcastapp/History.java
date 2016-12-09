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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by jari on 07/12/2016.
 */

public class History {

    public void getHistoryItems(String url) throws ExecutionException, InterruptedException {
        new GetHistory().execute(url).get();
    }

    public void createHistoryItems(String url, String programID) throws ExecutionException, InterruptedException {
        new CreateHistory().execute(url, programID).get();
    }

    public void deleteHistoryItems(String url, String id, String token) throws ExecutionException, InterruptedException {
        new DeleteHistory().execute(url, id, token).get();
    }
}

class GetHistory extends AsyncTask<Object, String, String> {

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
            URL url = new URL((String) params[0]);
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
class CreateHistory extends AsyncTask<Object, String, String> {
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
            URL url = new URL((String) params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            String input = "{\"podcast_id\":\"" + params[1] + "\"}";
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

class DeleteHistory extends AsyncTask<Object, String, String> {
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
