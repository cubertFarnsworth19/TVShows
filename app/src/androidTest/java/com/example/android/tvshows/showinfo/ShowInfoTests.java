package com.example.android.tvshows.showinfo;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import com.example.android.tvshows.ui.actor.ActorActivity;
import com.example.android.tvshows.ui.episodes.EpisodesActivity;
import com.example.android.tvshows.util.CustomScrollActions;
import com.example.android.tvshows.R;
import com.example.android.tvshows.TestShowsApplication;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.ui.showinfo.ShowInfoActivity;
import com.example.android.tvshows.ui.showinfo.cast.CastAdapter;
import com.example.android.tvshows.ui.showinfo.cast.CastContract;
import com.example.android.tvshows.ui.showinfo.cast.CastFragment;
import com.example.android.tvshows.ui.showinfo.cast.CastModule;
import com.example.android.tvshows.ui.showinfo.details.CreatorAdapter;
import com.example.android.tvshows.ui.showinfo.details.DetailsContract;
import com.example.android.tvshows.ui.showinfo.details.DetailsFragment;
import com.example.android.tvshows.ui.showinfo.details.DetailsModule;
import com.example.android.tvshows.ui.showinfo.seasons.SeasonsAdapter;
import com.example.android.tvshows.ui.showinfo.seasons.SeasonsContract;
import com.example.android.tvshows.ui.showinfo.seasons.SeasonsFragment;
import com.example.android.tvshows.ui.showinfo.seasons.SeasonsModule;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
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
public class ShowInfoTests {

    private DetailsContract.Presenter mMockDetailsPresenter;
    private SeasonsContract.Presenter mMockSeasonsPresenter;
    private CastContract.Presenter mMockCastPresenter;
    private Picasso mMockPicasso;
    private CreatorAdapter mCreatorAdapter;
    private SeasonsAdapter mSeasonsAdapter;
    private CastAdapter mCastAdapter;
    private Context mContext;
    private ShowInfoActivity mActivity;
    private final String showName = "Show Name";
    private final String showOverview = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas suscipit orci eu diam tincidunt bibendum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. In dui nulla, varius ac nulla at, sodales fringilla metus. Vivamus euismod, magna facilisis fringilla porttitor, velit arcu tincidunt neque, at efficitur nibh tortor a dolor. Cras vitae bibendum ex. Mauris facilisis libero eu odio sodales tincidunt. Donec non tincidunt felis. Nam leo lorem, volutpat id interdum nec, lacinia sed libero. Vestibulum metus enim, ornare placerat neque a, condimentum placerat quam. Fusce condimentum ante nec magna volutpat, at varius metus tempus. Nulla facilisi.\n" +
            "\n" + "Duis vestibulum neque eget erat convallis, vitae egestas nunc pellentesque. Vestibulum ex est, dictum et finibus eget, blandit vitae nisl. Integer a lobortis augue, nec semper ipsum. Fusce dolor lorem, fermentum non ex ut, malesuada porttitor felis. Nullam consectetur ultrices leo et scelerisque. Etiam sed dui ac lorem rutrum ultricies. Curabitur laoreet venenatis justo. Aliquam quis dolor metus. Suspendisse turpis mauris, semper eget auctor ac, elementum sit amet risus. Donec efficitur viverra est, sollicitudin blandit sem maximus faucibus. Phasellus finibus eu augue vitae consectetur.\n" +
            "\n" + "Cras nulla urna, pharetra id tempus vel, facilisis quis felis. Nunc elementum urna ante, vitae accumsan nulla posuere eget. Pellentesque egestas massa in dui fermentum interdum. Donec in mollis lacus. Vivamus eros arcu, tristique quis urna id, sodales dapibus neque. Curabitur et blandit nibh. Sed lacinia sit amet magna et dignissim. Nunc lobortis sed turpis non tristique.\n" +
            "\n" + "Nullam ultrices ac tortor nec suscipit. Duis finibus, dolor malesuada consequat auctor, mi leo sagittis nunc, ac rutrum urna dolor in leo. Ut sed tempus elit. Quisque condimentum neque augue, quis aliquet sem vulputate quis. Maecenas neque leo, aliquam a leo in, aliquet mollis lectus. Nulla facilisi. Nullam a rhoncus urna. Vivamus tincidunt quam vitae arcu aliquet, eget vulputate lorem congue. Integer vitae finibus neque.\n" +
            "\n" + "Vestibulum eleifend diam leo, fringilla rutrum neque mattis eu. Mauris in lectus placerat, ultricies eros id, vulputate est. Quisque ut urna sit amet ipsum euismod semper vitae et eros. Fusce accumsan arcu id turpis lacinia facilisis. Vivamus placerat mi eros, quis rutrum purus mattis nec. Nullam ac lacus eget lacus lacinia porttitor. Proin tincidunt venenatis augue eu tincidunt. Vestibulum gravida auctor tempor. Nunc venenatis sapien sit amet turpis gravida semper. Curabitur varius commodo imperdiet. Maecenas fringilla, nisl vel rhoncus viverra, mi risus sagittis sem, non ornare enim lorem vel magna. Proin porta urna neque, quis viverra eros vestibulum ut. Nullam nec augue sodales, volutpat libero eu, placerat urna. Aliquam et molestie nulla.";



