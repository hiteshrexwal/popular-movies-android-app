package com.example.hiteshrexwal.popularmovies.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class FavoriteMovieHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="favorite.db";
    public static final int DATABASE_VERSION=2;

    public FavoriteMovieHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String FAVORITE_MOVIE_CREATE_TABLE="CREATE TABLE "+
                FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME+" ( "+
                FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ID+ " INTEGER PRIMARY KEY NOT NULL, "+
                FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_NAME+ " TEXT NOT NULL, "+
                FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_IMAGE+ " BLOB NOT NULL, "+
                FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_RELEASEDATE+ " TEXT NOT NULL, "+
                FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_OVERVIEW+ " TEXT NOT NULL, "+
                FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_RATING+ " TEXT NOT NULL "+
                " );";

        db.execSQL(FAVORITE_MOVIE_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
