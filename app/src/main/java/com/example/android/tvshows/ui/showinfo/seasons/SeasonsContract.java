package com.example.android.tvshows.ui.showinfo.seasons;

import android.content.Context;

public interface SeasonsContract {

    interface View{
        void seasonDataLoaded(int size);
    }

    interface Presenter{
        void loadSeasonsData();
        String getSeasonName(int adapterPosition);
        String getPosterUrl(Context context,int adapterPosition);
        String getAirDate(int position);
        String getOverview(int position);
        String getNumberOfEpisodes(int position);
        void startEpisodesActivity(Context context,int adapterPosition);
        void closeCursor();
    }
}