    @Rule
    public IntentsTestRule<ShowInfoActivity> mActivityTestRule =
            new IntentsTestRule<>(ShowInfoActivity.class,false,false);

    @Before
    public void setUp(){

        mContext = InstrumentationRegistry.getInstrumentation().getContext();

        DetailsModule mockDetailsModule = mock(DetailsModule.class);
        SeasonsModule mockSeasonsModule = mock(SeasonsModule.class);
        CastModule mockCastModule = mock(CastModule.class);

        mMockPicasso = mock(Picasso.class);
        when(mMockPicasso.load(any(String.class))).thenReturn(mock(RequestCreator.class));

        mMockDetailsPresenter = mock(DetailsContract.Presenter.class);
        when(mockDetailsModule.providesDetailsContractPresenter(any(ShowsRepository.class),any(ApiService.class)))
                .thenReturn(mMockDetailsPresenter);

        mMockSeasonsPresenter = mock(SeasonsContract.Presenter.class);
        when(mockSeasonsModule.providesSeasonsContractPresenter(any(ShowsRepository.class)))
                .thenReturn(mMockSeasonsPresenter);

        mMockCastPresenter = mock(CastContract.Presenter.class);
        when(mockCastModule.providesCastContractPresenter(any(ShowsRepository.class)))
                .thenReturn(mMockCastPresenter);

        mCreatorAdapter = new CreatorAdapter(mMockDetailsPresenter);
        when(mockDetailsModule.provideCreatorAdapter(mMockDetailsPresenter))
                .thenReturn(mCreatorAdapter);

        mSeasonsAdapter = new SeasonsAdapter(mContext,mMockSeasonsPresenter,mMockPicasso);
        when(mockSeasonsModule.provideSeasonsAdapter(mMockSeasonsPresenter,mMockPicasso))
                .thenReturn(mSeasonsAdapter);

        mCastAdapter = new CastAdapter(mContext,mMockCastPresenter,mMockPicasso);
        when(mockCastModule.provideCastAdapter(mMockCastPresenter,mMockPicasso))
                .thenReturn(mCastAdapter);

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        TestShowsApplication app = (TestShowsApplication) instrumentation.getTargetContext().getApplicationContext();

        app.setDetailsModule(mockDetailsModule);
        app.setSeasonsModule(mockSeasonsModule);
        app.setCastModule(mockCastModule);
        app.setPicassoMock(mMockPicasso);
    }

    @Test
    public void testDisplayShowInfo() {
        mActivityTestRule.launchActivity(getIntent());
        mActivity = mActivityTestRule.getActivity();

        verify(mMockDetailsPresenter).loadShowDetails(mActivity);

        mockDetailsPresenterMethods();

        testDisplayDetails(0);

        onView(allOf(withId(R.id.pager),isDisplayed())).perform(swipeLeft());

        mockSeasonsPresenterMethods();

        testDisplaySeasons(1);

        onView(allOf(withId(R.id.pager),isDisplayed())).perform(swipeLeft());

        mockCastPresenterMethods();

        testDisplayCast(2);

    }

    private Intent getIntent(){
        Intent intent = new Intent();
        intent.putExtra(ShowsDbContract.ShowsEntry.COLUMN_NAME,showName);
        return intent;
    }

    private void mockDetailsPresenterMethods(){
        when(mMockDetailsPresenter.getCreatorName(0)).thenReturn("Creator Name 1");
        when(mMockDetailsPresenter.getCreatorName(1)).thenReturn("Creator Name 2");
        when(mMockDetailsPresenter.getTitle()).thenReturn(showName);
        when(mMockDetailsPresenter.getImdbId()).thenReturn("300");
        when(mMockDetailsPresenter.downloadExternalIds()).thenReturn(true);
    }

    private void mockSeasonsPresenterMethods(){
        for(int i=1;i<5;i++) {
            when(mMockSeasonsPresenter.getSeasonName(i-1)).thenReturn("Season " + i);
            when(mMockSeasonsPresenter.getAirDate(i-1)).thenReturn("1"+i+" OCT " + "200"+i);
            when(mMockSeasonsPresenter.getNumberOfEpisodes(i-1,mContext)).thenReturn("10 Episodes");
            when(mMockSeasonsPresenter.getOverview(i-1)).thenReturn("Season overview for Season " + i + ".");
        }
    }

    private void mockCastPresenterMethods(){
        for(int i=1;i<5;i++) {
            when(mMockCastPresenter.getActorName(i-1)).thenReturn("Actor " + i);
            when(mMockCastPresenter.getCharacterName(i-1)).thenReturn("Character " + i);
        }
    }

