package com.alifesoftware.instaassignment.fragments;


import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.alifesoftware.instaassignment.adapters.PopularPicturesAdapter;
import com.alifesoftware.instaassignment.interfaces.IPopularPicturesReceiver;
import com.alifesoftware.instaassignment.model.PopularPicturesModel;

import java.util.ArrayList;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class PopularPicturesFragment extends Fragment
                                    implements IPopularPicturesReceiver {
    // ListView to display Popular Pictures
    private ListView lvPicturesList;

    // Adapter for PopularPictures
    private PopularPicturesAdapter picturesAdapter = null;

    public void onPictureDataRetrieved(ArrayList<PopularPicturesModel> popularPicturesArray) {

    }
}
