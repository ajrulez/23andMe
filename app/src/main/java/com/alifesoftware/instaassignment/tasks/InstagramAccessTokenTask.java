package com.alifesoftware.instaassignment.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.alifesoftware.instaassignment.R;
import com.alifesoftware.instaassignment.interfaces.ITokenResultListener;
import com.alifesoftware.instaassignment.utils.Constants;
import com.alifesoftware.instaassignment.utils.Utils;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class InstagramAccessTokenTask extends AsyncTask<String, Void, String> {
    // Listener to get the Token Result
    ITokenResultListener tokenListener;

    // Context
    Context appContext;

    // Progress Dialog
    ProgressDialog progressDialog;

    public InstagramAccessTokenTask(Context ctx, ProgressDialog pdlg, ITokenResultListener listener) {
        appContext = ctx;
        progressDialog = pdlg;
        tokenListener = listener;
    }

    @Override
    protected void onPreExecute() {
       // Show Progress Dialog
        if(appContext != null &&
                progressDialog != null) {
            progressDialog.setMessage(appContext.getResources().getString(R.string.requesting_access_token));
            progressDialog.show();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        if(params.length == 0) {
            return null;
        }

        String accessToken = "";

        try {
            URL url = new URL(Constants.TOKEN_URL_REQUEST);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(
                    urlConnection.getOutputStream());

            writer.write("client_id=" + Constants.CLIENT_ID + "&client_secret="
                    + Constants.CLIENT_SECRET + "&grant_type=authorization_code"
                    + "&redirect_uri=" + Constants.REDIRECT_URI + "&code=" + params[0]);
            writer.flush();

            String response = Utils.streamToString(urlConnection.getInputStream());

            // Convert the String response to JSONObject
            JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();

            accessToken = jsonObj.getString("access_token");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    @Override
    protected void onPostExecute(String accessToken) {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }

        if(tokenListener != null) {
            if(Utils.isNullOrEmpty(accessToken)) {
                tokenListener.onTokenReceived(false, "");
            }
            else {
                tokenListener.onTokenReceived(true, accessToken);
            }
        }
    }
}
