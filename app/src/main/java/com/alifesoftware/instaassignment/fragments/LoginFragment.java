package com.alifesoftware.instaassignment.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alifesoftware.instaassignment.R;
import com.alifesoftware.instaassignment.activities.MainActivity;
import com.alifesoftware.instaassignment.businesslogic.InstagramClient;
import com.alifesoftware.instaassignment.interfaces.IOauthListener;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class LoginFragment extends Fragment {
    // Key to get View Width from Arguments
    public static final String ARGUMENTS_KEY_WIDTH = "width";

    // Key to get View Height from Arguments
    public static  final String ARGUMENTS_KEY_HEIGHT = "height";

    // Instagram Client
    InstagramClient instaClient = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.login_fragment, container, false);
        Button loginButton = (Button) view.findViewById(R.id.loginButton);

        instaClient = new InstagramClient(getActivity(), getArguments().getInt(ARGUMENTS_KEY_WIDTH),
                getArguments().getInt(ARGUMENTS_KEY_HEIGHT));

        instaClient.setListener(new IOauthListener() {
            @Override
            public void onSuccess() {
                int ajs = 10;
                ajs++;
                // TODO
            }

            @Override
            public void onFail(String error) {
                int ajs = 10;
                ajs++;
                // TODO
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instaClient.startAuthentication();
            }
        });

        return view;
    }
}
