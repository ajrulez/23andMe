package com.alifesoftware.instaassignment.businesslogic;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class SessionManager {
    private SharedPreferences prefStore;
    private Editor prefEditor;

    private static final String APP_PREF_STORE = "instaprefs";
    private static final String INSTAGRAM_ACCESS_TOKEN = "access_token";

    public SessionManager(Context context) {
        prefStore = context.getSharedPreferences(APP_PREF_STORE, Context.MODE_PRIVATE);
        prefEditor = prefStore.edit();
    }

    public void storeAccessToken(String accessToken) {
        prefEditor.putString(INSTAGRAM_ACCESS_TOKEN, accessToken);
        prefEditor.commit();
    }

    public void resetAccessToken() {
        prefEditor.putString(INSTAGRAM_ACCESS_TOKEN, null);
        prefEditor.commit();
    }

    public String getAccessToken() {
        return prefStore.getString(INSTAGRAM_ACCESS_TOKEN, null);
    }
}
