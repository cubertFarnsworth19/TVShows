package com.example.android.tvshows.ui.actor;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.example.android.tvshows.BuildConfig;
import com.example.android.tvshows.data.model.Actor;
import com.example.android.tvshows.data.model.ExternalIds;
import com.example.android.tvshows.data.model.actortvcredits.ActorTVCredits;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.ui.RetryUntilDownloaded;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
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
    public void downloadActorData(Context context) {

        mActorView.startingDownload();

        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Observable<Actor> actorObservable = mApiService.getActorDetails(mTmdbActorId.toString(), BuildConfig.TMDB_API_KEY)
                    .retryWhen(new RetryUntilDownloaded(2000));
            Observable<ActorTVCredits> actorTVCreditsObservable = mApiService.getActorTVCredits(mTmdbActorId.toString(), BuildConfig.TMDB_API_KEY)
                    .retryWhen(new RetryUntilDownloaded(2000));
            Observable<ExternalIds> externalIdsObservable = mApiService.getExternalIds(mTmdbActorId.toString(), BuildConfig.TMDB_API_KEY)
                    .retryWhen(new RetryUntilDownloaded(2000));

            Observable<ActorFullDetails> observableZipped = Observable.zip(actorObservable, actorTVCreditsObservable, externalIdsObservable,
                    new Function3<Actor, ActorTVCredits, ExternalIds, ActorFullDetails>() {
                        @Override
                        public ActorFullDetails apply(@NonNull Actor actor, @NonNull ActorTVCredits actorTVCredits, @NonNull ExternalIds externalIds) throws Exception {

                            return new ActorFullDetails(actor, actorTVCredits, externalIds);
                        }
                    });

            observableZipped.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ActorFullDetails>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ActorFullDetails actorFullDetails) {
                            mActor = actorFullDetails.mActor;
                            mActorTVCredits = actorFullDetails.mActorTVCredits;
                            mExternalIds = actorFullDetails.mExternalIds;
                            mActorView.setName(actorFullDetails.mActor.getName());
                            mActorView.setBiography(actorFullDetails.mActor.getBiography());
                            mActorView.setImage(actorFullDetails.mActor.getProfilePath());
                            mActorView.displayCredits(mActorTVCredits.getCast().size());
                        }

                        @Override
                        public void onError(Throwable e) {
                            mActorView.noConnection();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else{
            mActorView.noConnection();
        }
    }

    @Override
    public void setActorData() {
        mActorView.setName(mActor.getName());
        mActorView.setBiography(mActor.getBiography());
        mActorView.setImage(mActor.getProfilePath());
        mActorView.displayCredits(mActorTVCredits.getCast().size());
    }

    @Override
    public String getActorIMDBId() {
        return mExternalIds.getImdbId();
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


