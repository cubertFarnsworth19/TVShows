package com.example.android.tvshows.ui.updates;

import com.example.android.tvshows.ApplicationComponent;

import dagger.Component;

@UpdatesScope
@Component(modules = UpdatesModule.class, dependencies = ApplicationComponent.class)
public interface UpdatesComponent {
    void inject(UpdatesFragment fragment);
}
