package com.example.elisarajaniemi.podcastapp;

import java.util.ArrayList;

/**
 * Created by jari on 07/12/2016.
 */
public class HistoryPodcastItems {
    private ArrayList<PodcastItem> items;

    private static HistoryPodcastItems ourInstance = new HistoryPodcastItems();

    public static HistoryPodcastItems getInstance() {
        return ourInstance;
    }

    private HistoryPodcastItems() {
        this.items = new ArrayList<>();
    }

    public void addPodcastItem(PodcastItem item){
        if(!items.contains(item)){
            items.add(0, item);
        }
    }
    public void addAll(ArrayList<PodcastItem> lista) {
        items = lista;
    }
    public ArrayList<PodcastItem> getItems(){
        return items;
    }

    public void clearList(){
        this.items.clear();
    }
}
