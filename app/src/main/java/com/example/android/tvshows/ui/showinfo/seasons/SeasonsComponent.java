package com.example.android.tvshows.ui.showinfo.seasons;

import com.example.android.tvshows.ApplicationComponent;
import dagger.Component;

@SeasonsScope
@Component(modules = SeasonsModule.class, dependencies = ApplicationComponent.class)
public interface SeasonsComponent {
    void inject(SeasonsFragment fragment);

}
