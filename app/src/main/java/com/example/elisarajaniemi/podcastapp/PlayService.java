package com.example.elisarajaniemi.podcastapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.io.IOException;


/**
 * Created by Kade on 31.10.2016.
 */

public class PlayService extends Service implements MediaPlayer.OnErrorListener {

    private final IBinder mBinder = new ServiceBinder();
    MediaPlayer mPlayer;
    private int length = 0;
    private String audioPath;
    private boolean started = false;
    private PodcastItem pi;
    private boolean hasPodcast;
    private boolean isPaused = false;
    private NotificationManager mNotificationManager;




    public PlayService() {
    }

    public class ServiceBinder extends Binder {
        PlayService getService() {
            return PlayService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initPlayer();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        started = true;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_favorite_black_24dp)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.putExtra("isPlayerFragment",true);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int mNotificationId = 001;
        mNotificationManager.notify(mNotificationId, mBuilder.build());


        return START_STICKY;
    }

    public void cancelNotification(){
        mNotificationManager.cancelAll();
    }
    public boolean isPaused(){
        return isPaused;
    }

    public boolean isStarted() {return this.started;}

    public void setAudioPath(){
        try {
            mPlayer.setDataSource(this.pi.url); // setup song from https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
            mPlayer.prepare();

            // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void setPodcastObject(PodcastItem pi){
        this.pi = pi;
        hasPodcast = true;
    }
    public PodcastItem getPodcastObject(){
         return this.pi;
    }
    public void initPlayer(){
        mPlayer = new MediaPlayer();
        mPlayer.setOnErrorListener(this);

        if (mPlayer != null) {
            mPlayer.setLooping(true);
            mPlayer.setVolume(100, 100);
        }

        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            public boolean onError(MediaPlayer mp, int what, int
                    extra) {
                onError(mPlayer, what, extra);
                return true;
            }
        });

    }
    public void playMusic() {
        if (!mPlayer.isPlaying()) {
            mPlayer.start();
            isPaused = false;
        }
    }public void pauseMusic() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            length = mPlayer.getCurrentPosition();
            isPaused = true;

        }
    }

    public void resumeMusic() {
        if (!mPlayer.isPlaying()) {
            mPlayer.seekTo(length);
            mPlayer.start();
        }
    }

    public void stopMusic() {
        mPlayer.stop();
        mPlayer.reset();
        mPlayer.release();
        length = 0;
        mPlayer = null;
    }
    public void setPosition(int position){
        this.length = position;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelNotification();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }
}
