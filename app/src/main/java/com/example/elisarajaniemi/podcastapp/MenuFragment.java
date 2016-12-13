package com.example.elisarajaniemi.podcastapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.ExecutionException;

/**
 * Created by Elisa Rajaniemi on 26.10.2016.
 */

public class MenuFragment extends DialogFragment implements View.OnClickListener {

    //private MainActivity ma;
    private TextView playList, favorite, queue, history, continuePlay, signIn, usernameView;
    private PlaylistsFragment plf;
    private LinearLayout userLayout;
    //private MenuFragment mf;
    private RegisterAndLogin rali;
    private String password_, password2_, username_, email_, token;
    private AlertDialog alertDialog;
    private FrontPageFragment frontPageFragment;
    private Favorites favorites;
    private CollectionFragment collectionFragment;
    private History historyClass;
    private String user;
    private MainActivity mainActivity;
    private CheckBox autoplay;
    private boolean doAutoplay;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_layout, container , false);
        user = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("user", "");
        token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("token", "");
        doAutoplay = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("autoplay", true);

        new GetUsersHelper().execute("http://media.mw.metropolia.fi/arsu/users?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
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
        autoplay = (CheckBox) view.findViewById(R.id.autoplay);


        playList.setOnClickListener(this);
        favorite.setOnClickListener(this);
        queue.setOnClickListener(this);
        history.setOnClickListener(this);
        continuePlay.setOnClickListener(this);
        signIn.setOnClickListener(this);
        autoplay.setOnClickListener(this);

        plf = new PlaylistsFragment();
        //mf = new MenuFragment();
        rali = new RegisterAndLogin();
        frontPageFragment = new FrontPageFragment();
        favorites = new Favorites();
        historyClass = new History();
        this.mainActivity = (MainActivity) getActivity();
        usernameView.setText(user);

        if(token.equalsIgnoreCase("")){
            userLayout.setVisibility(View.GONE);
            playList.setVisibility(View.GONE);
            favorite.setVisibility(View.GONE);
            history.setVisibility(View.GONE);
            continuePlay.setVisibility(View.GONE);
        }
        if(!token.equalsIgnoreCase("")){
            signIn.setText("Logout");
        }

        if(doAutoplay){
            autoplay.setChecked(true);
        }
        else{
            autoplay.setChecked(false);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.playlists:
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_container, plf).addToBackStack( "tag" ).commit();
                break;

            case R.id.queue:
                System.out.println("QUEUE");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                collectionFragment = new CollectionFragment();
                Bundle queueBundle = new Bundle();
                queueBundle.putBoolean("fromQueue", true);
                collectionFragment.setArguments(queueBundle);
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("queue")
                        .replace(R.id.frag_container, collectionFragment).commit();
                break;

            case R.id.history:
                System.out.println("HISTORY");
                try {
                    historyClass.getHistoryItems("http://media.mw.metropolia.fi/arsu/history?token=" + PreferenceManager.getDefaultSharedPreferences(getContext()).getString("token", "0"));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                collectionFragment = new CollectionFragment();
                Bundle historyBundle = new Bundle();
                historyBundle.putBoolean("fromHistory", true);
                collectionFragment.setArguments(historyBundle);
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("history")
                        .replace(R.id.frag_container, collectionFragment).commit();

                break;

            case R.id.continuePlaying:
                System.out.println("CONTINUE");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                break;

            case R.id.favorites:
                try {
                    favorites.getFavorites("http://media.mw.metropolia.fi/arsu/favourites/?token=", PreferenceManager.getDefaultSharedPreferences(getContext()).getString("token", "0"));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("FAVS");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                collectionFragment = new CollectionFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("fromFavorites", true);
                collectionFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("favorites")
                        .replace(R.id.frag_container, collectionFragment).commit();
                break;

            case R.id.signIn:

                if(token.equalsIgnoreCase("")) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
                    alertDialogBuilder.setTitle("Login");

                    LinearLayout lp = new LinearLayout(getContext());
                    lp.setOrientation(LinearLayout.VERTICAL);
                    lp.setPadding(16, 16, 16, 16);

                    final EditText username = new EditText(getActivity());
                    username.setHint("Username");
                    lp.addView(username);

                    final EditText password = new EditText(getActivity());
                    password.setHint("Password");
                    lp.addView(password);

                    alertDialogBuilder.setView(lp);
                    alertDialog = alertDialogBuilder.create();

                    //LOGIN
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Login", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            username_ = username.getText().toString();
                            password_ = password.getText().toString();
                            try {
                                rali.login(username_, password_, getContext());
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            user = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("user", "");
                            token = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("token", "");

                            if(!token.equalsIgnoreCase("")) {
                                Toast.makeText(getContext(), "User " + username_ + " logged in", Toast.LENGTH_SHORT).show();
                                System.out.println("--------User in list-------");
                                signIn.setText("Logout");
                                usernameView.setText(user);
                                userLayout.setVisibility(View.VISIBLE);
                                playList.setVisibility(View.VISIBLE);
                                favorite.setVisibility(View.VISIBLE);
                                history.setVisibility(View.VISIBLE);
                                continuePlay.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    //CANCEL
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

                    //REGISTER
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Register", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
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
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            lp.addView(password);

                            final EditText password2 = new EditText(getActivity());
                            password2.setHint("Confirm password");
                            lp.addView(password2);

                            final EditText email = new EditText(getActivity());
                            email.setHint("Email");
                            lp.addView(email);
                            alertDialogBuilder.setView(lp);
                            AlertDialog alertDialog2 = alertDialogBuilder.create();

                            alertDialog2.setButton(AlertDialog.BUTTON_POSITIVE,"Register",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    username_ = username.getText().toString();
                                    password_ = password.getText().toString();
                                    password2_ = password2.getText().toString();
                                    email_ = email.getText().toString();
                                    try {
                                        rali.registerUser(username_, password_, password2_, email_, getContext());
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    user = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("user", "");

                                    if(user.length() > 0) {
                                        Toast.makeText(getContext(), "User " + username_ + " created", Toast.LENGTH_SHORT).show();
                                        signIn.setText("Logout");
                                        usernameView.setText(user);
                                        userLayout.setVisibility(View.VISIBLE);
                                        playList.setVisibility(View.VISIBLE);
                                        favorite.setVisibility(View.VISIBLE);
                                        history.setVisibility(View.VISIBLE);
                                        continuePlay.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        Toast.makeText(getContext(), "Registering failed", Toast.LENGTH_SHORT).show();
                                    }

                                    alertDialog.cancel();

                                }
                            });
                            alertDialog2.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                            alertDialog2.show();

                        }});

                    alertDialog.show();

                }
                else{
                    //LOGOUT
                    rali.logout(getContext());
                    mainActivity.hidePlayer();
                    mainActivity.pServ.stopMusic();
                    mainActivity.pServ.cancelNotification();
                    dismiss();
                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("favorites")
                            .replace(R.id.frag_container, frontPageFragment).commit();
                    getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }

                break;

            case R.id.autoplay:
                if(autoplay.isChecked()){
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("autoplay", true).apply();
                }
                else{
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("autoplay", false).apply();
                }
                break;
        }

    }
    public void onResume(){
        super.onResume();
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getDialog().getWindow().setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.x = 0;
        p.y = 0;
        double width = (getResources().getDisplayMetrics().widthPixels)*0.7;
        int height = getResources().getDisplayMetrics().heightPixels;
        getDialog().getWindow().setLayout((int)width, height);
        getDialog().getWindow().setAttributes(p);
    }
}


