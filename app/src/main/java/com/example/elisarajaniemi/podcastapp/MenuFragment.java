package com.example.elisarajaniemi.podcastapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Elisa Rajaniemi on 26.10.2016.
 */

public class MenuFragment extends Fragment implements View.OnClickListener {

    private MainActivity ma;
    private FrameLayout fl;
    private TextView playList, favorite, queue, history, continuePlay, signIn;
    private PlaylistsFragment plf;
    private SinglePlaylistFragment splf;
    private MenuFragment mf;
    private String password_, password2_, username_, email_;
    private boolean registered;
    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.menu_layout, container, false);

        registered = false;

        playList = (TextView) view.findViewById(R.id.playlists);
        favorite = (TextView) view.findViewById(R.id.favorites);
        queue = (TextView) view.findViewById(R.id.queue);
        history = (TextView) view.findViewById(R.id.history);
        continuePlay = (TextView) view.findViewById(R.id.continuePlaying);
        signIn = (TextView) view.findViewById(R.id.signIn);
        fl = (FrameLayout) view.findViewById(R.id.outside);


        playList.setOnClickListener(this);
        favorite.setOnClickListener(this);
        queue.setOnClickListener(this);
        history.setOnClickListener(this);
        continuePlay.setOnClickListener(this);
        signIn.setOnClickListener(this);
        fl.setOnClickListener(this);

        plf = new PlaylistsFragment();
        splf = new SinglePlaylistFragment();
        mf = new MenuFragment();

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

                //LOGIN
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Login");

                //editTexts in dialog
                LinearLayout lp = new LinearLayout(getContext());
                lp.setOrientation(LinearLayout.VERTICAL);
                lp.setPadding(16,16,16,16);

                final EditText username = new EditText(getActivity());
                username.setHint("Username");
                lp.addView(username);

                final EditText password = new EditText(getActivity());
                password.setHint("Password");
                lp.addView(password);

                final EditText email = new EditText(getActivity());
                email.setHint("Email");
                lp.addView(email);

                final Button register = new Button(getActivity());
                register.setText("Register");
                lp.addView(register);

                alertDialogBuilder.setView(lp);

                alertDialogBuilder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        username_ = username.getText().toString();
                        password_ = password.getText().toString();
                        email_ = email.getText().toString();
                        Toast.makeText(getContext(), "User " + username_ + " logged in", Toast.LENGTH_SHORT).show();

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
                                registered = true;
                                alertDialog.cancel();
                                //alertDialogBuilder.

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

                break;

            case R.id.outside:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                break;
        }

    }
}


