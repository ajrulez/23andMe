package com.alifesoftware.instaassignment.authentication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alifesoftware.instaassignment.R;
import com.alifesoftware.instaassignment.interfaces.IAuthDialogListener;
import com.alifesoftware.instaassignment.utils.Constants;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class AuthDialog extends Dialog {
    // Authentication Listener
    private IAuthDialogListener authListener;

    // Context
    private Context dlgContext;

    // Progress Dialog
    private ProgressDialog progressDlg;

    // WebView for Auth
    private WebView authWebView;

    // Content Holder
    private LinearLayout contentHolder;

    // Title
    private TextView title;

    // Width of the Window
    private int windowWidth;

    // Height of the Window
    private int windowHeight;

    public AuthDialog(Context context, int width, int height,  IAuthDialogListener listener) {
        super(context);

        dlgContext = context;
        windowWidth = width;
        windowHeight = height;
        authListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDlg = new ProgressDialog(dlgContext);
        progressDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDlg.setMessage(dlgContext.getResources().getString(R.string.please_wait));

        contentHolder = new LinearLayout(getContext());
        contentHolder.setOrientation(LinearLayout.VERTICAL);

        setUpTitle();
        setUpWebView();


        addContentView(contentHolder, new LinearLayout.LayoutParams(windowWidth, windowHeight));

        CookieSyncManager.createInstance(getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    private void setUpTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        title = new TextView(dlgContext);
        title.setText(dlgContext.getResources().getString(R.string.instagram_authentication));
        title.setTextColor(Color.WHITE);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setBackgroundColor(Color.BLACK);
        contentHolder.addView(title);
    }

    private void setUpWebView() {
        authWebView = new WebView(getContext());
        authWebView.setVerticalScrollBarEnabled(false);
        authWebView.setHorizontalScrollBarEnabled(false);
        authWebView.setWebViewClient(new AuthWebViewClient(authListener, this));
        authWebView.getSettings().setJavaScriptEnabled(true);
        authWebView.loadUrl(Constants.AUTH_URL_REQUEST);
        authWebView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        contentHolder.addView(authWebView);
    }

    public void showProgressDialog() {
        progressDlg.show();
    }

    public void hideProgressDialog() {
        progressDlg.dismiss();
    }
}
