package com.example.android.tvshows.actor;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.tvshows.R;
import com.example.android.tvshows.TestShowsApplication;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.ui.actor.ActorActivity;
import com.example.android.tvshows.ui.actor.ActorAdapter;
import com.example.android.tvshows.ui.actor.ActorContract;
import com.example.android.tvshows.ui.actor.ActorModule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.tvshows.util.RecyclerViewMatcher.withRecyclerView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
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
    public IntentsTestRule<ActorActivity> mActivityTestRule =
            new IntentsTestRule<>(ActorActivity.class,false,false);

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

        mockPresenterMethods();

        mActivityTestRule.launchActivity(new Intent());

        mActivity = mActivityTestRule.getActivity();

        verify(mMockPresenter).downloadActorData(any(Context.class));

        displayActorDetails();

    }

    @Test
    public void launch_imdb(){
        mockPresenterMethods();

        mActivityTestRule.launchActivity(new Intent());

        // do nothing when an external intent is called
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

        onView(withId(R.id.action_links)).perform(click());
        onView(withText(R.string.link_imdb)).inRoot(isPlatformPopup()).perform(click());

        intended(allOf(hasAction(Intent.ACTION_VIEW),hasData(Uri.parse("http://www.imdb.com/name/" + "1" + "/?ref_=tt_cl_t4"))));
    }

    private void mockPresenterMethods(){
        for(int i=0;i<3;i++) {
            when(mMockPresenter.getCharacterName(i)).thenReturn("Character " + i);
            when(mMockPresenter.getTVShowTitle(i)).thenReturn("TV Show " + i);
        }

        when(mMockPresenter.getActorIMDBId()).thenReturn("1");
    }

    private void displayActorDetails(){
        final String name = "Actor Name";
        final String biography = "This is the actor biography";

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


    private void rotateDevice(){

    }

}
