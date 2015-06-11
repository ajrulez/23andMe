package com.alifesoftware.instaassignment.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

import com.alifesoftware.instaassignment.businesslogic.SessionManager;
import com.alifesoftware.instaassignment.interfaces.ILikeResultListener;
import com.alifesoftware.instaassignment.parser.InstagramLikeResponseParser;
import com.alifesoftware.instaassignment.utils.Constants;
import com.alifesoftware.instaassignment.utils.Utils;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by anujsaluja on 6/10/15.
 */

/**
 Note: "Like" a picture feature is not working as the request fails
 with an error. It is likely because even though we are requesting scope=like
 Instagram is not granting that privilege to our AccessToken because they mention
 that they need to review the app for any of the "write" scopes that we request

 Link: https://instagram.com/developer/authentication/
 In the above link, see Scope (Permissions) section
 */


public class InstagramPictureLikeTask extends AsyncTask<String, Void, Boolean> {
    // ILikeResultListener
    private ILikeResultListener likeListener;

    // Context
    private Context appContext;

    // Button
    private Button button;

    public InstagramPictureLikeTask(Context ctx, Button btn, ILikeResultListener listener) {
        appContext = ctx;
        likeListener = listener;
        button = btn;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if(params.length == 0) {
            return Boolean.FALSE;
        }

        SessionManager sessionManager = new SessionManager(appContext);
        String accessToken = sessionManager.getAccessToken();

        if(Utils.isNullOrEmpty(accessToken)) {
            return Boolean.FALSE;
        }

        try {
            String likeUrl = Constants.PICTURE_LIKE_URL.replace("{media-id}", params[0]);
            URL url = new URL(likeUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(
                    urlConnection.getOutputStream());

            writer.write("access_token=" + accessToken);
            writer.flush();

            String response = Utils.streamToString(urlConnection.getInputStream());

            // Convert the String response to JSONObject
            JSONObject jsonObj = new JSONObject(response);

            InstagramLikeResponseParser parser = new InstagramLikeResponseParser();
            return parser.parse(jsonObj);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return Boolean.FALSE;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(likeListener != null) {
            likeListener.onLikeCompleted(success.booleanValue(), button);
        }
    }
}
