package com.example.elisarajaniemi.podcastapp;

import android.app.IntentService;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;

import java.io.IOException;


/**
 * Created by Kade on 31.10.2016.
 */

public class PlayService extends IntentService implements MediaPlayer.OnErrorListener {

    private final IBinder mBinder = new ServiceBinder();
    MediaPlayer mPlayer;
    private int length = 0;
    private String audioPath;
    private boolean started = false;
    private PodcastItem pi;
    private boolean hasPodcast;
    private boolean isPaused = false;
    private NotificationManager mNotificationManager;

    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_PLAY = "action_play";
    public static final String START_SERVICE = "start_service";

    private ImageView notificationPlayBtn;


    public PlayService() {
        super("oma");

    }


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public PlayService(String name) {
        super(name);
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

        String action = intent.getAction();
        if (action.equalsIgnoreCase(START_SERVICE)) {
            createNotification();
            started = true;
        } else if (action.equalsIgnoreCase(ACTION_PAUSE)) {
            pauseMusic();

        } else if (action.equalsIgnoreCase(ACTION_PLAY)) {
            playMusic();
        }

        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("PlayerServise onHandleIntent");

    }

    public void createNotification() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification);

        //the intent that is started when the notification is clicked (works)
        Intent playerIntent = new Intent(this, MainActivity.class);
        playerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        playerIntent.putExtra("isPlayerFragment", true);
        PendingIntent playerPendingIntent = PendingIntent.getActivity(this, 0, playerIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_pause_circle_filled_black_24dp).setTicker("Tähän teksti").setContent(notificationView);
        notificationView.setImageViewResource(R.id.notifiationImage, R.drawable.podcast_headphones);
        notificationView.setTextViewText(R.id.notifiationText1, pi.title);
        notificationView.setTextViewText(R.id.notifiationText2, pi.collectionName);


        Intent pauseIntent = new Intent(this, PlayService.class);
        pauseIntent.setAction(ACTION_PAUSE);
        PendingIntent pendingPauseIntent = PendingIntent.getService(getApplicationContext(), 1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playIntent = new Intent(this, PlayService.class);
        playIntent.setAction(ACTION_PLAY);
        PendingIntent pendingPlayIntent = PendingIntent.getService(getApplicationContext(), 1, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (mPlayer.isPlaying()) {
            notificationView.setImageViewResource(R.id.notificationPlayBtn, R.drawable.ic_pause_black_50dp);
            notificationView.setOnClickPendingIntent(R.id.notificationPlayBtn, pendingPauseIntent);
            mBuilder.setSmallIcon(R.drawable.ic_play_circle_filled_black_24dp);
        } else {
            notificationView.setImageViewResource(R.id.notificationPlayBtn, R.drawable.ic_play_arrow_black_50dp);
            notificationView.setOnClickPendingIntent(R.id.notificationPlayBtn, pendingPlayIntent);
            mBuilder.setSmallIcon(R.drawable.ic_pause_circle_filled_black_24dp);
        }
        notificationView.setImageViewResource(R.id.notificationSkipBtn, R.drawable.ic_skip_next_black_50dp);
        mBuilder.setContentIntent(playerPendingIntent);
        mNotificationManager.notify(1, mBuilder.build());
    }

    public void newNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle(pi.title);

        Intent pauseIntent = new Intent(this, PlayService.class);
        pauseIntent.setAction(ACTION_PAUSE);
        PendingIntent pendingPauseIntent = PendingIntent.getService(getApplicationContext(), 1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playIntent = new Intent(this, PlayService.class);
        playIntent.setAction(ACTION_PLAY);
        PendingIntent pendingPlayIntent = PendingIntent.getService(getApplicationContext(), 1, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (mPlayer.isPlaying()) {
            mBuilder.setSmallIcon(R.drawable.ic_pause_circle_filled_black_24dp);
            mBuilder.addAction(R.drawable.ic_pause_circle_filled_black_24dp, "Pause", pendingPauseIntent);
        } else {
            mBuilder.setSmallIcon(R.drawable.ic_play_circle_filled_black_24dp);
            mBuilder.addAction(R.drawable.ic_play_circle_filled_black_24dp, "Play", pendingPlayIntent);
        }
        //mBuilder.addAction(R.drawable.ic_pause_circle_filled_black_24dp, "Pause", pendingPauseIntent);
        //mBuilder.addAction(R.drawable.ic_play_circle_filled_black_24dp, "Play", pendingPlayIntent);
        Intent playerIntent = new Intent(this, MainActivity.class);
        playerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        playerIntent.putExtra("isPlayerFragment", true);
        PendingIntent playerPendingIntent = PendingIntent.getActivity(this, 0, playerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(playerPendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int mNotificationId = 001;
        mNotificationManager.notify(1, mBuilder.build());

    }


    public void cancelNotification() {
        mNotificationManager.cancelAll();
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isStarted() {
        return this.started;
    }

    public void setAudioPath() {
        try {
            mPlayer.setDataSource(this.pi.url); // setup song from https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
            mPlayer.prepare();

            // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setPodcastObject(PodcastItem pi) {
        this.pi = pi;
        hasPodcast = true;
        createNotification();
    }

    public PodcastItem getPodcastObject() {
        return this.pi;
    }

    public void initPlayer() {
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
            createNotification();

        }
    }

    public void pauseMusic() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            length = mPlayer.getCurrentPosition();
            isPaused = true;
            createNotification();

        }
    }

    public void resumeMusic() {
        if (!mPlayer.isPlaying()) {
            mPlayer.seekTo(length);
            mPlayer.start();
            createNotification();
        }
    }

    public void stopMusic() {
        mPlayer.stop();
        mPlayer.reset();
        mPlayer.release();
        length = 0;
        mPlayer = null;
    }

    public void setPosition(int position) {
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
