package com.example.elisarajaniemi.podcastapp;

import java.util.ArrayList;

/**
 * Created by Kade on 12.12.2016.
 */

public class QueueItems {

    private ArrayList<PodcastItem> items;

    private static QueueItems  ourInstance = new QueueItems ();

    public static QueueItems  getInstance() {
        return ourInstance;
    }

    public AutoplayItems autoplayItems = AutoplayItems.getInstance();

    private QueueItems() {
        this.items = new ArrayList<>();
    }

    public void addQueueItems (PodcastItem item){
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
    public void addOne(PodcastItem item){
        this.items.add(0, item);
        autoplayItems.addOne(item);
    }

    public ArrayList<PodcastItem> getItems(){
        return items;
    }

}