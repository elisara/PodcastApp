package com.example.elisarajaniemi.podcastapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.ExecutionException;

/**
 * Created by Elisa Rajaniemi on 26.10.2016.
 */

public class MenuFragment extends DialogFragment implements View.OnClickListener {


    private TextView playList, favorite, queue, history, continuePlay, signIn, usernameView;
    private PlaylistsFragment plf;
    private LinearLayout userLayout;
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
    private FirebaseAuth auth;
    private FirebaseDatabase database;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_layout, container , false);
        user = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("user", "");

        doAutoplay = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("autoplay", true);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();



        playList = (TextView) view.findViewById(R.id.playlists);
        favorite = (TextView) view.findViewById(R.id.favorites);
        queue = (TextView) view.findViewById(R.id.queue);
        history = (TextView) view.findViewById(R.id.history);

        signIn = (TextView) view.findViewById(R.id.signIn);
        usernameView = (TextView) view.findViewById(R.id.username);
        userLayout = (LinearLayout) view.findViewById(R.id.user_layout);
        autoplay = (CheckBox) view.findViewById(R.id.autoplay);


        playList.setOnClickListener(this);
        favorite.setOnClickListener(this);
        queue.setOnClickListener(this);
        history.setOnClickListener(this);

        signIn.setOnClickListener(this);
        autoplay.setOnClickListener(this);

        plf = new PlaylistsFragment();
        rali = new RegisterAndLogin();
        frontPageFragment = new FrontPageFragment();
        favorites = new Favorites();
        historyClass = new History();
        this.mainActivity = (MainActivity) getActivity();
        usernameView.setText(user);

        if(auth.getCurrentUser() == null){
            userLayout.setVisibility(View.GONE);
            playList.setVisibility(View.GONE);
            favorite.setVisibility(View.GONE);
            history.setVisibility(View.GONE);

        }
        if(auth.getCurrentUser() != null){
            login();
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
                /**try {
                    historyClass.getHistoryItems("http://media.mw.metropolia.fi/arsu/history?token=" + PreferenceManager.getDefaultSharedPreferences(getContext()).getString("token", ""));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                collectionFragment = new CollectionFragment();
                Bundle historyBundle = new Bundle();
                historyBundle.putBoolean("fromHistory", true);
                collectionFragment.setArguments(historyBundle);
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("history")
                        .replace(R.id.frag_container, collectionFragment).commit();

                break;

            case R.id.favorites:
                /**try {
                    favorites.getFavorites("http://media.mw.metropolia.fi/arsu/favourites/?token=", PreferenceManager.getDefaultSharedPreferences(getContext()).getString("token", ""));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
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

                if(auth.getCurrentUser() == null) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
                    alertDialogBuilder.setTitle("Login");

                    LinearLayout lp = new LinearLayout(getContext());
                    lp.setOrientation(LinearLayout.VERTICAL);
                    lp.setPadding(16, 16, 16, 16);

                    final EditText email = new EditText(getActivity());
                    email.setHint("Email");
                    lp.addView(email);

                    final EditText password = new EditText(getActivity());
                    password.setHint("Password");
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    lp.addView(password);

                    alertDialogBuilder.setView(lp);
                    alertDialog = alertDialogBuilder.create();

                    //LOGIN
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Login", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            email_ = email.getText().toString().trim().toLowerCase();
                            password_ = password.getText().toString().trim();
                            System.out.println(email_ + " " + password_);

                            auth.signInWithEmailAndPassword(email_, password_)
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            System.out.println("signInWithEmail:onComplete:" + task.isSuccessful());
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            usernameView.setText(user.getEmail());
                                            login();


                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            if (!task.isSuccessful()) {
                                                System.out.println("signInWithEmail:failed" + task.getException());

                                                //Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                                            }

                                            // ...
                                        }
                                    });
                            /**FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            System.out.println(user.getEmail());
                            DatabaseReference myRef = database.getReference("users/").child(user.getUid());

                            myRef.child("favorites").push().setValue("viesti1");
                            myRef.child("favorites").push().setValue("viesti2");
                            */



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

                            final EditText userName = new EditText(getActivity());
                            userName.setHint("Username");
                            lp.addView(userName);

                            final EditText email = new EditText(getActivity());
                            email.setHint("Email");
                            lp.addView(email);

                            final EditText password = new EditText(getActivity());
                            password.setHint("Password");
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            lp.addView(password);

                            final EditText password2 = new EditText(getActivity());
                            password2.setHint("Confirm password");
                            password2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            lp.addView(password2);
                            alertDialogBuilder.setView(lp);
                            AlertDialog alertDialog2 = alertDialogBuilder.create();

                            alertDialog2.setButton(AlertDialog.BUTTON_POSITIVE,"Register",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    username_ = userName.getText().toString().trim();
                                    password_ = password.getText().toString().trim();
                                    password2_ = password2.getText().toString().trim();
                                    email_ = email.getText().toString().toLowerCase().trim();
                                    System.out.println(email_ + " " + password_);
                                    auth.createUserWithEmailAndPassword(email_, password_)
                                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                System.out.println("Authentication successful" + task.isSuccessful());
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                DatabaseReference myRef = database.getReference("users/").child(user.getUid());
                                                myRef.child("username").setValue(username_);
                                                login();

                                            } else {
                                                System.out.println("Authentication failed" + task.getException());
                                                //Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    //System.out.println(user.getEmail());
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
                    auth.signOut();
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
    public void login(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();




        DatabaseReference myRef = database.getReference("users/").child(user.getUid()).child("username");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue();
                usernameView.setText(value);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        signIn.setText("Logout");
        userLayout.setVisibility(View.VISIBLE);
        playList.setVisibility(View.VISIBLE);
        favorite.setVisibility(View.VISIBLE);
        history.setVisibility(View.VISIBLE);
    }

}


