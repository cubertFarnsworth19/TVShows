package com.example.android.tvshows.ui.showinfo.details;

import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.rest.ApiService;

import dagger.Module;
import dagger.Provides;

@Module
public class DetailsModule {

    private final DetailsFragment mDetailsFragment;
    private final DetailsContract.View mView;
    private int tmdbId;

    public DetailsModule(DetailsFragment detailsFragment, DetailsContract.View view, int tmdbId) {
        mView = view;
        this.tmdbId = tmdbId;
        mDetailsFragment = detailsFragment;
    }

    @Provides
    @DetailsScope
    public DetailsContract.Presenter providesDetailsContractPresenter(ShowsRepository showsRepository, ApiService apiService){
        return new DetailsPresenter(mView,showsRepository,apiService,tmdbId);
    }

    @Provides
    @DetailsScope
    public CreatorAdapter provideCreatorAdapter(DetailsContract.Presenter presenter){
        return new CreatorAdapter(presenter);
    }

}
