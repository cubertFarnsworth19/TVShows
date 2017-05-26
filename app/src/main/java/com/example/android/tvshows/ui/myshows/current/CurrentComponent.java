package com.example.android.tvshows.ui.myshows.current;

import com.example.android.tvshows.ApplicationComponent;
import com.example.android.tvshows.ui.showinfo.cast.CastFragment;

import dagger.Component;

@CurrentScope
@Component(modules = CurrentModule.class, dependencies = ApplicationComponent.class)
public interface CurrentComponent {
    void inject(CurrentFragment fragment);
}
