package com.example.android.tvshows.ui.episodes;


import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.rest.ApiService;

import dagger.Module;
import dagger.Provides;

@Module
public class EpisodesModule {

    private final EpisodesActivity mEpisodesActivity;
    private final EpisodesContract.View mView;
    private int mShowId;
    private int mSeasonNumber;
    // the numbers of each season in the spinner
    private int[] mSeasonNumbers;
    private String[] mSeasonNames;

    public EpisodesModule(EpisodesActivity episodesActivity, EpisodesContract.View view,String[] seasonNames, int showId, int seasonNumber, int[] seasonNumbers) {
        mSeasonNames = seasonNames;
        mShowId = showId;
        mSeasonNumber = seasonNumber;
        mSeasonNumbers = seasonNumbers;
        mEpisodesActivity = episodesActivity;
        mView = view;
    }

    @Provides
    @EpisodesActivityScope
    public EpisodesContract.Presenter provideEpisodesContractPresenter(ShowsRepository showsRepository, ApiService apiService){
        return new EpisodesPresenter(mView,showsRepository,apiService,mShowId,mSeasonNumber,mSeasonNumbers,mSeasonNames);
    }


}
