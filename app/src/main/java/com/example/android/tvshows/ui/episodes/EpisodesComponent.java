package com.example.android.tvshows.ui.episodes;

import com.example.android.tvshows.ApplicationComponent;

import dagger.Component;

@EpisodesActivityScope
@Component(modules = EpisodesModule.class, dependencies = ApplicationComponent.class)
public interface EpisodesComponent {
    void inject(EpisodesActivity episodesActivity);
}
