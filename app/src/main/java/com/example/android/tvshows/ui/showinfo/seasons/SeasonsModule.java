package com.example.android.tvshows.ui.showinfo.seasons;

import com.example.android.tvshows.data.db.ShowsRepository;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module
public class SeasonsModule {

    private final SeasonsFragment mSeasonsFragment;
    private final SeasonsContract.View mView;
    private int tmdbId;

    public SeasonsModule(SeasonsFragment seasonsFragment, SeasonsContract.View view, int tmdbId) {
        mView = view;
        this.tmdbId = tmdbId;
        mSeasonsFragment = seasonsFragment;
    }

    @Provides
    @SeasonsScope
    public SeasonsContract.Presenter providesSeasonsContractPresenter(ShowsRepository showsRepository){
        return new SeasonsPresenter(mView,showsRepository,tmdbId);
    }

    @Provides
    @SeasonsScope
    public SeasonsAdapter provideSeasonsAdapter(SeasonsContract.Presenter presenter, Picasso picasso){
        return new SeasonsAdapter(mSeasonsFragment.getActivity(),presenter,picasso);
    }

}
