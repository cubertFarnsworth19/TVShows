package com.example.android.tvshows;


import com.example.android.tvshows.ui.find.ResultsContract;
import com.example.android.tvshows.ui.find.ResultsFragment;
import com.example.android.tvshows.ui.find.ResultsModule;

public class TestShowsApplication extends ShowsApplication{

    private ResultsModule mResultsModule;

    @Override
    public ResultsModule getResultsModule(ResultsFragment resultsFragment, ResultsContract.View view) {
        if(mResultsModule==null)
            return super.getResultsModule(resultsFragment, view);
        return mResultsModule;
    }

    public void setResultsModule(ResultsModule resultsModule) {
        mResultsModule = resultsModule;
    }
}
