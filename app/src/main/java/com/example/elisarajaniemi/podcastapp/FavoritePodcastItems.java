package com.example.elisarajaniemi.podcastapp;

import java.util.ArrayList;

/**
 * Created by jari on 30/11/2016.
 */
public class FavoritePodcastItems {

    private ArrayList<PodcastItem> items;

    private static FavoritePodcastItems ourInstance = new FavoritePodcastItems();

    public static FavoritePodcastItems getInstance() {
        return ourInstance;
    }

    private FavoritePodcastItems() {
        this.items = new ArrayList<>();
    }

    public void addPodcastItem(PodcastItem item){
        if(!items.contains(item)){
            items.add(item);
        }
    }
    public void addAll(ArrayList<PodcastItem> lista){
        items = lista;
    }
    public ArrayList<PodcastItem> getItems(){
        return items;
    }

    public void clearList(){
        this.items.clear();
    }
}
