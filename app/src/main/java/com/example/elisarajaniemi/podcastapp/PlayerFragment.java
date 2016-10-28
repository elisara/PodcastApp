package com.example.elisarajaniemi.podcastapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

/**
 * Created by Kade on 28.10.2016.
 */

public class PlayerFragment extends Fragment implements View.OnClickListener {
    private ImageView sleepBtn, replayBtn, playBtn, forwardBtn, speedBtn, previousBtn, nextBtn, queueBtn, playlistBtn, favoriteBtn, shareBtn;
    private SeekBar seekbar;
    private MediaPlayer mediaPlayer;
    private int playbackPosition=0;
    static final String AUDIO_PATH =
            "http://dev.mw.metropolia.fi//aanimaisema//filestore//4//4_06d78bfc816994c//44_9074990dfa84c42.mp3?v=2016-10-27+13%3A29%3A30";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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





        return view;

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.sleepBtn:

                break;
            case R.id.replayBtn:

                break;
            case R.id.playBtn:
                //Play voicefile
                try {
                    playAudio(AUDIO_PATH);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //MediaPlayer.create(getBaseContext(), R.raw.voicefile).start();
                break;
            case R.id.forwardBtn:

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
    private void playAudio(String url) throws Exception
    {
        killMediaPlayer();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }
    private void killMediaPlayer() {
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
