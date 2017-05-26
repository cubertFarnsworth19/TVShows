package com.example.android.tvshows;

import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.rest.ApiService;
import com.squareup.picasso.Picasso;

import dagger.Component;

@ShowsApplicationScope
@Component(modules = {ApiServiceModule.class,PicassoModule.class,RepositoryModule.class})
public interface ApplicationComponent {
    Picasso getPicasso();

    ApiService getApiService();

    ShowsRepository getShowsRepository();
}
