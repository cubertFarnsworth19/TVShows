package com.example.android.tvshows.find;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.view.menu.MenuView;
import android.widget.ImageView;

import com.example.android.tvshows.ContextModule;
import com.example.android.tvshows.R;
import com.example.android.tvshows.TestShowsApplication;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.ui.find.ResultsAdapter;
import com.example.android.tvshows.ui.find.ResultsContract;
import com.example.android.tvshows.ui.find.ResultsFragment;
import com.example.android.tvshows.ui.find.ResultsModule;
import com.example.android.tvshows.ui.find.discover.DiscoverActivity;
import com.example.android.tvshows.util.Utility;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class DiscoverDisplayResultsTest {

    private ResultsContract.Presenter mMockPresenter;
    private ResultsAdapter mAdapter;
    private Picasso mMockPicasso;
    ResultsFragment mResultsFragment;
    private final String FRAGMENT = "Results Fragment";

    @Rule
    public ActivityTestRule<DiscoverActivity> mActivityTestRule =
            new ActivityTestRule<>(DiscoverActivity.class,false,false);

    @Before
    public void setUp(){

        ResultsModule mockResultsModule = mock(ResultsModule.class);
        mMockPresenter = mock(ResultsContract.Presenter.class);
        mMockPicasso = mock(Picasso.class);

        when(mockResultsModule.provideResultsContractPresenter(any(ApiService.class),any(ShowsRepository.class)))
                .thenReturn(mMockPresenter);
        Context context = InstrumentationRegistry.getInstrumentation().getContext();


//       mMockPicasso = new Picasso.Builder(context).build();

        mAdapter = new ResultsAdapter(context,mMockPresenter,mMockPicasso);

        when(mockResultsModule.provideResultsContractPresenter(any(ApiService.class),any(ShowsRepository.class)))
                .thenReturn(mMockPresenter);

        when(mockResultsModule.provideResultsAdapter(any(ResultsContract.Presenter.class),any(Picasso.class)))
                .thenReturn(mAdapter);

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        TestShowsApplication app = (TestShowsApplication) instrumentation.getTargetContext().getApplicationContext();

        app.setResultsModule(mockResultsModule);
    }

    @Test
    public void testDisplayResults() throws InterruptedException {
        mActivityTestRule.launchActivity(new Intent());

        for(int i=0;i<40;i++) {

            when(mMockPresenter.showAddButton(i)).thenReturn(true);
            when(mMockPresenter.getName(i)).thenReturn("Name " + i);
            when(mMockPresenter.getFirstAirDate(i)).thenReturn("20"+i);
            when(mMockPresenter.getVoteAverage(i)).thenReturn("7.2");
            when(mMockPresenter.getVoteAverageBackgroundColor(InstrumentationRegistry.getInstrumentation().getContext(), i)).
                    thenReturn(0xffC6FF00);
            when(mMockPresenter.getVoteAverageTextColor(InstrumentationRegistry.getInstrumentation().getContext(), i)).
                    thenReturn(0xff000000);
            when(mMockPresenter.getTmdbId(i)).thenReturn(i);

        }
        when(mMockPicasso.load(any(String.class))).thenReturn(mock(RequestCreator.class));

        mActivityTestRule.getActivity().getFragmentManager();
        mResultsFragment = (ResultsFragment) mActivityTestRule.getActivity().getSupportFragmentManager().findFragmentByTag(FRAGMENT);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mResultsFragment.setResultsAdapter(20);
            }});


        Thread.sleep(3000);
    }

}
