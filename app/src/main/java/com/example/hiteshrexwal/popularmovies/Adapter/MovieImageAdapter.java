package com.example.hiteshrexwal.popularmovies.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hiteshrexwal.popularmovies.Activity.ImageDisplayActivity;
import com.example.hiteshrexwal.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieImageAdapter extends RecyclerView.Adapter<MovieImageAdapter.ViewHolder> {
    private TextView see_all;
    private ArrayList<String> list;
    private Context context;
    private boolean isActivity=false;
    public MovieImageAdapter(ArrayList<String> list,Context context,TextView see_all) {
        this.list = list;
        this.context=context;
        this.see_all=see_all;
    }
    public MovieImageAdapter(ArrayList<String> list,Context context,boolean isActivity) {
        this.list = list;
        this.context=context;
        this.isActivity=isActivity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(!isActivity)
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_images_recyclerview,parent,false);
        else
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String temps=list.get(position);
        String link="https://image.tmdb.org/t/p/w500";
        link+=temps;
        Picasso.get().load(link).placeholder(R.drawable.loading).error(R.drawable.error).into(holder.movie_poster);
        if(!isActivity) {
            see_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Class destination = ImageDisplayActivity.class;
                    Intent secondActivity = new Intent(context, destination);
                    secondActivity.putStringArrayListExtra("Img_link", list);
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
        public ViewHolder(View itemView) {
            super(itemView);
            if(!isActivity) {
                movie_poster =  itemView.findViewById(R.id.movie_images);
            }
            else{
                movie_poster = itemView.findViewById(R.id.img_poster);
            }
        }
    }

}