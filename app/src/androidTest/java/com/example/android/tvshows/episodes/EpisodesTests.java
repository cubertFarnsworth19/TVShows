package com.example.android.tvshows.episodes;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.tvshows.util.CustomScrollActions;
import com.example.android.tvshows.R;
import com.example.android.tvshows.TestShowsApplication;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.ui.episodes.EpisodeData;
import com.example.android.tvshows.ui.episodes.EpisodesActivity;
import com.example.android.tvshows.ui.episodes.EpisodesContract;
import com.example.android.tvshows.ui.episodes.EpisodesModule;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class EpisodesTests {

    EpisodesContract.Presenter mMockPresenter;
    Intent mIntent;
    String[] mSeasonsName;
    ArrayList<EpisodeData> mEpisodes;
    final int numberOfEpisodes = 4;

    @Mock
    Picasso mMockPicasso;

    EpisodesActivity mActivity;

    @Rule
    public IntentsTestRule<EpisodesActivity> mActivityTestRule =
            new IntentsTestRule<>(EpisodesActivity.class,false,false);

    @Before
    public void setUp(){

        EpisodesModule mockEpisodesModule = mock(EpisodesModule.class);

        mMockPresenter = mock(EpisodesContract.Presenter.class);
        when(mockEpisodesModule.provideEpisodesContractPresenter(any(ShowsRepository.class),any(ApiService.class)))
                .thenReturn(mMockPresenter);

        mMockPicasso = mock(Picasso.class);

        when(mMockPicasso.load(any(String.class))).thenReturn(mock(RequestCreator.class));

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        TestShowsApplication app = (TestShowsApplication) instrumentation.getTargetContext().getApplicationContext();

        app.setPicassoMock(mMockPicasso);
        app.setEpisodesModule(mockEpisodesModule);

    }

    @Before
    public void setIntent(){
        mIntent = new Intent();
        mIntent.putExtra(ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY,1);
        mSeasonsName = new String[3];
        int [] seasonNumbers = new int[3];

        for (int i=0;i<mSeasonsName.length;i++){
            int season = i+1;
            mSeasonsName[i] = "Season " + season;
            seasonNumbers[i] = i+1;
        }

        mIntent.putExtra(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NAME,mSeasonsName);
        mIntent.putExtra(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NUMBER,seasonNumbers);
        mIntent.putExtra(EpisodesActivity.adapterPosition,1);
    }

    @Before
    public void setEpisodesData(){
        mEpisodes = new ArrayList<>(4);

        for (int i=0;i<4;i++) {
            mEpisodes.add(new EpisodeData(1, "Episode Name "+i,
                    "Episode " + i + " Overview. " + "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas suscipit orci eu diam tincidunt bibendum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. In dui nulla, varius ac nulla at, sodales fringilla metus. Vivamus euismod, magna facilisis fringilla porttitor, velit arcu tincidunt neque, at efficitur nibh tortor a dolor. Cras vitae bibendum ex. Mauris facilisis libero eu odio sodales tincidunt. Donec non tincidunt felis. Nam leo lorem, volutpat id interdum nec, lacinia sed libero. Vestibulum metus enim, ornare placerat neque a, condimentum placerat quam. Fusce condimentum ante nec magna volutpat, at varius metus tempus. Nulla facilisi.\n" +
                    "\n" + "Duis vestibulum neque eget erat convallis, vitae egestas nunc pellentesque. Vestibulum ex est, dictum et finibus eget, blandit vitae nisl. Integer a lobortis augue, nec semper ipsum. Fusce dolor lorem, fermentum non ex ut, malesuada porttitor felis. Nullam consectetur ultrices leo et scelerisque. Etiam sed dui ac lorem rutrum ultricies. Curabitur laoreet venenatis justo. Aliquam quis dolor metus. Suspendisse turpis mauris, semper eget auctor ac, elementum sit amet risus. Donec efficitur viverra est, sollicitudin blandit sem maximus faucibus. Phasellus finibus eu augue vitae consectetur.\n" +
                    "\n" + "Cras nulla urna, pharetra id tempus vel, facilisis quis felis. Nunc elementum urna ante, vitae accumsan nulla posuere eget. Pellentesque egestas massa in dui fermentum interdum. Donec in mollis lacus. Vivamus eros arcu, tristique quis urna id, sodales dapibus neque. Curabitur et blandit nibh. Sed lacinia sit amet magna et dignissim. Nunc lobortis sed turpis non tristique.\n" +
                    "\n" + "Nullam ultrices ac tortor nec suscipit. Duis finibus, dolor malesuada consequat auctor, mi leo sagittis nunc, ac rutrum urna dolor in leo. Ut sed tempus elit. Quisque condimentum neque augue, quis aliquet sem vulputate quis. Maecenas neque leo, aliquam a leo in, aliquet mollis lectus. Nulla facilisi. Nullam a rhoncus urna. Vivamus tincidunt quam vitae arcu aliquet, eget vulputate lorem congue. Integer vitae finibus neque.\n" +
                    "\n" + "Vestibulum eleifend diam leo, fringilla rutrum neque mattis eu. Mauris in lectus placerat, ultricies eros id, vulputate est. Quisque ut urna sit amet ipsum euismod semper vitae et eros. Fusce accumsan arcu id turpis lacinia facilisis. Vivamus placerat mi eros, quis rutrum purus mattis nec. Nullam ac lacus eget lacus lacinia porttitor. Proin tincidunt venenatis augue eu tincidunt. Vestibulum gravida auctor tempor. Nunc venenatis sapien sit amet turpis gravida semper. Curabitur varius commodo imperdiet. Maecenas fringilla, nisl vel rhoncus viverra, mi risus sagittis sem, non ornare enim lorem vel magna. Proin porta urna neque, quis viverra eros vestibulum ut. Nullam nec augue sodales, volutpat libero eu, placerat urna. Aliquam et molestie nulla.",
                    "mock_picasso_string", i+1+" OCT 2016", i));
        }
    }

    @Test
    public void testDisplayEpisodes(){

        mActivityTestRule.launchActivity(mIntent);
        mActivity = mActivityTestRule.getActivity();

        mockPresenterMethods();

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mActivity.episodeDataLoaded(numberOfEpisodes);
            }});

        for (int i=0;i<numberOfEpisodes;i++) {
            onView(allOf(isDisplayed(),withId(R.id.episode_name))).check(matches(withText(mEpisodes.get(i).getEpisodeName())));
            onView(allOf(isDisplayed(),withId(R.id.original_air_date))).check(matches(withText(mEpisodes.get(i).getOriginalAirDate())));
            onView(allOf(isDisplayed(),withId(R.id.overview))).check(matches(withText(mEpisodes.get(i).getOverview())));
            onView(allOf(isDisplayed(),withId(R.id.still_photo))).perform(swipeLeft());
        }

        // do nothing when an external intent is called
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

        onView(allOf(withId(R.id.link_imdb),isDescendantOfA(allOf(withId(R.id.episode_layout),isDisplayed()))))
                .perform(CustomScrollActions.nestedScrollTo()).perform(click());
        intended(Matchers.allOf(hasAction(Intent.ACTION_VIEW),hasData(Uri.parse(mActivity.getString(R.string.imdb_tv_show_webpage)+"1"))));

        onView(allOf(withId(R.id.link_tmdb),isDescendantOfA(allOf(withId(R.id.episode_layout),isDisplayed()))))
                .perform(CustomScrollActions.nestedScrollTo()).perform(click());
        intended(Matchers.allOf(hasAction(Intent.ACTION_VIEW),hasData(Uri.parse(mActivity.getString(R.string.tmdb_tv_show_webpage)+"1"))));

        onView(allOf(withId(R.id.link_google_search),isDescendantOfA(allOf(withId(R.id.episode_layout),isDisplayed()))))
                .perform(CustomScrollActions.nestedScrollTo()).perform(click());
        intended(Matchers.allOf(hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse(mActivity.getString(R.string.google_search_webpage)+"TV Show "+mEpisodes.get(3).getEpisodeName()))));

        onView(allOf(withId(R.id.link_youtube_search),isDescendantOfA(allOf(withId(R.id.episode_layout),isDisplayed()))))
                .perform(CustomScrollActions.nestedScrollTo()).perform(click());
        intended(Matchers.allOf(hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse(mActivity.getString(R.string.youtube_search_webpage)+"TV Show "+mEpisodes.get(3).getEpisodeName()))));

    }

    @Test
    public void testChangeSeasons(){
        final int numberOfEpisodes = 4;

        mActivityTestRule.launchActivity(mIntent);
        final EpisodesActivity activity = mActivityTestRule.getActivity();

        for (int i=0;i<numberOfEpisodes;i++) {
            when(mMockPresenter.getEpisodeData(activity, i)).thenReturn(mEpisodes.get(i));
        }

        when(mMockPresenter.getSeasonNames()).thenReturn(mSeasonsName);

        when(mMockPresenter.getIntentForNewEpisodeActivity(
                activity.getBaseContext(),0)).thenReturn(new Intent(activity.getBaseContext(),EpisodesActivity.class));

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                activity.episodeDataLoaded(numberOfEpisodes);
            }});

        // do nothing when an internal intent is called
        intending(isInternal()).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

        onView(withId(R.id.spinner_seasons)).perform(click());
        onView(withText("Season 1")).perform(click());

        intended(hasComponent(EpisodesActivity.class.getName()));
    }

    private void mockPresenterMethods(){

        for (int i=0;i<numberOfEpisodes;i++) {
            when(mMockPresenter.getEpisodeData(mActivity, i)).thenReturn(mEpisodes.get(i));
        }

        when(mMockPresenter.getSeasonNames()).thenReturn(mSeasonsName);

        when(mMockPresenter.getImdbId()).thenReturn("1");
        when(mMockPresenter.getTitle()).thenReturn("TV Show");

        when(mMockPresenter.downloadExternalIds(4)).thenReturn(true);

    }


}
