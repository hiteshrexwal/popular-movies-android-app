package com.example.hiteshrexwal.popularmovies.Network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import com.example.hiteshrexwal.popularmovies.Adapter.ImageSliderAdapter;
import com.example.hiteshrexwal.popularmovies.Adapter.MovieImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;


public class MovieImageDownloader implements LoaderManager.LoaderCallbacks<String> {
    private Context context;
    private ArrayList<String> movie_images;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private Handler handler;
    private Runnable runnable;
    private RecyclerView recyclerView;
    private TextView seeall;
    private static int IMAGE_LOADER=10;

    public MovieImageDownloader(Context context, ArrayList<String> movie_images, ViewPager viewPager,CircleIndicator circleIndicator,RecyclerView recyclerView,TextView seeall,LoaderManager loaderManager,Bundle args) {
        this.context = context;
        this.movie_images = movie_images;
        this.viewPager = viewPager;
        this.circleIndicator=circleIndicator;
        this.recyclerView=recyclerView;
        this.seeall=seeall;
        Loader<String> review_loader = loaderManager.getLoader(IMAGE_LOADER);
        if(review_loader==null){
            loaderManager.initLoader(IMAGE_LOADER, args,this);
        }
        loaderManager.restartLoader(IMAGE_LOADER,args,this);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(context) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                if(args==null){
                    return null;
                }
                URL url;
                try {
                    url=UrlBuilderandConnector.build_url(args.getString("id"),"images");
                    return UrlBuilderandConnector.getDataFromHttp(url);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                //
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        try {
            JSONObject json_data = new JSONObject(data);
            JSONArray results=json_data.getJSONArray("backdrops");
            if(results!=null) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject temp = results.getJSONObject(i);
                    String filePath = temp.getString("file_path");
                    movie_images.add(filePath);
                }
            }
            if(movie_images.size()>0) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new MovieImageAdapter(movie_images, context, seeall));


                ImageSliderAdapter adapter = new ImageSliderAdapter(context, movie_images);
                viewPager.setAdapter(adapter);
                circleIndicator.setViewPager(viewPager);
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        int i = viewPager.getCurrentItem();
                        if (i == 3) {
                            i = 0;
                        } else
                            i++;
                        viewPager.setCurrentItem(i, true);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(runnable);
                    }
                }, 2000, 2000);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
