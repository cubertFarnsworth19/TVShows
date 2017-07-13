package com.example.android.tvshows.updates;


import android.icu.lang.UProperty;

import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.updates.SeasonForUpdate;
import com.example.android.tvshows.ui.updates.TVShow;
import com.example.android.tvshows.ui.updates.UpdatesComponent;
import com.example.android.tvshows.ui.updates.UpdatesContract;
import com.example.android.tvshows.ui.updates.UpdatesPresenter;

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
@PrepareForTest(UpdatesPresenter.class)
public class UpdatesUnitTest {

    @Mock
    ShowsRepository mRepository;

    @Mock
    UpdatesContract.View mView;

    UpdatesPresenter mPresenter;

    private static final String METHOD_DISPLAY = "display";
    private static final String METHOD_REGISTER = "registerReceivers";

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);

        suppress(method(UpdatesPresenter.class,METHOD_REGISTER));
        suppress(method(UpdatesPresenter.class,METHOD_DISPLAY));

        mPresenter = new UpdatesPresenter(mView,mRepository);

        when(mRepository.getAllShowsUpdate()).thenReturn(getMockTvShows());
        when(mRepository.getAllSeasons()).thenReturn(getMockSeasons());

        mPresenter.loadShowsFromDatabase(false);

    }

    private ArrayList<TVShow> getMockTvShows(){
        ArrayList<TVShow> tvShows = new ArrayList<>();

        tvShows.add(new TVShow(1,"Show 1",21,6,2017));
        tvShows.add(new TVShow(2,"Show 2",21,6,2017));
        tvShows.add(new TVShow(3,"Show 3",21,6,2017));
        tvShows.add(new TVShow(4,"Show 4",21,6,2017));

        return tvShows;
    }

    private ArrayList<SeasonForUpdate> getMockSeasons(){
        ArrayList<SeasonForUpdate> seasonsForUpdate = new ArrayList<>();

        seasonsForUpdate.add(new SeasonForUpdate(1,"Season 1",1,22,6,2017));
        seasonsForUpdate.add(new SeasonForUpdate(1,"Season 2",1,22,6,2017));
        seasonsForUpdate.add(new SeasonForUpdate(1,"Season 3",1,22,6,2017));

        seasonsForUpdate.add(new SeasonForUpdate(2,"Season 1",1,22,6,2017));
        seasonsForUpdate.add(new SeasonForUpdate(2,"Season 2",1,22,6,2017));
        seasonsForUpdate.add(new SeasonForUpdate(2,"Season 3",1,26,6,2017));

        seasonsForUpdate.add(new SeasonForUpdate(3,"Season 1",1,22,6,2017));
        seasonsForUpdate.add(new SeasonForUpdate(3,"Season 2",1,24,6,2017));
        seasonsForUpdate.add(new SeasonForUpdate(3,"Season 3",1,25,6,2017));

        seasonsForUpdate.add(new SeasonForUpdate(4,"Season 1",1,22,6,2017));
        seasonsForUpdate.add(new SeasonForUpdate(4,"Season 2",1,22,6,2017));


        return seasonsForUpdate;
    }

    @Test
    public void numberOfSeasons(){

        assertTrue(mPresenter.getNumberOfSeasons(0)==3);
        assertTrue(mPresenter.getNumberOfSeasons(1)==3);
        assertTrue(mPresenter.getNumberOfSeasons(2)==3);
        assertTrue(mPresenter.getNumberOfSeasons(3)==2);
    }


}
