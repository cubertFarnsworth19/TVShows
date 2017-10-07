package com.example.android.tvshows.actor;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.tvshows.BuildConfig;
import com.example.android.tvshows.R;
import com.example.android.tvshows.TestShowsApplication;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.data.model.Actor;
import com.example.android.tvshows.data.model.ExternalIds;
import com.example.android.tvshows.data.model.actortvcredits.ActorTVCredits;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.ui.actor.ActorActivity;
import com.example.android.tvshows.util.CustomScrollActions;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ActorVPTests {

    private ApiService mockApiService;

    @Rule
    public IntentsTestRule<ActorActivity> mActivityTestRule =
            new IntentsTestRule<>(ActorActivity.class,false,false);

    @Before
    public void setUp(){
        mockApiService = mock(ApiService.class);

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        TestShowsApplication app = (TestShowsApplication) instrumentation.getTargetContext().getApplicationContext();

        app.setApiServiceMock(mockApiService);
    }

    @Test
    public void display_actor_info()  {
        int actorId = 1;

        Actor actor = getActor();
        ExternalIds externalIds = getExternalIds();
        ActorTVCredits actorTVCredits = getActorTVCredits();

        mockApiServiceMethods(actorId,actor,externalIds,actorTVCredits);

        mActivityTestRule.launchActivity(getIntent(actorId));

        testDataDisplayed(actor,actorTVCredits);

        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        testDataDisplayed(actor,actorTVCredits);

        //verify that the apiservice methods were called exactly once,
        // i.e. not called when phone is orientation is changed
        verify(mockApiService,times(1)).getActorDetails(any(String.class),any(String.class));
        verify(mockApiService,times(1)).getActorTVCredits(any(String.class),any(String.class));
        verify(mockApiService,times(1)).getExternalIds(any(String.class),any(String.class));
    }

    @Test
    public void launch_imdb()throws InterruptedException{

        int actorId = 1;
        Actor actor = getActor();
        ExternalIds externalIds = getExternalIds();
        ActorTVCredits actorTVCredits = getActorTVCredits();
        mockApiServiceMethods(actorId,actor,externalIds,actorTVCredits);

        mActivityTestRule.launchActivity(getIntent(actorId));

        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

        onView(withId(R.id.action_links)).perform(click());
        Thread.sleep(1000);
        onView(withText(R.string.link_imdb)).inRoot(isPlatformPopup()).perform(click());

        intended(allOf(hasAction(Intent.ACTION_VIEW),hasData(Uri.parse("http://www.imdb.com/name/" + externalIds.getImdbId() + "/?ref_=tt_cl_t4"))));


    }

    private Intent getIntent(int actorId){
        Intent intent = new Intent();
        intent.putExtra(ShowsDbContract.CastEntry.COLUMN_PERSON_ID,actorId);
        return intent;
    }

    private Actor getActor(){
        String jsonActor = "{\"birthday\":\"1969-10-15\",\"deathday\":null,\"id\":17287,\"name\":\"Dominic West\",\"also_known_as\":[],\"gender\":2,\"biography\":\"Dominic Gerard Fe West (born 15 October 1969) is an English actor best known in the United States for his role as Detective Jimmy McNulty in the HBO drama series The Wire.\",\"popularity\":4.570727,\"place_of_birth\":\"Sheffield, Yorkshire, England, UK\",\"profile_path\":\"\\/56zmVMiuUhqas13xn700hvXUA32.jpg\",\"adult\":false,\"imdb_id\":\"nm0922035\",\"homepage\":null}";
        Gson gson = new Gson();
        return gson.fromJson(jsonActor,Actor.class);
    }

    private ExternalIds getExternalIds(){
        String jsonExternalIds = "{\n  \"facebook_id\": null,\n \n\t \"freebase_mid\": \"/m/0686zv\",\n \n \t\"freebase_id\": \"/en/dominic_west\",\n  \n\t\"imdb_id\": \"nm0922035\",\n \n\t \"instagram_id\": null,\n  \n\t\"tvrage_id\": 67132,\n \n\t \"twitter_id\": null,\n \n\t \"id\": 17287\n}";
        Gson gson = new Gson();
        return gson.fromJson(jsonExternalIds,ExternalIds.class);
    }

    private ActorTVCredits getActorTVCredits(){
        String jsonCredits = "{\"cast\":[{\"credit_id\":\"5256f15519c2956ff6114a41\",\"original_name\":\"The Wire\",\"id\":1438,\"genre_ids\":[80,18],\"character\":\"Jimmy McNulty\",\"name\":\"The Wire\",\"poster_path\":\"\\/dg7NuKDjmS6OzuNy33qt8kSkPA1.jpg\",\"vote_count\":437,\"vote_average\":8.1,\"popularity\":36.324379,\"episode_count\":60,\"original_language\":\"en\",\"first_air_date\":\"2002-06-02\",\"backdrop_path\":\"\\/4hWfYN3wiOZZXC7t6B70BF9iUvk.jpg\",\"overview\":\"Told from the points of view of both the Baltimore homicide and narcotics detectives and their targets, the series captures a universe in which the national war on drugs has become a permanent, self-sustaining bureaucracy, and distinctions between good and evil are routinely obliterated.\",\"origin_country\":[\"US\"]},{\"credit_id\":\"52582ab7760ee36aaa86a3ba\",\"original_name\":\"The Devil's Whore\",\"id\":13791,\"genre_ids\":[18],\"character\":\"Oliver Cromwell\",\"name\":\"The Devil's Whore\",\"poster_path\":\"\\/yWhCbxR6zbI2K1ReAaJh2kABSy2.jpg\",\"vote_count\":8,\"vote_average\":7,\"popularity\":1.97072,\"episode_count\":4,\"original_language\":\"en\",\"first_air_date\":\"2008-11-19\",\"backdrop_path\":\"\\/4BDJG8Ex2pN5lx3nvqNRAsZRPKh.jpg\",\"overview\":\"The Devil's Whore is a four-part television series set during the English Civil War, produced by Company Pictures for Channel 4 in 2008. It centres on the adventures of the fictional Angelica Fanshawe and the historical Leveller soldier Edward Sexby and spans the years 1638 to 1660. It was written by Peter Flannery, who began working on the script in 1997. It is believed to have a budget of £7 million.\",\"origin_country\":[\"GB\"]},{\"original_name\":\"Out of Hours\",\"credit_id\":\"52587994760ee346612f050b\",\"vote_count\":0,\"vote_average\":0,\"name\":\"Out of Hours\",\"character\":\"Dr. Paul Featherstone\",\"poster_path\":null,\"original_language\":\"en\",\"popularity\":1.382189,\"episode_count\":0,\"genre_ids\":[],\"id\":24928,\"backdrop_path\":null,\"overview\":\"\",\"origin_country\":[\"GB\"]},{\"credit_id\":\"52596791760ee346619cbd08\",\"original_name\":\"Appropriate Adult\",\"id\":44434,\"genre_ids\":[80,18],\"character\":\"Fred West\",\"name\":\"Appropriate Adult\",\"poster_path\":\"\\/g3iM6GVyrBr7itfnDh94Id1rB4k.jpg\",\"vote_count\":4,\"vote_average\":7.5,\"popularity\":1.757188,\"episode_count\":2,\"original_language\":\"en\",\"first_air_date\":\"2011-09-04\",\"backdrop_path\":\"\\/52qjlCHHalYZkcgPGBaiLMK9PXt.jpg\",\"overview\":\"Appropriate Adult is a 2011 British television film, first shown on ITV in two 90-minute parts on 4 and 11 September 2011. It starred Dominic West, Emily Watson, and Monica Dolan. It is based on the true story of Gloucester serial killer Fred West, and focuses on the events between the Wests' arrests in 1994 and Fred's suicide in Birmingham's Winson Green Prison on New Year's Day 1995.\",\"origin_country\":[\"GB\"]},{\"credit_id\":\"5253952f19c29579402c3dc0\",\"original_name\":\"This Morning\",\"id\":766,\"genre_ids\":[],\"character\":\"\",\"name\":\"This Morning\",\"poster_path\":null,\"vote_count\":2,\"vote_average\":2.5,\"popularity\":1.090383,\"episode_count\":1,\"original_language\":\"en\",\"first_air_date\":\"1988-10-03\",\"backdrop_path\":null,\"overview\":\"This Morning is a British daytime television programme broadcast on ITV. It is currently presented by Phillip Schofield, Holly Willoughby on Monday to Thursday with Ruth Langsford and Eamonn Holmes presenting on a Friday and during the school holidays with guest presenters standing in or contributing items to the show. The show began airing on 3 October 1988 featuring Richard Madeley and Judy Finnigan as hosts until their departure in July 2001.\\n\\nThe show was devised by Granada Television and was first broadcast from studios at the Albert Dock in Liverpool, before relocating to The London Studios in 1996. This Morning now airs daily on ITV at 10:30am till 12:30pm featuring news, topical items, showbiz, style and beauty, home and garden, food, health, real life and more similar features.\\n\\nSince 2010, during the summer, This Morning Summer has aired on the same channel at the same time slot. It is generally a continuation of the same format effectively making the show a year-round broadcast, featuring Eamonn Holmes and Ruth Langsford as the main presenters.\",\"origin_country\":[\"GB\"]},{\"credit_id\":\"52595aeb760ee34661915b1b\",\"original_name\":\"The Hour\",\"id\":39718,\"genre_ids\":[18],\"character\":\"Hector Madden\",\"name\":\"The Hour\",\"poster_path\":\"\\/wXSxKwFklylXFONPdgLIN2hv4q.jpg\",\"vote_count\":29,\"vote_average\":7,\"popularity\":2.503987,\"episode_count\":14,\"original_language\":\"en\",\"first_air_date\":\"2011-07-19\",\"backdrop_path\":\"\\/g3gCsn2AsZX4nQZRqGmV5ZXGbD5.jpg\",\"overview\":\"A behind-the-scenes drama and espionage thriller in Cold War-era England that centers on a journalist, a producer, and an anchorman for an investigative news programme.\",\"origin_country\":[\"GB\"]},{\"credit_id\":\"5434fc8ec3a36831a900bac1\",\"original_name\":\"The Affair\",\"id\":61463,\"genre_ids\":[18],\"character\":\"Noah Solloway\",\"name\":\"The Affair\",\"poster_path\":\"\\/4GIe35RiqCvwFlwIndcyucbw9uT.jpg\",\"vote_count\":81,\"vote_average\":6.1,\"popularity\":11.372717,\"episode_count\":32,\"original_language\":\"en\",\"first_air_date\":\"2014-10-12\",\"backdrop_path\":\"\\/8qA6DMj0anIb4N6nTOyhfkUm900.jpg\",\"overview\":\"The Affair explores the emotional effects of an extramarital relationship between Noah Solloway and Alison Lockhart after the two meet in the resort town of Montauk in Long Island. Noah is a New York City schoolteacher with one novel published (book entitled A Person who Visits a Place) and he is struggling to write a second book. He is happily married with four children, but resents his dependence on his wealthy father-in-law. Alison is a young waitress trying to piece her life and marriage back together in the wake of the tragic death of her child. The story of the affair is told separately, complete with distinct memory biases, from the male and female perspectives.\",\"origin_country\":[\"US\"]},{\"credit_id\":\"57f35b13c3a368334a0073d3\",\"original_name\":\"The Graham Norton Show\",\"id\":1220,\"genre_ids\":[35,10767],\"character\":\"Himself\",\"name\":\"The Graham Norton Show\",\"poster_path\":\"\\/irOmnAKLi1elTmPylng4M1IkoyF.jpg\",\"vote_count\":46,\"vote_average\":7,\"popularity\":31.349698,\"episode_count\":1,\"original_language\":\"en\",\"first_air_date\":\"2007-02-22\",\"backdrop_path\":\"\\/sVSepFJ2ymjFwY4Q0qmVF925PvK.jpg\",\"overview\":\"Each week celebrity guests join Irish comedian Graham Norton to discuss what's being going on around the world that week. The guests poke fun and share their opinions on the main news stories. Graham is often joined by a band or artist to play the show out.\",\"origin_country\":[\"GB\"]}],\"crew\":[{\"id\":1438,\"department\":\"Directing\",\"original_language\":\"en\",\"episode_count\":1,\"job\":\"Director\",\"overview\":\"Told from the points of view of both the Baltimore homicide and narcotics detectives and their targets, the series captures a universe in which the national war on drugs has become a permanent, self-sustaining bureaucracy, and distinctions between good and evil are routinely obliterated.\",\"origin_country\":[\"US\"],\"original_name\":\"The Wire\",\"genre_ids\":[80,18],\"name\":\"The Wire\",\"first_air_date\":\"2002-06-02\",\"backdrop_path\":\"\\/4hWfYN3wiOZZXC7t6B70BF9iUvk.jpg\",\"popularity\":36.324379,\"vote_count\":437,\"vote_average\":8.1,\"poster_path\":\"\\/dg7NuKDjmS6OzuNy33qt8kSkPA1.jpg\",\"credit_id\":\"5256f15119c2956ff6114707\"}],\"id\":17287}";
        Gson gson = new Gson();
        return gson.fromJson(jsonCredits,ActorTVCredits.class);
    }
    
    private void mockApiServiceMethods(int actorId,final  Actor actor,final ExternalIds externalIds,final ActorTVCredits credits){
        String id = String.valueOf(actorId);

        Observable<Actor> observableActor = Observable.create(new ObservableOnSubscribe<Actor>() {
            @Override
            public void subscribe(ObservableEmitter<Actor> e) throws Exception {
               e.onNext(actor);
            }
        });

        Observable<ExternalIds> observableExternalIds = Observable.create(new ObservableOnSubscribe<ExternalIds>() {
            @Override
            public void subscribe(ObservableEmitter<ExternalIds> e) throws Exception {
                e.onNext(externalIds);
            }
        });

        Observable<ActorTVCredits> observableCredits = Observable.create(new ObservableOnSubscribe<ActorTVCredits>() {
            @Override
            public void subscribe(ObservableEmitter<ActorTVCredits> e) throws Exception {
                e.onNext(credits);
            }
        });

        when(mockApiService.getActorDetails(id, BuildConfig.TMDB_API_KEY)).thenReturn(observableActor);
        when(mockApiService.getExternalIds(id, BuildConfig.TMDB_API_KEY)).thenReturn(observableExternalIds);
        when(mockApiService.getActorTVCredits(id, BuildConfig.TMDB_API_KEY)).thenReturn(observableCredits);
    }

    private void testDataDisplayed(Actor actor,ActorTVCredits actorTVCredits){
        onView(withId(R.id.actor_name)).check(matches(withText(actor.getName())));
        onView(withId(R.id.actor_biography)).check(matches(withText(actor.getBiography())));

        onView(withId(R.id.recyclerview_actor)).perform(CustomScrollActions.nestedScrollTo());

        for(int i=0;i<actorTVCredits.getCast().size();i++) {
            onView(withId(R.id.recyclerview_actor))
                    .perform(RecyclerViewActions.scrollToPosition(i));
            onView(withRecyclerView(R.id.recyclerview_actor).atPositionOnView(i,R.id.character_name))
                    .check(matches(withText(actorTVCredits.getCast().get(i).getCharacter())));
            onView(withRecyclerView(R.id.recyclerview_actor).atPositionOnView(i,R.id.tvshow_name))
                    .check(matches(withText(actorTVCredits.getCast().get(i).getName())));
        }
    }
    
}
