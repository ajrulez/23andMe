package com.alifesoftware.instaassignment.utils;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * This class defines constant values and application
 * configuration flags for any customization
 *
 */
public class Constants {
    // Endpoint URL to get the Code as first step of Authentication
    public static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";

    // Endpoint URL to get AccessToken using the Code
    public static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";

    // Base Endpoint URL for Instagram APIs
    public static final String API_URL = "https://api.instagram.com/v1";

    // URL to retrieve Popular Pictures from Instagra, - Uses GET
    public static final String POPULAR_PHOTOS_URL = Constants.API_URL + "/media/popular?access_token=";

    // URL to post a Like for a Picture - Uses POST
    public static final String PICTURE_LIKE_URL = Constants.API_URL + "/media/{media-id}/likes"; // POST

    // Client ID as generated by Instagram for this application
    public static final String CLIENT_ID = "12a64b6f88fc4759ba9768d9f6358797";

    // Client Secret as generated by Instagram for this application
    public static final String CLIENT_SECRET = "d4406e9d0f25439288485774b80b1abf";

    // Redirect URI needed by Instagram to send us the Code when we initialize
    // the Authentication process. This must match the value of Redirect URI
    // that was set up on Instagram for this application
    public static final String REDIRECT_URI = "http://localhost/auth/instagram/callback";

    // Request for getting AccessToken from Instagram using the Code that
    // we get in the first step of Authentication process. This is the
    // second step in the Authentication process.
    //
    // Note: Using the scope=likes because we want to support "Like" for
    // Popular Pictures.
    //
    // Note 2: Currently "Like" feature is not working. I think I have the right
    // request, but somehow Instagram rejects the request.
    // Instagram documentation states that we may need to get the application
    // approved by Instagram to use any "write" access to Instagram, and "Like"
    // is included in "write" access features.
    //
    // Link: https://instagram.com/developer/endpoints/likes/
    // and
    // https://instagram.com/developer/authentication/ (Look at Scope (Permissions)
    //
    public static final String TOKEN_URL_REQUEST = Constants.TOKEN_URL + "?client_id=" + Constants.CLIENT_ID + "&client_secret="
            + Constants.CLIENT_SECRET + "&redirect_uri=" + Constants.REDIRECT_URI
            + "&grant_type=authorization_code&scope=likes";

    // Request for getting Code from Instagram - First step in the Authentication process
    public static final String AUTH_URL_REQUEST = Constants.AUTH_URL
            + "?client_id="
            + Constants.CLIENT_ID
            + "&redirect_uri="
            + Constants.REDIRECT_URI
            + "&response_type=code&display=touch&scope=likes";

    // Dimensions we want to use for the WebView that kicks
    // off the Authentication process
    public static final float[] LANDSCAPE = { 480, 340 };
    public static final float[] PORTRAIT = { 340, 340 };

    // Instagram offers images in three formats:
    //
    // 1. Thumbnail
    // 2. Low-Res
    // 3. Standard-Res / High-Res
    //
    // This flag indicates if we want to use High-Res images
    // or the low-Res images. We are not using Thumbnails.
    //
    public static boolean showHighResolutionImages = true;

    // Enum contains types of LazyLoading libraries
    // we support in this app
    public static enum LazyImageLoaderType {
        UNIVERSAL_IMAGE_LOADER,
        SQUARE_PICASO
    }

    // This constant indicates whether we want to use UIL or Picaso for Lazy Loading images
    public static LazyImageLoaderType lazyLoaderToUse = LazyImageLoaderType.UNIVERSAL_IMAGE_LOADER;

    // JSON Response for Popular Pictures also has some videos, and these
    // videos also have iages that we can display. This flag indicates if
    // we want to include videos or exclude vidoes
    //
    // Set to false to exclude videos
    public static boolean includeVideosInPopularPictures = true;
}
