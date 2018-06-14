package com.example.hiteshrexwal.popularmovies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.hiteshrexwal.popularmovies.R;
import java.util.ArrayList;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private ArrayList<String> author,contents;
    private Context context;
    public ReviewsAdapter(ArrayList<String> author,ArrayList<String> contents,Context context) {
        this.author = author;
        this.contents=contents;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_reviews_recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.author.setText(author.get(position));
        holder.content.setText(contents.get(position));

    }

    @Override
    public int getItemCount() {
        return author.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView author,content;
        public ViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author_reviews);
            content = itemView.findViewById(R.id.content_reviews);
        }

    }

}
