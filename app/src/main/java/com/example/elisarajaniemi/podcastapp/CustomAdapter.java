package com.example.elisarajaniemi.podcastapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static java.security.AccessController.getContext;

/**
 * Created by Elisa Rajaniemi on 30.11.2016.
 */

public class CustomAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<PodcastItem> groupList;
    private PodcastItem podcastItem;
    private PlaylistsFragment playlistsFragment;
    private MainActivity mainActivity;
    private PlayerFragment playerFragment;
    CurrentUser currentUser = CurrentUser.getInstance();
    private FavoritesFragment favoritesFragment;


    public CustomAdapter(Context context, ArrayList<PodcastItem> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    //TÄMÄ???
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String description = groupList.get(groupPosition).description;
        return description;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        podcastItem = (PodcastItem) getGroup(groupPosition);
        View myView = convertView;
        if (myView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myView = inf.inflate(R.layout.episode_list_item, parent, false);
        }

        TextView tv = (TextView) myView.findViewById(R.id.episodeName);
        tv.setText(podcastItem.title);

        ImageView iv = (ImageView) myView.findViewById(R.id.episodeIcon);
        //iv.setImageBitmap(value.picture);

        ImageButton playBtn = (ImageButton) myView.findViewById(R.id.episodeIcon);
        playBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playerFragment = new PlayerFragment();
                Bundle bundle2 = new Bundle();
                System.out.println("FromYLE: " + podcastItem.fromYLE);
                if (podcastItem.fromYLE == true){
                    try {
                        new DecodeURL().execute(podcastItem).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                bundle2.putSerializable("episode", podcastItem);
                playerFragment.setArguments(bundle2);
                ((MainActivity) context).setFragment(playerFragment);

            }
        });

        ImageButton button = (ImageButton) myView.findViewById(R.id.itemMenu);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                System.out.println("item imagebutton clicked");
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Add to");

                LinearLayout lp = new LinearLayout(context);
                lp.setOrientation(LinearLayout.VERTICAL);
                lp.setPadding(30,30,30,30);


                final TextView toPlaylist = new TextView(context);
                toPlaylist.setText("Playlist");
                toPlaylist.setTextSize(20);
                toPlaylist.setPadding(30, 20, 20, 20);
                lp.addView(toPlaylist);

                final TextView toQueue = new TextView(context);
                toQueue.setText("Queue");
                toQueue.setPadding(30, 20, 20, 20);
                toQueue.setTextSize(20);
                lp.addView(toQueue);

                final TextView toFavorites = new TextView(context);
                toFavorites.setText("Favorites");
                toFavorites.setPadding(30, 20, 20, 10);
                toFavorites.setTextSize(20);
                lp.addView(toFavorites);

                alertDialogBuilder.setView(lp);
                final AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                toPlaylist.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        playlistsFragment = new PlaylistsFragment();
                        alertDialog.cancel();
                        //addToPlaylist = true;
                        //podcastItem = value;
                        playlistsFragment.addToPlaylistDialog(podcastItem, context);
                    }
                });

                toQueue.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        System.out.println("Clicked to queue");
                        alertDialog.cancel();
                    }
                });

                toFavorites.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        favoritesFragment = new FavoritesFragment();
                        System.out.println("Clicked on: " + podcastItem.programID + ", CurrentUser: " + currentUser.getCurrentUser().get(0).id);
                        try {
                            favoritesFragment.addToFavorites(podcastItem.programID.replace("-", ""), currentUser.getCurrentUser().get(0).id,
                                    "http://media.mw.metropolia.fi/arsu/favourites?token=", currentUser.getCurrentUser().get(0).token);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        alertDialog.cancel();
                    }
                });

                alertDialog.show();

            }
        });

        return myView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String description = (String)getChild(groupPosition, childPosition);
        View myView = convertView;
        if (myView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myView = infalInflater.inflate(R.layout.child_items, parent, false);
        }

        TextView descriptionView = (TextView) myView.findViewById(R.id.description_view);
        descriptionView.setText(description);
        System.out.println("------------CHILD VIEW");

        return myView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
