package com.example.elisarajaniemi.podcastapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

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
 * Created by Elisa Rajaniemi on 10.11.2016.
 */

public class RegisterAndLogin{

    private final String TOKEN = "";

    private boolean loggedIn;
    private User user;
    Users users = Users.getInstance();
    private Thread t;
    private boolean exists;
    private boolean registered;
    private String encryptedPassword, encryptedUsername, encryptedEmail;
    private MyCrypt myCrypt = new MyCrypt();
    private String token;
    private int userID;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;


    public boolean registerUser(String username, String password, String password2, String email, Context context) throws ExecutionException, InterruptedException {

        exists = false;
        testIfExists(username, password, context);

        if (password.equals(password2)) {
            //testIfExists(username, password, context);
            System.out.println("Register: Password match");

            if (this.exists == false && !loggedIn) {
                System.out.println("Register: User didn't exist");
                this.encryptedUsername = myCrypt.doEncoding(username).toString();
                this.encryptedPassword = myCrypt.doEncoding(password).toString();
                this.encryptedEmail = myCrypt.doEncoding(email).toString();

                try {
                    new Register().execute(encryptedUsername, encryptedPassword, encryptedEmail).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("user", username).apply();
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("token", "").apply();
                PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("id", 0).apply();
                login(username, password ,context);
                loggedIn = true;
            }else if(this.exists) {
                Toast.makeText(context, "Username already in use", Toast.LENGTH_SHORT).show();
            }
            else
            {
                System.out.println("Register: User existed");
                loggedIn = false;
            }
        } else {
            System.out.println("Register: Password didn't match");
            loggedIn = false;
        }
        return loggedIn;
    }

    public String login(String username, String password, Context context) throws ExecutionException, InterruptedException {
        exists = false;
        testIfExists(username, password, context);

        if (exists == true && loggedIn != true) {
            this.encryptedUsername = myCrypt.doEncoding(username).toString();
            this.encryptedPassword = myCrypt.doEncoding(password).toString();

            try{
                token = new GetToken().execute(encryptedUsername,encryptedPassword).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (!token.equalsIgnoreCase("")) {
                loggedIn = true;
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("user", username).apply();
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("token", token).apply();
                PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("id", userID).apply();
                new GetPlayListsHelper().execute("http://media.mw.metropolia.fi/arsu/playlists/user/"+ PreferenceManager.getDefaultSharedPreferences(context).getInt("id", 0) + "?token=" + PreferenceManager.getDefaultSharedPreferences(context).getString("token", "")).get();

            }
        } else {
            System.out.println("Login: User doesn't exist or user is already logged in.");
            loggedIn = false;
        }



        return username;

    }

    public void logout(Context context) {

        token = null;
        loggedIn = false;
        exists = false;
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("user", "").apply();
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("token", "").apply();
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt("id", 0).apply();

    }

    public boolean testIfExists(String username, String email, Context context) throws ExecutionException, InterruptedException {

        //here check if user is registered
        this.encryptedUsername = myCrypt.doEncoding(username).trim();
        this.encryptedEmail = myCrypt.doEncoding(email).toString();

        new GetUsersHelper().execute(TOKEN).get();

        for (int i = 0; i < Users.getInstance().getUsers().size(); i++) {
            if (users.getUsers().get(i).username.equalsIgnoreCase(this.encryptedUsername)) {
                //currentUser.addCurrentUser(Users.getInstance().getUsers().get(i));
                token = users.getUsers().get(i).token;
                userID = users.getUsers().get(i).id;

                this.exists = true;
            }

        }
        System.out.println("Test exists: " + exists);
        return this.exists;
    }

}

class GetToken extends AsyncTask<String, String, String> {

    String token = "";

    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

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

class Register extends AsyncTask<String, String, String> {

    //String token = "";
    private final String TOKEN = "http://media.mw.metropolia.fi/arsu/users?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
            "eyJpZCI6MiwidXNlcm5hbWUiOiJtb2kiLCJwYXNzd29yZCI6ImhlcHMiLCJlbWFpbCI6Im1vaUB0ZXN0LmZpIiwiZGF0Z" +
            "SI6IjIwMTYtMTAtMjhUMTA6NDI6NTcuMDAwWiIsImlhdCI6MTQ3OTEwODI1NCwiZXhwIjoxNTEwNjQ0MjU0fQ." +
            "fOTXWAjP7pvnpCfowHgJ6qHEAWXiGQmvZAibLOkqqdM";

    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            URL url = new URL(TOKEN);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String input = "{\"username\":\"" + params[0] + "\",\"password\":\"" + params[1] + "\",\"email\":\"" + params[2] + "\"}";
            input = input.replace("\n", "");
            //System.out.println(input);
            //String input = "{\"username\":\"kana\",\"password\":\"\",\"email\":\"\"}";
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
                    String message = jObject.getString("message");
                    System.out.println("Message: " + message);
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

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);


    }
}
