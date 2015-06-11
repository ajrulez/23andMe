package com.alifesoftware.instaassignment.interfaces;

import com.alifesoftware.instaassignment.model.PopularPicturesModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by anujsaluja on 6/10/15.
 */
public interface IPopularImageParser {
    ArrayList<PopularPicturesModel> parse(JSONObject jsonObj);
}
