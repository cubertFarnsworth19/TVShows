package com.example.android.tvshows.ui.actor;

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

    public ActorModule(ActorActivity actorActivity, ActorContract.View view,int tmdbActorId) {
        mActorActivity = actorActivity;
        mView = view;
        mTmdbActorId = tmdbActorId;
    }

    @Provides
    @ActorActivityScope
    public ActorActivity provideActorActivity(){return mActorActivity;}

    @Provides
    @ActorActivityScope
    public ActorContract.View provideActorContractView(){return mView;}

    @Provides
    @ActorActivityScope
    public ActorContract.Presenter provideActorContractPresenter(ApiService apiService){
        return new ActorPresenter(mView,mTmdbActorId,apiService);
    }

    @Provides
    @ActorActivityScope
    public ActorAdapter provideActorAdapter(ActorContract.Presenter presenter){
        return new ActorAdapter(mActorActivity,presenter);
    }

}
