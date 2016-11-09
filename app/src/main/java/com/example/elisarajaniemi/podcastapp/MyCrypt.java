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

public class MyCrypt {

    //String password;
    private String iv = "fedcba9876543210";            // Dummy iv (CHANGE IT!)
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;
    private String SecretKey = "0123456789abcdef";     // Dummy secretKey (CHANGE IT!)

    public MyCrypt() {
        ivspec = new IvParameterSpec(iv.getBytes());
        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

        try {

            // cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public byte[] encrypt(String text) throws Exception {
        if (text == null || text.length() == 0)
            throw new Exception("Empty string");

        byte[] encrypted = null;
        try {
        // Cipher.ENCRYPT_MODE = Constant for encryption operation mode.
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

            encrypted = cipher.doFinal(padString(text).getBytes());
        } catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }
        return encrypted;
    }

    public byte[] decrypt(String text) throws Exception {
        if (text == null || text.length() == 0)
            throw new Exception("Empty string");

        byte[] decrypted = null;
        try {
        // Cipher.DECRYPT_MODE = Constant for decryption operation mode.
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            decrypted = cipher.doFinal(hexToBytes(text));
        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
    }

    public static String byteArrayToHexString(byte[] array) {
        StringBuffer hexString = new StringBuffer();
        for (byte b : array) {
            int intVal = b & 0xff;
            if (intVal < 0x10)
                hexString.append("0");
            hexString.append(Integer.toHexString(intVal));
        }
        return hexString.toString();
    }

    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {

            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(
                        str.substring(i * 2, i * 2 + 2), 16);

            }
            return buffer;
        }
    }

    private static String padString(String source) {
        char paddingChar = 0;
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;
        for (int i = 0; i < padLength; i++) {
            source += paddingChar;
        }
        return source;
    }

}



/**
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
 */





