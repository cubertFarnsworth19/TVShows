package com.example.android.tvshows.ui.find;

import android.support.annotation.Nullable;

import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.rest.ApiService;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module
public class ResultsModule {

    private final ResultsFragment mResultsFragment;
    private final ResultsContract.View mView;
    private SaveResultsPresenterState mSaveResultsPresenterState;
    private ResultsAdapter mResultsAdapter;
    private boolean mSaved;

    public ResultsModule(ResultsFragment resultsFragment, ResultsContract.View view) {
        mResultsFragment = resultsFragment;
        mView = view;
        mSaved = false;
    }

    public ResultsModule(ResultsFragment resultsFragment, ResultsContract.View view,SaveResultsPresenterState saveResultsPresenterState,ResultsAdapter resultsAdapter) {
        mResultsFragment = resultsFragment;
        mView = view;
        mSaveResultsPresenterState = saveResultsPresenterState;
        mResultsAdapter = resultsAdapter;
        mSaved = true;
    }

    @Provides
    @FragmentScoped
    public ResultsContract.Presenter provideResultsContractPresenter(ApiService service, ShowsRepository showsRepository){
        if(mSaved){
            return new ResultsPresenter(mView, service, showsRepository,mSaveResultsPresenterState);
        }
        else {
            return new ResultsPresenter(mView, service, showsRepository);
        }
    }

    @Provides
    @FragmentScoped
    public ResultsAdapter provideResultsAdapter(ResultsContract.Presenter presenter, Picasso picasso){
        if(mSaved){
            mResultsAdapter.setVariables(mResultsFragment.getActivity(), presenter, picasso);
            return mResultsAdapter;
        }
        else {
            return new ResultsAdapter(mResultsFragment.getActivity(), presenter, picasso);
        }
    }

}
