package com.example.elisarajaniemi.podcastapp;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Elisa Rajaniemi on 30.11.2016.
 */

public class Token extends AsyncTask<String, String, String> {

    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String token = "";

        try {
            URL url = new URL("http://media.mw.metropolia.fi/arsu/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String input = "{\"username\":\"" + params[0] + "\",\"password\":\"" + params[1] + "\"}";
            input = input.replace("\n", "");
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
                    System.out.println("Login Output: " + jObject);
                    token = jObject.getString("token");
                    System.out.println("Token: " + token);
                    //user = new User(CurrentUser.getInstance().getCurrentUser().get(0).id, CurrentUser.getInstance().getCurrentUser().get(0).username, CurrentUser.getInstance().getCurrentUser().get(0).email, token);
                    //currentUser.replaceCurrentUser(user);
                    //new GetPlayListsHelper().execute("http://media.mw.metropolia.fi/arsu/playlists/user/"+ currentUser.getCurrentUser().get(0).id + "?token=" + currentUser.getCurrentUser().get(0).token);
                } catch (JSONException e) {
                    System.out.println(e);
                }
            }

            conn.disconnect();
        } catch (
                MalformedURLException e
                ) {
            e.printStackTrace();

        } catch (
                IOException e
                )

        {
            e.printStackTrace();
        }

        return token;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);



    }

}

