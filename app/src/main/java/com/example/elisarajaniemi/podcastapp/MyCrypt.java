package com.example.elisarajaniemi.podcastapp;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Elisa Rajaniemi on 7.11.2016.
 */

public class MyCrypt {

    private final String YLE_SECRET = "5824ec07e5183ac5";

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

    public void decryptURL(String url) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        String secret = YLE_SECRET;
        String data = url;
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] baseDecoded = Base64.decode(data, Base64.DEFAULT);
        byte[] iv = Arrays.copyOfRange(baseDecoded, 0, 16);
        byte[] msg = Arrays.copyOfRange(baseDecoded, 16, baseDecoded.length);

        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes("UTF-8"), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
        byte[] resultBytes = cipher.doFinal(msg);
        System.out.println("Decrypted URL: " + new String(resultBytes));
    }

}





