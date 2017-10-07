package com.example.android.tvshows;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.example.android.tvshows.data.model.Actor;
import com.example.android.tvshows.data.model.ExternalIds;
import com.example.android.tvshows.data.model.actortvcredits.ActorTVCredits;
import com.example.android.tvshows.service.DownloadService;
import com.example.android.tvshows.service.ServiceModule;
import com.example.android.tvshows.ui.actor.ActorActivity;
import com.example.android.tvshows.ui.actor.ActorContract;
import com.example.android.tvshows.ui.actor.ActorModule;
import com.example.android.tvshows.ui.actor.ActorPresenter;
import com.example.android.tvshows.ui.episodes.EpisodesActivity;
import com.example.android.tvshows.ui.episodes.EpisodesContract;
import com.example.android.tvshows.ui.episodes.EpisodesModule;
import com.example.android.tvshows.ui.find.ResultsAdapter;
import com.example.android.tvshows.ui.find.ResultsContract;
import com.example.android.tvshows.ui.find.ResultsFragment;
import com.example.android.tvshows.ui.find.ResultsModule;
import com.example.android.tvshows.ui.find.SaveResultsPresenterState;
import com.example.android.tvshows.ui.myshows.current.CurrentAdapter;
import com.example.android.tvshows.ui.myshows.current.CurrentContract;
import com.example.android.tvshows.ui.myshows.current.CurrentFragment;
import com.example.android.tvshows.ui.myshows.current.CurrentInfo;
import com.example.android.tvshows.ui.myshows.current.CurrentModule;
import com.example.android.tvshows.ui.myshows.current.ShowDate;
import com.example.android.tvshows.ui.myshows.shows.ShowInfo;
import com.example.android.tvshows.ui.myshows.shows.ShowsAdapter;
import com.example.android.tvshows.ui.myshows.shows.ShowsContract;
import com.example.android.tvshows.ui.myshows.shows.ShowsFragment;
import com.example.android.tvshows.ui.myshows.shows.ShowsModule;
import com.example.android.tvshows.ui.showinfo.cast.CastAdapter;
import com.example.android.tvshows.ui.showinfo.cast.CastContract;
import com.example.android.tvshows.ui.showinfo.cast.CastFragment;
import com.example.android.tvshows.ui.showinfo.cast.CastInfo;
import com.example.android.tvshows.ui.showinfo.cast.CastModule;
import com.example.android.tvshows.ui.showinfo.details.DetailsContract;
import com.example.android.tvshows.ui.showinfo.details.DetailsFragment;
import com.example.android.tvshows.ui.showinfo.details.DetailsModule;
import com.example.android.tvshows.ui.showinfo.seasons.SeasonInfo;
import com.example.android.tvshows.ui.showinfo.seasons.SeasonsAdapter;
import com.example.android.tvshows.ui.showinfo.seasons.SeasonsContract;
import com.example.android.tvshows.ui.showinfo.seasons.SeasonsFragment;
import com.example.android.tvshows.ui.showinfo.seasons.SeasonsModule;
import com.example.android.tvshows.ui.updates.UpdatesContract;
import com.example.android.tvshows.ui.updates.UpdatesFragment;
import com.example.android.tvshows.ui.updates.UpdatesModule;

import java.util.ArrayList;

import timber.log.Timber;

public class ShowsApplication extends Application {

//    public static ShowsApplication get(Activity activity){
//        return (ShowsApplication) activity.getApplication();
//    }
//
//    public static ShowsApplication get(Service service){
//        return (ShowsApplication) service.getApplication();
//    }

    ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        mComponent = DaggerApplicationComponent.builder()
               .contextModule(new ContextModule(this))
               .build();
    }

    public ShowsApplication get(Activity activity){
        return (ShowsApplication) activity.getApplication();
    }

    public ShowsApplication get(Service service){
        return (ShowsApplication) service.getApplication();
    }

    public ApplicationComponent getComponent(){return  mComponent;}

    public ServiceModule getServiceModule(DownloadService downloadService){
        return new ServiceModule(downloadService);
    }

    public ActorModule getActorModule(ActorActivity actorActivity, ActorContract.View view, int tmdbActorId) {
        return new ActorModule(actorActivity,view,tmdbActorId);
    }

    public ActorModule getActorModule(ActorActivity actorActivity, ActorContract.View view, int tmdbActorId,
                                      ExternalIds externalIds, ActorTVCredits actorTVCredits, Actor actor) {
        return new ActorModule(actorActivity,view,tmdbActorId,externalIds,actorTVCredits, actor);
    }

    public EpisodesModule getEpisodesModule(EpisodesActivity episodesActivity, EpisodesContract.View view, String[] seasonNames, int showId, int seasonNumber, int[] seasonNumbers) {
        return new EpisodesModule(episodesActivity, view, seasonNames, showId, seasonNumber, seasonNumbers);
    }

    public ResultsModule getResultsModule(ResultsFragment resultsFragment, ResultsContract.View view) {
        return new ResultsModule(resultsFragment,view);
    }

    public ResultsModule getResultsModule(ResultsFragment resultsFragment, ResultsContract.View view,SaveResultsPresenterState saveResultsPresenterState,ResultsAdapter resultsAdapter) {
        return new ResultsModule(resultsFragment,view,saveResultsPresenterState,resultsAdapter);
    }

    public CurrentModule getCurrentModule(CurrentContract.View view, CurrentFragment currentFragment, int currentType) {
        return new CurrentModule(view,currentFragment,currentType);
    }

    public CurrentModule getCurrentModule(CurrentContract.View view, CurrentFragment currentFragment, int currentType,
                                          ArrayList<CurrentInfo> currentInfo, ArrayList<ShowDate> dates, CurrentAdapter adapter) {
        return new CurrentModule(view,currentFragment,currentType,currentInfo,dates,adapter);
    }

    public ShowsModule getShowsModule(ShowsFragment showsFragment, ShowsContract.View view) {
        return new ShowsModule(showsFragment,view);
    }

    public ShowsModule getShowsModule(ShowsFragment showsFragment, ShowsContract.View view,
                                      ArrayList<ShowInfo> showsInfo, ShowsAdapter showsAdapter) {
        return new ShowsModule(showsFragment,view,showsInfo,showsAdapter);
    }

    public CastModule getCastModule(CastFragment castFragment, CastContract.View view, int tmdbId) {
        return new CastModule(castFragment,view,tmdbId);
    }

    public CastModule getCastModule(CastFragment castFragment, CastContract.View view, int tmdbId,
                                    ArrayList<CastInfo> castInfo, CastAdapter castAdapter) {
        return new CastModule(castFragment,view,tmdbId,castInfo,castAdapter);
    }

    public DetailsModule getDetailsModule(DetailsFragment detailsFragment, DetailsContract.View view, int tmdbId) {
        return new DetailsModule(detailsFragment, view, tmdbId);
    }

    public SeasonsModule getSeasonsModule(SeasonsFragment seasonsFragment, SeasonsContract.View view, int tmdbId) {
        return new SeasonsModule(seasonsFragment,view,tmdbId);
    }

    public SeasonsModule getSeasonsModule(SeasonsFragment seasonsFragment, SeasonsContract.View view, int tmdbId,
    ArrayList<SeasonInfo> seasonsInfo,SeasonsAdapter seasonsAdapter){
        return new SeasonsModule(seasonsFragment,view,tmdbId,seasonsInfo,seasonsAdapter);
    }

    public UpdatesModule getUpdatesModule(UpdatesFragment updatesFragment, UpdatesContract.View view){
        return new UpdatesModule(updatesFragment, view);
    }


}
