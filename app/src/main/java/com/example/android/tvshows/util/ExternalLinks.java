package com.example.android.tvshows.util;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.android.tvshows.R;

public class ExternalLinks {

    public static void vistIMDBShowPage(Context context,String imdbId){
        if(imdbId!="")
            visitWebsite(context,Uri.parse(context.getString(R.string.imdb_tv_show_webpage) + imdbId));
    }

    public static void visitIMDBActorPage(Context context,String imdbId){
        if(imdbId!="")
            visitWebsite(context,Uri.parse(context.getString(R.string.imdb_actor_webage_start)
                    + imdbId + context.getString(R.string.imdb_actor_webage_end)));
    }

    public static void visitTMDBPage(Context context,int tmdbId){
        visitWebsite(context,Uri.parse(context.getString(R.string.tmdb_tv_show_webpage) + tmdbId));
    }

    public static void searchGoogle(Context context,String title){
        visitWebsite(context,Uri.parse(context.getString(R.string.google_search_webpage) + title));
    }

    public static void searchYoutube(Context context,String title){
        visitWebsite(context,Uri.parse(context.getString(R.string.youtube_search_webpage) + title));
    }

    public static void visitWikipedia(Context context,String title){
        visitWebsite(context,Uri.parse(getWikipediaTVSeriesWebpage(context,title)));
    }

    private static void visitWebsite(Context context,Uri webpage){
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
    }

    private static String getWikipediaTVSeriesWebpage(Context context,String title){
        title.replace(' ','_');
        return context.getString(R.string.wikipedia_webapge)+title+context.getString(R.string.wikipedia_webpage_end);
    }

}
