package com.example.elisarajaniemi.podcastapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jari on 28/10/2016.
 */

public class HttpGetHelper extends AsyncTask<String, String, String> {

    String result = "";

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
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
            }
            result = buffer.toString();

            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {

                    JSONArray finalArray = jArray.getJSONArray(i);
                    for (int j = 0; j < finalArray.length(); j++) {

                        JSONObject jObject = finalArray.getJSONObject(j);
                        System.out.println("Yksi objekti: " + jObject);

                        /**String name = jObject.getString("name");
                         String tab1_text = jObject.getString("tab1_text");
                         int active = jObject.getInt("active");
                         */
                    }

                } // End Loop
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

}


/**
 * private String apiKey;
 *
 * @Override protected String doInBackground(String... args) {
 * <p>
 * try {
 * URL url = new URL(args[0]);
 * HttpURLConnection conn = (HttpURLConnection) url.openConnection();
 * conn.setDoOutput(true);
 * conn.setRequestMethod("POST");
 * conn.setRequestProperty("Content-Type", "application/json");
 * <p>
 * String input = "{\"username\":\"podcast\",\"password\":\"podcast16\"}";
 * <p>
 * OutputStream os = conn.getOutputStream();
 * os.write(input.getBytes());
 * os.flush();
 * <p>
 * BufferedReader br = new BufferedReader(new InputStreamReader(
 * (conn.getInputStream())));
 * <p>
 * String output;
 * System.out.println("Output from Server .... \n");
 * while ((output = br.readLine()) != null) {
 * try {
 * JSONObject jObject = new JSONObject(output);
 * apiKey = jObject.getString("api_key");
 * System.out.println(apiKey);
 * JsonElement jsonElement = new JsonParser().parse(getJSON("http://dev.mw.metropolia.fi/aanimaisema/plugins/api_audio_search/index.php/?key=" + apiKey + "&category=%20&link=true", 20000));
 * jsonArray = jsonElement.getAsJsonArray();
 * } catch (JSONException e) {
 * System.out.println(e);
 * }
 * }
 * conn.disconnect();
 * } catch (
 * MalformedURLException e
 * ) {
 * e.printStackTrace();
 * <p>
 * } catch (
 * IOException e
 * )
 * <p>
 * {
 * e.printStackTrace();
 * }
 * <p>
 * return null;
 * }
 * @Override protected void onPostExecute(String jsonArray) {
 * <p>
 * //Something needs to be done here
 * }
 * <p>
 * public String getJSON(String url, int timeout) {
 * HttpURLConnection c = null;
 * try {
 * URL u = new URL(url);
 * c = (HttpURLConnection) u.openConnection();
 * c.setRequestMethod("GET");
 * c.setRequestProperty("Content-length", "0");
 * c.setUseCaches(false);
 * c.setAllowUserInteraction(false);
 * c.setConnectTimeout(timeout);
 * c.setReadTimeout(timeout);
 * c.connect();
 * int status = c.getResponseCode();
 * <p>
 * switch (status) {
 * case 200:
 * case 201:
 * BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
 * StringBuilder sb = new StringBuilder();
 * String line;
 * while ((line = br.readLine()) != null) {
 * sb.append(line+"\n");
 * }
 * br.close();
 * return sb.toString();
 * }
 * <p>
 * } catch (MalformedURLException ex) {
 * Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
 * } catch (IOException ex) {
 * Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
 * } finally {
 * if (c != null) {
 * try {
 * c.disconnect();
 * } catch (Exception ex) {
 * Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
 * }
 * }
 * }
 * return null;
 * }
 * <p>
 * }
 */