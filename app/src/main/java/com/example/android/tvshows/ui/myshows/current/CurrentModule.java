package com.example.android.tvshows.ui.myshows.current;

import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.myshows.shows.ShowsScope;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module
public class CurrentModule {

    private final CurrentFragment mCurrentFragment;
    private final CurrentContract.View mView;
    private int mCurrentType;

    public CurrentModule(CurrentContract.View view, CurrentFragment currentFragment, int currentType) {
        mView = view;
        mCurrentFragment = currentFragment;
        mCurrentType = currentType;
    }

    @Provides
    @CurrentScope
    public CurrentContract.Presenter providesCurrentContractPresenter(ShowsRepository showsRepository){
        return new CurrentPresenter(mView,showsRepository,mCurrentType);
    }

    @Provides
    @CurrentScope
    public CurrentAdapter provideCurrentAdapter(CurrentContract.Presenter presenter ,Picasso picasso){
        return new CurrentAdapter(mCurrentFragment.getActivity(),presenter,picasso);
    }

}
