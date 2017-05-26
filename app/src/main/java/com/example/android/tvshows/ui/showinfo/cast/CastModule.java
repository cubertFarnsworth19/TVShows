package com.example.android.tvshows.ui.showinfo.cast;

import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.myshows.current.CurrentFragment;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module
public class CastModule {

    private final CastFragment mCastFragment;
    private final CastContract.View mView;
    private int tmdbId;

    public CastModule(CastFragment castFragment, CastContract.View view, int tmdbId) {
        mView = view;
        this.tmdbId = tmdbId;
        mCastFragment = castFragment;
    }

    @Provides
    @CastScope
    public CastContract.Presenter providesCastContractPresenter(ShowsRepository showsRepository){
        return new CastPresenter(mView,showsRepository,tmdbId);
    }

    @Provides
    @CastScope
    public CastAdapter provideCastAdapter(CastContract.Presenter presenter, Picasso picasso){
        return new CastAdapter(mCastFragment.getActivity(),presenter,picasso);
    }

}
