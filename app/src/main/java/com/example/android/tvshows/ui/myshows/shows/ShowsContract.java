package com.example.android.tvshows.ui.myshows.shows;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public interface ShowsContract {

    interface View{
        void showsDataLoaded(int size);
    }

    interface Presenter{
        void loadShowsFromDatabase(Context context);
        String getTitle(int position);
        String getPosterUrl(int position);
        String getNumberOfSeasons(int position);
        String getNumberOfEpisodes(int position);
        String getInProduction(int position);
        boolean isFavorite(int position);
        void setFavorite(int position,boolean favorite);
        void startShowInfoActivity(Context context,int position);
        void removeShow(int position);
        ArrayList<ShowInfo> getShowsInfo();
    }
}
