package com.alifesoftware.instaassignment.utils;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class Utils {
    public static boolean isNullOrEmpty(String str) {
        if(str == null ||
                str.isEmpty()) {
            return true;
        }

        return false;
    }
}
