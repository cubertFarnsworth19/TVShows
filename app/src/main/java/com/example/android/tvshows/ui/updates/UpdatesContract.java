package com.example.android.tvshows.ui.updates;

import android.content.Context;
import android.support.v4.util.Pair;

import java.util.ArrayList;

public interface UpdatesContract {

    interface View{
        void showsDataLoaded(int size);
        void updateSelected();
    }

    interface Presenter{
        void loadShowsFromDatabase();
        int getShowId(int position);
        int getNumberOfSeasons(int position);
        String getShowName(int position);
        String getLastUpdate(int position);
        String getShowLastUpdate(int position);
        String getSeasonName(Integer showId,int position);
        String getSeasonLastUpdate(Integer showId,int position);
        void makeUpdatesRequest(Context context, ArrayList<Pair<Boolean,ArrayList<Boolean>>> checked);
    }

}
