package com.example.android.tvshows.ui.find;

import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.rest.ApiService;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

@Module
public class ResultsModule {

    private final ResultsFragment mResultsFragment;
    private final ResultsContract.View mView;

    public ResultsModule(ResultsFragment resultsFragment, ResultsContract.View view) {
        mResultsFragment = resultsFragment;
        mView = view;
    }

    @Provides
    @FragmentScoped
    public ResultsFragment provideResultsFragment(){return mResultsFragment;}

    @Provides
    @FragmentScoped
    public ResultsContract.View provideResultsContractView(){return mView;}

    @Provides
    @FragmentScoped
    public ResultsContract.Presenter provideResultsContractPresenter(ResultsContract.View view, ApiService service, ShowsRepository showsRepository){
        return new ResultsPresenter(view,service,showsRepository);
    }

    @Provides
    @FragmentScoped
    public ResultsAdapter provideResultsAdapter(ResultsContract.Presenter presenter, Picasso picasso){
        return new ResultsAdapter(mResultsFragment.getActivity(),presenter,picasso);
    }

}
