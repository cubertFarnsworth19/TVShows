package com.example.android.tvshows.ui.myshows.current;

import android.content.Context;

public interface CurrentContract {

    interface View{
        void showsDataLoaded(int size);
    }

    interface Presenter{
        void loadShowsFromDatabase();
        String getDate(int position);
        int getNumberOfShowsOnDate(int position);
        // day position is the adapter position in the day
        // show position is the position of the shwo for the given day
        String getShowPosterUrl(Context context,int dayPosition,int showPosition);
        String getShowName(int dayPosition, int showPosition);
        String getEpisodeName(int dayPosition, int showPosition);
        String getShowOverview(int dayPosition, int showPosition);
    }
}
