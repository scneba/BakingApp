package com.clasence.shu.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Neba.
 */

public class StepsHelper implements Parcelable{
    private String id, shortDescription, description, videoUrl, thumbnailUrl;

    public StepsHelper(String id, String shortDescription, String description, String videoUrl, String thumbnailUrl){
        this.id=id;
        this.shortDescription=shortDescription;
        this.description=description;
        this.videoUrl=videoUrl;
        this.thumbnailUrl=thumbnailUrl;
    }

    protected StepsHelper(Parcel in) {
        id = in.readString();
        shortDescription = in.readString();
        description = in.readString();
        videoUrl = in.readString();
        thumbnailUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StepsHelper> CREATOR = new Creator<StepsHelper>() {
        @Override
        public StepsHelper createFromParcel(Parcel in) {
            return new StepsHelper(in);
        }

        @Override
        public StepsHelper[] newArray(int size) {
            return new StepsHelper[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }


}
