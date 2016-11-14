package com.example.elisarajaniemi.podcastapp;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by Elisa Rajaniemi on 7.11.2016.
 */

public class MyCrypt {

    public MyCrypt(){

    }

    public void doAll(){
        // String to be encoded with Base64
        String text = "Koira";
        // Sending side
        byte[] data = null;
        try {
            data = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        System.out.println("Encoded text: " + base64);

        // Receiving side
        byte[] data1 = Base64.decode(base64, Base64.DEFAULT);
        String text1 = null;
        try {
            text1 = new String(data1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Decoded text: " + text1);

    }


    public String doEncoding(String username){
        // String to be encoded with Base64
        String text = username;
        // Sending side
        byte[] data = null;
        try {
            data = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        System.out.println("Encoded text: " + base64);

        return base64;

    }

    //NOT READY
    public String doDecoding(String base64){
        // Receiving side
        byte[] data1 = Base64.decode(base64, Base64.DEFAULT);
        String text1 = null;
        try {
            text1 = new String(data1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Decoded text: " + text1);

        return text1;

    }

}





