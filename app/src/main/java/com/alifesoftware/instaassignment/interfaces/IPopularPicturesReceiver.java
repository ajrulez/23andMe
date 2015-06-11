package com.alifesoftware.instaassignment.interfaces;

import com.alifesoftware.instaassignment.model.PopularPicturesModel;

import java.util.ArrayList;

/**
 * Created by anujsaluja on 6/10/15.
 */
public interface IPopularPicturesReceiver {
    void onPictureDataRetrieved(ArrayList<PopularPicturesModel> popularPicturesArray);
}
