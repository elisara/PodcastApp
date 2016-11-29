package com.example.elisarajaniemi.podcastapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by Elisa Rajaniemi on 24.11.2016.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PodcastItem> list;
    private TextView textView;
    private ImageView imageView;
    private PodcastItem pi;
    private MainActivity mActivity = new MainActivity();
    protected ImageLoader imageLoader = ImageLoader.getInstance();


    public GridViewAdapter(Context context, ArrayList<PodcastItem> list) {
        this.context = context;
        this.list = list;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

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
                //.writeDebugLogs()
                .build();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_add_black_24dp)
                .showImageForEmptyUri(R.drawable.ic_add_black_24dp)
                .showImageOnFail(R.drawable.ic_add_black_24dp)
                .cacheOnDisc(true)
                .build();

        //ImageLoader.getInstance().init(config);

        View myView = convertView;

        if (myView == null) {
            myView = LayoutInflater.from(context).inflate(R.layout.gridview_item, parent, false);


        } else {
           myView = convertView;
        }

        imageView = (ImageView) myView.findViewById(R.id.grid_item_image);
        imageView.setImageResource(R.drawable.ic_add_black_24dp);
        if(!list.get(position).collectionName.contains("Metropolia")) {
            imageLoader.displayImage("http://images.cdn.yle.fi/image/upload//w_850,c_fill/" + list.get(position).imageURL + ".jpg", imageView, options);
            //w_705,h_520,c_fill,g_auto
        }
        else{
            imageLoader.displayImage("https://s3.postimg.org/gzeoosubn/kissaholder.jpg", imageView, options);
        }

        textView = (TextView) myView.findViewById(R.id.grid_item_label);
        textView.setText(list.get(position).collectionName);

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

