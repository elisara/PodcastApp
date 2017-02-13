package com.example.elisarajaniemi.podcastapp;

import java.util.ArrayList;

/**
 * Created by Kade on 13.2.2017.
 */

public class PlaylistArray {

    private ArrayList<ArrayList> lists;

    private static PlaylistArray ourInstance = new PlaylistArray();

    public static PlaylistArray getInstance() {
        return ourInstance;
    }

    private PlaylistArray() {
        this.lists = new ArrayList<>();
    }

    public void addPlaylist(ArrayList<String> lista){
        lists.add(lista);
        /**boolean löyty = false;
         int k= 0;
         if(lists.size()>0) {
         for (int i = 0; i < lists.size(); i++) {
         if (lists.get(i).programID.equals(item.programID)) {
         löyty = true;
         k = i;
         }
         }
         }
         if(löyty){
         lists.remove(k);
         lists.add(item);
         }
         else lists.add(item);
         */
    }
    /**public void addAllIds(ArrayList<String> lista ){
     for(int i = 0; i<lista.size();i++){
     PodcastItem podcastItem = new PodcastItem(lista.get(i));
     addPlaylist(podcastItem);
     }
     }
     public ArrayList<String> getIdList(){
     ArrayList<String> list = new ArrayList<>();
     for(int i = 0; i< lists.size(); i++){
     if(lists.get(i).programID!=null)list.add(lists.get(i).programID);
     }
     return list;
     }
     public ArrayList<String> removePodcast(String id){
     for(int i = 0; i< lists.size(); i++){
     if(lists.get(i).programID.equals(id)) lists.remove(i);
     }
     ArrayList<String> list = new ArrayList<>();
     list = getIdList();
     return list;
     }
     */
    public ArrayList<ArrayList> getList(){
        return lists;
    }

    public void deletePodcast(int position){
        lists.remove(position);
    }

    public void clearList(){
        this.lists.clear();
    }
}

