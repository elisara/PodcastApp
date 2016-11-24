package com.example.elisarajaniemi.podcastapp;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by jari on 22/11/2016.
 */

public class GetYlePodcastHelper extends AsyncTask<String, String, String> {
    MainActivity mActivity;

    private final String YLE_API_KEY = "app_key=2acb02a2a89f0d366e569b228320619b&app_id=950fdb28";

    private String result = "";
    public PodcastItems podcastItems = PodcastItems.getInstance();
    public SerieItems serieItems = SerieItems.getInstance();

    public GetYlePodcastHelper(MainActivity mActivity){
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
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                System.out.println("YLE podcast array: " + jsonArray.length());
                ArrayList<String> mediaIDArray = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jObject = jsonArray.getJSONObject(i);
                    JSONArray publicationEventArray = jObject.getJSONArray("publicationEvent");

                    for (int i1 = 0; i1 < publicationEventArray.length(); i1++ ){
                        JSONObject publicationEventObject = publicationEventArray.getJSONObject(i1);
                        if (publicationEventObject.getJSONObject("media").length() > 0) {
                            mediaIDArray.add(publicationEventObject.getJSONObject("media").getString("id"));
                        }
                    }

                    String encryptedURL = "https://external.api.yle.fi/v1/media/playouts.json?program_id=" + jObject.getString("id") + "&protocol=PMD&media_id=" + mediaIDArray.get(i) + "&" + YLE_API_KEY;
                    System.out.println("CryptedURL: " + encryptedURL);

                    PodcastItem podcastItem = new PodcastItem(jObject.getJSONObject("title").getString("fi"), encryptedURL);
                    System.out.println("Yle podcast title: " + podcastItem.title);
                    podcastItems.addPodcastItem(podcastItem);

                }// End Loop

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