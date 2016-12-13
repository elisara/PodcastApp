package com.example.elisarajaniemi.podcastapp;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.util.ArrayList;

/**
 * Created by Elisa Rajaniemi on 24.11.2016.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PodcastItem> list;
    private TextView titleView,collectionVIew, lengthView;
    private ImageView imageView;
    protected ImageLoader imageLoader;


    public GridViewAdapter(Context context, ArrayList<PodcastItem> list) {
        this.context = context;
        this.list = list;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View myView = convertView;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileCount(100)
                .imageDownloader(new BaseImageDownloader(context)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .build();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_add_black_24dp)
                .showImageForEmptyUri(R.drawable.ic_add_black_24dp)
                .showImageOnFail(R.drawable.ic_add_black_24dp)
                .cacheOnDisc(true)
                .build();

        if(!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().init(config);
        }

        if (myView == null) {
            myView = LayoutInflater.from(context).inflate(R.layout.gridview_item, parent, false);

        } else {
            myView = convertView;
        }

        int width =  (context.getResources().getDisplayMetrics().widthPixels)/2;
        int height = (context.getResources().getDisplayMetrics().heightPixels)/7;


        imageView = (ImageView) myView.findViewById(R.id.grid_item_image);
        imageView.setBackgroundColor(Color.BLACK);
        if (!list.get(position).collectionName.contains("Metropolia")) {
            ImageLoader.getInstance().displayImage("http://images.cdn.yle.fi/image/upload//w_" + width + ",h_" + height + ",c_fill/" + list.get(position).imageURL + ".jpg", imageView, options);

        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            imageView.setLayoutParams(layoutParams);
            ImageLoader.getInstance().displayImage("https://s3.postimg.org/gzeoosubn/kissaholder.jpg", imageView, options);
        }


        String length = DateUtils.formatElapsedTime(list.get(position).length); //(list.get(position).length) / 60;

        collectionVIew = (TextView) myView.findViewById(R.id.grid_item_collection);
        collectionVIew.setText(list.get(position).collectionName);

        titleView = (TextView) myView.findViewById(R.id.grid_item_title);
        titleView.setText(list.get(position).title);

        DateUtils.formatElapsedTime(list.get(position).length);

        lengthView = (TextView) myView.findViewById(R.id.grid_item_length);
        lengthView.setText(length);

        return myView;
    }

    @Override public int getCount () {
        return list.size();
    }


    @Override public Object getItem ( int position){
        return position;
    }

    @Override public long getItemId ( int position){
        return position;
    }

}

