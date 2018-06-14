package com.example.hiteshrexwal.popularmovies.Network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.example.hiteshrexwal.popularmovies.Activity.MovieActivity;
import com.example.hiteshrexwal.popularmovies.Adapter.MovieAdapter;
import com.example.hiteshrexwal.popularmovies.Extras.Movies;
import com.victor.loading.rotate.RotateLoading;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class  MovieJsonDownloader implements LoaderManager.LoaderCallbacks<String> {
    private Context context;
    private RecyclerView recyclerView;
    private RotateLoading avi;
    private ArrayList<Movies> movie_data;
    private boolean additems=false,addsimilar=false;
    private MovieAdapter adapter;
    public static int cur_page;
    public static int total_page;
    private GridLayoutManager manager;
    public static Bundle args;
    private static int MOVIE_LOADER=20;
    private static int SIMILAR_LOADER=13;
    private LoaderManager loaderManager;



    public MovieJsonDownloader(Context context, ArrayList<Movies> movie_data, RotateLoading avi, RecyclerView recyclerView, boolean additems, int cur_page,Bundle args,LoaderManager loaderManager) {
        this.context = context;
        this.movie_data=movie_data;
        this.avi=avi;
        this.recyclerView=recyclerView;
        this.additems=additems;
        this.cur_page=cur_page;
        this.args=args;
        this.loaderManager=loaderManager;
        Loader<String> movie_loader = loaderManager.getLoader(MOVIE_LOADER);
        if(movie_loader==null){
            loaderManager.initLoader(MOVIE_LOADER, args,this);
        }
        loaderManager.restartLoader(MOVIE_LOADER,args,this);
    }
    public MovieJsonDownloader(Context context,ArrayList<Movies>movie_data,RotateLoading avi,RecyclerView recyclerView,boolean additems,MovieAdapter adapter,int cur_page,int total_page,GridLayoutManager manager,Bundle args,LoaderManager loaderManager) {
        this.context = context;
        this.movie_data=movie_data;
        this.avi=avi;
        this.recyclerView=recyclerView;
        this.additems=additems;
        this.adapter=adapter;
        this.cur_page=cur_page;
        this.total_page=total_page;
        this.manager=manager;
        this.args=args;
        this.loaderManager=loaderManager;
        Loader<String> movie_loader = loaderManager.getLoader(MOVIE_LOADER);
        if(movie_loader==null){
            loaderManager.initLoader(MOVIE_LOADER, args,this);
        }
        loaderManager.restartLoader(MOVIE_LOADER,args,this);

    }
   public MovieJsonDownloader(Context context, ArrayList<Movies> movie_data, RecyclerView recyclerView, boolean addsimilar, int cur_page, Bundle args,LoaderManager loaderManager) {
        this.context = context;
        this.movie_data=movie_data;
        this.recyclerView=recyclerView;
        this.addsimilar=addsimilar;
        this.cur_page=cur_page;
        this.args=args;
       Loader<String> similar_loader = loaderManager.getLoader(SIMILAR_LOADER);
       if(similar_loader==null){
           loaderManager.initLoader(SIMILAR_LOADER, args,this);
       }
       loaderManager.restartLoader(SIMILAR_LOADER,args,this);
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id,final Bundle params) {

        return new AsyncTaskLoader<String>(context) {
            @Override
            protected void onStartLoading() {
                forceLoad();
                super.onStartLoading();
            }

            @Override
            public String loadInBackground() {
                if(params==null){
                    return null;
                }
                try {

                    URL url;
                    if(!addsimilar) {
                        Log.i("page no",Integer.toString(cur_page));
                        url = UrlBuilderandConnector.link_builder(params.getString("type"),cur_page);
                        return UrlBuilderandConnector.getDataFromHttp(url);
                    }
                    else{
                        url = UrlBuilderandConnector.build_url(params.getString("id"), "similar");
                        return UrlBuilderandConnector.getDataFromHttp(url);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(final Loader<String> loader, String data) {
        try {
            JSONObject json_data = new JSONObject(data);
            cur_page = Integer.parseInt(json_data.getString("page"));
            Log.i("page no",Integer.toString(cur_page));
            total_page = Integer.parseInt(json_data.getString("total_pages"));
            JSONArray results = json_data.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject temp = results.getJSONObject(i);
                String title = temp.getString("title");
                String votes = temp.getString("vote_average");
                String img_path = temp.getString("poster_path");
                String overview = temp.getString("overview");
                String release = temp.getString("release_date");
                String id = temp.getString("id");
                Movies temp_movie = new Movies(Parcel.obtain());
                temp_movie.id = id;
                temp_movie.title = title;
                temp_movie.votes = votes;
                temp_movie.img_path = img_path;
                temp_movie.overview = overview;
                temp_movie.release_date = release;
                movie_data.add(temp_movie);
                //textView.append(title+votes+img_path+overview+"\n\n\n");
            }
            if (!addsimilar) {
                avi.stop();
                //logo.setVisibility(View.GONE);
                avi.setVisibility(View.GONE);
            }
            //recyclerView.setVisibility(View.VISIBLE);
            //recyclerView=(RecyclerView)((Activity)context).findViewById(R.id.recycle);

            if (additems && !addsimilar) {
                adapter.notifyDataSetChanged();
            }
            else if (!additems && !addsimilar) {
                Log.i("col",Integer.toString(MovieActivity.calculateNoOfColumns(context)));

                GridLayoutManager layoutManager =new GridLayoutManager(context,MovieActivity.calculateNoOfColumns(context));
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new MovieAdapter(movie_data, context, false);
                recyclerView.setAdapter(adapter);
                manager = layoutManager;
            }
            else if (addsimilar) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new MovieAdapter(movie_data, context, true);
                recyclerView.setAdapter(adapter);
            }

            if (!additems && !addsimilar) {
                loadMore();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void loadMore(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    if(total_page>=cur_page ) {
                        avi.setVisibility(View.VISIBLE);
                        avi.start();
                        Bundle bundle = new Bundle();
                        bundle.putString("type", args.getString("type"));
                        MovieJsonDownloader downloader = new MovieJsonDownloader(context, movie_data, avi, recyclerView, true, adapter, ++cur_page, total_page, manager, bundle, loaderManager);
                    }
                }
            }

        });

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }


}