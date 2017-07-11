package com.example.android.tvshows.myshows;


import android.content.Context;

import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.myshows.current.CurrentContract;
import com.example.android.tvshows.ui.myshows.current.CurrentDatabaseLoad;
import com.example.android.tvshows.ui.myshows.current.CurrentInfo;
import com.example.android.tvshows.ui.myshows.current.CurrentPresenter;
import com.example.android.tvshows.ui.myshows.current.ShowDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CurrentPresenter.class)
public class CurrentUnitTest {

    @Mock
    ShowsRepository mRepository;

    @Mock
    CurrentContract.View mView;

    CurrentPresenter mPresenter;

    @Mock
    Context mContext;

    String posterPathStart = "http://image.tmdb.org/t/p/w300";

    private static final String METHOD_REGISTER = "registerReceivers";

    @Before
    public void init(){

        MockitoAnnotations.initMocks(this);

        suppress(method(CurrentPresenter.class,METHOD_REGISTER));
        mPresenter = new CurrentPresenter(mView,mRepository,1);

        when(mContext.getString(R.string.poster_path)).thenReturn(posterPathStart);

        ArrayList<CurrentDatabaseLoad> currentEpisodes = getMockCurrentEpisodes();
        mPresenter.setCurrent(currentEpisodes,mContext);
        mPresenter.setDates(currentEpisodes);

    }

    @Test
    public void getFromRepository(){



        ArrayList<CurrentInfo> currentInfo = new ArrayList<>();

        currentInfo.add(new CurrentInfo("Show 1","S 1 Ep 1","s1e1 overview",posterPathStart+"/s1e1"));
        currentInfo.add(new CurrentInfo("Show 1","S 1 Ep 2","s1e2 overview",posterPathStart+"/s1e2"));
        currentInfo.add(new CurrentInfo("Show 2","S 2 Ep 1","s2e1 overview",posterPathStart+"/s2e1"));
        currentInfo.add(new CurrentInfo("Show 3","S 3 Ep 1","s3e1 overview",posterPathStart+"/s3e1"));
        currentInfo.add(new CurrentInfo("Show 2","S 2 Ep 2","s2e2 overview",posterPathStart+"/s2e2"));
        currentInfo.add(new CurrentInfo("Show 3","S 3 Ep 2","s3e2 overview",posterPathStart+"/s3e2"));

        assertTrue(mPresenter.getCurrentInfo().equals(currentInfo));

        ArrayList<ShowDate> dates = new ArrayList<>();

        dates.add(new ShowDate("10 JUN 2017",0));
        dates.add(new ShowDate("11 JUN 2017",1));
        dates.get(1).addShow();
        dates.get(1).addShow();
        dates.add(new ShowDate("14 JUN 2017",4));
        dates.get(2).addShow();

        assertTrue(mPresenter.getDates().equals(dates));

    }

    @Test
    public void getDataForView(){

        assertTrue(mPresenter.getDate(2).equals("14 JUN 2017"));
        assertTrue(mPresenter.getShowName(1,1).equals("Show 2"));
        assertTrue(mPresenter.getEpisodeName(1,2).equals("S 3 Ep 1"));
        assertTrue(mPresenter.getShowPosterUrl(2,1).equals(posterPathStart+"/s3e2"));
    }

    private ArrayList<CurrentDatabaseLoad> getMockCurrentEpisodes() {

        ArrayList<CurrentDatabaseLoad> currentEpisodes = new ArrayList<>();

        currentEpisodes.add(new CurrentDatabaseLoad("Show 1","S 1 Ep 1","s1e1 overview","/s1e1",10,6,2017));
        currentEpisodes.add(new CurrentDatabaseLoad("Show 1","S 1 Ep 2","s1e2 overview","/s1e2",11,6,2017));
        currentEpisodes.add(new CurrentDatabaseLoad("Show 2","S 2 Ep 1","s2e1 overview","/s2e1",11,6,2017));
        currentEpisodes.add(new CurrentDatabaseLoad("Show 3","S 3 Ep 1","s3e1 overview","/s3e1",11,6,2017));
        currentEpisodes.add(new CurrentDatabaseLoad("Show 2","S 2 Ep 2","s2e2 overview","/s2e2",14,6,2017));
        currentEpisodes.add(new CurrentDatabaseLoad("Show 3","S 3 Ep 2","s3e2 overview","/s3e2",14,6,2017));

        return currentEpisodes;

    }


}
