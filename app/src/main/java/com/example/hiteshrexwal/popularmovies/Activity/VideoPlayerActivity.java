package com.example.hiteshrexwal.popularmovies.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.hiteshrexwal.popularmovies.BuildConfig;
import com.example.hiteshrexwal.popularmovies.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoPlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private String videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video__player);
        TextView video_name=findViewById(R.id.videos_name);
        YouTubePlayerView  youTubePlayerView=findViewById(R.id.player);
        Intent fromActivity=getIntent();
        if(fromActivity.hasExtra("video_key")&& fromActivity.hasExtra("Video Name")){
            videoId=fromActivity.getStringExtra("video_key");
            video_name.setText(fromActivity.getStringExtra("Video Name"));
            try {
                youTubePlayerView.initialize(BuildConfig.THE_YOUTUBE_API_TOKEN, this);
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo(videoId);

        youTubePlayer.play();
        youTubePlayer.setShowFullscreenButton(false);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

}
