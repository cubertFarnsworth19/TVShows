package com.example.android.tvshows.ui.showinfo.cast;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.tvshows.R;
import com.example.android.tvshows.ShowsApplication;
import com.example.android.tvshows.data.db.ShowsRepository;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CastFragment extends Fragment implements CastContract.View{

    private final String OUTSTATE_CAST_INFO = "cast_info";
    private final String OUTSTATE_ADAPTER = "adapter";

    private static int mTmdbId;

    public static CastFragment getInstance(int tmdbId){
        mTmdbId = tmdbId;
        return new CastFragment();
    }

    @BindView(R.id.recyclerview_cast) RecyclerView mRecyclerView;

    @Inject CastAdapter mCastAdapter;
    @Inject CastContract.Presenter mCastPresenter;

    private boolean mLoaded;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.show_info_cast_fragment,container,false);
        ButterKnife.bind(this,rootview);

        ShowsApplication showsApplication = (ShowsApplication) getActivity().getApplication();

        if(savedInstanceState!=null){
            ArrayList<CastInfo> castInfo = savedInstanceState.getParcelableArrayList(OUTSTATE_CAST_INFO);
            CastAdapter adapter =  savedInstanceState.getParcelable(OUTSTATE_ADAPTER);

            CastComponent component = DaggerCastComponent.builder()
                    .applicationComponent(showsApplication.get(getActivity()).getComponent())
                    .castModule(showsApplication.getCastModule(this, this, mTmdbId,castInfo,adapter))
                    .build();

            component.inject(this);

            mLoaded = true;
        }
        else {
            mLoaded = false;

            CastComponent component = DaggerCastComponent.builder()
                    .applicationComponent(showsApplication.get(getActivity()).getComponent())
                    .castModule(showsApplication.getCastModule(this, this, mTmdbId))
                    .build();

            component.inject(this);
        }
        setupRecyclerView();

        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!mLoaded) mCastPresenter.loadCastData(getActivity());
    }

    private void setupRecyclerView(){
        mRecyclerView.setAdapter(mCastAdapter);
        GridLayoutManager glm = new GridLayoutManager(getActivity(),1);
        mRecyclerView.setLayoutManager(glm);
    }

    @Override
    public void castDataLoaded(int size) {
        mCastAdapter.displayCast(size);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(OUTSTATE_CAST_INFO, mCastPresenter.getCastInfo());
        outState.putParcelable(OUTSTATE_ADAPTER, mCastAdapter);

    }
}
