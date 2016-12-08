package com.example.elisarajaniemi.podcastapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by jari on 01/12/2016.
 */

public class Search {

    public SearchItems searchItems = SearchItems.getInstance();
    public PodcastItems podcastItems = PodcastItems.getInstance();

    public void searchDialog(final Context context, final Fragment fragment){

        AlertDialog alertDialog;

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
        alertDialogBuilder.setTitle("Search");

        LinearLayout lp = new LinearLayout(context);
        lp.setOrientation(LinearLayout.VERTICAL);
        lp.setPadding(30, 30, 30, 60);

        final EditText searchField = new EditText(context);

        lp.addView(searchField);
        alertDialogBuilder.setView(lp);

        alertDialogBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                searchItems.getSearchItems().clear();

                for (int j = 0; j < podcastItems.getItems().size(); j++) {

                    if (podcastItems.getItems().get(j).title.toLowerCase().contains(searchField.getText().toString().toLowerCase())
                            || podcastItems.getItems().get(j).description.toLowerCase().contains(searchField.getText().toString().toLowerCase())
                            || podcastItems.getItems().get(j).tags.toLowerCase().contains(searchField.getText().toString().toLowerCase())
                            || podcastItems.getItems().get(j).category.toLowerCase().contains(searchField.getText().toString().toLowerCase())
                            || podcastItems.getItems().get(j).collectionName.toLowerCase().contains(searchField.getText().toString().toLowerCase())) {

                        searchItems.addSearchItem(PodcastItems.getInstance().getItems().get(j));
                        //System.out.println("Added to list: " + PodcastItems.getInstance().getItems().get(j).title);

                    }
                }
                Bundle bundle = new Bundle();
                bundle.putBoolean("fromSearch", true);
                fragment.setArguments(bundle);
                ((MainActivity) context).setFragment(fragment);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
