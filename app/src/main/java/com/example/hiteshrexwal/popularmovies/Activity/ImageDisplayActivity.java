package com.example.hiteshrexwal.popularmovies.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.hiteshrexwal.popularmovies.Adapter.MovieImageAdapter;
import com.example.hiteshrexwal.popularmovies.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageDisplayActivity extends AppCompatActivity {
    @BindView(R.id.recycle_image)  RecyclerView recyclerView;
    private ArrayList<String> list;
    @BindView(R.id.image_coordinator)CoordinatorLayout layout;
    @BindView(R.id.toolbar_image)Toolbar toolbar;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image__display);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Intent fromActivity=getIntent();
        if(fromActivity.hasExtra("Img_link")){

            list=fromActivity.getStringArrayListExtra("Img_link");
            Downloader();

        }

    }
    private void Downloader(){
        boolean network_avail=isNetworkConnectionAvailable();
        if(network_avail) {
            GridLayoutManager layoutManager =new GridLayoutManager(this,MovieActivity.calculateNoOfColumns(this));
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new MovieImageAdapter(list, this, true));
        }

    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
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
                        .make(layout, getString(R.string.noInternet),Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.retry), new View.OnClickListener() {
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
