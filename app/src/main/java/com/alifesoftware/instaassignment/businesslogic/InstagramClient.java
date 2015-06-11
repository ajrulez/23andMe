package com.alifesoftware.instaassignment.businesslogic;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.alifesoftware.instaassignment.R;
import com.alifesoftware.instaassignment.authentication.AuthDialog;
import com.alifesoftware.instaassignment.interfaces.IAuthDialogListener;
import com.alifesoftware.instaassignment.interfaces.IOauthListener;
import com.alifesoftware.instaassignment.interfaces.ITokenResultListener;
import com.alifesoftware.instaassignment.tasks.InstagramAccessTokenTask;
import com.alifesoftware.instaassignment.utils.Utils;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class InstagramClient implements ITokenResultListener {
    // Session manager to Store Tokens
    private SessionManager sessionManager;

    // Authentication Dialog
    private AuthDialog authDialog;

    // OAuth Listener
    private IOauthListener oAuthListener;

    // Context
    private Context appContext;

    // Progress Dialog
    private ProgressDialog progressDialog;

    // Access Token
    private String accessToken;

    public InstagramClient(Context context, int width, int height) {
        appContext = context;
        sessionManager = new SessionManager(appContext);
        accessToken = sessionManager.getAccessToken();

        IAuthDialogListener listener = new IAuthDialogListener() {
            @Override
            public void onComplete(String code) {
                // Get Access Token using the Code
                requestAccessToken(code);
            }

            @Override
            public void onError(String error) {
                oAuthListener.onFail(appContext.getResources().getString(R.string.authentication_failed) + " :" + error);
            }
        };

        authDialog = new AuthDialog(appContext, width, height, listener);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
    }

    // Method to request Access Token using the Code
    private void requestAccessToken(final String code) {
        InstagramAccessTokenTask accessTokenTask = new InstagramAccessTokenTask(appContext,
                                                            progressDialog, this);
        accessTokenTask.execute(new String[] {code});
    }

    // ITokenResultListener Implementation
    @Override
    public void onTokenReceived(boolean success, String accessToken) {
        if(success &&
                !Utils.isNullOrEmpty(accessToken)) {
            sessionManager.storeAccessToken(accessToken);
        }
        else {
            Toast.makeText(appContext,
                    appContext.getResources().getString(R.string.failed_to_get_access_token),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void setListener(IOauthListener listener) {
        oAuthListener = listener;
    }

    public void startAuthentication() {
        authDialog.show();
    }
}
