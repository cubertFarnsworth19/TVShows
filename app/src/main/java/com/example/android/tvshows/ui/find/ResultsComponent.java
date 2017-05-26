package com.example.android.tvshows.ui.find;

import com.example.android.tvshows.ApplicationComponent;

import dagger.Component;

@FragmentScoped
@Component(modules=ResultsModule.class,dependencies = ApplicationComponent.class)
public interface ResultsComponent {

    void inject(ResultsFragment fragment);
}
