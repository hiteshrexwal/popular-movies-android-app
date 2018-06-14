package com.example.hiteshrexwal.popularmovies.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.hiteshrexwal.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ImageSliderAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> movie_images;

    public ImageSliderAdapter(Context context, ArrayList<String> movie_images) {
        this.context = context;
        this.movie_images = movie_images;
    }

    @Override
    public int getCount() {

        if(movie_images.size()>6)
         return 6;
        return movie_images.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view= inflater.inflate(R.layout.image_slider,container,false);
            ImageView images=view.findViewById(R.id.image_slider);
            String link="https://image.tmdb.org/t/p/w500";
            link+=movie_images.get(position);
            Picasso.get().load(link).placeholder(R.drawable.loading).error(R.drawable.error).into(images);
            container.addView(view);
            return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
