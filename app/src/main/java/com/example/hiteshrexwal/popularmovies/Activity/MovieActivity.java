package com.example.hiteshrexwal.popularmovies.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.example.hiteshrexwal.popularmovies.Adapter.MovieAdapter;
import com.example.hiteshrexwal.popularmovies.Extras.Movies;
import com.example.hiteshrexwal.popularmovies.Network.DatabaseLoader;
import com.example.hiteshrexwal.popularmovies.Network.MovieJsonDownloader;
import com.example.hiteshrexwal.popularmovies.R;
import com.victor.loading.rotate.RotateLoading;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.loading)  RotateLoading  avi;
    @BindView(R.id.recycle_popualar)RecyclerView recyclerView;
    @BindView(R.id.constraint_layout)ConstraintLayout constraintLayout;
    private ArrayList<Movies> movie_data;
    private static Bundle bundle;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private int page_no,total_page;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        ButterKnife.bind(this);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        movie_data=new ArrayList<>();
        if(bundle!=null){
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(this,MovieActivity.calculateNoOfColumns(this));
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(gridLayoutManager);
            movie_data=bundle.getParcelableArrayList("list");
            final MovieAdapter adapter = new MovieAdapter(movie_data, this, false);
            recyclerView.setAdapter(adapter);
            page_no=bundle.getInt("cur_page");
            total_page=bundle.getInt("total");
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1)) {
                        //Toast.makeText(getApplicationContext(),"Reached End",Toast.LENGTH_LONG).show();
                        if(total_page>=page_no ) {
                            avi.setVisibility(View.VISIBLE);
                            avi.start();
                            Bundle args= new Bundle();
                            args.putString("type", bundle.getString("type"));
                            MovieJsonDownloader downloader = new MovieJsonDownloader(getApplicationContext(), movie_data, avi, recyclerView, true, adapter, ++page_no, total_page, gridLayoutManager, bundle, getSupportLoaderManager());
                        }
                    }
                }

            });

        }
        else {
            Downloader(getString(R.string.popular), 1);
        }


    }
    @Override
    protected void onPause()
    {
        super.onPause();
        bundle = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        bundle.putParcelable(KEY_RECYCLER_STATE, listState);
        bundle.putParcelableArrayList("list",movie_data);
        bundle.putInt("cur_page",MovieJsonDownloader.cur_page);
        bundle.putInt("total",MovieJsonDownloader.total_page);
        bundle.putString("type",MovieJsonDownloader.args.getString("type"));

    }


    private void Downloader(String s, int cur_page){
        boolean network_avail=isNetworkConnectionAvailable();
        if(network_avail) {
            avi.setVisibility(View.VISIBLE);
            avi.start();
            Bundle queryBundle = new Bundle();
            queryBundle.putString("type", s);
            movie_data.clear();
            MovieJsonDownloader downloader = new MovieJsonDownloader(this, movie_data, avi, recyclerView, false, cur_page, queryBundle,getSupportLoaderManager());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort ){
            String[] listitems = new String[]{"Popular", "Top Rated", "Upcoming", "Now Playing", "Favorite"};
            AlertDialog.Builder dialog=new AlertDialog.Builder(MovieActivity.this);
            dialog.setTitle("Sort");
            dialog.setIcon(R.mipmap.sort_icon);
            dialog.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                    if(i==0){
                        Downloader(getString(R.string.popular),1);
                    }
                    else if(i==1){
                        Downloader(getString(R.string.toprated),1);
                    }
                    else if (i==2) {
                        Downloader(getString(R.string.upcoming),1);

                    }else if (i==3) {
                        Downloader(getString(R.string.nowplaying),1);
                    }else if(i==4){
                        printDataFromDatabase();
                    }

                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            AlertDialog alert=dialog.create();
            alert.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.popular_movie) {
            Downloader(getString(R.string.popular),1);
        } else if (id == R.id.high_rated) {
            Downloader(getString(R.string.toprated),1);

        }else if (id == R.id.upcoming) {
            Downloader(getString(R.string.upcoming),1);

        }else if (id == R.id.now_playing) {
            Downloader(getString(R.string.nowplaying),1);

        }
        else if (id == R.id.favorite_movies) {
            printDataFromDatabase();

        }
        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                        .make(constraintLayout, getString(R.string.noInternet),Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.retry), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Downloader(getString(R.string.popular),1);
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
    void printDataFromDatabase(){
        DatabaseLoader loader=new DatabaseLoader(this,getContentResolver(),recyclerView,getSupportLoaderManager());

    }
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

}
