package com.alifesoftware.instaassignment.interfaces;

/**
 * Created by anujsaluja on 6/10/15.
 */
public interface ITokenResultListener {
    void onTokenReceived(boolean success, String accessToken);
}
