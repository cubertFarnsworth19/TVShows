package com.example.android.tvshows.ui.find;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;

import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.FragmentManager;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.tvshows.R;
import com.example.android.tvshows.ShowsApplication;
import com.example.android.tvshows.idlingResource.SimpleIdlingResource;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class ResultsFragment extends Fragment implements ResultsContract.View {

    private final String OUTSTATE_PRESENTER_STATE = "presenter";
    private final String OUTSTATE_ADAPTER = "adapter";

    protected @Inject ResultsContract.Presenter mResultsPresenter;
    protected @Inject ResultsAdapter mResultsAdapter;

    protected @BindView(R.id.recyclerview_results) RecyclerView mRecyclerView;
    protected @BindView(R.id.loading_indicator)ProgressBar mProgressBar;

    protected View mRootview;
    protected StaggeredGridLayoutManager mGridLayoutManager;

    // true if the find_results_item should be wide
    private boolean mWide;

    // The Idling Resource which will be null in production.
    @Nullable private SimpleIdlingResource mIdlingResource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootview = inflater.inflate(R.layout.find_results_fragment, container, false);

        ButterKnife.bind(this,mRootview);

        ShowsApplication showsApplication = (ShowsApplication) getActivity().getApplication();

        if(savedInstanceState!=null){
            SaveResultsPresenterState saveResultsPresenterState = savedInstanceState.getParcelable(OUTSTATE_PRESENTER_STATE);
            ResultsAdapter adapter =  savedInstanceState.getParcelable(OUTSTATE_ADAPTER);

            ResultsComponent component = DaggerResultsComponent.builder()
                    .applicationComponent(showsApplication.get(getActivity()).getComponent())
                    .resultsModule(showsApplication.getResultsModule(this, this,saveResultsPresenterState,adapter))
                    .build();

            component.inject(this);
        }
        else {
            ResultsComponent component = DaggerResultsComponent.builder()
                    .applicationComponent(showsApplication.get(getActivity()).getComponent())
                    .resultsModule(showsApplication.getResultsModule(this, this))
                    .build();

            component.inject(this);
        }
        setRetainInstance(true);
        setupRecyclerView();

        return mRootview;
    }

    private void setupRecyclerView(){
        int columnsStandard = numberColumnsStandard();
        int columnsWide = numberColumnsWide();

        mWide = columnsStandard==columnsWide;
        mResultsAdapter.setLayoutType(mWide);
        mRecyclerView.setAdapter(mResultsAdapter);
        mGridLayoutManager = new StaggeredGridLayoutManager(columnsStandard,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
    }

    private int numberColumnsStandard() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        int itemWidth = (int) (( 2*getResources().getDimension(R.dimen.padding_results_item) +
                2*getResources().getDimension(R.dimen.padding_small) +
                getResources().getDimension(R.dimen.results_item_title_width))/ displayMetrics.density);

        return (int) (dpWidth / itemWidth);
    }

    private int numberColumnsWide() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        int itemWidth = (int) (( 2*getResources().getDimension(R.dimen.padding_results_item) +
                3*getResources().getDimension(R.dimen.padding_small) +
                getResources().getDimension(R.dimen.image_view_width) +
                getResources().getDimension(R.dimen.results_item_title_width_wide))/ displayMetrics.density);

        return (int) (dpWidth / itemWidth);
    }

    @Override
    public void setResultsAdapter(int size) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mResultsAdapter.updateDiscoverResults(size);
    }

    @Override
    public void setFilters(String sortBy, String withGenres, String withoutGenres, String minVoteAverage, String minVoteCount, String firstAirDateAfter, String firstAirDateBefore)  {
        mProgressBar.setVisibility(View.VISIBLE);
        mResultsPresenter.makeDiscoverRequest(getActivity(),sortBy,withGenres,withoutGenres,minVoteAverage,minVoteCount,firstAirDateAfter,firstAirDateBefore);
    }

    @Override
    public void search(String searchTerm) {
        mProgressBar.setVisibility(View.VISIBLE);
        mResultsPresenter.search(getActivity(),searchTerm);
    }

    @Override
    public FragmentManager getFragmentManagerForDialog() {
        return getActivity().getSupportFragmentManager();
    }

    @Override
    public void updateAdapter() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mResultsAdapter.notifyDataSetChanged();
    }

    @Override
    public void noConnection() {
        mProgressBar.setVisibility(View.INVISIBLE);
        Snackbar snackbar = Snackbar.make(mRootview,getResources().getString(R.string.no_connection),Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        setIdleStateWhenSnackbarIsDisplayed(snackbar);
    }

    @Override
    public void noConnectionWithRetry(final Integer tmdbId) {
        mProgressBar.setVisibility(View.INVISIBLE);
        Snackbar snackbar = Snackbar.make(mRootview, getResources().getString(R.string.no_connection), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.retry),
                        new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                mProgressBar.setVisibility(View.VISIBLE);
                                mResultsPresenter.getRecommendations(getActivity(),tmdbId);
                            }
                        }
                );

        snackbar.show();

        setIdleStateWhenSnackbarIsDisplayed(snackbar);
    }

    @Override
    public void noConnectionRetryNewPage(final Integer page) {
        mProgressBar.setVisibility(View.INVISIBLE);
        Snackbar snackbar = Snackbar.make(mRootview, getResources().getString(R.string.no_connection), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.retry),
                        new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                mResultsPresenter.getDiscoverPage(getActivity(),page);
                            }
                        }
                );

        snackbar.show();

        setIdleStateWhenSnackbarIsDisplayed(snackbar);
    }

    private void setIdleStateWhenSnackbarIsDisplayed(Snackbar snackbar){
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
            }

            @Override
            public void onShown(Snackbar snackbar) {
                if (mIdlingResource != null) mIdlingResource.setIdleState(true);
            }
        });
    }

    public ResultsContract.Presenter getResultsPresenter(){
        return mResultsPresenter;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(OUTSTATE_PRESENTER_STATE, mResultsPresenter.getSaveResultsPresenterState());
        outState.putParcelable(OUTSTATE_ADAPTER, mResultsAdapter);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public void removeProgressBar(){
        ((ViewGroup)mProgressBar.getParent()).removeView(mProgressBar);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @VisibleForTesting
    public void setNotIdle(){
        if (mIdlingResource != null) mIdlingResource.setIdleState(false);
    }

}
