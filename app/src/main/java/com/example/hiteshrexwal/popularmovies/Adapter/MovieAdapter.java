package com.example.hiteshrexwal.popularmovies.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hiteshrexwal.popularmovies.Activity.MovieDetailActivity;
import com.example.hiteshrexwal.popularmovies.Extras.Movies;
import com.example.hiteshrexwal.popularmovies.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;




public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private ArrayList<Movies> list;
    private Context context;
    private boolean addsimilar;
    public MovieAdapter(ArrayList<Movies> list,Context context,boolean addsimilar) {
        this.list = list;
        this.context=context;
        this.addsimilar=addsimilar;
    }
    public void changelist(ArrayList<Movies> list){
        this.list=list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(!addsimilar)
              view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
        else
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_similar_recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Movies temps=list.get(position);
        String link="https://image.tmdb.org/t/p/w500";
        link+=temps.img_path;
        final String new_link=link;
        Picasso.get().load(link).placeholder(R.drawable.loading).error(R.drawable.error).into(holder.movie_poster);
       if(!addsimilar) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Class destination = MovieDetailActivity.class;
                    Intent secondActivity = new Intent(context, destination);
                    secondActivity.putExtra("Img_link", new_link)
                            .putExtra("Img_Title", temps.title)
                            .putExtra("Img_overview", temps.overview)
                            .putExtra("Img_rating", temps.votes)
                            .putExtra("Img_release_date", temps.release_date)
                            .putExtra("Img_id", temps.id)
                            .putExtra("isFavorite",false);

                    context.startActivity(secondActivity);

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView movie_poster;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            if(!addsimilar) {
                movie_poster = itemView.findViewById(R.id.img_poster);
                cardView = itemView.findViewById(R.id.card);
            }
            else{
                movie_poster = itemView.findViewById(R.id.movie_images);
                cardView = itemView.findViewById(R.id.similar_movies);
            }
        }
    }

}
