package com.example.android.tvshows.ui.updates;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.android.tvshows.R;
import com.example.android.tvshows.ShowsApplication;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdatesFragment extends Fragment implements  UpdatesContract.View{

    private final String OUTSTATE_PRESENTER = "presenter";
    private final String OUTSTATE_ADAPTER = "adapter";

    protected @Inject UpdatesContract.Presenter mUpdatesPresenter;
    protected @Inject UpdatesAdapter mUpdatesAdapter;

    protected @BindView(R.id.recyclerview_updates_detail) RecyclerView mRecyclerView;

    private View mRootview;
    private StaggeredGridLayoutManager mGridLayoutManager;

    private boolean mLoaded;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootview = inflater.inflate(R.layout.updates_fragment, container, false);

        ButterKnife.bind(this,mRootview);

        if(savedInstanceState!=null){
            mUpdatesPresenter = savedInstanceState.getParcelable(OUTSTATE_PRESENTER);
            mUpdatesAdapter = savedInstanceState.getParcelable(OUTSTATE_ADAPTER);
            mLoaded = true;
        }
        else {

            mLoaded = false;

            ShowsApplication showsApplication = (ShowsApplication) getActivity().getApplication();

            UpdatesComponent component = DaggerUpdatesComponent.builder()
                    .applicationComponent(showsApplication.get(getActivity()).getComponent())
                    .updatesModule(showsApplication.getUpdatesModule(this, this))
                    .build();

            component.inject(this);
        }
        setupRecyclerView();
        return mRootview;

    }

    private void setupRecyclerView(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int columns = (int) dpWidth/ 400;
        columns = columns>=1 ? columns:1;
        mRecyclerView.setAdapter(mUpdatesAdapter);
        mGridLayoutManager = new StaggeredGridLayoutManager(columns,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!mLoaded) mUpdatesPresenter.loadShowsFromDatabase(false);
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

    @Override
    public void noConnection() {
        Snackbar.make(mRootview,getResources().getString(R.string.no_connection),Snackbar.LENGTH_INDEFINITE).show();
    }

    @Override
    public void displayUpdate() {
        mUpdatesAdapter.displayUpdates();
    }

    public void unCheckAll(){
        mUpdatesAdapter.uncheckAll();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(getActivity().isChangingConfigurations()) {
            super.onSaveInstanceState(outState);
            outState.putParcelable(OUTSTATE_ADAPTER, mUpdatesAdapter);
            outState.putParcelable(OUTSTATE_PRESENTER,mUpdatesPresenter);
        }
    }
}
