package com.example.elisarajaniemi.podcastapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by Kade on 28.10.2016.
 */

public class PlayerFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, OnBufferingUpdateListener, OnCompletionListener {
    private ImageView sleepBtn, replayBtn, playBtn, forwardBtn, speedBtn, previousBtn, nextBtn, queueBtn, playlistBtn, favoriteBtn, shareBtn;
    private SeekBar seekbar;
    private TextView currentTime, fullTime;
    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds;
    private final Handler handler = new Handler();
    private boolean serviceStarted = false;

    MainActivity mActivity;

    static final String AUDIO_PATH =
            "http://dev.mw.metropolia.fi//aanimaisema//filestore//4//4_06d78bfc816994c//44_9074990dfa84c42.mp3?v=2016-10-27+13%3A29%3A30";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.play_screen, container, false);

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

        currentTime = (TextView) view.findViewById(R.id.currentTime) ;
        fullTime = (TextView) view.findViewById(R.id.fullTime) ;

        mActivity.pServ.mPlayer.setOnBufferingUpdateListener(this);
        mActivity.pServ.mPlayer.setOnCompletionListener(this);

        return view;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sleepBtn:

                break;
            case R.id.replayBtn:
                mActivity.pServ.mPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
                break;
            case R.id.playBtn:

                if(!serviceStarted) {
                    Intent podcast = new Intent(getActivity(), PlayService.class);
                    getActivity().startService(podcast);
                    serviceStarted = true;
                    playBtn.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                }else{
                    if(mActivity.pServ.isPlaying()){
                        mActivity.pServ.pauseMusic();
                        playBtn.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);

                    }else {
                        mActivity.pServ.resumeMusic();
                        playBtn.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                    }
                }

                mediaFileLengthInMilliseconds = mActivity.pServ.mPlayer.getDuration(); // gets the song length in milliseconds from URL
                primarySeekBarProgressUpdater();
                break;
            case R.id.forwardBtn:
                mActivity.pServ.mPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
                break;
            case R.id.speedBtn:

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

    private void primarySeekBarProgressUpdater() {
        seekbar.setProgress((int) (((float) mActivity.pServ.mPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"
        int millis = mActivity.pServ.mPlayer.getCurrentPosition();
        int millisLeft = mActivity.pServ.mPlayer.getDuration()-mActivity.pServ.mPlayer.getCurrentPosition();

        currentTime.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
        fullTime.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millisLeft),
                TimeUnit.MILLISECONDS.toSeconds(millisLeft) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisLeft))));
        if (mActivity.mIsBound) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        if (v.getId() == R.id.seekBar) {
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
            if (mActivity.mIsBound) {
                SeekBar sb = (SeekBar) v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mActivity.pServ.mPlayer.seekTo(playPositionInMillisecconds);
            }
        }

        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }

    private void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void onCompletion(MediaPlayer mp) {
        /** MediaPlayer onCompletion event handler. Method which calls then song playing is complete*/
        playBtn.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
    }

    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        /** Method which updates the SeekBar secondary progress by current song loading from URL position*/
        seekbar.setSecondaryProgress(percent);
    }
}

