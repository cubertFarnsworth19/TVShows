package com.example.android.tvshows.ui.find;

import android.content.ContentValues;
import android.content.Context;

import com.example.android.tvshows.data.model.search.DiscoverResults;
import com.example.android.tvshows.data.model.search.Result;

import java.util.List;

public interface ResultsContract {

    interface View{
        //void setResultsAdapter(DiscoverResults discoverResults);
        void setResultsAdapter(int size);
        void setFilters(String sortBy, String withGenres, String withoutGenres,
                                    String minVoteAverage, String minVoteCount,
                                    String firstAirDateAfter, String firstAirDateBefore);
        void search(String searchTerm);
    }

    interface Presenter{
        void saveSelectedToDatabase(Integer id);
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
        boolean showAddButton(int position);
        int getTmdbId(int position);
    }

}
