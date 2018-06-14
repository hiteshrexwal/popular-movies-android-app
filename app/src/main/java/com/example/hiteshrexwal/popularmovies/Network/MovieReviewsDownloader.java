package com.example.hiteshrexwal.popularmovies.Network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hiteshrexwal.popularmovies.Adapter.ReviewsAdapter;
import com.example.hiteshrexwal.popularmovies.Extras.RetrieveListReviews;
import com.example.hiteshrexwal.popularmovies.Extras.Reviews;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;




public class MovieReviewsDownloader implements LoaderManager.LoaderCallbacks<String>{
    private Context context;
    private ArrayList<Reviews> movie_reviews;
    private TextView author,content,see_all;
    private String review_id;
    private static int REVIEW_LOADER=22;

    public MovieReviewsDownloader(Context context, ArrayList<Reviews> movie_reviews,TextView author,TextView content,TextView see_all,String review_id,LoaderManager loaderManager) {
        this.context = context;
        this.movie_reviews = movie_reviews;
        this.author=author;
        this.content=content;
        this.see_all=see_all;
        this.review_id=review_id;
        Loader<String> review_loader = loaderManager.getLoader(REVIEW_LOADER);
        if(review_loader==null){
            loaderManager.initLoader(REVIEW_LOADER, null,this);
        }
        loaderManager.restartLoader(REVIEW_LOADER,null,this);

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
                URL url;
                try {
                    url = UrlBuilderandConnector.build_url(review_id, "reviews");
                    return UrlBuilderandConnector.getDataFromHttp(url);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                //
                return null;
            }


        };
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(data!=null) {

            try {
                JSONObject json_data = new JSONObject(data);
                JSONArray results = json_data.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject temp = results.getJSONObject(i);
                        Reviews reviews = new Reviews(Parcel.obtain());
                        reviews.author = temp.getString("author");
                        reviews.content = temp.getString("content");
                        movie_reviews.add(reviews);
                    }
                    if(movie_reviews.size()>0) {
                        Reviews temp = movie_reviews.get(0);
                        author.setText(temp.author);
                        content.setText(temp.content);
                        see_all.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Class destination = ReviewsAdapter.class;
                                Intent secondActivity = new Intent(context, destination);
                                RetrieveListReviews retrieve_list_reviews = new RetrieveListReviews(movie_reviews);
                                secondActivity.putStringArrayListExtra("author", retrieve_list_reviews.getauthors());
                                secondActivity.putStringArrayListExtra("contents", retrieve_list_reviews.getcontents());
                                context.startActivity(secondActivity);

                            }
                        });
                    }
                    else{
                        author.setText("No Reviews");
                    }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(context,"No Reviews loaded",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }



}
