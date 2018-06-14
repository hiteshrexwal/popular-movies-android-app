package com.example.hiteshrexwal.popularmovies.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.hiteshrexwal.popularmovies.Activity.VideoPlayerActivity;
import com.example.hiteshrexwal.popularmovies.Extras.VideoDetails;
import com.example.hiteshrexwal.popularmovies.Network.UrlBuilderandConnector;
import com.example.hiteshrexwal.popularmovies.R;
import com.squareup.picasso.Picasso;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private ArrayList<VideoDetails> list;
    private Context context;
    public VideoAdapter(ArrayList<VideoDetails> list,Context context) {
        this.list = list;
        this.context=context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_video_recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final VideoDetails temp=list.get(position);
        URL link= null;
        try {
            link = UrlBuilderandConnector.video_thumnail_url(temp.video_key);
            Log.i("link", String.valueOf(link));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Picasso.get().load(String.valueOf(link)).placeholder(R.drawable.loading).error(R.drawable.error).into(holder.movie_poster);
        try {
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent secondActivity = new Intent(context,VideoPlayerActivity.class);
                    secondActivity.putExtra("video_key",temp.video_key)
                    .putExtra("Video Name",temp.video_name);
                    context.startActivity(secondActivity);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView movie_poster;
        CardView cardView;
        ImageButton button;
        public ViewHolder(View itemView) {
            super(itemView);
            movie_poster=itemView.findViewById(R.id.movie_video);
            cardView=itemView.findViewById(R.id.card_video_recyclerview);
            button=itemView.findViewById(R.id.play_video);
        }
    }

}
