package com.example.android.tvshows.ui.myshows.shows;

import com.example.android.tvshows.data.db.ShowsRepository;

import com.squareup.picasso.Picasso;


import dagger.Module;
import dagger.Provides;

@Module
public class ShowsModule {

    private final ShowsFragment mShowsFragment;
    private final ShowsContract.View mView;

    public ShowsModule(ShowsFragment showsFragment, ShowsContract.View view) {
        mShowsFragment = showsFragment;
        mView = view;
    }

    @Provides
    @ShowsScope
    public ShowsContract.Presenter providesShowsContractPresenter(ShowsRepository showsRepository){
        return new ShowsPresenter(mView,showsRepository);
    }

    @Provides
    @ShowsScope
    public ShowsAdapter provideShowsAdapter(ShowsContract.Presenter presenter ,Picasso picasso){
        return new ShowsAdapter(mShowsFragment.getActivity(),presenter,picasso);
    }
}


