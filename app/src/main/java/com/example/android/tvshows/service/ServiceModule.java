package com.example.android.tvshows.service;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    private final DownloadService mDownloadService;

    public ServiceModule(DownloadService downloadService) {
        mDownloadService = downloadService;
    }

    @Provides
    @ServiceScope
    public DownloadService provideResultsService(){return mDownloadService;}

}
