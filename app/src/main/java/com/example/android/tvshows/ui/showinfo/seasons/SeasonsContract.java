package com.example.android.tvshows.ui.showinfo.seasons;

import android.content.Context;
import android.os.Parcelable;

import java.util.ArrayList;

public interface SeasonsContract {

    interface View{
        void seasonDataLoaded(int size);
        void setSave(boolean save);
    }

    interface Presenter {
        void loadSeasonsData(Context context);
        String getSeasonName(int adapterPosition);
        String getPosterUrl(Context context,int adapterPosition);
        String getAirDate(int adapterPosition);
        String getOverview(int adapterPosition);
        String getNumberOfEpisodes(int adapterPosition);
        void startEpisodesActivity(Context context,int adapterPosition);
        ArrayList<SeasonInfo> getSeasonsInfo();
    }
}
