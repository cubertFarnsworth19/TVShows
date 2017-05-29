package com.example.android.tvshows.ui.myshows.shows;

import com.example.android.tvshows.data.db.ShowsRepository;

import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public class ShowsModule {

    private final ShowsFragment mShowsFragment;
    private final ShowsContract.View mView;
    private ArrayList<ShowInfo> mShowsInfo;
    private ShowsAdapter mShowsAdapter;

    public ShowsModule(ShowsFragment showsFragment, ShowsContract.View view) {
        mShowsFragment = showsFragment;
        mView = view;
    }

    public ShowsModule(ShowsFragment showsFragment, ShowsContract.View view,
                       ArrayList<ShowInfo> showsInfo, ShowsAdapter showsAdapter) {
        mShowsFragment = showsFragment;
        mView = view;
        mShowsInfo = showsInfo;
        mShowsAdapter = showsAdapter;
    }

    @Provides
    @ShowsScope
    public ShowsContract.Presenter providesShowsContractPresenter(ShowsRepository showsRepository){
        if(mShowsInfo!=null)
            return new ShowsPresenter(mView,showsRepository,mShowsInfo);

        return new ShowsPresenter(mView,showsRepository);
    }

    @Provides
    @ShowsScope
    public ShowsAdapter provideShowsAdapter(ShowsContract.Presenter presenter ,Picasso picasso){
        if(mShowsAdapter!=null){
            mShowsAdapter.setVariables(mShowsFragment.getActivity(),presenter,picasso);
            return mShowsAdapter;
        }

        return new ShowsAdapter(mShowsFragment.getActivity(),presenter,picasso);
    }

}


