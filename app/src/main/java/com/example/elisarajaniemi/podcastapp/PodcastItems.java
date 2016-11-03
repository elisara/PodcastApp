package com.example.elisarajaniemi.podcastapp;

import java.util.ArrayList;

/**
 * Created by jari on 02/11/2016.
 */
public class PodcastItems {

    private ArrayList<PodcastItem> items, episodes;

    private static PodcastItems ourInstance = new PodcastItems();

    public static PodcastItems getInstance() {
        return ourInstance;
    }

    private PodcastItems() {
        this.items = new ArrayList<>();
        this.episodes = new ArrayList<>();
    }

    public void addPodcastItem(PodcastItem item){
        if(!items.contains(item)){
            items.add(item);
        }

    }

    public void addEpisodes(PodcastItem item){
        if(!episodes.contains(item)){
            episodes.add(item);
        }

    }

    public void clearEpisodes(){
        episodes = null;
    }

    public ArrayList<PodcastItem> getItems(){
        return items;
    }

    public ArrayList<PodcastItem> getEpisodes(){
        return episodes;
    }
}
