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

public class MainActivity extends AppCompatActivity implements IPopularPicturesViewSwitcher {
    // FrameLayout - Fragment Container
    private FrameLayout fragmentContainer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);

        // Avoid duplicate Fragments when state changes
        if (savedInstanceState == null)
        {
            // Load the appropriate Fragment
            loadFragment(this);
        }
    }

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

            Display display = getWindow().getWindowManager().getDefaultDisplay();
            final float scale = getResources().getDisplayMetrics().density;

            float[] dimensions = (display.getWidth() < display.getHeight()) ? Constants.PORTRAIT
                    : Constants.LANDSCAPE;

            args.putInt(LoginFragment.ARGUMENTS_KEY_WIDTH, (int) (dimensions[0] * scale + 0.5f));
            args.putInt(LoginFragment.ARGUMENTS_KEY_HEIGHT, (int) (dimensions[1] * scale + 0.5f));

            loginFragment.setArguments(args);
            transaction.add(fragmentContainer.getId(), loginFragment)
                    .commit();
        }
        else {
            // Load PopularPicturesFragment
            PopularPicturesFragment popularPicturesFragment = new PopularPicturesFragment();
            transaction.add(fragmentContainer.getId(), popularPicturesFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

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

    @Override
    public void switchToPopularPicturesView() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        PopularPicturesFragment popularPicturesFragment = new PopularPicturesFragment();
        transaction.replace(fragmentContainer.getId(), popularPicturesFragment)
                   .commit();
    }
}
