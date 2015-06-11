package com.alifesoftware.instaassignment.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.alifesoftware.instaassignment.R;
import com.alifesoftware.instaassignment.businesslogic.SessionManager;
import com.alifesoftware.instaassignment.interfaces.IPopularImageParser;
import com.alifesoftware.instaassignment.interfaces.IPopularPicturesReceiver;
import com.alifesoftware.instaassignment.model.PopularPicturesModel;
import com.alifesoftware.instaassignment.parser.ParserFactory;
import com.alifesoftware.instaassignment.utils.Constants;
import com.alifesoftware.instaassignment.utils.Utils;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class InstagramPopularPicturesTask extends AsyncTask<Void, Void, ArrayList<PopularPicturesModel>> {

    // IPopularPicturesReceiver Receiver to receive the Results
    IPopularPicturesReceiver resultReceiver = null;

    // Progress Dialog
    private ProgressDialog progressDialog;

    // Context
    private Context appContext;

    public InstagramPopularPicturesTask(Context ctx, ProgressDialog pdlg, IPopularPicturesReceiver rcvr) {
        appContext = ctx;
        progressDialog = pdlg;
        resultReceiver = rcvr;
    }

    @Override
    protected void onPreExecute() {
        if(progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    protected ArrayList<PopularPicturesModel> doInBackground(Void... params) {
        try {
            SessionManager sessionManager = new SessionManager(appContext);
            String accessToken = sessionManager.getAccessToken();
            if(Utils.isNullOrEmpty(accessToken)) {
                return null;
            }

            URL url = new URL(Constants.POPULAR_PHOTOS_URL + accessToken);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("GET");
            String response = Utils.streamToString(urlConnection.getInputStream());

            // Convert the String response to JSONObject
            JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();

            if(jsonObj != null) {
                // Using a Factory Pattern just to demonstrate design skills
                IPopularImageParser parser = ParserFactory.createParser(ParserFactory.ParserType.PARSER_INSTAGRAM_JSON);
                if(parser != null) {
                    return parser.parse(jsonObj);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<PopularPicturesModel> popularPicturesArray) {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }

        if(resultReceiver != null) {
            resultReceiver.onPictureDataRetrieved(popularPicturesArray);
        }
    }
}
