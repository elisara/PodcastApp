package com.example.elisarajaniemi.podcastapp;

import java.util.ArrayList;

/**
 * Created by Kade on 12.12.2016.
 */

public class AutoplayItems {private ArrayList<PodcastItem> items;

    private static AutoplayItems ourInstance = new AutoplayItems();

    public static AutoplayItems getInstance() {
        return ourInstance;
    }

    private AutoplayItems() {
        this.items = new ArrayList<>();
    }

    public void addAutoplayItems(PodcastItem item){
        if(!items.contains(item)){
            items.add(item);
        }
    }
    public void addAll(ArrayList<PodcastItem> lista){
        items.addAll(lista);
    }

    public void removeOne(int index){
        this.items.remove(index);

    }
    public PodcastItem getOne(){
        return items.get(0);
    }

    public void clearList(){
        this.items.clear();
    }

    public ArrayList<PodcastItem> getItems(){
        return items;
    }

}