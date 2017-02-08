package com.example.elisarajaniemi.podcastapp;

import java.util.ArrayList;

/**
 * Created by Kade on 8.2.2017.
 */

public class PlaylistPodcastIDArray {

    private ArrayList<PodcastItem> items;

    private static PlaylistPodcastIDArray ourInstance = new PlaylistPodcastIDArray();

    public static PlaylistPodcastIDArray getInstance() {
        return ourInstance;
    }

    private PlaylistPodcastIDArray() {
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
