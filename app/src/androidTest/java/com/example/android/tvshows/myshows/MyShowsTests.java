package com.example.android.tvshows.myshows;

import android.app.Activity;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.v4.app.Fragment;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.tvshows.R;
import com.example.android.tvshows.TestShowsApplication;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.episodes.EpisodesActivity;
import com.example.android.tvshows.ui.myshows.MyShowsActivity;
import com.example.android.tvshows.ui.myshows.current.CurrentAdapter;
import com.example.android.tvshows.ui.myshows.current.CurrentContract;
import com.example.android.tvshows.ui.myshows.current.CurrentFragment;
import com.example.android.tvshows.ui.myshows.current.CurrentModule;
import com.example.android.tvshows.ui.myshows.shows.ShowsAdapter;
import com.example.android.tvshows.ui.myshows.shows.ShowsContract;
import com.example.android.tvshows.ui.myshows.shows.ShowsFragment;
import com.example.android.tvshows.ui.myshows.shows.ShowsModule;
import com.example.android.tvshows.ui.showinfo.ShowInfoActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.tvshows.util.RecyclerViewAction.clickChildViewWithId;
import static com.example.android.tvshows.util.RecyclerViewMatcher.withRecyclerView;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MyShowsTests {

    private ShowsContract.Presenter mMockShowsPresenter;
    private CurrentContract.Presenter mMockCurrentPresenter;
    private ShowsAdapter mShowsAdapter;
    private CurrentAdapter mCurrentAdapter;
    private Picasso mMockPicasso;
    private IdlingResource mIdlingResource;
    private Context mContext;
    private MyShowsActivity mActivity;

    @Rule
    public IntentsTestRule<MyShowsActivity> mActivityTestRule =
            new IntentsTestRule<>(MyShowsActivity.class,false,false);

    @Before
    public void setUp(){

        ShowsModule mockShowsModule = mock(ShowsModule.class);
        CurrentModule mockCurrentModule = mock(CurrentModule.class);

        mMockShowsPresenter = mock(ShowsContract.Presenter.class);
        mMockCurrentPresenter = mock(CurrentContract.Presenter.class);

        mMockPicasso = mock(Picasso.class);
        when(mMockPicasso.load(any(String.class))).thenReturn(mock(RequestCreator.class));

        when(mockShowsModule.providesShowsContractPresenter(any(ShowsRepository.class)))
                .thenReturn(mMockShowsPresenter);
        when(mockCurrentModule.providesCurrentContractPresenter(any(ShowsRepository.class)))
                .thenReturn(mMockCurrentPresenter);

        mContext = InstrumentationRegistry.getInstrumentation().getContext();

        mShowsAdapter = new ShowsAdapter(mContext,mMockShowsPresenter,mMockPicasso);

        when(mockShowsModule.provideShowsAdapter(any(ShowsContract.Presenter.class),any(Picasso.class)))
                .thenReturn(mShowsAdapter);

        mCurrentAdapter = new CurrentAdapter(mContext,mMockCurrentPresenter,mMockPicasso);

        when(mockCurrentModule.provideCurrentAdapter(any(CurrentContract.Presenter.class),any(Picasso.class)))
                .thenReturn(mCurrentAdapter);

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        TestShowsApplication app = (TestShowsApplication) instrumentation.getTargetContext().getApplicationContext();

        app.setShowsModule(mockShowsModule);
        app.setCurrentModule(mockCurrentModule);

    }

    @Test
    public void testDisplayShows(){
        mActivityTestRule.launchActivity(new Intent());
        mActivity = mActivityTestRule.getActivity();

        List<Fragment> fragments = mActivity.getSupportFragmentManager().getFragments();
        final ShowsFragment showsFragment = (ShowsFragment) fragments.get(0);
        final CurrentFragment currentFragment =  (CurrentFragment) fragments.get(1);

        mIdlingResource = mActivity.getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);

        verify(mMockShowsPresenter).loadShowsFromDatabase(mActivity,false,false);

        onView(withId(R.id.btn_filter)).check(matches(isDisplayed()));

        mockShowPresenterMethods();

        testDisplayMyShows(showsFragment);

        onView(withId(R.id.recyclerview_shows))
                .perform(RecyclerViewActions.scrollToPosition(0));

        onView(allOf(withId(R.id.pager),isDisplayed())).perform(swipeLeft());
        mActivity.setNotIdle();
        verify(mMockCurrentPresenter).loadShowsFromDatabase(mActivity);

        mockCurrentPresenterMethods();

        testDisplayCurrentShows(currentFragment);


        onView(allOf(withId(R.id.pager),isDisplayed())).perform(swipeRight());
        mActivity.setNotIdle();

        onView(withId(R.id.btn_filter)).check(matches(isDisplayed()));

    }

    private void mockShowPresenterMethods(){
        for(int i=0;i<6;i++) {
            String inProduction = i%2==0 ? "Continuing":"Finished";
            when(mMockShowsPresenter.getInProduction(i)).thenReturn(inProduction);
            when(mMockShowsPresenter.getTitle(i)).thenReturn("Show " + i);
            when(mMockShowsPresenter.getNumberOfEpisodes(mContext,i))
                    .thenReturn("1"+i+" Episodes");
            when(mMockShowsPresenter.getNumberOfSeasons(mContext,i))
                    .thenReturn("1"+i+" Seasons");
            boolean favorite = i%3==0 ? true:false;
            when(mMockShowsPresenter.isFavorite(i)).thenReturn(favorite);
        }
    }

    private void mockCurrentPresenterMethods(){
        when(mMockCurrentPresenter.getDate(0)).thenReturn("DAY 1");
        when(mMockCurrentPresenter.getNumberOfShowsOnDate(0)).thenReturn(2);
        when(mMockCurrentPresenter.getDate(1)).thenReturn("DAY 2");
        when(mMockCurrentPresenter.getNumberOfShowsOnDate(1)).thenReturn(1);
        when(mMockCurrentPresenter.getShowName(0,0)).thenReturn("Show 1");
        when(mMockCurrentPresenter.getShowName(0,1)).thenReturn("Show 2");
        when(mMockCurrentPresenter.getShowName(1,0)).thenReturn("Show 3");
        when(mMockCurrentPresenter.getEpisodeName(0,0)).thenReturn("Episode 1");
        when(mMockCurrentPresenter.getEpisodeName(0,1)).thenReturn("Episode 2");
        when(mMockCurrentPresenter.getEpisodeName(1,0)).thenReturn("Episode 3");
        when(mMockCurrentPresenter.getShowOverview(0,0)).thenReturn("Episode Description 1");
        when(mMockCurrentPresenter.getShowOverview(0,1)).thenReturn("Episode Description 2");
        when(mMockCurrentPresenter.getShowOverview(1,0)).thenReturn("Episode Description 3");
    }

    private void testDisplayMyShows(final ShowsFragment showsFragment){
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                showsFragment.showsDataLoaded(6);
            }});

        for(int i=0;i<6;i++) {
            onView(withId(R.id.recyclerview_shows))
                    .perform(RecyclerViewActions.scrollToPosition(i));
            onView(withRecyclerView(R.id.recyclerview_shows).atPositionOnView(i, R.id.title))
                    .check(matches(withText("Show " + i)));
            onView(withRecyclerView(R.id.recyclerview_shows).atPositionOnView(i, R.id.show_seasons))
                    .check(matches(withText("1"+i+" Seasons")));
            onView(withRecyclerView(R.id.recyclerview_shows).atPositionOnView(i, R.id.show_episodes))
                    .check(matches(withText("1"+i+" Episodes")));
            String inProduction = i%2==0 ? "Continuing":"Finished";
            onView(withRecyclerView(R.id.recyclerview_shows).atPositionOnView(i, R.id.in_production))
                    .check(matches(withText(inProduction)));

            boolean favorite = i%3==0;

            onView(withId(R.id.recyclerview_shows))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, clickChildViewWithId(R.id.favorite)));

            verify(mMockShowsPresenter).setFavorite(i,!favorite);


            when(mMockShowsPresenter.getIntentForShowInfoActivity(mContext,i))
                    .thenReturn(new Intent(mActivity.getBaseContext(), ShowInfoActivity.class));
            // do nothing when an internal intent is called
            intending(isInternal()).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

            onView(withId(R.id.recyclerview_shows))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, clickChildViewWithId(R.id.show_layout)));

            intended(hasComponent(ShowInfoActivity.class.getName()),times(i+1));
        }
    }

    private void testDisplayCurrentShows(final CurrentFragment currentFragment){


        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                currentFragment.showsDataLoaded(2);
            }});

        onView(withId(R.id.btn_filter)).check(matches(not(isDisplayed())));

        onView(allOf(withId(R.id.recyclerview_current),isDisplayed()))
                .perform(RecyclerViewActions.scrollToPosition(0));
        onView(withRecyclerView(R.id.recyclerview_current).atPositionOnView(0, R.id.shows_date))
                .check(matches(withText("DAY 1")));

        onView(allOf(withId(R.id.recyclerview_current),isDisplayed()))
                .perform(RecyclerViewActions.scrollToPosition(1));
        onView(withRecyclerView(R.id.recyclerview_current).atPositionOnView(1, R.id.shows_date))
                .check(matches(withText("DAY 2")));

        onView(allOf(withRecyclerView(R.id.recyclerview_day_shows).atPositionOnView(0, R.id.title),
                isDescendantOfA(withRecyclerView(R.id.recyclerview_current).atPositionOnView(0, R.id.recyclerview_day_shows))))
                .check(matches(withText("Show 1")));
        onView(allOf(withRecyclerView(R.id.recyclerview_day_shows).atPositionOnView(1, R.id.title),
                isDescendantOfA(withRecyclerView(R.id.recyclerview_current).atPositionOnView(0, R.id.recyclerview_day_shows))))
                .check(matches(withText("Show 2")));
        onView(allOf(withRecyclerView(R.id.recyclerview_day_shows).atPositionOnView(0, R.id.episode_name),
                isDescendantOfA(withRecyclerView(R.id.recyclerview_current).atPositionOnView(0, R.id.recyclerview_day_shows))))
                .check(matches(withText("Episode 1")));
        onView(allOf(withRecyclerView(R.id.recyclerview_day_shows).atPositionOnView(1, R.id.episode_name),
                isDescendantOfA(withRecyclerView(R.id.recyclerview_current).atPositionOnView(0, R.id.recyclerview_day_shows))))
                .check(matches(withText("Episode 2")));
        onView(allOf(withRecyclerView(R.id.recyclerview_day_shows).atPositionOnView(0, R.id.overview),
                isDescendantOfA(withRecyclerView(R.id.recyclerview_current).atPositionOnView(0, R.id.recyclerview_day_shows))))
                .check(matches(withText("Episode Description 1")));
        onView(allOf(withRecyclerView(R.id.recyclerview_day_shows).atPositionOnView(1, R.id.overview),
                isDescendantOfA(withRecyclerView(R.id.recyclerview_current).atPositionOnView(0, R.id.recyclerview_day_shows))))
                .check(matches(withText("Episode Description 2")));

        onView(allOf(withId(R.id.title),isDescendantOfA(withRecyclerView(R.id.recyclerview_current).atPositionOnView(1, R.id.recyclerview_day_shows))))
                .check(matches(withText("Show 3")));;
        onView(allOf(withId(R.id.episode_name),isDescendantOfA(withRecyclerView(R.id.recyclerview_current).atPositionOnView(1, R.id.recyclerview_day_shows))))
                .check(matches(withText("Episode 3")));;
        onView(allOf(withId(R.id.overview),isDescendantOfA(withRecyclerView(R.id.recyclerview_current).atPositionOnView(1, R.id.recyclerview_day_shows))))
                .check(matches(withText("Episode Description 3")));;
    }


}
