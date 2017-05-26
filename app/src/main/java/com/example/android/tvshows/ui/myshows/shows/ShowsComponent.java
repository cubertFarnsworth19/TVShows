package com.example.android.tvshows.ui.myshows.shows;

import com.example.android.tvshows.ApplicationComponent;

import dagger.Component;

@ShowsScope
@Component(modules = ShowsModule.class, dependencies = ApplicationComponent.class)
public interface ShowsComponent {
    void inject(ShowsFragment fragment);
}
