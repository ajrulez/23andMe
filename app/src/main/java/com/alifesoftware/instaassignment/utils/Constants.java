package com.alifesoftware.instaassignment.utils;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class Constants {
    public static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    public static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    public static final String API_URL = "https://api.instagram.com/v1";

    //public static final String CLIENT_ID = "12a64b6f88fc4759ba9768d9f6358797";
    //public static final String CLIENT_SECRET = "d4406e9d0f25439288485774b80b1abf";
    //public static final String REDIRECT_URI = "http://localhost:3000/auth/instagram/callback";

    public static final String CLIENT_ID = "103babf92fe1445f8bc4a38a8e2cfa58";
    public static final String CLIENT_SECRET = "a1b7991ed1dc43df9ba8e706d4296b23";
    public static final String REDIRECT_URI = "instagram://connect";

    public static final String TOKEN_URL_REQUEST = Constants.TOKEN_URL + "?client_id=" + Constants.CLIENT_ID + "&client_secret="
            + Constants.CLIENT_SECRET + "&redirect_uri=" + Constants.REDIRECT_URI
            + "&grant_type=authorization_code";

    public static final String AUTH_URL_REQUEST = Constants.AUTH_URL
            + "?client_id="
            + Constants.CLIENT_ID
            + "&redirect_uri="
            + Constants.REDIRECT_URI
            + "&response_type=code&display=touch&scope=likes";

    public static final float[] LANDSCAPE = { 480, 340 };
    public static final float[] PORTRAIT = { 340, 480 };
}
