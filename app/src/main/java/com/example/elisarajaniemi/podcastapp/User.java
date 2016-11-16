package com.example.elisarajaniemi.podcastapp;

import java.io.Serializable;

/**
 * Created by jari on 11/11/2016.
 */

public class User implements Serializable {

    public String username;
    public String email;
    public String token;
    public int id;

    public User(int id, String username, String email){

        this.id = id;
        this.username = username;
        this.email = email;
    }

    public User(int id, String username, String email, String token){

        this.id = id;
        this.username = username;
        this.email = email;
        this.token = token;
    }
}
