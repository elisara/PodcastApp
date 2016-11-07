package com.example.elisarajaniemi.podcastapp;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Extension;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Elisa Rajaniemi on 7.11.2016.
 */

public class RegisterAndLogin {

    String password;

    private boolean loggedIn;

    public boolean RegisterUser(String username, String password, String password2, String email){
        loggedIn = true;
        return this.loggedIn;
    }

    public boolean Login(String user, String password){
        loggedIn = true;
        return this.loggedIn;
    }

    public boolean LogOut(){
        loggedIn = false;
        return this.loggedIn;
    }

    public static void encryptString() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        String password  = "password";
        int iterationCount = 1000;
        int keyLength = 256;
        int saltLength = keyLength / 8; // same size as key output

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[saltLength];
        random.nextBytes(salt);
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength);

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[cipher.getBlockSize()];
        random.nextBytes(iv);
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);

        //byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF-8"));
        byte[] ciphertext = cipher.doFinal(password.getBytes("UTF-8"));

        System.out.println("CIPHERTTEXT: " + ciphertext);
    }

}
