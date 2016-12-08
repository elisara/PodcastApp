package com.example.elisarajaniemi.podcastapp;

import java.util.ArrayList;

/**
 * Created by jari on 16/11/2016.
 */
public class Playlists {

    private ArrayList<PlaylistItem> playlists;
    private static Playlists ourInstance = new Playlists();

    public static Playlists getInstance() {
        return ourInstance;
    }

    private Playlists() {
        this.playlists = new ArrayList<>();
    }

    public void addPlaylist(PlaylistItem playlist){
        this.playlists.add(playlist);
        System.out.println("Added " + playlist.name);
    }

    public ArrayList<PlaylistItem> getPlaylists(){
        return this.playlists;
    }

    public void clearPlaylists(){
        this.playlists.clear();
    }

    public void deletePlaylist(int id){
        playlists.remove(id);
    }
}
