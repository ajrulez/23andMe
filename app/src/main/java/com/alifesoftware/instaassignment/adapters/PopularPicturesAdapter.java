package com.alifesoftware.instaassignment.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alifesoftware.instaassignment.R;
import com.alifesoftware.instaassignment.model.PopularPicturesModel;
import com.alifesoftware.instaassignment.viewholders.PopularPicturesViewHolder;

import java.util.ArrayList;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.Picasso;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class PopularPicturesAdapter extends ArrayAdapter<PopularPicturesModel> {

    // Data
    private ArrayList<PopularPicturesModel> arrPictureData = new ArrayList<PopularPicturesModel> ();

    // Using Universal Image Loader to LazyLoad the Images
    private ImageLoader imageLoader;

    // Context
    private Context appContext;

    // Activity
    private Activity activity;

    // OnClickListener for the Like Button
    private View.OnClickListener onClickListener;

    // Default Constructor
    public PopularPicturesAdapter(Context context, int resource, Activity activity, View.OnClickListener listener) {
        super(context, resource);

        appContext = context;
        this.activity = activity;
        onClickListener = listener;

        @SuppressWarnings("deprecation")
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        @SuppressWarnings("deprecation")
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                appContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);

        imageLoader = ImageLoader.getInstance();
    }

    // Method to update the Data
    public synchronized void updateData(final ArrayList<PopularPicturesModel> data) {
        try {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    arrPictureData = data;
                    notifyDataSetChanged();
                }
            });
        }

        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return arrPictureData.size();
    }



    // getView
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PopularPicturesViewHolder holder = null;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.popular_pictures_row_item, null);

            // Creates a ViewHolder and store references to the children views
            // we want to bind data to.
            holder = new PopularPicturesViewHolder();
            holder.tvCaption = (TextView) convertView.findViewById(R.id.popularCaptionTextView);
            holder.ivImage = (ImageView) convertView.findViewById(R.id.popularImageView);
            holder.btnLike = (Button) convertView.findViewById(R.id.likeButton);
            holder.btnLike.setOnClickListener(onClickListener);

            convertView.setTag(holder);
        }

        else {
            // Get the ViewHolder back to get fast access to the TextView
            holder = (PopularPicturesViewHolder) convertView.getTag();
        }

        if(arrPictureData != null &&
                arrPictureData.size() > 0 &&
                position < arrPictureData.size()) {

            // Bind the data efficiently with the holder.
            holder.tvCaption.setText(arrPictureData.get(position).getPictureCaption());

            // While we load the Image in the ImageView, set a default Image
            holder.ivImage.setImageDrawable(appContext.getResources().getDrawable(R.mipmap.ic_launcher));

            String imageUrl = arrPictureData.get(position).getPictureUrl();
            // Issue the LazyLoad Request to UIL
            /*Picasso.with(appContext)
                    .load(imageUrl)
                    .resize(150, 150)
                    .centerCrop()
                    .into(holder.ivImage);*/
            //Picasso.with(appContext).load(arrPictureData.get(position).getPictureUrl()).into(holder.ivImage);
            imageLoader.displayImage(arrPictureData.get(position).getPictureUrl(), holder.ivImage);

            // Add the ID of the image as a Tag for the button so when
            // the button is clicked, we can get the ID from the button
            // and post the Like
            holder.btnLike.setTag(arrPictureData.get(position).getPictureId());

            // Disable the Like button is user has already liked it
            if(arrPictureData.get(position).isHasUserLiked()) {
                holder.btnLike.setEnabled(false);
            }
            else {
                holder.btnLike.setEnabled(true);
            }
        }

        return convertView;
    }

}
