package com.alifesoftware.instaassignment.parser;

import com.alifesoftware.instaassignment.interfaces.IPopularImageParser;
import com.alifesoftware.instaassignment.model.PopularPicturesModel;
import com.alifesoftware.instaassignment.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class InstagramPopularPictureParserJson implements IPopularImageParser {
    private final static String TAG = "InstagrameParser";
    /**
     * For now, I am using standard JSON libraries to
     * parse the data.
     *
     * Another option is to use Gson. If I have time towards the
     * end of this assignment, I will switch to Gson
     */

    public ArrayList<PopularPicturesModel> parse(JSONObject jsonObj) {
        if(jsonObj == null ||
                jsonObj.length() <= 0) {
            return null;
        }

        ArrayList<PopularPicturesModel> popularPictures =
                new ArrayList<PopularPicturesModel>();

        try {
            // Get the JSONArray named "data" from the root object
            JSONArray dataArray = jsonObj.optJSONArray("data");

            if (dataArray != null &&
                    dataArray.length() > 0) {
                // For each JSONObject in the dataArray, retrieve the
                // data as needed by PopularPicturesModel
                for (int count = 0; count < dataArray.length(); count++) {
                    JSONObject pictureObj = dataArray.optJSONObject(count);

                    if(pictureObj != null) {
                        PopularPicturesModel pictureModel = new PopularPicturesModel();

                        String imageUrlLowRes = "";
                        String imageUrlStandardRes = "";

                        // Get the Link to the Image
                        JSONObject imagesObj = pictureObj.optJSONObject("images");
                        if(imagesObj != null) {
                            // Get the low-resolution image
                            JSONObject lowResolutionObj = imagesObj.optJSONObject("low_resolution");
                            JSONObject standardResolutionObj = imagesObj.optJSONObject("standard_resolution");

                            if(lowResolutionObj != null) {
                                imageUrlLowRes = lowResolutionObj.optString("url");
                            }

                            if(standardResolutionObj != null) {
                                imageUrlStandardRes = standardResolutionObj.optString("url");
                            }
                        }

                        // Get the ID
                        String id = pictureObj.optString("id", "-1");

                        // Get the user_has_liked flag
                        boolean userHasLiked = pictureObj.optBoolean("user_has_liked", false);

                        String captionText = "";

                        // Get the caption text from caption object
                        JSONObject captionObj = pictureObj.getJSONObject("caption");
                        if(captionObj != null) {
                            captionText = captionObj.optString("text", "");
                        }

                        // Add to the collection after checking for some
                        // basic required values
                        if(! Utils.isNullOrEmpty(imageUrlLowRes) &&
                                !Utils.isNullOrEmpty(id)) {

                            pictureModel.setPictureUrl(imageUrlLowRes);
                            pictureModel.setHighResPictureUrl(imageUrlStandardRes);
                            pictureModel.setPictureId(id);
                            pictureModel.setPictureCaption(captionText);
                            pictureModel.setHasUserLiked(userHasLiked);

                            popularPictures.add(pictureModel);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            android.util.Log.e(TAG, "Exception when parsing the data" + e);
        }

        return popularPictures;
    }
}
