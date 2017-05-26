package com.example.android.tvshows.service;

import com.example.android.tvshows.ApplicationComponent;

import dagger.Component;

@ServiceScope
@Component(modules=ServiceModule.class,dependencies = ApplicationComponent.class)
public interface ServiceComponent {

    void inject(DownloadService service);

}
