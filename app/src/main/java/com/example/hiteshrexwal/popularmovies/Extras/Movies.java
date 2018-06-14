package com.example.hiteshrexwal.popularmovies.Extras;


import android.os.Parcel;
import android.os.Parcelable;

public class Movies implements Parcelable {
    public String id;
    public String title;
    public String votes;
    public String img_path;
    public String overview;
    public String release_date;

    public Movies(Parcel in) {
        id = in.readString();
        title = in.readString();
        votes = in.readString();
        img_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(votes);
        dest.writeString(img_path);
        dest.writeString(overview);
        dest.writeString(release_date);
    }
}
