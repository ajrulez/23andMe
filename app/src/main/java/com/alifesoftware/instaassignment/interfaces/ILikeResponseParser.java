package com.alifesoftware.instaassignment.interfaces;

import com.alifesoftware.instaassignment.model.InstagramErrorModel;

import org.json.JSONObject;

/**
 * Created by anujsaluja on 6/10/15.
 *
 * This interface is for parsing the result
 * of posting a Like for a Popular Picture is finished
 *
 */
public interface ILikeResponseParser {
    Boolean parse(JSONObject jsonObj);
    InstagramErrorModel parseError(JSONObject jsonErrorObject);
}
