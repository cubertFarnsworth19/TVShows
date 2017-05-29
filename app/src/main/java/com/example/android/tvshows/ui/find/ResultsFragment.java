package com.example.android.tvshows.ui.find;

import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.tvshows.R;
import com.example.android.tvshows.ShowsApplication;
import com.example.android.tvshows.data.model.Actor;
import com.example.android.tvshows.data.model.search.DiscoverResults;

//import com.example.android.tvshows.ui.find.DaggerResultsComponent;
import com.example.android.tvshows.ui.find.discover.DiscoverActivity;
import com.example.android.tvshows.ui.find.search.SearchActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ResultsFragment extends Fragment implements ResultsContract.View {

    private final String OUTSTATE_PRESENTER = "presenter";
    private final String OUTSTATE_ADAPTER = "adapter";

    protected @Inject ResultsContract.Presenter mResultsPresenter;
    protected @Inject ResultsAdapter mResultsAdapter;
    //protected @Inject Picasso mPicasso;

    protected @BindView(R.id.recyclerview_results) RecyclerView mRecyclerView;
    protected StaggeredGridLayoutManager mGridLayoutManager;

    // true if the savedInstanceState should be set
   //private boolean mSave = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.find_results_fragment, container, false);

        ButterKnife.bind(this,rootview);

        if(savedInstanceState!=null){
            mResultsPresenter = savedInstanceState.getParcelable(OUTSTATE_PRESENTER);
            mResultsAdapter =  savedInstanceState.getParcelable(OUTSTATE_ADAPTER);
        }
        else {
            ResultsComponent component = DaggerResultsComponent.builder()
                    .applicationComponent(ShowsApplication.get(getActivity()).getComponent())
                    .resultsModule(new ResultsModule(this, this))
                    .build();

            component.inject(this);
        }
        setRetainInstance(true);
        setupRecyclerView();

        return rootview;
    }

    private void setupRecyclerView(){
        mRecyclerView.setAdapter(mResultsAdapter);
        mGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
    }

    @Override
    public void setResultsAdapter(int size) {
       // mResultsAdapter = new ResultsAdapter(this.getActivity(),discoverResults,mResultsPresenter,mPicasso);
        mResultsAdapter.updateDiscoverResults(size);
    }

    @Override
    public void setFilters(String sortBy, String withGenres, String withoutGenres, String minVoteAverage, String minVoteCount, String firstAirDateAfter, String firstAirDateBefore) {
        mResultsPresenter.makeDiscoverRequest(sortBy,withGenres,withoutGenres,minVoteAverage,minVoteCount,firstAirDateAfter,firstAirDateBefore);
    }

    @Override
    public void search(String searchTerm) {
        mResultsPresenter.search(searchTerm);
    }

    @Override
    public FragmentManager getFragmentManagerForDialog() {
        return getActivity().getSupportFragmentManager();
    }

    @Override
    public void updateAdapter() {
        mResultsAdapter.notifyDataSetChanged();
    }


    public ResultsContract.Presenter getResultsPresenter(){
        return mResultsPresenter;
    }

    //public void setSave(boolean save){
    //    mSave = save;
    //}

    @Override
    public void onSaveInstanceState(Bundle outState) {
      //  if(getActivity().isChangingConfigurations()) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(OUTSTATE_PRESENTER, mResultsPresenter);
        outState.putParcelable(OUTSTATE_ADAPTER, mResultsAdapter);
      //  }
    }
}
