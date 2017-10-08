package com.example.android.tvshows.ui.showinfo.seasons;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import java.util.ArrayList;

public interface SeasonsContract {

    interface View{
        void seasonDataLoaded(int size);
    }

    interface Presenter {
        void loadSeasonsData(Context context);
        String getSeasonName(int adapterPosition);
        String getPosterUrl(Context context,int adapterPosition);
        String getAirDate(int adapterPosition);
        String getOverview(int adapterPosition);
        String getNumberOfEpisodes(int adapterPosition,Context context);
       // void startEpisodesActivity(Context context,int adapterPosition);
        Intent getIntentForEpisodesActivity(Context context,int adapterPosition);
        ArrayList<SeasonInfo> getSeasonsInfo();
    }
}
