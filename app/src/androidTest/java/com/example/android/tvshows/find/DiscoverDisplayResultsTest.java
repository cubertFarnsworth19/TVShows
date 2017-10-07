package com.example.android.tvshows.find;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.tvshows.R;
import com.example.android.tvshows.TestShowsApplication;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.ui.find.ResultsAdapter;
import com.example.android.tvshows.ui.find.ResultsContract;
import com.example.android.tvshows.ui.find.ResultsFragment;
import com.example.android.tvshows.ui.find.ResultsModule;
import com.example.android.tvshows.ui.find.discover.DiscoverActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.tvshows.util.RecyclerViewAction.clickChildViewWithId;
import static com.example.android.tvshows.util.RecyclerViewMatcher.withRecyclerView;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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

        mAdapter = new ResultsAdapter(context,mMockPresenter,mMockPicasso);

        when(mockResultsModule.provideResultsAdapter(any(ResultsContract.Presenter.class),any(Picasso.class)))
                .thenReturn(mAdapter);

        when(mMockPicasso.load(any(String.class))).thenReturn(mock(RequestCreator.class));

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        TestShowsApplication app = (TestShowsApplication) instrumentation.getTargetContext().getApplicationContext();

        app.setResultsModule(mockResultsModule);
    }

    @Test
    public void testDisplayResults(){
        mActivityTestRule.launchActivity(new Intent());

        setMockPresenterMethods();

        mActivityTestRule.getActivity().getFragmentManager();
        mResultsFragment = (ResultsFragment) mActivityTestRule.getActivity().getSupportFragmentManager().findFragmentByTag(FRAGMENT);

        testDisplayInitialMethods();

        testScrollAndDisplayMethods();

    }

    private void setMockPresenterMethods(){
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
    }

    private void testDisplayInitialMethods(){
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mResultsFragment.setResultsAdapter(20);
            }});

        onView(withId(R.id.recyclerview_results))
                .perform(RecyclerViewActions.actionOnItemAtPosition(14, clickChildViewWithId(R.id.show_more_details)));

        verify(mMockPresenter).openMoreDetailsDialog(InstrumentationRegistry.getInstrumentation().getContext(),14);

        onView(withId(R.id.recyclerview_results))
                .perform(RecyclerViewActions.actionOnItemAtPosition(15, clickChildViewWithId(R.id.button_add)));

        verify(mMockPresenter).saveSelectedToDatabase(InstrumentationRegistry.getInstrumentation().getContext(),15);

        onView(withRecyclerView(R.id.recyclerview_results).atPositionOnView(15,R.id.button_add)).check(matches(not(isDisplayed())));
    }

    private void testScrollAndDisplayMethods(){
        onView(withId(R.id.recyclerview_results))
                .perform(RecyclerViewActions.scrollToPosition(19));

        verify(mMockPresenter).getDiscoverPage(mActivityTestRule.getActivity(),2);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mResultsFragment.setResultsAdapter(40);
            }});

        onView(withId(R.id.recyclerview_results))
                .perform(RecyclerViewActions.scrollToPosition(39));

        onView(withRecyclerView(R.id.recyclerview_results).atPositionOnView(39,R.id.title)).check(matches(withText("Name 39")));
        onView(withRecyclerView(R.id.recyclerview_results).atPositionOnView(39,R.id.start_year)).check(matches(withText("2039")));
        onView(withRecyclerView(R.id.recyclerview_results).atPositionOnView(39,R.id.user_score)).check(matches(withText("7.2")));

    }

}
