package com.alifesoftware.instaassignment.parser;

import org.json.JSONObject;
import com.alifesoftware.instaassignment.interfaces.ILikeResponseParser;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class InstagramLikeResponseParser implements ILikeResponseParser {
    @Override
    public Boolean parse(JSONObject jsonObj) {
        if(jsonObj == null) {
            return Boolean.FALSE;
        }

        try {
            JSONObject metaObject = jsonObj.getJSONObject("meta");
            if (metaObject != null) {
                int code = metaObject.optInt("code", -1);
                if (code == 200) {
                    return Boolean.TRUE;
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return Boolean.FALSE;
    }
}
