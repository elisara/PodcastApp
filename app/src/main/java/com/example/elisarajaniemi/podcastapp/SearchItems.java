package com.example.elisarajaniemi.podcastapp;

import java.util.ArrayList;

/**
 * Created by jari on 10/11/2016.
 */

public class SearchItems {

    private ArrayList<PodcastItem> items;

    private static SearchItems ourInstance = new SearchItems();

    public static SearchItems getInstance() {
        return ourInstance;
    }

    private SearchItems() {

        this.items = new ArrayList<>();
    }

    public void addSearchItem(PodcastItem item){
        if(!items.contains(item)){
            items.add(item);
        }
    }

    public ArrayList<PodcastItem> getSearchItems(){
        return items;
    }
}
