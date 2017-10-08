package com.example.android.tvshows;


import android.content.Context;

import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.model.Actor;
import com.example.android.tvshows.data.model.ExternalIds;
import com.example.android.tvshows.data.model.actortvcredits.ActorTVCredits;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.service.DownloadService;
import com.example.android.tvshows.service.ServiceModule;
import com.example.android.tvshows.ui.actor.ActorActivity;
import com.example.android.tvshows.ui.actor.ActorContract;
import com.example.android.tvshows.ui.actor.ActorModule;
import com.example.android.tvshows.ui.episodes.EpisodesActivity;
import com.example.android.tvshows.ui.episodes.EpisodesContract;
import com.example.android.tvshows.ui.episodes.EpisodesModule;
import com.example.android.tvshows.ui.find.ResultsContract;
import com.example.android.tvshows.ui.find.ResultsFragment;
import com.example.android.tvshows.ui.find.ResultsModule;
import com.example.android.tvshows.ui.myshows.current.CurrentContract;
import com.example.android.tvshows.ui.myshows.current.CurrentFragment;
import com.example.android.tvshows.ui.myshows.current.CurrentModule;
import com.example.android.tvshows.ui.myshows.shows.ShowsContract;
import com.example.android.tvshows.ui.myshows.shows.ShowsFragment;
import com.example.android.tvshows.ui.myshows.shows.ShowsModule;
import com.example.android.tvshows.ui.showinfo.cast.CastContract;
import com.example.android.tvshows.ui.showinfo.cast.CastFragment;
import com.example.android.tvshows.ui.showinfo.cast.CastModule;
import com.example.android.tvshows.ui.showinfo.details.DetailsContract;
import com.example.android.tvshows.ui.showinfo.details.DetailsFragment;
import com.example.android.tvshows.ui.showinfo.details.DetailsModule;
import com.example.android.tvshows.ui.showinfo.seasons.SeasonsContract;
import com.example.android.tvshows.ui.showinfo.seasons.SeasonsFragment;
import com.example.android.tvshows.ui.showinfo.seasons.SeasonsModule;
import com.example.android.tvshows.ui.updates.UpdatesContract;
import com.example.android.tvshows.ui.updates.UpdatesFragment;
import com.example.android.tvshows.ui.updates.UpdatesModule;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestShowsApplication extends ShowsApplication{

    private ServiceModule mServiceModule;
    private ActorModule mActorModule;
    private EpisodesModule mEpisodesModule;
    private ResultsModule mResultsModule;
    private CurrentModule mCurrentModule;
    private ShowsModule mShowsModule;
    private CastModule mCastModule;
    private DetailsModule mDetailsModule;
    private SeasonsModule mSeasonsModule;
    private UpdatesModule mUpdatesModule;

   public void setPicassoMock(Picasso mockPicasso){
       PicassoModule mockPicassoModule = mock(PicassoModule.class);
       when(mockPicassoModule.providePicasso(any(Context.class),any(OkHttp3Downloader.class))).thenReturn(mockPicasso);

       mComponent =  DaggerApplicationComponent.builder()
               .contextModule(new ContextModule(this))
               .picassoModule(mockPicassoModule)
               .build();
   }

    public void setApiServiceMock(ApiService mockApiService){
        ApiServiceModule mockApiServiceModule = mock(ApiServiceModule.class);
        when(mockApiServiceModule.provideApiService(any(Retrofit.class))).thenReturn(mockApiService);

        mComponent = DaggerApplicationComponent.builder()
                .contextModule(new ContextModule(this))
                .apiServiceModule(mockApiServiceModule)
                .build();
    }

    public void setMocks(ApiService mockApiService, ShowsRepository mockShowsRepository){
        ApiServiceModule mockApiServiceModule = mock(ApiServiceModule.class);
        when(mockApiServiceModule.provideApiService(any(Retrofit.class))).thenReturn(mockApiService);

        RepositoryModule mockRepositoryModule = mock(RepositoryModule.class);
        when(mockRepositoryModule.provideRepository(any(Context.class))).thenReturn(mockShowsRepository);

        mComponent = DaggerApplicationComponent.builder()
                .contextModule(new ContextModule(this))
                .apiServiceModule(mockApiServiceModule)
                .repositoryModule(mockRepositoryModule)
                .build();
    }

    @Override
    public ServiceModule getServiceModule(DownloadService downloadService) {
        if(mServiceModule==null) return super.getServiceModule(downloadService);
        return  mServiceModule;
    }

    @Override
    public ActorModule getActorModule(ActorActivity actorActivity, ActorContract.View view, int tmdbActorId) {
        if(mActorModule==null)
            return super.getActorModule(actorActivity, view, tmdbActorId);
        return mActorModule;
    }

    @Override
    public ActorModule getActorModule(ActorActivity actorActivity, ActorContract.View view, int tmdbActorId, ExternalIds externalIds, ActorTVCredits actorTVCredits, Actor actor) {
        if(mActorModule==null)
            return super.getActorModule(actorActivity, view, tmdbActorId, externalIds, actorTVCredits, actor);
        return mActorModule;
    }

    @Override
    public EpisodesModule getEpisodesModule(EpisodesActivity episodesActivity, EpisodesContract.View view, String[] seasonNames, int showId, int seasonNumber, int[] seasonNumbers) {
        if(mEpisodesModule==null) return super.getEpisodesModule(episodesActivity, view, seasonNames, showId, seasonNumber, seasonNumbers);
        return mEpisodesModule;
    }

    @Override
    public ResultsModule getResultsModule(ResultsFragment resultsFragment, ResultsContract.View view) {
        if(mResultsModule==null) return super.getResultsModule(resultsFragment, view);
        return mResultsModule;
    }

    @Override
    public CurrentModule getCurrentModule(CurrentContract.View view, CurrentFragment currentFragment, int currentType) {
        if(mCurrentModule==null) return super.getCurrentModule(view, currentFragment, currentType);
        return mCurrentModule;
    }

    @Override
    public ShowsModule getShowsModule(ShowsFragment showsFragment, ShowsContract.View view) {
        if(mShowsModule==null) return super.getShowsModule(showsFragment, view);
        return mShowsModule;
    }

    @Override
    public CastModule getCastModule(CastFragment castFragment, CastContract.View view, int tmdbId) {
        if(mCastModule==null) return super.getCastModule(castFragment, view, tmdbId);
        return mCastModule;
    }

    @Override
    public DetailsModule getDetailsModule(DetailsFragment detailsFragment, DetailsContract.View view, int tmdbId) {
        if(mDetailsModule==null) return super.getDetailsModule(detailsFragment, view, tmdbId);
        return mDetailsModule;

    }

    @Override
    public SeasonsModule getSeasonsModule(SeasonsFragment seasonsFragment, SeasonsContract.View view, int tmdbId) {
        if(mSeasonsModule==null) return super.getSeasonsModule(seasonsFragment, view, tmdbId);
        return mSeasonsModule;
    }

    @Override
    public UpdatesModule getUpdatesModule(UpdatesFragment updatesFragment, UpdatesContract.View view) {
        if(mUpdatesModule==null) return super.getUpdatesModule(updatesFragment, view);
        return mUpdatesModule;
    }


    public void setServiceModule(ServiceModule serviceModule) {
        mServiceModule = serviceModule;
    }

    public void setActorModule(ActorModule actorModule){
        mActorModule = actorModule;
    }

    public void setEpisodesModule(EpisodesModule episodesModule) {
        mEpisodesModule = episodesModule;
    }

    public void setResultsModule(ResultsModule resultsModule) {
        mResultsModule = resultsModule;
    }

    public void setCurrentModule(CurrentModule currentModule) {
        mCurrentModule = currentModule;
    }

    public void setShowsModule(ShowsModule showsModule) {
        mShowsModule = showsModule;
    }

    public void setCastModule(CastModule castModule) {
        mCastModule = castModule;
    }

    public void setDetailsModule(DetailsModule detailsModule) {
        mDetailsModule = detailsModule;
    }

    public void setSeasonsModule(SeasonsModule seasonsModule) {
        mSeasonsModule = seasonsModule;
    }

    public void setUpdatesModule(UpdatesModule updatesModule) {
        mUpdatesModule = updatesModule;
    }

}
