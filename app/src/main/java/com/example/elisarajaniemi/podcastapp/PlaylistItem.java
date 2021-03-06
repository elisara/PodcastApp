package com.example.elisarajaniemi.podcastapp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Elisa Rajaniemi on 9.11.2016.
 */

public class PlaylistItem implements Serializable {

    public String name;
    public int id;
    public ArrayList<PodcastItem> list;

    /**public PlaylistItem(String name, ArrayList<PodcastItem> list){
        this.name = name;
        this.list = list;
    }*/

    public PlaylistItem(int id, String name){
        this.id = id;
        this.name = name;
    }
}
