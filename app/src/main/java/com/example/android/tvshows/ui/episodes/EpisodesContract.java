package com.example.android.tvshows.ui.episodes;

import android.content.Context;

public interface EpisodesContract {

    interface View{
        void episodeDataLoaded(int numberOfEpisodes);
        void endActivity();
    }

    interface Presenter{
        void loadEpisodesData(Context context);
        EpisodeData getEpisodeData(Context context,int position);
        void startNewEpisodesActvity(Context context,int index);
        String[] getSeasonNames();
        void visitIMDbPage(Context context,int episodeNumber);
        void visitTMDBPage(Context context,int tmdbId);
        void searchGoogle(Context context,String episodeName);
        void searchYouTube(Context context,String episodeName);

    }

}
