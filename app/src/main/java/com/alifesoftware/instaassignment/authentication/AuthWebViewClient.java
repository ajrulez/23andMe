package com.alifesoftware.instaassignment.authentication;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alifesoftware.instaassignment.interfaces.IAuthDialogListener;
import com.alifesoftware.instaassignment.utils.Constants;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class AuthWebViewClient extends WebViewClient {

    // Login Tag
    private static final String TAG = "AuthWebViewClient";

    // Authentication Listener
    IAuthDialogListener authListener = null;

    // Authentication Dialog
    AuthDialog authDialog = null;


    public AuthWebViewClient(IAuthDialogListener listener, AuthDialog dialog) {
        authListener = listener;
        authDialog = dialog;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d(TAG, "Redirecting URL " + url);
        if (url.startsWith(Constants.REDIRECT_URI)) {
            String urls[] = url.split("=");
            authListener.onComplete(urls[1]);
            authDialog.dismiss();
            return true;
        }

        return false;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        Log.d(TAG, "Page error: " + description);

        super.onReceivedError(view, errorCode, description, failingUrl);
        authListener.onError(description);
        authDialog.dismiss();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.d(TAG, "Loading URL: " + url);

        super.onPageStarted(view, url, favicon);
        authDialog.showProgressDialog();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        Log.d(TAG, "onPageFinished URL: " + url);
        authDialog.hideProgressDialog();
    }
}
