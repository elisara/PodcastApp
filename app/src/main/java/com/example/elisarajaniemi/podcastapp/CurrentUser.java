package com.example.elisarajaniemi.podcastapp;

import java.util.ArrayList;

/**
 * Created by jari on 16/11/2016.
 */
public class CurrentUser {

    private ArrayList<User> currentUser;

    private static CurrentUser ourInstance = new CurrentUser();

    public static CurrentUser getInstance() {
        return ourInstance;
    }

    private CurrentUser() {
        this.currentUser = new ArrayList<>();
    }

    public void addCurrentUser(User user){
        this.currentUser.add(user);
    }

    public ArrayList<User> getCurrentUser(){
        return this.currentUser;
    }

    public void replaceCurrentUser(User user){
        this.currentUser.clear();
        this.currentUser.add(user);
    }
}
