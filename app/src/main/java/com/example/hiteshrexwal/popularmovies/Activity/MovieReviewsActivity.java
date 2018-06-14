package com.example.hiteshrexwal.popularmovies.Activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hiteshrexwal.popularmovies.Adapter.ReviewsAdapter;
import com.example.hiteshrexwal.popularmovies.R;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieReviewsActivity extends AppCompatActivity {
    private final String KEY_RECYCLER_STATE = "recycler_state";
    @BindView(R.id.recycle_reviews)
    RecyclerView recyclerView;
    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie__reviews);
        ButterKnife.bind(this);
        Intent fromActivity=getIntent();
        if(fromActivity.hasExtra("author")&& fromActivity.hasExtra("contents")) {
            ArrayList<String> author,contents;
            author=fromActivity.getStringArrayListExtra("author");
            contents=fromActivity.getStringArrayListExtra("contents");
            LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            ReviewsAdapter adapter = new ReviewsAdapter(author,contents, this);
            recyclerView.setAdapter(adapter);
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
}
