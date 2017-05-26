package com.example.android.tvshows.ui.showinfo.details;

import com.example.android.tvshows.ApplicationComponent;
import dagger.Component;

@DetailsScope
@Component(modules = DetailsModule.class, dependencies = ApplicationComponent.class)
public interface DetailsComponent {
    void inject(DetailsFragment fragment);
}
