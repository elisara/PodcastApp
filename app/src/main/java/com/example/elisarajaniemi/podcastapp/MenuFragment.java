package com.example.elisarajaniemi.podcastapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Elisa Rajaniemi on 26.10.2016.
 */

public class MenuFragment extends DialogFragment implements View.OnClickListener {

    private MainActivity ma;
    private GetUsersHelper getUsersHelper;
    private TextView playList, favorite, queue, history, continuePlay, signIn, usernameView;
    private PlaylistsFragment plf;
    private LinearLayout userLayout;
    private SinglePlaylistFragment splf;
    private MenuFragment mf;
    private RegisterAndLogin rali;
    private String password_, password2_, username_, email_, token;
    private AlertDialog alertDialog;
    CurrentUser currentUser =  CurrentUser.getInstance();
    private SerieFragment sf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.menu_layout, container , false);

        getDialog().getWindow().setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        //p.width = 700;
        p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        p.x = 0;
        p.y = 170;
        getDialog().getWindow().setAttributes(p);

        getUsersHelper = new GetUsersHelper();

        getUsersHelper.execute("http://media.mw.metropolia.fi/arsu/users?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJpZCI6MiwidXNlcm5hbWUiOiJtb2kiLCJwYXNzd29yZCI6ImhlcHMiLCJlbWFpbCI6Im1vaUB0ZXN0LmZpIiwiZGF0Z" +
                "SI6IjIwMTYtMTAtMjhUMTA6NDI6NTcuMDAwWiIsImlhdCI6MTQ3OTEwODI1NCwiZXhwIjoxNTEwNjQ0MjU0fQ." +
                "fOTXWAjP7pvnpCfowHgJ6qHEAWXiGQmvZAibLOkqqdM");

        playList = (TextView) view.findViewById(R.id.playlists);
        favorite = (TextView) view.findViewById(R.id.favorites);
        queue = (TextView) view.findViewById(R.id.queue);
        history = (TextView) view.findViewById(R.id.history);
        continuePlay = (TextView) view.findViewById(R.id.continuePlaying);
        signIn = (TextView) view.findViewById(R.id.signIn);
        usernameView = (TextView) view.findViewById(R.id.username);
        userLayout = (LinearLayout) view.findViewById(R.id.user_layout);
        //logOutBtn = (Button) view.findViewById(R.id.logout);


        playList.setOnClickListener(this);
        favorite.setOnClickListener(this);
        queue.setOnClickListener(this);
        history.setOnClickListener(this);
        continuePlay.setOnClickListener(this);
        signIn.setOnClickListener(this);
        //logOutBtn.setOnClickListener(this);

        plf = new PlaylistsFragment();
        splf = new SinglePlaylistFragment();
        mf = new MenuFragment();
        rali = new RegisterAndLogin();
        sf = new SerieFragment();

        if(currentUser.getCurrentUser().size() < 1){
            userLayout.setVisibility(View.GONE);
            playList.setVisibility(View.GONE);
            favorite.setVisibility(View.GONE);
            history.setVisibility(View.GONE);
            continuePlay.setVisibility(View.GONE);


        }




        return view;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.playlists:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, plf).addToBackStack( "tag" ).commit();
                break;

            case R.id.queue:
                System.out.println("QUEUE");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, splf).addToBackStack( "tag" ).commit();
                break;

            case R.id.history:
                System.out.println("HISTORY");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, splf).addToBackStack( "tag" ).commit();
                break;

            case R.id.continuePlaying:
                System.out.println("CONTINUE");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, splf).addToBackStack( "tag" ).commit();
                break;

            case R.id.favorites:
                System.out.println("FAVS");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, splf).addToBackStack( "tag" ).commit();
                break;

            case R.id.signIn:

                if(currentUser.getCurrentUser().size() <1) {
                    //LOGIN
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setTitle("Login");

                    //editTexts in dialog
                    LinearLayout lp = new LinearLayout(getContext());
                    lp.setOrientation(LinearLayout.VERTICAL);
                    lp.setPadding(16, 16, 16, 16);

                    final EditText username = new EditText(getActivity());
                    username.setHint("Username");
                    lp.addView(username);

                    final EditText password = new EditText(getActivity());
                    password.setHint("Password");
                    lp.addView(password);

                    final Button register = new Button(getActivity());
                    register.setText("Register");
                    lp.addView(register);

                    alertDialogBuilder.setView(lp);

                    alertDialogBuilder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            username_ = username.getText().toString();
                            password_ = password.getText().toString();
                            rali.login(username_, password_);
                            Toast.makeText(getContext(), "User " + username_ + " logged in", Toast.LENGTH_SHORT).show();

                            System.out.println("CurrentUser array size: " + currentUser.getCurrentUser().size());

                            if(currentUser.getCurrentUser().size() > 0) {
                                System.out.println("--------User in list-------");
                                signIn.setText("Sign out");
                                usernameView.setText(username_);
                                userLayout.setVisibility(View.VISIBLE);
                                playList.setVisibility(View.VISIBLE);
                                favorite.setVisibility(View.VISIBLE);
                                history.setVisibility(View.VISIBLE);
                                continuePlay.setVisibility(View.VISIBLE);
                            }
                        }
                    })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });


                    // create alert dialog
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                //REGISTER
                register.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setTitle("Register");

                        //editText in dialog
                        LinearLayout lp = new LinearLayout(getContext());
                        lp.setOrientation(LinearLayout.VERTICAL);
                        lp.setPadding(16,16,16,16);

                        final EditText username = new EditText(getActivity());
                        username.setHint("Username");
                        lp.addView(username);

                        final EditText password = new EditText(getActivity());
                        password.setHint("Password");
                        lp.addView(password);

                        final EditText password2 = new EditText(getActivity());
                        password2.setHint("Confirm password");
                        lp.addView(password2);

                        final EditText email = new EditText(getActivity());
                        email.setHint("Email");
                        lp.addView(email);
                        alertDialogBuilder.setView(lp);

                        alertDialogBuilder.setPositiveButton("Register",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                username_ = username.getText().toString();
                                password_ = password.getText().toString();
                                password2_ = password2.getText().toString();
                                email_ = email.getText().toString();
                                Toast.makeText(getContext(), "User "+ username_ +" created", Toast.LENGTH_SHORT).show();
                                rali.registerUser(username_, password_, password2_, email_);
                                System.out.println("Registers executed");
                                alertDialog.cancel();

                            }
                        })
                                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog2 = alertDialogBuilder.create();
                        alertDialog2.show();

                    }
                });
                }
                else{
                    System.out.println("-----in else logout----");
                    rali.logout();
                    System.out.println("CurrentUser array size: " + currentUser.getCurrentUser().size());
                    dismiss();
                }

                break;

        }

    }
}


