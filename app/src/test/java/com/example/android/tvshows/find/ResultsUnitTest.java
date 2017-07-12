package com.example.android.tvshows.find;


import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.model.search.Result;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.ui.find.ResultsContract;
import com.example.android.tvshows.ui.find.ResultsPresenter;
import com.example.android.tvshows.ui.find.SaveResultsPresenterState;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ResultsUnitTest {

    @Mock
    ShowsRepository mRepository;

    @Mock
    ResultsContract.View mView;

    @Mock
    ApiService mApiService;

    ResultsPresenter mPresenter;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);

        mPresenter = new ResultsPresenter(mView,mApiService,mRepository,getResultsState());

    }

    private SaveResultsPresenterState getResultsState(){

        List<Result> results = new ArrayList<>();

        results.add(new Result(23,6.921,31,"2007"));
        results.add(new Result(26,0.0,0,""));
        results.add(new Result(48,8.4,42,"2009"));
        results.add(new Result(21,10.0,1,"2008"));
        results.add(new Result(26,0.0,28,"2001"));


        ArrayList<Integer> ids = new ArrayList<>();

        ids.add(23);
        ids.add(83);
        ids.add(21);
        ids.add(65);

        SaveResultsPresenterState resultsState = new SaveResultsPresenterState(
                results,1,3,60,"","","","","","2004","2016",ids);

        return resultsState;
    }

    @Test
    public void voteAverage(){
        assertTrue(mPresenter.getVoteAverage(0).equals("6.9"));
        assertTrue(mPresenter.getVoteAverage(1).equals(""));
        assertTrue(mPresenter.getVoteAverage(2).equals("8.4"));
        assertTrue(mPresenter.getVoteAverage(3).equals("10"));
        assertTrue(mPresenter.getVoteAverage(4).equals("0.0"));

    }

    @Test
    public void firstAirDate(){
        assertTrue(mPresenter.getFirstAirDate(0).equals("2007"));
        assertTrue(mPresenter.getFirstAirDate(1).equals(""));
    }

    @Test
    public void showAddButton(){
        assertTrue(mPresenter.showAddButton(1));
        assertTrue(mPresenter.showAddButton(2));
        assertFalse(mPresenter.showAddButton(0));
        assertFalse(mPresenter.showAddButton(3));
    }

}
