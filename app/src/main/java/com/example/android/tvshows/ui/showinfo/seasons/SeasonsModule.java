package com.example.android.tvshows.ui.showinfo.seasons;

import com.example.android.tvshows.data.db.ShowsRepository;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public class SeasonsModule {

    private final SeasonsFragment mSeasonsFragment;
    private final SeasonsContract.View mView;
    private int tmdbId;
    private ArrayList<SeasonInfo> mSeasonsInfo;
    private SeasonsAdapter mSeasonsAdapter;
    // true if data has been saved
    private boolean mSaved;

    public SeasonsModule(SeasonsFragment seasonsFragment, SeasonsContract.View view, int tmdbId) {
        mView = view;
        this.tmdbId = tmdbId;
        mSeasonsFragment = seasonsFragment;
        mSaved = false;
    }

    public SeasonsModule(SeasonsFragment seasonsFragment, SeasonsContract.View view, int tmdbId,
                         ArrayList<SeasonInfo> seasonsInfo,SeasonsAdapter seasonsAdapter) {
        mView = view;
        this.tmdbId = tmdbId;
        mSeasonsFragment = seasonsFragment;
        mSeasonsInfo = seasonsInfo;
        mSeasonsAdapter = seasonsAdapter;
        mSaved = true;
    }

    @Provides
    @SeasonsScope
    public SeasonsContract.Presenter providesSeasonsContractPresenter(ShowsRepository showsRepository){
        if(mSaved){
            return new SeasonsPresenter(mView, showsRepository, tmdbId,mSeasonsInfo);
        }
        else {
            return new SeasonsPresenter(mView, showsRepository, tmdbId);
        }
    }

    @Provides
    @SeasonsScope
    public SeasonsAdapter provideSeasonsAdapter(SeasonsContract.Presenter presenter, Picasso picasso){

        if(mSaved){
            mSeasonsAdapter.setVariables(mSeasonsFragment.getActivity(),presenter,picasso);
            return mSeasonsAdapter;
        }
        else{
            return new SeasonsAdapter(mSeasonsFragment.getActivity(),presenter,picasso);
        }

    }

}
