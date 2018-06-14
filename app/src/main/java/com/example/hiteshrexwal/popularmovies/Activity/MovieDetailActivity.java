package com.example.hiteshrexwal.popularmovies.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hiteshrexwal.popularmovies.Adapter.ImageSliderAdapter;
import com.example.hiteshrexwal.popularmovies.Adapter.MovieAdapter;
import com.example.hiteshrexwal.popularmovies.Adapter.MovieImageAdapter;
import com.example.hiteshrexwal.popularmovies.Adapter.ReviewsAdapter;
import com.example.hiteshrexwal.popularmovies.Adapter.VideoAdapter;
import com.example.hiteshrexwal.popularmovies.Database.FavoriteMoviesContract;
import com.example.hiteshrexwal.popularmovies.Extras.BitmapUtility;
import com.example.hiteshrexwal.popularmovies.Extras.Movies;
import com.example.hiteshrexwal.popularmovies.Extras.RetrieveListReviews;
import com.example.hiteshrexwal.popularmovies.Extras.Reviews;
import com.example.hiteshrexwal.popularmovies.Extras.VideoDetails;
import com.example.hiteshrexwal.popularmovies.Network.MovieImageDownloader;
import com.example.hiteshrexwal.popularmovies.Network.MovieJsonDownloader;
import com.example.hiteshrexwal.popularmovies.Network.MovieReviewsDownloader;
import com.example.hiteshrexwal.popularmovies.Network.MovieVideoDownloader;
import com.example.hiteshrexwal.popularmovies.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class MovieDetailActivity extends AppCompatActivity {
    @BindView(R.id.imageView2) ImageView poster2;
    @BindView(R.id.movie_title_2)TextView title;
    @BindView(R.id.movie_overview)TextView overview;
    @BindView(R.id.movie_rating)TextView rating;
    @BindView(R.id.release_date_view)TextView release;
    @BindView(R.id.viewpager)ViewPager viewPager;
    private String id;
    @BindView(R.id.circle_indicator)CircleIndicator circleIndicator;
    @BindView(R.id.images_recycleview)RecyclerView recyclerView;
    @BindView(R.id.videos_recycleview)RecyclerView recyclerView_video;
    @BindView(R.id.similar_recycleview) RecyclerView recyclerView_similar;
    @BindView(R.id.see_all) TextView see_all_image;
    @BindView(R.id.see_all_reviews_movies) TextView see_all_review;
    private Bitmap img =null;
    private boolean favorite=false;
    @BindView(R.id.favorite_button)FloatingActionButton actionButton;
    @BindView(R.id.coordinator_layout)CoordinatorLayout layout;
    private static Bundle bundle;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private ArrayList<String> movie_images;
    private ArrayList<VideoDetails> movie_video;
    private ArrayList<Movies> movie_similar;
    private ArrayList<Reviews> movie_reviews;
    @BindView(R.id.author) TextView author;
    @BindView(R.id.content) TextView content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar_movie_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);


        Intent fromActivity=getIntent();
        if(fromActivity.hasExtra("Img_link")&& fromActivity.hasExtra("Img_Title")&& fromActivity.hasExtra("Img_overview")&& fromActivity.hasExtra("Img_rating")) {
            id = fromActivity.getStringExtra("Img_id");
            Picasso.get().load(fromActivity.getStringExtra("Img_link")).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        img = bitmap;
                        poster2.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

                String rate = fromActivity.getStringExtra("Img_rating") + "/10";
                rating.setText(rate);
                title.setText(fromActivity.getStringExtra("Img_Title"));
                overview.setText(fromActivity.getStringExtra("Img_overview"));
                release.setText(fromActivity.getStringExtra("Img_release_date"));
                checkdatabase(id);
                if(bundle==null) {
                    Downloader();
                }
                else{
                    Downloaded();
                }

            }



        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkdatabase(id);
                if(!favorite){
                    if(img!=null) {
                        actionButton.setImageDrawable(getResources().getDrawable(R.drawable.star_red));
                        byte[] image = BitmapUtility.getBytes(img);
                        addMovies(id,image,title.getText().toString(),release.getText().toString(),overview.getText().toString(),rating.getText().toString() );
                    }

                }
                else {
                    actionButton.setImageDrawable(getResources().getDrawable(R.drawable.star_white));
                    Uri uri = FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(id).build();

                    getContentResolver().delete(uri, null, null);
                    favorite=false;
                }


            }
        });
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        bundle = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        bundle.putParcelable(KEY_RECYCLER_STATE, listState);
        bundle.putStringArrayList("images",movie_images);
        bundle.putParcelableArrayList("video",movie_video);
        bundle.putParcelableArrayList("similar",movie_similar);
        bundle.putParcelableArrayList("reviews",movie_reviews);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void Downloaded(){

        movie_images=bundle.getStringArrayList("images");
        if(movie_images!=null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(new MovieImageAdapter(movie_images, this, see_all_image));


            ImageSliderAdapter adapter = new ImageSliderAdapter(this, movie_images);
            viewPager.setAdapter(adapter);
            circleIndicator.setViewPager(viewPager);
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
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

        movie_video=bundle.getParcelableArrayList("video");
        if(movie_video!=null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView_video.setHasFixedSize(true);
            recyclerView_video.setLayoutManager(layoutManager);
            recyclerView_video.setAdapter(new VideoAdapter(movie_video, this));
        }

        movie_similar=bundle.getParcelableArrayList("similar");
        if(movie_similar!=null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView_similar.setHasFixedSize(true);
            recyclerView_similar.setLayoutManager(layoutManager);
            recyclerView_similar.setAdapter(new MovieAdapter(movie_similar, this, true));
        }

        movie_reviews=bundle.getParcelableArrayList("reviews");
        if(movie_reviews!=null) {
            Reviews temp = movie_reviews.get(0);
            author.setText(temp.author);
            content.setText(temp.content);
            see_all_review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Class destination = ReviewsAdapter.class;
                    Intent secondActivity = new Intent(MovieDetailActivity.this, destination);
                    RetrieveListReviews retrieve_list_reviews = new RetrieveListReviews(movie_reviews);
                    secondActivity.putStringArrayListExtra("author", retrieve_list_reviews.getauthors());
                    secondActivity.putStringArrayListExtra("contents", retrieve_list_reviews.getcontents());
                    MovieDetailActivity.this.startActivity(secondActivity);

                }
            });
        }
    }

    private void Downloader(){
        DownloadImages(id);
        DownloadVideoThumnail(id);
        DownloadSimilar(id);
        DownloadReviews(id);

    }
    private void DownloadImages(String i){
        boolean network_avail=isNetworkConnectionAvailable();
        if(network_avail) {
            movie_images = new ArrayList<>();
            Bundle bundle = new Bundle();
            bundle.putString("id", i);
            MovieImageDownloader downloader = new MovieImageDownloader(this, movie_images, viewPager, circleIndicator, recyclerView, see_all_image, getSupportLoaderManager(), bundle);
        }
    }
    private void DownloadVideoThumnail(String id){
        boolean network_avail=isNetworkConnectionAvailable();
        if(network_avail) {
            movie_video = new ArrayList<>();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            MovieVideoDownloader downloader = new MovieVideoDownloader(this, movie_video, recyclerView_video, getSupportLoaderManager(), bundle);
        }
    }
    private void DownloadSimilar(String id){
        boolean network_avail=isNetworkConnectionAvailable();
        if(network_avail) {
            movie_similar = new ArrayList<>();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            MovieJsonDownloader downloader = new MovieJsonDownloader(this, movie_similar, recyclerView_similar,true,1, bundle, getSupportLoaderManager());
        }
    }
    private void DownloadReviews(String review_id){
        boolean network_avail=isNetworkConnectionAvailable();
        if(network_avail) {
            movie_reviews = new ArrayList<>();
            MovieReviewsDownloader downloader = new MovieReviewsDownloader(this, movie_reviews, author, content, see_all_review, review_id, getSupportLoaderManager());
        }
    }
    private void addMovies(String id,byte[] image,String name,String releasedate,String overview,String rating){
        ContentValues cv=new ContentValues();
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ID,id);
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_IMAGE,image);
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_NAME,name);
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_RELEASEDATE,releasedate);
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_OVERVIEW,overview);
        cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_RATING,rating);
        getContentResolver().insert(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, cv);

    }


    private void checkdatabase(String id){
        String table = FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME;
        String[] columns = {FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ID};
        String selection = FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ID+" =?";
        String[] selectionArgs = {id};
        Cursor cursor=getContentResolver().query(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                columns,
                selection,
                selectionArgs,
                null);

        if (cursor.moveToNext()) {
            favorite=true;
            actionButton.setImageDrawable(getResources().getDrawable(R.drawable.star_red));
        }
        cursor.close();

    }

    private boolean isNetworkConnectionAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        try{
            NetworkInfo activeNetwork = null;
            if (cm != null) {
                activeNetwork = cm.getActiveNetworkInfo();
            }
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnected();
            if(isConnected) {
                return true;
            }
            else{
                Snackbar snackbar = Snackbar
                        .make(layout, com.example.hiteshrexwal.popularmovies.R.string.noInternet,Snackbar.LENGTH_INDEFINITE)
                        .setAction(com.example.hiteshrexwal.popularmovies.R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Downloader();
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                return false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


        return false;
    }


}
