package com.tradegenie.android.tradegenieapp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tradegenie.android.tradegenieapp.R;
import com.tradegenie.android.tradegenieapp.Utility.YouTubeConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YoutubeActivity extends YouTubeBaseActivity {

    YouTubePlayer.OnInitializedListener mOnInitializedListener;
    @BindView(R.id.youtubePlayerView)
    YouTubePlayerView youtubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        ButterKnife.bind(this);


        mOnInitializedListener=new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                try {
                    List<String> videoList=new ArrayList<>();

                    Log.e("check","Video link===="+getIntent().getStringExtra("video"));

                    videoList.add(getIntent().getStringExtra("video"));

                    youTubePlayer.loadVideos(videoList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };



        youtubePlayerView.initialize(YouTubeConfig.getApiKey(),mOnInitializedListener);
    }
}
