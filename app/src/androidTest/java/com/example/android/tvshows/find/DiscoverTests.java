package com.example.android.tvshows.find;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.NoMatchingViewException;
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

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.action.ViewActions.click;

import static android.support.test.espresso.action.ViewActions.typeText;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class DiscoverTests {

    private final String sortBy = "Vote Average";
    private final String minVoteAverage = "6";
    private final String minVoteCount = "50";
    private final String minYear = "2014";
    private final String maxYear = "2017";

    private final String FRAGMENT = "Results Fragment";
    private ResultsContract.Presenter mMockPresenter;
    private ResultsAdapter mMockAdapter;
    ResultsFragment mResultsFragment;

    @Rule
    public ActivityTestRule<DiscoverActivity> mActivityTestRule =
            new ActivityTestRule<>(DiscoverActivity.class,false,false);

    private IdlingResource mIdlingResource;

    @Before
    public void setUp(){

        ResultsModule mockResultsModule = mock(ResultsModule.class);
        mMockPresenter = mock(ResultsContract.Presenter.class);
        mMockAdapter = mock(ResultsAdapter.class);

        when(mockResultsModule.provideResultsContractPresenter(any(ApiService.class),any(ShowsRepository.class)))
                .thenReturn(mMockPresenter);

        when(mockResultsModule.provideResultsAdapter(any(ResultsContract.Presenter.class),any(Picasso.class)))
                .thenReturn(mMockAdapter);

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        TestShowsApplication app = (TestShowsApplication) instrumentation.getTargetContext().getApplicationContext();

        app.setResultsModule(mockResultsModule);
    }

    @Test
    public void testExpandAndFind(){
        mActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.btn_expand)).perform(click());

        onView(withId(R.id.spinner_sort_by)).perform(click());
        onView(withText(sortBy)).perform(click());

        onView(withId(R.id.edit_text_vote_average_min)).perform(typeText(minVoteAverage));
        Espresso.pressBack();

        onView(withId(R.id.edit_text_vote_count_min)).perform(typeText(minVoteCount));
        Espresso.pressBack();

        onView(withId(R.id.layout_include_genres)).perform(click());
        onView(withId(R.id.check_box_crime)).perform(click());
        onView(withId(R.id.check_box_drama)).perform(click());
        onView(withId(R.id.btn_select_genres)).perform(click());

        onView(withId(R.id.layout_exclude_genres)).perform(click());
        onView(withId(R.id.check_box_animation)).perform(click());
        onView(withId(R.id.check_box_sci_fi_fantasy)).perform(click());
        onView(withId(R.id.btn_select_genres)).perform(click());

        onView(withId(R.id.spinner_first_air_date_min)).perform(click());
        onView(withText(minYear)).perform(click());

        onView(withId(R.id.spinner_first_air_date_max)).perform(click());
        onView(withText(maxYear)).perform(click());

        onView(withId(R.id.btn_expand)).perform(click());

        mActivityTestRule.getActivity().getFragmentManager();
        mResultsFragment = (ResultsFragment) mActivityTestRule.getActivity().getSupportFragmentManager().findFragmentByTag(FRAGMENT);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mResultsFragment.removeProgressBar();
            }});

        onView(withId(R.id.btn_find)).perform(click());

       verify(mMockPresenter).makeDiscoverRequest(mActivityTestRule.getActivity(),
               mActivityTestRule.getActivity().getString(R.string.sort_by_vote_average_request),
               "80,18","16,10765",minVoteAverage,minVoteCount,minYear,maxYear);

    }

    @Test
    public void testNoConnection() {
        mActivityTestRule.launchActivity(new Intent());

        mActivityTestRule.getActivity().getFragmentManager();
        mResultsFragment = (ResultsFragment) mActivityTestRule.getActivity().getSupportFragmentManager().findFragmentByTag(FRAGMENT);

        mIdlingResource = mResultsFragment.getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);

        mResultsFragment.setNotIdle();

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mResultsFragment.removeProgressBar();
                mResultsFragment.noConnection();
            }});

        onView(allOf(withId(android.support.design.R.id.snackbar_text),
                withText(mResultsFragment.getResources().getString(R.string.no_connection))))
                .check(matches(isDisplayed()));

    }

    @Test
    public void testNoConnectionRetryNewPage() {
        mActivityTestRule.launchActivity(new Intent());

        mActivityTestRule.getActivity().getFragmentManager();
        mResultsFragment = (ResultsFragment) mActivityTestRule.getActivity().getSupportFragmentManager().findFragmentByTag(FRAGMENT);

        mIdlingResource = mResultsFragment.getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);

        mResultsFragment.setNotIdle();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mResultsFragment.noConnectionRetryNewPage(2);
            }};
        InstrumentationRegistry.getInstrumentation().runOnMainSync(runnable);

        onView(withText(mResultsFragment.getResources().getString(R.string.retry))).perform(click());

        verify(mMockPresenter).getDiscoverPage(mActivityTestRule.getActivity(),2);

    }

    @Test
    public void testNoConnectionWithRetry() {
        mActivityTestRule.launchActivity(new Intent());

        mActivityTestRule.getActivity().getFragmentManager();
        mResultsFragment = (ResultsFragment) mActivityTestRule.getActivity().getSupportFragmentManager().findFragmentByTag(FRAGMENT);

        mIdlingResource = mResultsFragment.getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);

        mResultsFragment.setNotIdle();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mResultsFragment.removeProgressBar();
                mResultsFragment.noConnectionWithRetry(2);
            }};
        InstrumentationRegistry.getInstrumentation().runOnMainSync(runnable);

        onView(withText(mResultsFragment.getResources().getString(R.string.retry))).perform(click());

        verify(mMockPresenter).getRecommendations(mActivityTestRule.getActivity(),2);

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}
