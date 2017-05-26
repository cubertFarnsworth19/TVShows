package com.example.android.tvshows.ui.showinfo.cast;

import com.example.android.tvshows.ApplicationComponent;
import dagger.Component;

@CastScope
@Component(modules = CastModule.class, dependencies = ApplicationComponent.class)
public interface CastComponent {
    void inject(CastFragment fragment);
}
