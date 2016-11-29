package com.example.elisarajaniemi.podcastapp;

import java.util.ArrayList;

/**
 * Created by jari on 28/11/2016.
 */
public class PodcastIDArray {

    private ArrayList<String> items;

    private static PodcastIDArray ourInstance = new PodcastIDArray();

    public static PodcastIDArray getInstance() {
        return ourInstance;
    }

    private PodcastIDArray() {
        this.items = new ArrayList<>();
    }

    public void addPodcastID(String item){
        if(!items.contains(item)){
            items.add(item);
        }
    }

    public ArrayList<String> getItems(){
        return items;
    }

    public void clearList(){
        this.items.clear();
    }
}
