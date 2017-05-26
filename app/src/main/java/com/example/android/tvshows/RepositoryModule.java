package com.example.android.tvshows;

import android.content.Context;

import com.example.android.tvshows.data.db.ShowsRepository;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ContextModule.class})
public class RepositoryModule {

    @Provides
    @ShowsApplicationScope
    public ShowsRepository provideRepository(Context context){
        return new ShowsRepository(context);
    }

}
