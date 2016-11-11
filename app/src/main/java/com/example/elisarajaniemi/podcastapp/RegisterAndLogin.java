package com.example.elisarajaniemi.podcastapp;

/**
 * Created by Elisa Rajaniemi on 10.11.2016.
 */

public class RegisterAndLogin {

    private boolean loggedIn;
    private boolean exists;
    private boolean registered;
    private String encryptedPassword, encryptedUsername, encryptedEmail;
    private MyCrypt myCrypt = new MyCrypt();


    public boolean RegisterUser(String username, String password, String password2, String email){
       if(password.equals(password2)) {
           testIfExists(username, password);

           if (exists == false) {
               encryptedUsername = myCrypt.doEncoding(username);
               encryptedPassword = myCrypt.doEncoding(password);
               encryptedEmail = myCrypt.doEncoding(email);

               //here send encrypted userdata to database
               loggedIn = true;
           }
           else {
               loggedIn = false;
           }
       }
       else {
            loggedIn = false;
        }
        return loggedIn;
    }

    public boolean Login(String username, String password){
        testIfExists(username, password);

        if(exists == true && loggedIn != true) {
            loggedIn = true;
        }
        else {
            loggedIn = false;
        }
        return loggedIn;

    }

    public boolean Logout(){
            loggedIn = false;
            return loggedIn;
    }

    public boolean testIfExists(String username, String password){
        encryptedUsername = myCrypt.doEncoding(username);
        encryptedPassword = myCrypt.doEncoding(password);
        //here check if user is already registered
        exists = false;
        return exists;
    }


}
