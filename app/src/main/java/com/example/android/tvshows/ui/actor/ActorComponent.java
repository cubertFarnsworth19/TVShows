package com.example.android.tvshows.ui.actor;

import com.example.android.tvshows.ApplicationComponent;

import dagger.Component;

@ActorActivityScope
@Component(modules = ActorModule.class, dependencies = ApplicationComponent.class)
public interface ActorComponent {

    void inject(ActorActivity actorActivity);
}
