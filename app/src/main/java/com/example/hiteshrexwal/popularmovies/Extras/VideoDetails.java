package com.example.hiteshrexwal.popularmovies.Extras;


import android.os.Parcel;
import android.os.Parcelable;

public class VideoDetails implements Parcelable {
    public String video_key;
    public String video_name;

    public VideoDetails(Parcel in) {
        video_key = in.readString();
        video_name = in.readString();
    }

    public static final Creator<VideoDetails> CREATOR = new Creator<VideoDetails>() {
        @Override
        public VideoDetails createFromParcel(Parcel in) {
            return new VideoDetails(in);
        }

        @Override
        public VideoDetails[] newArray(int size) {
            return new VideoDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(video_key);
        dest.writeString(video_name);
    }
}
