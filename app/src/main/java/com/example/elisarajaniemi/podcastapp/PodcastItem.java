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
    public String id;
    public String imageURL;
    public boolean fromYLE;
    public String programID;
    public String mediaID;
    public String serieID;
    public ArrayList<String> categorys;

    public PodcastItem(){
        this.title = "";
        this.url = "";
        this.description = "";
        this.length = 0;
        this.category = "";
        this.tags = "";
        this.collectionName = "";
        this.collectionID = 0;
        this.fromYLE = true;
        this.decryptedURL = "";
        this.imageURL = "";
        this.programID = "";
        this.mediaID = "";
        this.serieID = "";


    }

    public PodcastItem(String id, String programID){
        this.id = id;
        this.programID = programID;
    }

    public PodcastItem alterPodcastItem(String title, String url, String description, String collectionName,String imageURL, String programID, String mediaID, ArrayList<String> categorys, int length){

        this.title = title;
        this.url = url;
        this.description = description;
        this.length = length;
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
        this.categorys = new ArrayList<>();
        this.categorys.add("Metropolia");
        this.serieID = "";


    }

    public void setURL(String url){
        this.decryptedURL = url;
    }
    public void setTitle(String title){this.title = title;}
    public void setUrl(String url){this.url = url;}
    public void setDecryptedURL(String decryptedURL){this.decryptedURL = decryptedURL;}
    public void setDescription(String description){this.description = description;}
    public void setCategory(){}
    public void setTags(){}
    public void setCollectionName(String collectionName){this.collectionName = collectionName;}
    public void setId(){}
    public void setImageURL(String imageURL){this.imageURL = imageURL;}
    public void setFromYLE(boolean fromYLE){this.fromYLE = fromYLE;}
    public void setMediaID(String mediaID){this.mediaID = mediaID;}
    public void setCategorys(ArrayList<String> categorys){this.categorys = categorys;}
    public void setSerieID(String serieID){this.serieID = serieID;}
    public void setLength(int length){this.length = length;}
    public void setProgramID(String programID){this.programID = programID;}

}