    private void testDisplayDetails(int fragmentPosition){
        List<Fragment> fragments = mActivity.getSupportFragmentManager().getFragments();

        final DetailsFragment detailsFragment = (DetailsFragment) fragments.get(fragmentPosition);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                detailsFragment.setUserInterfaceText(showOverview,"2016","7.2","600 votes","Crime, Drama",0xffC6FF00,0xff000000);
                detailsFragment.creatorDataLoaded(2);
            }});

        onView(withId(R.id.show_name)).check(matches(withText(showName)));

        onView(withId(R.id.start_year)).check(matches(withText("2016")));
        onView(withId(R.id.user_score)).check(matches(withText("7.2")));
        onView(withId(R.id.vote_count)).check(matches(withText("600 votes")));
        onView(withId(R.id.overview)).check(matches(withText(showOverview)));
        onView(withId(R.id.genres)).check(matches(withText("Crime, Drama")));

        // do nothing when an external intent is called
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

        onView(withId(R.id.link_imdb)).perform(CustomScrollActions.nestedScrollTo()).perform(click());
        intended(Matchers.allOf(hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse(mActivity.getString(R.string.imdb_tv_show_webpage)+"300"))));

        onView(withId(R.id.link_tmdb)).perform(CustomScrollActions.nestedScrollTo()).perform(click());
        intended(Matchers.allOf(hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse(mActivity.getString(R.string.tmdb_tv_show_webpage)+"-1"))));

        onView(withId(R.id.link_google_search)).perform(CustomScrollActions.nestedScrollTo()).perform(click());
        intended(Matchers.allOf(hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse(mActivity.getString(R.string.google_search_webpage)+showName))));

        onView(withId(R.id.link_youtube_search)).perform(CustomScrollActions.nestedScrollTo()).perform(click());
        intended(Matchers.allOf(hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse(mActivity.getString(R.string.youtube_search_webpage)+showName))));

        onView(withId(R.id.link_wikipedia)).perform(CustomScrollActions.nestedScrollTo()).perform(click());
        intended(Matchers.allOf(hasAction(Intent.ACTION_VIEW),
                hasData(Uri.parse(mActivity.getString(R.string.wikipedia_webapge)
                        +showName+mActivity.getString(R.string.wikipedia_webpage_end)))));
    }

    private void testDisplaySeasons(int fragmentPosition) {
        List<Fragment> fragments = mActivity.getSupportFragmentManager().getFragments();
        final SeasonsFragment seasonsFragment = (SeasonsFragment) fragments.get(fragmentPosition);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                seasonsFragment.seasonDataLoaded(4);
            }});

        // do nothing when an internal intent is called
        intending(isInternal()).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

        for(int i=1;i<5;i++) {
            when(mMockSeasonsPresenter.getIntentForEpisodesActivity(
                    mContext,i-1)).thenReturn(new Intent(mActivity.getBaseContext(),EpisodesActivity.class));

            onView(withId(R.id.recyclerview_seasons))
                    .perform(RecyclerViewActions.scrollToPosition(i));
            onView(withRecyclerView(R.id.recyclerview_seasons).atPositionOnView(i-1, R.id.season_name))
                    .check(matches(withText("Season " + i)));
            onView(withRecyclerView(R.id.recyclerview_seasons).atPositionOnView(i-1, R.id.season_air_date))
                    .check(matches(withText("1"+i+" OCT " + "200"+i)));
            onView(withRecyclerView(R.id.recyclerview_seasons).atPositionOnView(i-1, R.id.season_episodes_number))
                    .check(matches(withText("10 Episodes")));
            onView(withRecyclerView(R.id.recyclerview_seasons).atPositionOnView(i-1, R.id.season_overview))
                    .check(matches(withText("Season overview for Season " + i + ".")));
            onView(withRecyclerView(R.id.recyclerview_seasons).atPositionOnView(i-1, R.id.seasons_layout))
                    .perform(click());

            intended(hasComponent(EpisodesActivity.class.getName()),times(i));
        }

    }

    private void testDisplayCast(int fragmentPosition){
        List<Fragment> fragments = mActivity.getSupportFragmentManager().getFragments();
        final CastFragment castFragment = (CastFragment) fragments.get(fragmentPosition);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                castFragment.castDataLoaded(4);
            }});

        // do nothing when an internal intent is called
        intending(isInternal()).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

        for(int i=1;i<5;i++) {
            when(mMockCastPresenter.getIntentForActorActivity(
                    mContext,i-1)).thenReturn(new Intent(mActivity.getBaseContext(),ActorActivity.class));

            onView(withId(R.id.recyclerview_cast))
                    .perform(RecyclerViewActions.scrollToPosition(i-1));
            onView(withRecyclerView(R.id.recyclerview_cast).atPositionOnView(i-1, R.id.actor_name))
                    .check(matches(withText("Actor " + i)));
            onView(withRecyclerView(R.id.recyclerview_cast).atPositionOnView(i-1, R.id.character_name))
                    .check(matches(withText("Character " + i)));
            onView(withRecyclerView(R.id.recyclerview_cast).atPositionOnView(i-1, R.id.cast_layout))
                    .perform(click());
            intended(hasComponent(ActorActivity.class.getName()),times(i));
        }
    }

}
