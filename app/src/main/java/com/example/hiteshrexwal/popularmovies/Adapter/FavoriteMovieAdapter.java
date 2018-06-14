package com.example.hiteshrexwal.popularmovies.Adapter;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hiteshrexwal.popularmovies.Activity.FavoriteMovieActivity;
import com.example.hiteshrexwal.popularmovies.Database.FavoriteMoviesContract;
import com.example.hiteshrexwal.popularmovies.Extras.BitmapUtility;
import com.example.hiteshrexwal.popularmovies.R;


public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.ViewHolder>{
    private Cursor cursor;
    private Context context;
    public FavoriteMovieAdapter (Cursor cursor,Context context) {
        this.cursor=cursor;
        this.context=context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        final byte[] img = cursor.getBlob(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_IMAGE));
        final String id=cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ID));
        holder.movie_poster.setImageBitmap(BitmapUtility.getImage(img));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Class destination = FavoriteMovieActivity.class;
                Intent secondActivity = new Intent(context, destination);
                secondActivity.putExtra("Img_id",id ).putExtra("isFavorite",true);
                context.startActivity(secondActivity);

            }
        });

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView movie_poster;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            movie_poster =  itemView.findViewById(R.id.img_poster);
            cardView = itemView.findViewById(R.id.card);
        }
    }

}

