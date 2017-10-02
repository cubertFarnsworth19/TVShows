package com.example.android.tvshows.myshows;

import android.support.test.espresso.contrib.RecyclerViewActions;
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
import com.example.android.tvshows.ui.find.discover.DiscoverActivity;
import com.example.android.tvshows.ui.myshows.MyShowsActivity;
import com.example.android.tvshows.ui.myshows.current.CurrentAdapter;
import com.example.android.tvshows.ui.myshows.current.CurrentContract;
import com.example.android.tvshows.ui.myshows.current.CurrentFragment;
import com.example.android.tvshows.ui.myshows.current.CurrentModule;
import com.example.android.tvshows.ui.myshows.shows.ShowsAdapter;
import com.example.android.tvshows.ui.myshows.shows.ShowsContract;
import com.example.android.tvshows.ui.myshows.shows.ShowsFragment;
import com.example.android.tvshows.ui.myshows.shows.ShowsModule;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.tvshows.RecyclerViewMatcher.withRecyclerView;
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

    @Rule
    public ActivityTestRule<MyShowsActivity> mActivityTestRule =
            new ActivityTestRule<>(MyShowsActivity.class,false,false);

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

        Context context = InstrumentationRegistry.getInstrumentation().getContext();

        mShowsAdapter = new ShowsAdapter(context,mMockShowsPresenter,mMockPicasso);

        when(mockShowsModule.provideShowsAdapter(any(ShowsContract.Presenter.class),any(Picasso.class)))
                .thenReturn(mShowsAdapter);

        mCurrentAdapter = new CurrentAdapter(context,mMockCurrentPresenter,mMockPicasso);

        when(mockCurrentModule.provideCurrentAdapter(any(CurrentContract.Presenter.class),any(Picasso.class)))
                .thenReturn(mCurrentAdapter);

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        TestShowsApplication app = (TestShowsApplication) instrumentation.getTargetContext().getApplicationContext();

        app.setShowsModule(mockShowsModule);
        app.setCurrentModule(mockCurrentModule);

    }

    @Test
    public void testDisplayShows() throws InterruptedException {
        mActivityTestRule.launchActivity(new Intent());
        MyShowsActivity activity = mActivityTestRule.getActivity();

        List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();
        final ShowsFragment showsFragment = (ShowsFragment) fragments.get(0);
        CurrentFragment currentFragment =  (CurrentFragment) fragments.get(1);

        verify(mMockShowsPresenter).loadShowsFromDatabase(activity,false,false);


        for(int i=0;i<6;i++) {
            String inProduction = i%2==0 ? "Continuing":"Finished";
            when(mMockShowsPresenter.getInProduction(i)).thenReturn(inProduction);
            when(mMockShowsPresenter.getTitle(i)).thenReturn("Show " + i);
            when(mMockShowsPresenter.getNumberOfEpisodes(InstrumentationRegistry.getInstrumentation().getContext(),i))
                    .thenReturn("1"+i+" Episodes");
            when(mMockShowsPresenter.getNumberOfSeasons(InstrumentationRegistry.getInstrumentation().getContext(),i))
                    .thenReturn("1"+i+" Seasons");
            boolean favorite = i%3==0 ? true:false;
            when(mMockShowsPresenter.isFavorite(i)).thenReturn(favorite);
        }

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

        }
        Thread.sleep(3000);
    }




}
