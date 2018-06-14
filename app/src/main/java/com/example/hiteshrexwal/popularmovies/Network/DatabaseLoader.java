package com.example.hiteshrexwal.popularmovies.Network;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.hiteshrexwal.popularmovies.Activity.MovieActivity;
import com.example.hiteshrexwal.popularmovies.Adapter.FavoriteMovieAdapter;
import com.example.hiteshrexwal.popularmovies.Database.FavoriteMoviesContract;


public class DatabaseLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context context;
    private ContentResolver contentResolver;
    private RecyclerView recyclerView;
    private static int DATALOADER=50;

    public DatabaseLoader(Context context, ContentResolver contentResolver,RecyclerView recyclerView,LoaderManager loaderManager) {
        this.context = context;
        this.contentResolver=contentResolver;
        this.recyclerView=recyclerView;
        Loader<Cursor> movie_loader = loaderManager.getLoader(DATALOADER);
        if(movie_loader==null){
            loaderManager.initLoader(DATALOADER, null,this);
        }
        loaderManager.restartLoader(DATALOADER,null,this);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(context) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return contentResolver.query(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        GridLayoutManager layoutManager =new GridLayoutManager(context, MovieActivity.calculateNoOfColumns(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new FavoriteMovieAdapter(data,context));


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
