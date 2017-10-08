package com.example.android.tvshows.ui.episodes;

import android.content.Context;
import android.content.Intent;

public interface EpisodesContract {

    interface View{
        void episodeDataLoaded(int numberOfEpisodes);
        void setIMDBId(String imdbId);
        void endActivity();
    }

    interface Presenter{
        void loadEpisodesData(Context context);
        EpisodeData getEpisodeData(Context context,int position);
        Intent getIntentForNewEpisodeActivity(Context context,int index);
        String[] getSeasonNames();
        String getTitle();
        boolean downloadExternalIds(final int episodeNumber);
        String getImdbId();
    }

}
