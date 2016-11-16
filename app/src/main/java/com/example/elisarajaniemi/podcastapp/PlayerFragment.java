package com.example.elisarajaniemi.podcastapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by Kade on 28.10.2016.
 */

public class PlayerFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, OnBufferingUpdateListener, SeekBar.OnSeekBarChangeListener, ServiceCallbacks {
    private ImageView sleepBtn, replayBtn, playBtn, forwardBtn, speedBtn, previousBtn, nextBtn, queueBtn, playlistBtn, favoriteBtn, shareBtn, podcastPic;
    private SeekBar seekbar;
    private TextView currentTime, fullTime;
    private int mediaFileLengthInMilliseconds;
    private final Handler handler = new Handler();
    private Utilities utils;
    private PodcastItem piFromService, piFromClick, pi2;


    MainActivity mActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mActivity = (MainActivity) getActivity();
        mActivity.pServ.setCallbacks(PlayerFragment.this);
        utils = new Utilities();
        View view = inflater.inflate(R.layout.play_screen, container, false);
        piFromService = mActivity.pServ.getPodcastObject();
        if (getArguments() == null) piFromClick = piFromService;
        else {
            piFromClick = (PodcastItem) getArguments().getSerializable("episode");
            pi2 = (PodcastItem) getArguments().getSerializable("podcastItem");
            if (pi2 != null) {
                piFromClick = pi2;
                System.out.println("Podcast URL ELSE IF: " + piFromClick.url);
            }
        }
        podcastPic = (ImageView) view.findViewById(R.id.podcastPic);
        sleepBtn = (ImageView) view.findViewById(R.id.sleepBtn);
        replayBtn = (ImageView) view.findViewById(R.id.replayBtn);
        playBtn = (ImageView) view.findViewById(R.id.playBtn);
        forwardBtn = (ImageView) view.findViewById(R.id.forwardBtn);
        speedBtn = (ImageView) view.findViewById(R.id.speedBtn);
        previousBtn = (ImageView) view.findViewById(R.id.previousBtn);
        nextBtn = (ImageView) view.findViewById(R.id.nextBtn);
        queueBtn = (ImageView) view.findViewById(R.id.queueBtn);
        playlistBtn = (ImageView) view.findViewById(R.id.playlistBtn);
        favoriteBtn = (ImageView) view.findViewById(R.id.favoriteBtn);
        shareBtn = (ImageView) view.findViewById(R.id.shareBtn);

        sleepBtn.setOnClickListener(this);
        replayBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        forwardBtn.setOnClickListener(this);
        speedBtn.setOnClickListener(this);
        previousBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        queueBtn.setOnClickListener(this);
        playlistBtn.setOnClickListener(this);
        favoriteBtn.setOnClickListener(this);
        shareBtn.setOnClickListener(this);

        seekbar = (SeekBar) view.findViewById(R.id.seekBar);
        seekbar.setMax(99);
        seekbar.setOnTouchListener(this);

        currentTime = (TextView) view.findViewById(R.id.currentTime);
        fullTime = (TextView) view.findViewById(R.id.fullTime);
        currentTime.setText("00:00");
        fullTime.setText("00:00");


        seekbar.setOnSeekBarChangeListener(this);
        utils = new Utilities();
        if (mActivity.pServ.getStatus() < 2) {
            mActivity.pServ.setPodcastObject(piFromClick);
            mActivity.pServ.setAudioPath();
            mActivity.pServ.mPlayer.setOnBufferingUpdateListener(this);


        } else if (!piFromClick.url.equals(piFromService.url)) {
                mediaFileLengthInMilliseconds = 0;
                mActivity.pServ.stopMusic();
                mActivity.pServ.initPlayer();
                mActivity.pServ.setPodcastObject(piFromClick);
                mActivity.pServ.setAudioPath();
                mActivity.pServ.mPlayer.setOnBufferingUpdateListener(this);

            }else if (mActivity.pServ.getStatus() == 3) {
            serviceCallbackMethod();
        }
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sleepBtn:

                break;
            case R.id.replayBtn:
                mActivity.pServ.setPosition(mActivity.pServ.mPlayer.getCurrentPosition() - 10000);
                mActivity.pServ.mPlayer.seekTo(mActivity.pServ.mPlayer.getCurrentPosition() - 10000);
                break;
            case R.id.playBtn:
                if (mActivity.pServ.getStatus() > 1) {
                    if (mActivity.pServ.mPlayer.isPlaying()) mActivity.pServ.pauseMusic();
                    else mActivity.pServ.resumeMusic();
                    mediaFileLengthInMilliseconds = mActivity.pServ.mPlayer.getDuration(); // gets the song length in milliseconds from URL
                    updateProgressBar();
                }
                break;
            case R.id.forwardBtn:
                mActivity.pServ.setPosition(mActivity.pServ.mPlayer.getCurrentPosition() + 10000);
                mActivity.pServ.mPlayer.seekTo(mActivity.pServ.mPlayer.getCurrentPosition() + 10000);
                break;
            case R.id.speedBtn:
                //mActivity.pServ.setSpeed();
                break;
            case R.id.previousBtn:

                break;
            case R.id.nextBtn:

                break;
            case R.id.queueBtn:

                break;
            case R.id.playlistBtn:

                break;
            case R.id.favoriteBtn:

                break;
            case R.id.shareBtn:

                break;
        }
    }

    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        handler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            int millis = mActivity.pServ.mPlayer.getCurrentPosition();
            int millisLeft = mActivity.pServ.mPlayer.getDuration() - mActivity.pServ.mPlayer.getCurrentPosition();

            // Displaying Total Duration time
            fullTime.setText("" + utils.milliSecondsToTimer(millisLeft));
            // Displaying time completed playing
            currentTime.setText("" + utils.milliSecondsToTimer(millis));

            // Updating progress bar
            seekbar.setProgress((int) (((float) mActivity.pServ.mPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"

            // Displaying play or pause icon
            if (mActivity.pServ.mPlayer.isPlaying())
                playBtn.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
            else playBtn.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);

            // Running this thread after 100 milliseconds
            handler.postDelayed(this, 100);

        }
    };

    /**
     *
     * */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
    }

    /**
     * When user starts moving the progress handler
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        handler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mActivity.pServ.mPlayer.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mActivity.pServ.mPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        if (v.getId() == R.id.seekBar) {
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
            if (mActivity.mIsBound) {
                SeekBar sb = (SeekBar) v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mActivity.pServ.setPosition(playPositionInMillisecconds);
                mActivity.pServ.mPlayer.seekTo(playPositionInMillisecconds);
            }
        }

        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(mUpdateTimeTask);
    }


    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        /** Method which updates the SeekBar secondary progress by current song loading from URL position*/
        seekbar.setSecondaryProgress(percent);
    }


    @Override
    public void serviceCallbackMethod() {
        mActivity.imageLoader.loadImage(mActivity.pServ.getPodcastObject().imageURL, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                podcastPic.setImageBitmap(loadedImage);
            }
        });

        mediaFileLengthInMilliseconds = mActivity.pServ.mPlayer.getDuration();
        updateProgressBar();
    }
}

