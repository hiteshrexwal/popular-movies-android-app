package com.example.hiteshrexwal.popularmovies.Network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.hiteshrexwal.popularmovies.Adapter.VideoAdapter;
import com.example.hiteshrexwal.popularmovies.Extras.VideoDetails;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MovieVideoDownloader implements LoaderManager.LoaderCallbacks<String> {
    private Context context;
    private ArrayList<VideoDetails> movie_video;
    private RecyclerView recyclerView;
    private static int VIDEO_LOADER=11;

    public MovieVideoDownloader(Context context, ArrayList<VideoDetails> movie_video,RecyclerView recyclerView,LoaderManager loaderManager,Bundle args) {
        this.context = context;
        this.movie_video = movie_video;
        this.recyclerView=recyclerView;
        Loader<String> review_loader = loaderManager.getLoader(VIDEO_LOADER);
        if(review_loader==null){
            loaderManager.initLoader(VIDEO_LOADER, args,this);
        }
        loaderManager.restartLoader(VIDEO_LOADER,args,this);
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
                    url=UrlBuilderandConnector.build_url(args.getString("id"),"videos");
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
            JSONObject  json_data = new JSONObject(data);
            JSONArray results=json_data.getJSONArray("results");
            for(int i=0;i<results.length();i++) {
                JSONObject temp = results.getJSONObject(i);
                VideoDetails video_details=new VideoDetails(Parcel.obtain());
                video_details.video_key=temp.getString("key");
                video_details.video_name=temp.getString("name");
                movie_video.add(video_details);


            }
            if(movie_video.size()>0) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                VideoAdapter adapter = new VideoAdapter(movie_video, context);

                recyclerView.setAdapter(adapter);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
