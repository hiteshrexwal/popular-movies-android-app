package com.example.hiteshrexwal.popularmovies.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hiteshrexwal.popularmovies.Database.FavoriteMoviesContract;
import com.example.hiteshrexwal.popularmovies.Extras.BitmapUtility;
import com.example.hiteshrexwal.popularmovies.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteMovieActivity extends AppCompatActivity {
    @BindView(R.id.imageView2)ImageView poster2;
    @BindView(R.id.movie_title_2) TextView title;
    @BindView(R.id.movie_overview) TextView overview;
    @BindView(R.id.movie_rating) TextView rating;
    @BindView(R.id.release_date_view) TextView release;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite__movie);

        ButterKnife.bind(this);


        Intent fromActivity=getIntent();
        String id=fromActivity.getStringExtra("Img_id");

        String selection = FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ID+" =?";
        String[] selectionArgs = {id};
        Cursor cursor=getContentResolver().query(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);

        if (cursor.moveToNext()) {
            cursor.moveToPosition(0);
            poster2.setImageBitmap(BitmapUtility.getImage(cursor.getBlob(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_IMAGE))));
            rating.setText(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_RATING)));
            title.setText(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_NAME)));
            overview.setText(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_OVERVIEW)));
            release.setText(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_RELEASEDATE)));
        }
        cursor.close();

    }
}
