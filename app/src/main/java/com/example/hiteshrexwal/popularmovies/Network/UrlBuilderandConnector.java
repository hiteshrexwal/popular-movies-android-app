package com.example.hiteshrexwal.popularmovies.Network;

import android.net.Uri;
import com.example.hiteshrexwal.popularmovies.BuildConfig;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;



public class UrlBuilderandConnector {


    public static String getDataFromHttp(URL url) throws IOException {
        HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
        InputStream inputStream=urlConnection.getInputStream();
        Scanner scanner=new Scanner(inputStream);
        scanner.useDelimiter("\\A");
        boolean hasInput=scanner.hasNext();
        if(hasInput){
            return scanner.next();
        }
        return null;
    }
    public static URL link_builder(String s,int page) throws MalformedURLException {
        final  String base_url="api.themoviedb.org";
        Uri.Builder builder=new Uri.Builder();
        final String API_KEY_Param="api_key";
        final String PAGE_Param="page";
        builder.scheme("https")
                .authority(base_url)
                .appendPath("3")
                .appendPath("movie")
                .appendPath(s)
                .appendQueryParameter(API_KEY_Param,BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .appendQueryParameter(PAGE_Param,String.valueOf(page));
        Uri img_uri=builder.build();
        URL url=new URL(img_uri.toString());
        return url;
    }
    public static URL build_url(String id,String type) throws MalformedURLException {
        final  String base_url="api.themoviedb.org";
        Uri.Builder builder=new Uri.Builder();
        final String API_KEY_Param="api_key";
        builder.scheme("https")
                .authority(base_url)
                .appendPath("3")
                .appendPath("movie")
                .appendPath(id)
                .appendPath(type)//"images"
                .appendQueryParameter(API_KEY_Param,BuildConfig.THE_MOVIE_DB_API_TOKEN);
        Uri img_uri=builder.build();
        URL url=new URL(img_uri.toString());
        return url;
    }
    public static URL video_thumnail_url(String video_key) throws MalformedURLException {
        final  String base_url="img.youtube.com";
        Uri.Builder builder=new Uri.Builder();
        builder.scheme("https")
                .authority(base_url)
                .appendPath("vi")
                .appendPath(video_key)
                .appendPath("0.jpg");
        Uri img_uri=builder.build();
        URL url=new URL(img_uri.toString());
        return url;
    }
}
