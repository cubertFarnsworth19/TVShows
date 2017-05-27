package com.example.android.tvshows.ui.updates;


import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.rest.ApiService;

import dagger.Module;
import dagger.Provides;

@Module
public class UpdatesModule {

    private final UpdatesFragment mUpdatesFragment;
    private final UpdatesContract.View mView;

    public UpdatesModule(UpdatesFragment updatesFragment, UpdatesContract.View view) {
        mUpdatesFragment = updatesFragment;
        mView = view;
    }

    @Provides
    @UpdatesScope
    public UpdatesContract.Presenter providesUupdatesContractPresenter(ShowsRepository showsRepository){
        return new UpdatesPresenter(mView,showsRepository);
    }

    @Provides
    @UpdatesScope
    public UpdatesAdapter provideUpdatesAdapter(UpdatesContract.Presenter presenter){
        return new UpdatesAdapter(mUpdatesFragment.getActivity(),presenter);
    }
}

