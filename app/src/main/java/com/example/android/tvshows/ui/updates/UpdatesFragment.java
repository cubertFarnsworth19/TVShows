package com.example.android.tvshows.ui.updates;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.android.tvshows.R;
import com.example.android.tvshows.ShowsApplication;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdatesFragment extends Fragment implements  UpdatesContract.View{

    protected @Inject UpdatesContract.Presenter mUpdatesPresenter;
    protected @Inject UpdatesAdapter mUpdatesAdapter;

    @BindView(R.id.check_box_auto_update) CheckBox mCheckBoxAutoUpdate;

    protected @BindView(R.id.recyclerview_updates_detail) RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mGridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.updates_fragment, container, false);

        ButterKnife.bind(this,rootview);

        UpdatesComponent component = DaggerUpdatesComponent.builder()
                .applicationComponent(ShowsApplication.get(getActivity()).getComponent())
                .updatesModule(new UpdatesModule(this,this))
                .build();

        component.inject(this);

        setupRecyclerView();
        return rootview;

    }

    private void setupRecyclerView(){
        mRecyclerView.setAdapter(mUpdatesAdapter);
        mGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mUpdatesPresenter.loadShowsFromDatabase();
    }

    @Override
    public void showsDataLoaded(int size) {
        mUpdatesAdapter.displayUpdates(size);
    }

    @Override
    public void updateSelected() {
        ArrayList<Pair<Boolean,ArrayList<Boolean>>> checked = mUpdatesAdapter.getChecked();
        mUpdatesPresenter.makeUpdatesRequest(getContext(),checked);
    }
}
