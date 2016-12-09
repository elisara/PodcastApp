package com.example.elisarajaniemi.podcastapp;

import java.util.ArrayList;

/**
 * Created by jari on 03/11/2016.
 */
public class SerieItems {

    private ArrayList<PodcastItem> seriePodcasts;

    private static SerieItems ourInstance = new SerieItems();

    public static SerieItems getInstance() {
        return ourInstance;
    }

    private SerieItems() {
        this.seriePodcasts = new ArrayList<>();
    }

    public void addSerieItem(PodcastItem item){
        if(!seriePodcasts.contains(item)){
            seriePodcasts.add(0, item);
        }
    }
    public void addAll(ArrayList<PodcastItem> lista){
        seriePodcasts.addAll(lista);
    }

    public ArrayList<PodcastItem> getSerieItems(){
        return seriePodcasts;
    }
    public void clearList(){
        this.seriePodcasts.clear();
    }
}

