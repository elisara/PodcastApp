package com.example.elisarajaniemi.podcastapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by Keni on 2016-11-16.
 */

public class SmallPlayerFragment extends Fragment implements View.OnClickListener {
    private ImageView podcastImage, button1, button2;
    private TextView text1, text2;
    private ProgressBar bar;
    private final Handler handler = new Handler();
    MainActivity mActivity;
    Boolean picLoaded = false;
    String currentPodcastId = " ";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.small_player, container, false);
        this.mActivity = (MainActivity) getActivity();

        bar = (ProgressBar) view.findViewById(R.id.smallPlayerProgress);
        podcastImage = (ImageView) view.findViewById(R.id.smallPlayerImage);
        text1 = (TextView) view.findViewById(R.id.smallPlayerText1);
        text2 = (TextView) view.findViewById(R.id.smallPlayerText2);
        button1 = (ImageView) view.findViewById(R.id.smallPlayerPlayBtn);
        button2 = (ImageView) view.findViewById(R.id.smallPlayerSkipBtn);


        //podcastImage.setImageResource(R.drawable.podcast_headphones);

        button1.setImageResource(R.drawable.ic_play_arrow_black_50dp);
        button2.setImageResource(R.drawable.ic_skip_next_black_50dp);

        podcastImage.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);

        bar.setMax(99);

        handler.postDelayed(updateTask, 100);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.smallPlayerImage:
                mActivity.openPlayer();

                break;
            case R.id.smallPlayerText1:
                mActivity.openPlayer();
                break;
            case R.id.smallPlayerText2:
                mActivity.openPlayer();
                break;
            case R.id.smallPlayerPlayBtn:
                if (mActivity.pServ.getStatus() > 1) {
                    if (mActivity.pServ.mPlayer.isPlaying()) mActivity.pServ.pauseMusic();
                    else mActivity.pServ.resumeMusic();
                }

                break;
            case R.id.smallPlayerSkipBtn:
                mActivity.pServ.playNext();
                break;

        }

    }

    private Runnable updateTask = new Runnable() {
        public void run() {
            if (mActivity.pServ.getStatus()==3){
                if (!currentPodcastId.equalsIgnoreCase(mActivity.pServ.getPodcastObject().programID)) picLoaded = false;
            }
            // Displaying play or pause icon
            if (mActivity.pServ.mPlayer != null) {

                if (mActivity.pServ.mPlayer.isPlaying())
                    button1.setImageResource(R.drawable.ic_pause_black_50dp);
                else button1.setImageResource(R.drawable.ic_play_arrow_black_50dp);
                if (mActivity.pServ.getStatus() == 3) {
                    //podcastImage.setImageBitmap(mActivity.pServ.getPodcastObject().picture);
                    bar.setProgress((int) (((float) mActivity.pServ.mPlayer.getCurrentPosition() / mActivity.pServ.mPlayer.getDuration()) * 100));
                    text1.setText(mActivity.pServ.getPodcastObject().collectionName);
                    text2.setText(mActivity.pServ.getPodcastObject().title);
                }

                if(mActivity.pServ.getStatus()>1&&picLoaded==false && !mActivity.pServ.getPodcastObject().imageURL.equals("")) {
                    mActivity.imageLoader.loadImage("http://images.cdn.yle.fi/image/upload/w_0.1,h_0.1/" + mActivity.pServ.getPodcastObject().imageURL + ".jpg", new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                            podcastImage.setImageBitmap(loadedImage);
                            picLoaded = true;
                        }
                    });

                }
                else if(mActivity.pServ.getStatus()>1&&picLoaded==false && mActivity.pServ.getPodcastObject().imageURL.equals("")) {
                    mActivity.imageLoader.loadImage("http://images.cdn.yle.fi/image/upload/w_0.1,h_0.1/" + mActivity.pServ.getPodcastObject().serieImageURL + ".jpg", new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                            podcastImage.setImageBitmap(loadedImage);
                            picLoaded = true;
                        }
                    });

                }
                if (mActivity.pServ.getStatus()==3) currentPodcastId = mActivity.pServ.getPodcastObject().programID;
                // Running this thread after 100 milliseconds
                handler.postDelayed(this, 100);

            }
        }
    };
}
