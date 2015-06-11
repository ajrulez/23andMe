package com.alifesoftware.instaassignment.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class PopularPicturesModel implements Parcelable  {

    // Note: There is a lot of information on each picture
    // that we retrieve via media/popular. Considering the scope
    // of this assignment, I am only taking into account the
    // following:
    //
    // Picture ID
    // Picture Caption
    // Picture URL
    // User has Liked

    // ID of the picture
    private String pictureId;

    // Caption of the picture
    private String pictureCaption;

    // URL of the picture
    private String pictureUrl;

    // Flag to indicate whether current user has liked the picture
    private boolean hasUserLiked;

    public PopularPicturesModel() {
        // Nothing to do
    }

    /**
     * Parcelable Implementation
     */
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(pictureId);
        out.writeString(pictureCaption);
        out.writeString(pictureUrl);

        if(hasUserLiked) {
            out.writeInt(1);
        }
        else {
            out.writeInt(0);
        }
    }

    public static final Parcelable.Creator<PopularPicturesModel> CREATOR
            = new Parcelable.Creator<PopularPicturesModel>() {
        public PopularPicturesModel createFromParcel(Parcel in) {
            return new PopularPicturesModel(in);
        }

        public PopularPicturesModel[] newArray(int size) {
            return new PopularPicturesModel[size];
        }
    };

    private PopularPicturesModel(Parcel in) {
        pictureId = in.readString();
        pictureCaption = in.readString();
        pictureUrl = in.readString();

        int flag = in.readInt();
        if(flag == 1) {
            hasUserLiked = true;
        }
        else {
            hasUserLiked = false;
        }
    }

    /**
     * Getter and Setter Methods for the above fields
     */
    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getPictureCaption() {
        return pictureCaption;
    }

    public void setPictureCaption(String pictureCaption) {
        this.pictureCaption = pictureCaption;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public boolean isHasUserLiked() {
        return hasUserLiked;
    }

    public void setHasUserLiked(boolean hasUserLiked) {
        this.hasUserLiked = hasUserLiked;
    }
}
