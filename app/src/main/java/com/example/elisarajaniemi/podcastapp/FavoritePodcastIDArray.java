package com.example.elisarajaniemi.podcastapp;

import java.util.ArrayList;

/**
 * Created by Kade on 8.2.2017.
 */

public class FavoritePodcastIDArray {
    private ArrayList<PodcastItem> items;

    private static FavoritePodcastIDArray ourInstance = new FavoritePodcastIDArray();

    public static FavoritePodcastIDArray getInstance() {
        return ourInstance;
    }

    private FavoritePodcastIDArray() {
        this.items = new ArrayList<>();
    }

    public void addPodcastID(PodcastItem item){
        if(!items.contains(item)){
            items.add(item);
        }
    }

    public ArrayList<PodcastItem> getItems(){
        return items;
    }

    public void deletePodcast(int position){
        items.remove(position);
    }

    public void clearList(){
        this.items.clear();
    }
}
