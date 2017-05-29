package com.example.android.tvshows.ui.myshows.current;

import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.myshows.shows.ShowsScope;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public class CurrentModule {

    private final CurrentFragment mCurrentFragment;
    private final CurrentContract.View mView;
    private int mCurrentType;
    private ArrayList<CurrentInfo> mCurrentInfo;
    private ArrayList<ShowDate> mDates;
    private CurrentAdapter mCurrentAdapter;

    public CurrentModule(CurrentContract.View view, CurrentFragment currentFragment, int currentType) {
        mView = view;
        mCurrentFragment = currentFragment;
        mCurrentType = currentType;
    }

    public CurrentModule(CurrentContract.View view, CurrentFragment currentFragment, int currentType,
                         ArrayList<CurrentInfo> currentInfo, ArrayList<ShowDate> dates, CurrentAdapter adapter) {
        mView = view;
        mCurrentFragment = currentFragment;
        mCurrentType = currentType;
        mCurrentInfo = currentInfo;
        mDates = dates;
        mCurrentAdapter = adapter;
    }

    @Provides
    @CurrentScope
    public CurrentContract.Presenter providesCurrentContractPresenter(ShowsRepository showsRepository){
        if(mCurrentInfo!=null && mDates!=null)
            return new CurrentPresenter(mView,showsRepository,mCurrentType,mCurrentInfo,mDates);

        return new CurrentPresenter(mView,showsRepository,mCurrentType);
    }

    @Provides
    @CurrentScope
    public CurrentAdapter provideCurrentAdapter(CurrentContract.Presenter presenter ,Picasso picasso){
        if(mCurrentAdapter!=null){
            mCurrentAdapter.setVariables(mCurrentFragment.getActivity(),presenter,picasso);
            return mCurrentAdapter;
        }
        return new CurrentAdapter(mCurrentFragment.getActivity(),presenter,picasso);
    }

}
