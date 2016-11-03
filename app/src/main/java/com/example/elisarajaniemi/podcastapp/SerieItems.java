package com.example.elisarajaniemi.podcastapp;

import java.util.ArrayList;

/**
 * Created by jari on 03/11/2016.
 */
public class SerieItems {

    private ArrayList<PodcastItem> series;
    PodcastItem value;

    private static SerieItems ourInstance = new SerieItems();

    public static SerieItems getInstance() {
        return ourInstance;
    }

    private SerieItems() {
        this.series = new ArrayList<>();
    }

    public void addSerieItem(PodcastItem item){
        if(!series.contains(item)){
            series.add(item);
        }
    }

    public ArrayList<PodcastItem> getSerieItems(){
        return series;
    }
}
