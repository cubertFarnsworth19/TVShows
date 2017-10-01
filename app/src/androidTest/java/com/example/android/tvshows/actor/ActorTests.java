package com.example.android.tvshows.actor;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.tvshows.R;
import com.example.android.tvshows.TestShowsApplication;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.ui.actor.ActorActivity;
import com.example.android.tvshows.ui.actor.ActorAdapter;
import com.example.android.tvshows.ui.actor.ActorContract;
import com.example.android.tvshows.ui.actor.ActorModule;
import com.example.android.tvshows.ui.find.ResultsAdapter;
import com.example.android.tvshows.ui.find.ResultsContract;
import com.example.android.tvshows.ui.find.discover.DiscoverActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
public class ActorTests {

    private ActorContract.Presenter mMockPresenter;
    private ActorAdapter mAdapter;
    private ActorActivity mActivity;

    @Rule
    public ActivityTestRule<ActorActivity> mActivityTestRule =
            new ActivityTestRule<>(ActorActivity.class,false,false);

    @Before
    public void setUp(){
        ActorModule mockActorModule = mock(ActorModule.class);

        mMockPresenter = mock(ActorContract.Presenter.class);
        when(mockActorModule.provideActorContractPresenter(any(ApiService.class)))
                .thenReturn(mMockPresenter);

        mAdapter = new ActorAdapter(InstrumentationRegistry.getInstrumentation().getContext(),mMockPresenter);

        when(mockActorModule.provideActorAdapter(any(ActorContract.Presenter.class)))
                .thenReturn(mAdapter);

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        TestShowsApplication app = (TestShowsApplication) instrumentation.getTargetContext().getApplicationContext();

        app.setActorModule(mockActorModule);
    }

    @Test
    public void testDisplayActor(){
        final String name = "Actor Name";
        final String biography = "This is the actor biography";

        for(int i=0;i<3;i++) {
            when(mMockPresenter.getCharacterName(i)).thenReturn("Character " + i);
            when(mMockPresenter.getTVShowTitle(i)).thenReturn("TV Show " + i);
        }
        mActivityTestRule.launchActivity(new Intent());

        mActivity = mActivityTestRule.getActivity();

        verify(mMockPresenter).downloadActorData(any(Context.class));


        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mActivity.setName(name);
                mActivity.setBiography(biography);
                mActivity.displayCredits(3);
            }});

        onView(withId(R.id.actor_name)).check(matches(withText(name)));
        onView(withId(R.id.actor_biography)).check(matches(withText(biography)));

        for(int i=0;i<3;i++) {
            onView(withRecyclerView(R.id.recyclerview_actor).atPositionOnView(i,R.id.character_name)).check(matches(withText("Character " + i)));
            onView(withRecyclerView(R.id.recyclerview_actor).atPositionOnView(i,R.id.tvshow_name)).check(matches(withText("TV Show " + i)));
        }

    }

}
