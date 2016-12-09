package com.example.elisarajaniemi.podcastapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by Elisa Rajaniemi on 30.11.2016.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<PodcastItem> groupList;
    private PlayerFragment playerFragment;
    private AddToLists addToLists;
    private FavoritesFragment favoritesFragment;
    private PlaylistsFragment playlistsFragment;
    private ImageButton playBtn;
    private TextView tv;


    public ExpandableListViewAdapter(Context context, ArrayList<PodcastItem> groupList) {
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
        return false;
    }

    @Override
    public int getGroupTypeCount() {
        return getGroupCount();
    }

/**
    @Override
    public int getViewType(int position) {

        item
        return position;
    }*/

    @SuppressWarnings("unchecked")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View myView = null;
        convertView = null;

        if (myView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            myView = inf.inflate(R.layout.episode_list_item, parent, false);
        }

        final PodcastItem podcastItem = (PodcastItem) getGroup(groupPosition);
        tv = (TextView) myView.findViewById(R.id.episodeName);
        playBtn = (ImageButton) myView.findViewById(R.id.episodeIcon);
        tv.setText(podcastItem.title);
        playBtn.setId(groupPosition);
        playBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playerFragment = new PlayerFragment();
                Bundle bundle2 = new Bundle();
                if (podcastItem.fromYLE == true){
                    try {
                        new DecodeYleURL().execute(podcastItem).get();
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
                favoritesFragment = new FavoritesFragment();
                playlistsFragment = new PlaylistsFragment();
                addToLists = new AddToLists();

                addToLists.addToListsDialog(context, podcastItem, playlistsFragment, favoritesFragment);

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

        return myView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
