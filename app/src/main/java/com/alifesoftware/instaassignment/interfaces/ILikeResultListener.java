package com.alifesoftware.instaassignment.interfaces;

import android.widget.Button;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * This interface is for a listener that is signaled
 * when background operation for posting a Like
 * for a Popular Picture is finished
 *
 */
public interface ILikeResultListener {
    void onLikeCompleted(boolean success, Button button);
}
