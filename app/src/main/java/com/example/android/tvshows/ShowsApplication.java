package com.example.android.tvshows;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import timber.log.Timber;

public class ShowsApplication extends Application {

    public static ShowsApplication get(Activity activity){
        return (ShowsApplication) activity.getApplication();
    }

    public static ShowsApplication get(Service service){
        return (ShowsApplication) service.getApplication();
    }

    ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        mComponent = DaggerApplicationComponent.builder()
               .contextModule(new ContextModule(this))
               .build();
    }

    public ApplicationComponent getComponent(){return  mComponent;}

}
