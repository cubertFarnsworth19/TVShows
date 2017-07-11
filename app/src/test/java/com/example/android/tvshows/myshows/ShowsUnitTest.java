package com.example.android.tvshows.myshows;


import android.content.Context;

import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.myshows.MyShowsActivity;
import com.example.android.tvshows.ui.myshows.shows.ShowInfo;
import com.example.android.tvshows.ui.myshows.shows.ShowsContract;
import com.example.android.tvshows.ui.myshows.shows.ShowsPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ShowsPresenter.class)
public class ShowsUnitTest {

    @Mock
    Context mContext;

    @Mock
    ShowsRepository mRepository;

    @Mock
    ShowsContract.View mView;

    @Mock
    MyShowsActivity mActivity;

    ShowsPresenter mPresenter;

    ArrayList<ShowInfo> mShowsInfo;

    private static final String METHOD_SET = "setBroadcastReceiver";
    private static final String METHOD_REGISTER = "registerReceivers";

    private String continuing = "Continuing";
    private String finished = "Finished";

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        when(mContext.getString(R.string.poster_path)).thenReturn("http://image.tmdb.org/t/p/w300");
        when(mContext.getString(R.string.continuing)).thenReturn(continuing);
        when(mContext.getString(R.string.finished)).thenReturn(finished);
        when(mContext.getString(R.string.seasons1)).thenReturn(" Season");
        when(mContext.getString(R.string.seasons)).thenReturn(" Seasons");
        when(mContext.getString(R.string.episodes1)).thenReturn(" Episode");
        when(mContext.getString(R.string.episodes)).thenReturn(" Episodes");
        when(mView.getActivity()).thenReturn(mActivity);


        suppress(method(ShowsPresenter.class,METHOD_SET));
        suppress(method(ShowsPresenter.class,METHOD_REGISTER));

        mShowsInfo = new ArrayList<>();
        mShowsInfo.add(new ShowInfo(mContext,1438,"The Wire","/dg7NuKDjmS6OzuNy33qt8kSkPA1.jpg",5,60,0,1));
        mShowsInfo.add(new ShowInfo(mContext,1396,"Breaking Bad","/1yeVJox3rjo2jBKrrihIMj7uoS9.jpg",6,62,0,0));
        mShowsInfo.add(new ShowInfo(mContext,456,"The Simpsons","/yTZQkSsxUFJZJe67IenRM0AEklc.jpg",30,618,1,0));
        mShowsInfo.add(new ShowInfo(mContext,100,"Mock Show","/yTZQkSsxUFJZJe67IenRM0AEklc.jpg",1,1,1,1));
        // spy cannot be used methods called in the constructor
//        ShowsPresenter spy = PowerMockito.spy(mPresenter);
//
//        try {
//            PowerMockito.doNothing().when(spy,METHOD_SET);
//            PowerMockito.doNothing().when(spy,METHOD_REGISTER);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        mPresenter = new ShowsPresenter(mView,mRepository,mShowsInfo);
    }

    @Test
    public void getTitle(){
        String title = "The Simpsons";
        assertTrue(mPresenter.getTitle(2).equals(title));
    }

    @Test
    public void getSeasons(){
        String seasons = "6 Seasons";
        assertTrue(mPresenter.getNumberOfSeasons(mContext,1).equals(seasons));
        seasons = "1 Season";
        assertTrue(mPresenter.getNumberOfSeasons(mContext,3).equals(seasons));
    }

    @Test
    public void getEpisodes(){
        String episodes = "62 Episodes";
        assertTrue(mPresenter.getNumberOfEpisodes(mContext,1).equals( episodes));
        episodes = "1 Episode";
        assertTrue(mPresenter.getNumberOfEpisodes(mContext,3).equals( episodes));
    }

    @Test
    public void getInProduction(){
        assertTrue(mPresenter.getInProduction(0).equals(finished));
        assertTrue(mPresenter.getInProduction(2).equals(continuing));
    }


}
