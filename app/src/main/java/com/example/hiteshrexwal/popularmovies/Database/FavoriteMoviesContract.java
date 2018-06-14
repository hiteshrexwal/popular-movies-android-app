package com.example.hiteshrexwal.popularmovies.Database;

import android.net.Uri;

public final class FavoriteMoviesContract {

    public static final String AUTHORITY = "com.example.hiteshrexwal.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITE_MOVIE = "favorite_movie";


    private FavoriteMoviesContract() {
    }


    public static final class FavoriteMoviesEntry {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIE).build();

        public static final String TABLE_NAME="favorite_movie";
        public static final String MOVIE_ID="movie_id";
        public static final String MOVIE_NAME="movie_name";
        public static final String MOVIE_IMAGE="movie_image";
        public static final String MOVIE_RELEASEDATE="movie_release";
        public static final String MOVIE_OVERVIEW="movie_overview";
        public static final String MOVIE_RATING="movie_rating";
    }

}
