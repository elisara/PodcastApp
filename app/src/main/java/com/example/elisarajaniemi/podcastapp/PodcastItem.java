package com.example.elisarajaniemi.podcastapp;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by jari on 31/10/2016.
 */

public class PodcastItem implements Serializable {

    public String title;
    public String url, decryptedURL;
    public String description;
    public int length;
    public String category;
    public String tags;
    public String collectionName;
    public int collectionID;
    public String imageURL;
    public boolean fromYLE;
    public String programID;
    public String mediaID;
    public ArrayList<String> categorys;

    public PodcastItem(){

    }

    public PodcastItem alterPodcastItem(String title, String url, String description, String collectionName,String imageURL, String programID, String mediaID, ArrayList<String> categorys){

        this.title = title;
        this.url = url;
        this.description = description;
        this.length = 0;
        this.category = "";
        this.tags = "";
        this.collectionName = collectionName;
        this.collectionID = 0;
        this.fromYLE = true;
        this.decryptedURL = "";
        this.imageURL = imageURL;
        this.programID = programID;
        this.mediaID = mediaID;
        this.categorys = categorys;

        return this;
    }

    public PodcastItem(String title, String url, String description, int length, String category, String tags, String collectionName, int collectionID, String imageURL){

        this.title = title;
        this.url = "";
        this.description = description;
        this.length = length;
        this.category = category;
        this.tags = tags;
        this.collectionName = "Metropolia";
        this.collectionID = collectionID;
        this.imageURL = imageURL;
        this.fromYLE = false;
        this.decryptedURL = url;
        this.programID = "";
        this.mediaID = "";
        this.categorys = null;


    }

    public void setURL(String url){
        this.decryptedURL = url;
    }

}
