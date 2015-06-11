package com.alifesoftware.instaassignment.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alifesoftware.instaassignment.R;
import com.alifesoftware.instaassignment.adapters.PopularPicturesAdapter;
import com.alifesoftware.instaassignment.interfaces.IPopularPicturesReceiver;
import com.alifesoftware.instaassignment.model.PopularPicturesModel;
import com.alifesoftware.instaassignment.tasks.InstagramPopularPicturesTask;

import java.util.ArrayList;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class PopularPicturesFragment extends Fragment
                                    implements IPopularPicturesReceiver,
                                                View.OnClickListener {
    // ListView to display Popular Pictures
    private ListView lvPicturesList;

    // Adapter for PopularPictures
    private PopularPicturesAdapter picturesAdapter = null;

    // Progress Dialog
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        requestPopularPhotos();
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

    }
}
