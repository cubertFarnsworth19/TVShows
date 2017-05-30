package com.example.android.tvshows.ui.actor;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.android.tvshows.BuildConfig;
import com.example.android.tvshows.data.model.Actor;
import com.example.android.tvshows.data.model.ExternalIds;
import com.example.android.tvshows.data.model.actortvcredits.ActorTVCredits;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.data.rest.ApiUtils;
import com.example.android.tvshows.ui.RetryUntilDownloaded;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

public class ActorPresenter implements ActorContract.Presenter {

    private ActorContract.View mActorView;
    private ApiService mApiService;
    private Integer mTmdbActorId;

    private ExternalIds mExternalIds;
    private ActorTVCredits mActorTVCredits;
    private Actor mActor;

    public ActorPresenter(ActorContract.View actorView,int tmdbActorId,ApiService apiService) {
        mActorView = actorView;
        mTmdbActorId = tmdbActorId;
        mApiService = apiService;
    }

    public ActorPresenter(ActorContract.View actorView,int tmdbActorId,ApiService apiService,
                          ExternalIds externalIds,ActorTVCredits actorTVCredits, Actor actor) {
        mActorView = actorView;
        mTmdbActorId = tmdbActorId;
        mApiService = apiService;
        mExternalIds = externalIds;
        mActorTVCredits = actorTVCredits;
        mActor = actor;
    }


    @Override
    public void downloadActorData() {

        Observable<Actor> actorObservable = mApiService.getActorDetails(mTmdbActorId.toString(), BuildConfig.TMDB_API_KEY)
                .retryWhen(new RetryUntilDownloaded(2000));
        Observable<ActorTVCredits> actorTVCreditsObservable = mApiService.getActorTVCredits(mTmdbActorId.toString(), BuildConfig.TMDB_API_KEY)
                .retryWhen(new RetryUntilDownloaded(2000));
        Observable<ExternalIds> externalIdsObservable = mApiService.getExternalIds(mTmdbActorId.toString(), BuildConfig.TMDB_API_KEY)
                .retryWhen(new RetryUntilDownloaded(2000));

        Observable< ActorFullDetails> observableZipped = Observable.zip(actorObservable, actorTVCreditsObservable, externalIdsObservable,
                new Function3<Actor, ActorTVCredits, ExternalIds, ActorFullDetails>() {
            @Override
            public ActorFullDetails apply(@NonNull Actor actor, @NonNull ActorTVCredits actorTVCredits, @NonNull ExternalIds externalIds) throws Exception {

                return new ActorFullDetails(actor,actorTVCredits,externalIds);
            }
        });

        observableZipped.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ActorFullDetails>() {
                    @Override
                    public void accept(@NonNull ActorFullDetails actorFullDetails) throws Exception {
                        Log.v("accept","");
                        mActor = actorFullDetails.mActor;
                        mActorTVCredits = actorFullDetails.mActorTVCredits;
                        mExternalIds = actorFullDetails.mExternalIds;
                        mActorView.setName(actorFullDetails.mActor.getName());
                        mActorView.setBiography(actorFullDetails.mActor.getBiography());
                        mActorView.setImage(actorFullDetails.mActor.getProfilePath());
                        mActorView.displayCredits(mActorTVCredits.getCast().size());
                    }
                });
    }

    @Override
    public void setActorData() {
        mActorView.setName(mActor.getName());
        mActorView.setBiography(mActor.getBiography());
        mActorView.setImage(mActor.getProfilePath());
        mActorView.displayCredits(mActorTVCredits.getCast().size());
    }

    @Override
    public void goToImdbPage(Context context) {
        String imdbKey = mExternalIds.getImdbId();
        if(imdbKey!="") {
            Uri webpage = Uri.parse("http://www.imdb.com/name/" + imdbKey + "/?ref_=tt_cl_t4");
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(context.getPackageManager()) != null)
                context.startActivity(intent);
        }
    }

    @Override
    public String getCharacterName(int position) {
        return mActorTVCredits.getCast().get(position).getCharacter();
    }

    @Override
    public String getTVShowTitle(int position) {
        return  mActorTVCredits.getCast().get(position).getName();
    }

    @Override
    public ActorTVCredits getActorTVCredits() {
        return mActorTVCredits;
    }

    @Override
    public ExternalIds getExternalIds() {
        return mExternalIds;
    }

    @Override
    public Actor getActor() {
        return mActor;
    }

    class ActorFullDetails{
        Actor mActor;
        ActorTVCredits mActorTVCredits;
        ExternalIds mExternalIds;

        public ActorFullDetails(Actor actor, ActorTVCredits actorTVCredits, ExternalIds externalIds) {
            mActor = actor;
            mActorTVCredits = actorTVCredits;
            mExternalIds = externalIds;
        }

    }
}


