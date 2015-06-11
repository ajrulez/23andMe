package com.alifesoftware.instaassignment.interfaces;

/**
 * Created by anujsaluja on 6/10/15.
 */
public interface IOauthListener {
    public void onSuccess();

    public void onFail(String error);
}
