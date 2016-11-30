package com.example.elisarajaniemi.podcastapp;

import java.util.ArrayList;

/**
 * Created by jari on 28/11/2016.
 */
public class PlaylistPodcastItems {

    private ArrayList<PodcastItem> items;

    private static PlaylistPodcastItems ourInstance = new PlaylistPodcastItems();

    public static PlaylistPodcastItems getInstance() {
        return ourInstance;
    }

    private PlaylistPodcastItems() {
        this.items = new ArrayList<>();
    }

    public void addPodcastItem(PodcastItem item){
        if(!items.contains(item)){
            items.add(item);
        }
    }

    public ArrayList<PodcastItem> getItems(){
        return items;
    }

    public void addAll(ArrayList<PodcastItem> lista){
        items = lista;
    }

    public void clearList(){
        this.items.clear();
        System.out.println("PlayListPodcastItems size: " + this.items.size());
    }
}
