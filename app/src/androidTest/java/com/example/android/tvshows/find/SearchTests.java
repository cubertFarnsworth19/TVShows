package com.example.android.tvshows.find;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.tvshows.R;
import com.example.android.tvshows.ShowsApplication;
import com.example.android.tvshows.TestShowsApplication;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.ui.find.ResultsAdapter;
import com.example.android.tvshows.ui.find.ResultsContract;
import com.example.android.tvshows.ui.find.ResultsFragment;
import com.example.android.tvshows.ui.find.ResultsModule;
import com.example.android.tvshows.ui.find.search.SearchActivity;
import com.squareup.picasso.Picasso;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.pressMenuKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class SearchTests {

    private String searchQuery = "Search";
    private final String FRAGMENT = "Results Fragment";
    private ResultsContract.Presenter mMockPresenter;
    private ResultsAdapter mMockAdapter;
    ResultsFragment mResultsFragment;

    @Rule
    public ActivityTestRule<SearchActivity> mActivityTestRule =
            new ActivityTestRule<SearchActivity>(SearchActivity.class,false,false);

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
    public void testSearch() {
        mActivityTestRule.launchActivity(new Intent());

        mActivityTestRule.getActivity().getFragmentManager();
        mResultsFragment = (ResultsFragment) mActivityTestRule.getActivity().getSupportFragmentManager().findFragmentByTag(FRAGMENT);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mResultsFragment.search(searchQuery);
            }});

        verify(mMockPresenter).search(mActivityTestRule.getActivity(),searchQuery);
    }

}
