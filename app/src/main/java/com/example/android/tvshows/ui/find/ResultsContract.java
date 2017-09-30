package com.example.android.tvshows.ui.find;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;

import com.example.android.tvshows.data.model.search.DiscoverResults;
import com.example.android.tvshows.data.model.search.Result;

import java.util.List;

public interface ResultsContract {

    interface View{
        void setResultsAdapter(int size);
        void setFilters(String sortBy, String withGenres, String withoutGenres,
                                    String minVoteAverage, String minVoteCount,
                                    String firstAirDateAfter, String firstAirDateBefore) throws InterruptedException;
        void search(String searchTerm);
        FragmentManager getFragmentManagerForDialog();
        void updateAdapter();
        void noConnection();
        void noConnectionWithRetry(Integer tmdbId);
        void noConnectionRetryNewPage(Integer page);
    }

    interface Presenter{
        void saveSelectedToDatabase(Context context, Integer id);
        void makeDiscoverRequest(Context context, String sortBy, String withGenres, String withoutGenres,
                                 String minVoteAverage, String minVoteCount,
                                 String firstAirDateAfter, String firstAirDateBefore);
        void getDiscoverPage(Context context, Integer page);
        void search(Context context, String searchTerm);
        void getRecommendations(Context context, Integer tmddId);
        String getName(int position);
        String getFirstAirDate(int position);
        int getVoteAverageBackgroundColor(Context context, int position);
        int getVoteAverageTextColor(Context context, int position);
        String getVoteAverage(int position);
        String getPosterUrl(Context context, int position);
        boolean showAddButton(int position);
        int getTmdbId(int position);
        void openMoreDetailsDialog(Context context,int position);
        void showAdded();
        SaveResultsPresenterState getSaveResultsPresenterState();
    }

}
