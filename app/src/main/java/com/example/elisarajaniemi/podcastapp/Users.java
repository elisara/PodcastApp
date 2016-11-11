package com.example.elisarajaniemi.podcastapp;

import java.util.ArrayList;

/**
 * Created by jari on 11/11/2016.
 */
public class Users {

    private ArrayList<User> users;

    private static Users ourInstance = new Users();

    public static Users getInstance() {
        return ourInstance;
    }

    private Users() {
        this.users = new ArrayList<>();
    }

    public void addUser(User user){
        users.add(user);
    }

    public ArrayList<User> getUsers(){
        return users;
    }
}
