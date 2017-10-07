package com.example.android.tvshows.updates;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.Pair;

import com.example.android.tvshows.R;
import com.example.android.tvshows.TestShowsApplication;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.updates.UpdatesActivity;
import com.example.android.tvshows.ui.updates.UpdatesAdapter;
import com.example.android.tvshows.ui.updates.UpdatesContract;
import com.example.android.tvshows.ui.updates.UpdatesFragment;
import com.example.android.tvshows.ui.updates.UpdatesModule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.tvshows.util.RecyclerViewMatcher.withRecyclerView;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class UpdatesTest {

    private UpdatesContract.Presenter mMockUpdatesPresenter;
    private UpdatesAdapter mUpdatesAdapter;
    private Context mContext;
    private IdlingResource mIdlingResource;
    private UpdatesFragment mFragment;

    @Rule
    public ActivityTestRule<UpdatesActivity> mActivityTestRule =
            new ActivityTestRule<>(UpdatesActivity.class,false,false);

    @Before
    public void setUp(){
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        UpdatesModule mockUpdatesModule = mock(UpdatesModule.class);

        mMockUpdatesPresenter = mock(UpdatesContract.Presenter.class);
        when(mockUpdatesModule.providesUupdatesContractPresenter(any(ShowsRepository.class)))
                .thenReturn(mMockUpdatesPresenter);

        mUpdatesAdapter = new UpdatesAdapter(mContext,mMockUpdatesPresenter);
        when(mockUpdatesModule.provideUpdatesAdapter(mMockUpdatesPresenter))
                .thenReturn(mUpdatesAdapter);

        mIdlingResource = mUpdatesAdapter.getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        TestShowsApplication app = (TestShowsApplication) instrumentation.getTargetContext().getApplicationContext();

        app.setUpdatesModule(mockUpdatesModule);
    }

    @Test
    public void testDisplayUpdates() {
        mActivityTestRule.launchActivity(new Intent());
        mFragment = (UpdatesFragment) mActivityTestRule.getActivity().getSupportFragmentManager().getFragments().get(0);
        mockPresenterMethods();

        checkDataLoaded();
    }

    @Test
    public void testCheckBoxes() {
        mActivityTestRule.launchActivity(new Intent());
        mFragment = (UpdatesFragment) mActivityTestRule.getActivity().getSupportFragmentManager().getFragments().get(0);
        mockPresenterMethods();

        checkAll();

    }

    private void mockPresenterMethods(){

        for(int i=1;i<6;i++) {
            when(mMockUpdatesPresenter.getShowName(i-1)).thenReturn("Show Name " + i);
            when(mMockUpdatesPresenter.getLastUpdate(i-1)).thenReturn(i+" OCT 2016");
            when(mMockUpdatesPresenter.getShowId(i-1)).thenReturn(i);
            when(mMockUpdatesPresenter.getNumberOfSeasons(i-1)).thenReturn(3);
            for(int j=1;j<4;j++) {
                when(mMockUpdatesPresenter.getSeasonName(i,j-1)).thenReturn("Season "+ j);
            }
        }
    }

    private void checkDataLoaded(){
        verify(mMockUpdatesPresenter).loadShowsFromDatabase(false);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mFragment.showsDataLoaded(5);
            }});

        for(int i=1;i<6;i++) {
            onView(withRecyclerView(R.id.recyclerview_updates_detail).atPositionOnView(i-1, R.id.show_name))
                    .check(matches(withText("Show Name " + i)));
            onView(withRecyclerView(R.id.recyclerview_updates_detail).atPositionOnView(i-1, R.id.last_update))
                    .check(matches(withText(i+" OCT 2016")));
        }

        onView(withId(R.id.recyclerview_updates_detail))
                .perform(RecyclerViewActions.scrollToPosition(0));
        onView(withRecyclerView(R.id.recyclerview_updates_detail).atPositionOnView(0, R.id.button_individual))
                .perform(click());

        onView(withRecyclerView(R.id.recyclerview_individual).atPositionOnView(0, R.id.season_name))
                .check(matches(withText(mContext.getString(R.string.details_update))));

        for(int j=1;j<4;j++) {
            onView(withRecyclerView(R.id.recyclerview_individual).atPositionOnView(j, R.id.season_name))
                    .check(matches(withText("Season "+ j)));
        }
    }

    private void checkAll(){

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mFragment.showsDataLoaded(5);
            }});

        onView(withId(R.id.recyclerview_updates_detail))
                .perform(RecyclerViewActions.scrollToPosition(0));

        onView(withRecyclerView(R.id.recyclerview_updates_detail).atPositionOnView(0, R.id.check_box_select_all))
                .perform(click());

        onView(withRecyclerView(R.id.recyclerview_updates_detail).atPositionOnView(0, R.id.button_individual))
                .perform(click());

        for(int j=1;j<4;j++) {
            onView(withRecyclerView(R.id.recyclerview_individual).atPositionOnView(j, R.id.check_box_update))
                    .check(matches(isChecked()));
        }

        onView(withId(R.id.btn_update)).perform(click());

        ArrayList<Pair<Boolean,ArrayList<Boolean>>> allChecked = new ArrayList<>();

        for(int i=0;i<5;i++){
            ArrayList<Boolean> checked = new ArrayList<>();

            for(int j=0;j<3;j++) {
                if(i==0) checked.add(true);
                else checked.add(false);
            }

            Pair<Boolean,ArrayList<Boolean>> pair = new Pair<>(i==0,checked);
            allChecked.add(pair);
        }

        verify(mMockUpdatesPresenter).makeUpdatesRequest(mActivityTestRule.getActivity(),allChecked);
    }

}
