package com.example.elisarajaniemi.podcastapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Kade on 2.2.2017.
 */

public class DataProvider extends ContentProvider {
    private static final String PROVIDER_NAME = "com.example.elisarajaniemi.podcastapp";
    private static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/data");

    public static final String _ID = "_id";
    public static final String FAFORITEID = "favoriteid";

    static final int FAVORITES = 1;
    static final int FAVORITE_ID =2;

    private static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "favorites", FAVORITES);
        uriMatcher.addURI(PROVIDER_NAME, "favorites/#", FAVORITE_ID);

    }

    private PodcastDataBase podcastDataBase = null;

    @Override
    public boolean onCreate() {

        Context context = getContext();
        podcastDataBase = new PodcastDataBase(context);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String id = null;
        if(uriMatcher.match(uri) == FAVORITE_ID) {
            //Query is for one single image. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }
        return podcastDataBase.query(id, projection, selection, selectionArgs, sortOrder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            case FAVORITES:
                return "vnd.android.cursor.dir/vnd.com.example.elisarajaniemi.podcastapp.data";

        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        try {
            long id = podcastDataBase.addNewFavorite(values);
            Uri returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
            return returnUri;
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == FAVORITE_ID) {
            //Delete is for one single image. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }

        return podcastDataBase.deleteFavorite(id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == FAVORITE_ID) {
            //Update is for one single image. Get the ID from the URI.
            id = uri.getPathSegments().get(1);
        }

        return podcastDataBase.updateFavorite(id, values);
    }
}
