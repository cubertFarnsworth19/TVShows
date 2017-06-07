package com.example.android.tvshows.ui.showinfo.recommendations;

import android.support.v4.app.Fragment;

import com.example.android.tvshows.ui.find.ResultsFragment;


public class RecommendationsFragment extends ResultsFragment {

    private static int mTmdbId;

    public static Fragment getInstance(int tmdbId){
        mTmdbId = tmdbId;
        return new RecommendationsFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        getResultsPresenter().getRecommendations(getActivity(),mTmdbId);
    }

}
