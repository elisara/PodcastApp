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
        boolean löyty = false;
        int k= 0;
        if(items.size()>0) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).programID.equals(item.programID)) {
                    löyty = true;
                    k = i;
                }
            }
        }
        if(löyty){
            items.remove(k);
            items.add(item);
        }
        else items.add(item);

    }
    public void addAllIds(ArrayList<String> lista ){
        for(int i = 0; i<lista.size();i++){
            PodcastItem podcastItem = new PodcastItem(lista.get(i));
            addPodcastID(podcastItem);
        }
    }
    public ArrayList<String> getIdList(){
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i<items.size();i++){
            if(items.get(i).programID!=null)list.add(items.get(i).programID);
        }
        return list;
    }
    public ArrayList<String> removePodcast(String id){
        for(int i = 0; i<items.size();i++){
            if(items.get(i).programID.equals(id))items.remove(i);
        }
        ArrayList<String> list = new ArrayList<>();
        list = getIdList();
        return list;
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
