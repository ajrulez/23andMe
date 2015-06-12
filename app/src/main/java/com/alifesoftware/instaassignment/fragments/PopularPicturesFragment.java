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
import com.alifesoftware.instaassignment.interfaces.IDataRefreshListener;
import com.alifesoftware.instaassignment.interfaces.ILikeResultListener;
import com.alifesoftware.instaassignment.interfaces.IPopularPicturesReceiver;
import com.alifesoftware.instaassignment.model.PopularPicturesModel;
import com.alifesoftware.instaassignment.tasks.InstagramPictureLikeTask;
import com.alifesoftware.instaassignment.tasks.InstagramPopularPicturesTask;
import com.alifesoftware.instaassignment.utils.Utils;

import java.util.ArrayList;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * This fragment is used to display the Popular Pictures from
 * Instagram along with a Like button. Idea is that the Like button
 * will be disabled if user has already Liked a picture.
 *
 * Currently I am having difficulties getting the Like to work, but
 * all the code is there for you t review.
 *
 * Here are the details on why *I think* Like feature is not
 * working - I think I have the right request and even have the
 * likes scope in my Authentication request, but somehow Instagram
 * rejects the request.
 * Instagram documentation states that we may need to get the application
 * approved by Instagram to use any "write" access to Instagram, and "Like"
 * is included in "write" access features.
 *
 * Link: https://instagram.com/developer/endpoints/likes/
 * and
 * https://instagram.com/developer/authentication/ (Look at Scope (Permissions)
 *
 * Note on Authentication / AccessTokens - There could be cases where the AccessToken
 * is saved, but has expired.
 * I haven't had the time to go through entire Instagram Authentication
 * documentation to see various error codes/exceptions that may indicate
 * that a token has expired.
 *
 * Basic idea is that I'll have to catch any errors/exceptions from the request
 * that signals Popular Pictures request has failed because of AccessToken
 * expiry, and then switch to LoginFragment to re-Authenticate the user.
 *
 * I have worked on OAuth clients before, and I
 * know that if an AccessToken has expired, user must be prompted to
 * login again. In general OAuth also has concept of RefreshToken, that is
 * used to get a new AccessToken without prompting the user for a login,
 * but I don't know if Instagram supports it. I didn't find any mention
 * of RefreshToken in Instagram documentation.
 *
 */
public class PopularPicturesFragment extends Fragment
                                    implements IPopularPicturesReceiver, // Receives the result when Picture data is retrieved
                                                ILikeResultListener, // Received the rsult of Like request
                                                IDataRefreshListener, // To receive Data Refresh Notifications
                                                View.OnClickListener { // Handler for a Click (Like button click in this case)
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

    /**
     * Set up Fragment View
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Saved data, if any before Fragment recreation
        savedBundle = savedInstanceState;

        // Inflate the layout for this fragment
        FrameLayout view = (FrameLayout) inflater.inflate(R.layout.popular_pictures_list, container, false);

        // Create the ListView
        lvPicturesList = (ListView) view.findViewById(R.id.popularPicturesListView);

        // Bind the adapter and ListView
        if(picturesAdapter == null) {
            picturesAdapter = new PopularPicturesAdapter(getActivity(), R.layout.popular_pictures_row_item, getActivity(), this);
            lvPicturesList.setAdapter(picturesAdapter);
        }

        // Set up the ProgressDialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setMessage(getActivity().getResources().getString(R.string.please_wait));

        return view;
    }

    /**
     * Overridden onResume
     *
     */
    @Override
    public void onResume() {
        super.onResume();

        // Do not reload the data when Fragment is recreated upon Configuration change,
        // only load it the first time Fragment is created
        if(savedBundle == null) {
            requestPopularPhotos();
        }

        // Restore old state if the Fragment is recreated
        else {
            // Restore the ListView state
            if(lvPicturesList != null) {
                lvPicturesList.onRestoreInstanceState(savedBundle.getParcelable(PopularPicturesFragment.POPULAR_PICTURES_LIST_VIEW_STATE_KEY));
            }

            // Update the adapter with saved data
            ArrayList<PopularPicturesModel> popularPictureData =
                    savedBundle.getParcelableArrayList(PopularPicturesFragment.POPULAR_PICTURES_DATA_KEY);
            if(popularPictureData != null) {
                picturesAdapter.updateData(popularPictureData);
            }
        }
    }

    /**
     * Save the state on Configuration Changes such
     * as rotation
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save PopularPicture Data and the Current position in ListView
        if(picturesAdapter != null) {
            outState.putParcelableArrayList(PopularPicturesFragment.POPULAR_PICTURES_DATA_KEY, picturesAdapter.getData());
        }

        // Save the ListView state
        if(lvPicturesList != null) {
            Parcelable listViewState = lvPicturesList.onSaveInstanceState();
            outState.putParcelable(PopularPicturesFragment.POPULAR_PICTURES_LIST_VIEW_STATE_KEY, listViewState);
        }
    }

    /**
     * Method to request Popular Pictures data from
     * Instagram using AsyncTask
     *
     */
    private void requestPopularPhotos() {
        // AsyncTask to request Popular Pictures data from Instagram
        InstagramPopularPicturesTask popularPicturesTask = new InstagramPopularPicturesTask(getActivity(), progressDialog, this);
        popularPicturesTask.execute(new Void[] {});
    }

    /**
     * Method that gets called when Popular Pictures AsyncTask
     * is finished.
     *
     * @param popularPicturesArray
     */
    public void onPictureDataRetrieved(ArrayList<PopularPicturesModel> popularPicturesArray) {
        if(popularPicturesArray != null &&
                popularPicturesArray.size() > 0) {
            // If we successfully got the data, then update
            // the ListView adapter with this data.
            picturesAdapter.updateData(popularPicturesArray);
        }
        else {
            // Show an error if we failed to get the Data from server
            //
            // Ideally our AsyncTask should send some error code here, and if the
            // error code indicates that AccessToken has expired or there
            // is an Auth issue, then we should switch to LoginFragment here
            // to give user a chance to re-Authenticate
            Toast.makeText(getActivity(),
                    getActivity().getResources().getString(R.string.error_downloading_pictures),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * Called when a View is clicked. In this case, the only
     * View we care about handling is the Like button
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(v instanceof Button) {
            // Tag of the Like button is the ID of the Image
            String imageId = (String) v.getTag();
            if(! Utils.isNullOrEmpty(imageId)) {
                // AsyncTask to Like a picture
                InstagramPictureLikeTask pictureLikeTask = new InstagramPictureLikeTask(getActivity(), (Button) v, this);
                pictureLikeTask.execute(new String[] {imageId});
            }
        }
    }

    /**
     * This method is called when Like picture operation is
     * completed
     *
     * @param success
     * @param button
     */
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

    /**
     * Activity can call this method on the Fragment
     * to request a refresh data
     *
     */
    @Override
    public void refreshData() {
        // Request new set of Popular Pictures
        requestPopularPhotos();
    }
}
