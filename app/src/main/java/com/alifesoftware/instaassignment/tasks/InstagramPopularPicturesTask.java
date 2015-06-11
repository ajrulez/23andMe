package com.alifesoftware.instaassignment.tasks;

import android.os.AsyncTask;
import android.widget.TextView;

import com.alifesoftware.instaassignment.R;
import com.alifesoftware.instaassignment.interfaces.IPopularPicturesReceiver;
import com.alifesoftware.instaassignment.model.PopularPicturesModel;

import java.util.ArrayList;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class InstagramPopularPicturesTask extends AsyncTask<Void, Void, ArrayList<PopularPicturesModel>> {

    // IPopularPicturesReceiver Receiver to receive the Results
    IPopularPicturesReceiver resultReceiver = null;

    public InstagramPopularPicturesTask(IPopularPicturesReceiver rcvr) {
        resultReceiver = rcvr;
    }

    @Override
    protected void onPreExecute() {
        // Nothing to do - Maybe show a Progress Dialog if we have time
    }

    @Override
    protected ArrayList<PopularPicturesModel> doInBackground(Void... params) {
        // TOD
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<PopularPicturesModel> popularPicturesArray) {
        // Maybe stop the Progress Dialog if we showed one in onPreExecute
        if(resultReceiver != null) {
            resultReceiver.onPictureDataRetrieved(popularPicturesArray);
        }
    }
}
