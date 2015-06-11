package com.alifesoftware.instaassignment.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alifesoftware.instaassignment.R;
import com.alifesoftware.instaassignment.adapters.PopularPicturesAdapter;
import com.alifesoftware.instaassignment.interfaces.ILikeResultListener;
import com.alifesoftware.instaassignment.interfaces.IPopularPicturesReceiver;
import com.alifesoftware.instaassignment.model.PopularPicturesModel;
import com.alifesoftware.instaassignment.tasks.InstagramPictureLikeTask;
import com.alifesoftware.instaassignment.tasks.InstagramPopularPicturesTask;
import com.alifesoftware.instaassignment.utils.Utils;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class PopularPicturesFragment extends Fragment
                                    implements IPopularPicturesReceiver,
                                                ILikeResultListener,
                                                View.OnClickListener {
    // ListView to display Popular Pictures
    private ListView lvPicturesList;

    // Adapter for PopularPictures
    private PopularPicturesAdapter picturesAdapter = null;

    // Progress Dialog
    private ProgressDialog progressDialog;

    // Handler
    private final Handler handler = new Handler();

    // Delay for updating the Button
    private final long UPDATE_DELAY = 300; // ms

    // Bundle
    private Bundle savedBundle = null;

    // Bundle Keys to save the data across configuration changes
    private static final String POPULAR_PICTURES_DATA_KEY = "popularpicturesdata";
    private static final String POPULAR_PICTURES_LIST_VIEW_STATE_KEY = "popularpictureslistviewstate";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        savedBundle = savedInstanceState;

        // Inflate the layout for this fragment
        FrameLayout view = (FrameLayout) inflater.inflate(R.layout.popular_pictures_list, container, false);

        lvPicturesList = (ListView) view.findViewById(R.id.popularPicturesListView);

        if(picturesAdapter == null) {
            picturesAdapter = new PopularPicturesAdapter(getActivity(), R.layout.popular_pictures_row_item, getActivity(), this);
            lvPicturesList.setAdapter(picturesAdapter);
        }

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage(getActivity().getResources().getString(R.string.please_wait));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Do not reload the data when Fragment is recreated
        if(savedBundle == null) {
            requestPopularPhotos();
        }
        // Restore old state
        else {
            if(lvPicturesList != null) {
                lvPicturesList.onRestoreInstanceState(savedBundle.getParcelable(PopularPicturesFragment.POPULAR_PICTURES_LIST_VIEW_STATE_KEY));
            }
            ArrayList<PopularPicturesModel> popularPictureData =
                    savedBundle.getParcelableArrayList(PopularPicturesFragment.POPULAR_PICTURES_DATA_KEY);
            if(popularPictureData != null) {
                picturesAdapter.updateData(popularPictureData);
            }
        }
    }

    // Save the state on Configuration Changes
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save PopularPicture Data and the Current position in ListView
        if(picturesAdapter != null) {
            outState.putParcelableArrayList(PopularPicturesFragment.POPULAR_PICTURES_DATA_KEY, picturesAdapter.getData());
        }

        if(lvPicturesList != null) {
            Parcelable listViewState = lvPicturesList.onSaveInstanceState();
            outState.putParcelable(PopularPicturesFragment.POPULAR_PICTURES_LIST_VIEW_STATE_KEY, listViewState);
        }
    }

    private void requestPopularPhotos() {
        InstagramPopularPicturesTask popularPicturesTask = new InstagramPopularPicturesTask(getActivity(), progressDialog, this);
        popularPicturesTask.execute(new Void[] {});
    }

    public void onPictureDataRetrieved(ArrayList<PopularPicturesModel> popularPicturesArray) {
        if(popularPicturesArray != null &&
                popularPicturesArray.size() > 0) {
            picturesAdapter.updateData(popularPicturesArray);
        }
        else {
            Toast.makeText(getActivity(),
                    getActivity().getResources().getString(R.string.error_downloading_pictures),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v instanceof Button) {
            String imageId = (String) v.getTag();
            if(! Utils.isNullOrEmpty(imageId)) {
                InstagramPictureLikeTask pictureLikeTask = new InstagramPictureLikeTask(getActivity(), (Button) v, this);
                pictureLikeTask.execute(new String[] {imageId});
            }
        }
    }

    @Override
    public void onLikeCompleted(boolean success, final Button button) {
        if(success &&
                button != null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    button.setEnabled(false);
                }
            }, UPDATE_DELAY);
        }
    }
}
