package com.example.elisarajaniemi.podcastapp;

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
 * Created by Elisa Rajaniemi on 10.11.2016.
 */

public class RegisterAndLogin {

    private boolean loggedIn;
    private boolean exists;
    private boolean registered;
    private String encryptedPassword, encryptedUsername, encryptedEmail;
    private MyCrypt myCrypt = new MyCrypt();
    private String token;


    public boolean registerUser(String username, String password, String password2, String email){


       if(password.equals(password2)) {
           testIfExists(username, password);
           System.out.println("Password match");

           if (exists == false) {
               System.out.println("User didn't exist");
               this.encryptedUsername = myCrypt.doEncoding(username).toString();
               this.encryptedPassword = myCrypt.doEncoding(password).toString();
               this.encryptedEmail = myCrypt.doEncoding(email).toString();

               Thread t = new Thread(r);
               t.start();

               //here send encrypted userdata to database
               loggedIn = true;
           }
           else {
               System.out.println("User existed");
               loggedIn = false;
           }
       }
       else {
           System.out.println("Password didn't match");
            loggedIn = false;
        }
        return loggedIn;
    }

    public boolean login(String username, String password){
        testIfExists(username, password);

        if(exists == true && loggedIn != true) {
            loggedIn = true;
        }
        else {
            loggedIn = false;
        }
        return loggedIn;

    }

    public boolean logout(){
            loggedIn = false;
            return loggedIn;
    }

    public boolean testIfExists(String username, String password){
        //here check if user is not already registered
        exists = false;
        return exists;
    }

        Runnable r = new Runnable() {
            public void run() {
                try {
                    URL url = new URL("http://media.mw.metropolia.fi/arsu/users?token=" + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                            "eyJpZCI6MiwidXNlcm5hbWUiOiJtb2kiLCJwYXNzd29yZCI6ImhlcHMiLCJlbWFpbCI6Im1vaUB0ZXN0LmZpIiwiZGF0ZSI6IjIwM" +
                            "TYtMTAtMjhUMTA6NDI6NTcuMDAwWiIsImlhdCI6MTQ3ODg1MDU2NSwiZXhwIjoxNDc4ODU3NzY1fQ.tclScuKmTev-iChsOrNUmyYTsyxEkbsRGQn29P3Ro7A");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");

                    String input = "{\"username\":\""+encryptedUsername+"\",\"password\":\""+encryptedPassword+"\",\"email\":\""+encryptedEmail+"\"}";
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
            }

        };
}
