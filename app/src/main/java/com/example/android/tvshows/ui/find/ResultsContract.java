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
                                    String firstAirDateAfter, String firstAirDateBefore);
        void search(String searchTerm);
        FragmentManager getFragmentManagerForDialog();
        void updateAdapter();
    }

    interface Presenter extends Parcelable {
        void saveSelectedToDatabase(Context context, Integer id);
        void makeDiscoverRequest(String sortBy, String withGenres, String withoutGenres,
                                 String minVoteAverage, String minVoteCount,
                                 String firstAirDateAfter, String firstAirDateBefore);
        void getDiscoverPage(Integer page);
        void search(String searchTerm);
        void getRecommendations(Integer tmddId);
        String getName(int position);
        String getFirstAirDate(int position);
        int getVoteAverageBackgroundColor(Context context, int position);
        int getVoteAverageTextColor(Context context, int position);
        String getVoteAverage(int position);
        String getPosterUrl(Context context, int position);
        public String getLastWithGenres();
        boolean showAddButton(int position);
        int getTmdbId(int position);
        int getNumberOfColumns(Context context);
        void openMoreDetailsDialog(int position);
        void showAdded();
        SaveResultsPresenterState getSaveResultsPresenterState();
    }

}
