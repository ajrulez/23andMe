package com.alifesoftware.instaassignment.interfaces;

/**
 * Created by anujsaluja on 6/10/15.
 */
public interface IAuthDialogListener {
    public void onComplete(String accessToken);
    public void onError(String error);
}
