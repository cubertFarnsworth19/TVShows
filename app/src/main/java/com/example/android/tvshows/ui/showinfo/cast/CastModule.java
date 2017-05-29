package com.example.android.tvshows.ui.showinfo.cast;

import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.myshows.current.CurrentFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public class CastModule {

    private final CastFragment mCastFragment;
    private final CastContract.View mView;
    private int tmdbId;
    private ArrayList<CastInfo> mCastInfo;
    private CastAdapter mCastAdapter;
    private boolean mSaved;

    public CastModule(CastFragment castFragment, CastContract.View view, int tmdbId) {
        mView = view;
        this.tmdbId = tmdbId;
        mCastFragment = castFragment;
        mSaved = false;
    }

    public CastModule(CastFragment castFragment, CastContract.View view, int tmdbId,
                      ArrayList<CastInfo> castInfo,CastAdapter castAdapter) {
        mView = view;
        this.tmdbId = tmdbId;
        mCastFragment = castFragment;
        mCastInfo = castInfo;
        mCastAdapter = castAdapter;
        mSaved = true;
    }

    @Provides
    @CastScope
    public CastContract.Presenter providesCastContractPresenter(ShowsRepository showsRepository){
        if(mSaved)
            return new CastPresenter(mView,showsRepository,tmdbId,mCastInfo);
        else
            return new CastPresenter(mView,showsRepository,tmdbId);
    }

    @Provides
    @CastScope
    public CastAdapter provideCastAdapter(CastContract.Presenter presenter, Picasso picasso){
        if(mSaved){
            mCastAdapter.setVariables(mCastFragment.getActivity(),presenter,picasso);
            return mCastAdapter;
        }
        else {
            return new CastAdapter(mCastFragment.getActivity(), presenter, picasso);
        }
    }

}
