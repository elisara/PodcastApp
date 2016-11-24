package com.example.elisarajaniemi.podcastapp;

import android.graphics.Bitmap;

import java.io.Serializable;


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


    public PodcastItem(String title, String url){

        this.title = title;
        this.url = url;
        this.description = "";
        this.length = 0;
        this.category = "";
        this.tags = "";
        this.collectionName = "";
        this.collectionID = 0;
        this.imageURL = "";
        this.fromYLE = true;
        this.decryptedURL = "";
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

    }

    public void setURL(String url){
        this.decryptedURL = url;
    }

}
