package com.alifesoftware.instaassignment.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.alifesoftware.instaassignment.R;
import com.alifesoftware.instaassignment.businesslogic.SessionManager;
import com.alifesoftware.instaassignment.fragments.LoginFragment;
import com.alifesoftware.instaassignment.fragments.PopularPicturesFragment;
import com.alifesoftware.instaassignment.interfaces.IPopularPicturesViewSwitcher;
import com.alifesoftware.instaassignment.utils.Constants;

/**
 * Launch Activity for this application. Only using one Activity, and
 * using Fragments to display various views within the Activity
 *
 * Activity implements IPopularPicturesViewSwitcher because it is
 * capable of switching Fragments. This is similar to
 * Switch Board pattern used when we have an Activity that loads
 * different Fragments based on application flow.
 *
 */
public class MainActivity extends AppCompatActivity implements IPopularPicturesViewSwitcher {
    // FrameLayout - Fragment Container
    private FrameLayout fragmentContainer = null;

    /**
     * onCreate method for Activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the View that will contain Fragments
        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);

        // Avoid duplicate Fragments when state changes
        if (savedInstanceState == null)
        {
            // Load the appropriate Fragment
            loadFragment(this);
        }
    }

    /**
     * Method to load appropriate Fragment based on the
     * Application flow
     *
     * @param ctx
     */
    private void loadFragment(Context ctx) {
        // Check if user is logged in, i.e. the
        // SharedPreferences has the tokens
        SessionManager sessionMgr = new SessionManager(ctx);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Allow Rotation by Default
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        if(sessionMgr.getAccessToken() == null) {
            // We want to disable the rotation when LoginFragment is being
            // displayed because a lot goes on during LoginFragment lifecycle -
            // WebView is displayed, ProgressDialog is displayed, and an Authentication
            // might be in progress. It is easier to block rotation for LoginFragment
            // than to control other aspects
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

            // Load LoginFragment
            LoginFragment loginFragment = new LoginFragment();
            Bundle args = new Bundle();

            // Display is used to determine the size of WebView / Dialog
            // that we want to display for initial step of Instagram
            // Authentication
            Display display = getWindow().getWindowManager().getDefaultDisplay();
            final float scale = getResources().getDisplayMetrics().density;

            float[] dimensions = (display.getWidth() < display.getHeight()) ? Constants.PORTRAIT
                    : Constants.LANDSCAPE;

            args.putInt(LoginFragment.ARGUMENTS_KEY_WIDTH, (int) (dimensions[0] * scale + 0.5f));
            args.putInt(LoginFragment.ARGUMENTS_KEY_HEIGHT, (int) (dimensions[1] * scale + 0.5f));

            // Set arguments for the Fragment
            loginFragment.setArguments(args);

            // Add the Fragment to the Fragment Container View
            transaction.add(fragmentContainer.getId(), loginFragment)
                    .commit();
        }
        // Already logged in because we have Access Token, show
        // Popular Pictures Fragment. See my comments in LoginFragment
        // and PopularPicturesFragment that illustrate that merely
        // having an AccessToken does NOT guaranty that user is logged
        // in (as the token may have expired).
        //
        // PopularPicturesFragment has a section of comments that
        // mentions how we can deal with cases where AccessToken is present
        // but is invalid/expired
        else {
            // Load PopularPicturesFragment
            PopularPicturesFragment popularPicturesFragment = new PopularPicturesFragment();
            transaction.add(fragmentContainer.getId(), popularPicturesFragment)
                    .commit();
        }
    }

    /**
     * Automatically Generated Method by Android Studio
     * I am not supporting any Menu/Settings
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    /**
     * Automatically Generated Method by Android Studio
     * I am not supporting any Menu/Settings
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to switch the current Fragment to PopularPicturesFragment
     *
     */
    @Override
    public void switchToPopularPicturesView() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        PopularPicturesFragment popularPicturesFragment = new PopularPicturesFragment();
        transaction.replace(fragmentContainer.getId(), popularPicturesFragment)
                   .commit();
    }
}
