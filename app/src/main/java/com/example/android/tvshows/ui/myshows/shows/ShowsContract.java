package com.example.android.tvshows.ui.myshows.shows;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.util.ArrayList;

public interface ShowsContract {

    interface View{
        void showsDataLoaded(int size);
        Context getActivity();
    }

    interface Presenter{
        void loadShowsFromDatabase(Context context,boolean continuing,boolean favorite);
        String getTitle(int position);
        String getPosterUrl(int position);
        String getNumberOfSeasons(Context context,int position);
        String getNumberOfEpisodes(Context context,int position);
        String getInProduction(int position);
        boolean isFavorite(int position);
        void setFavorite(int position,boolean favorite);
        void removeShow(int position);
        ArrayList<ShowInfo> getShowsInfo();
        Intent getIntentForShowInfoActivity(Context context,int position);
    }
}
