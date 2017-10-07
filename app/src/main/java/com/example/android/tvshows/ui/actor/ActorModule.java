package com.example.android.tvshows.ui.actor;

import com.example.android.tvshows.data.model.Actor;
import com.example.android.tvshows.data.model.ExternalIds;
import com.example.android.tvshows.data.model.actortvcredits.ActorTVCredits;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.ui.actor.ActorActivity;
import com.example.android.tvshows.ui.actor.ActorAdapter;
import com.example.android.tvshows.ui.actor.ActorContract;
import com.example.android.tvshows.ui.actor.ActorPresenter;
import com.example.android.tvshows.ui.actor.ActorActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ActorModule {

    private final ActorActivity mActorActivity;
    private final ActorContract.View mView;
    private final Integer mTmdbActorId;
    private ExternalIds mExternalIds;
    private ActorTVCredits mActorTVCredits;
    private Actor mActor;

    public ActorModule(ActorActivity actorActivity, ActorContract.View view,int tmdbActorId) {
        mActorActivity = actorActivity;
        mView = view;
        mTmdbActorId = tmdbActorId;
    }

    public ActorModule(ActorActivity actorActivity, ActorContract.View view,int tmdbActorId,
                       ExternalIds externalIds,ActorTVCredits actorTVCredits, Actor actor) {
        mActorActivity = actorActivity;
        mView = view;
        mTmdbActorId = tmdbActorId;
        mExternalIds = externalIds;
        mActorTVCredits = actorTVCredits;
        mActor = actor;
    }

    @Provides
    @ActorActivityScope
    public ActorContract.Presenter provideActorContractPresenter(ApiService apiService){
        if(mExternalIds!=null && mActorTVCredits!=null && mActor!=null)
            return new ActorPresenter(mView,mTmdbActorId,apiService,
                mExternalIds,mActorTVCredits, mActor);
        return new ActorPresenter(mView,mTmdbActorId,apiService);
    }

    @Provides
    @ActorActivityScope
    public ActorAdapter provideActorAdapter(ActorContract.Presenter presenter){
        return new ActorAdapter(mActorActivity,presenter);
    }

}
