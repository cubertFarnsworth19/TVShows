package com.example.android.tvshows;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public final class ContextModule {

    private final Context mContext;

    ContextModule(Context context){
        mContext = context;
    }

    @Provides
    @ShowsApplicationScope
    Context provideContext() {
        return mContext;
    }

}
