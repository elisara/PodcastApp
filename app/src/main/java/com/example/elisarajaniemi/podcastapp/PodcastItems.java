package com.example.elisarajaniemi.podcastapp;

import java.util.ArrayList;

/**
 * Created by jari on 02/11/2016.
 */
public class PodcastItems {

    private ArrayList<PodcastItem> items;

    private static PodcastItems ourInstance = new PodcastItems();

    public static PodcastItems getInstance() {
        return ourInstance;
    }

    private PodcastItems() {
        this.items = new ArrayList<>();
    }

    public void addPodcastItem(PodcastItem item){
        if(!items.contains(item)){
            items.add(item);
        }
    }
    public void addAll(ArrayList<PodcastItem> lista){
        items.addAll(lista);
    }

    public ArrayList<PodcastItem> getItems(){
        return items;
    }

}
