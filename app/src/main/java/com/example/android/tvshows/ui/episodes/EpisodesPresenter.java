package com.example.android.tvshows.ui.episodes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.example.android.tvshows.BuildConfig;
import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.model.ExternalIdsTvShow;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.ui.showinfo.details.DetailsData;
import com.example.android.tvshows.util.Utility;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EpisodesPresenter implements EpisodesContract.Presenter {

    private ShowsRepository mShowsRepository;
    private EpisodesContract.View mEpisodeView;
    private int mShowId;
    private int mSeasonNumber;
    // the numbers of each season in the spinner
    private int[] mSeasonNumbers;
    private String[] mSeasonNames;
    private ApiService mApiService;
    private ArrayList<EpisodeData> mEpisodeData;
    private ExternalIdsTvShow mExternalIdsTvShow;

    @Inject
    public EpisodesPresenter(EpisodesContract.View episodeView,ShowsRepository showsRepository,ApiService apiService,int showId,int seasonNumber,int[] seasonNumbers,String[] seasonNames){
        mEpisodeView = episodeView;
        mShowsRepository = showsRepository;
        mShowId = showId;
        mSeasonNumber = seasonNumber;
        mSeasonNumbers = seasonNumbers;
        mSeasonNames = seasonNames;
        mApiService = apiService;
    }

    @Override
    public void loadEpisodesData(final Context context) {

        Observable<ArrayList<EpisodeData>> observable = Observable.create(new ObservableOnSubscribe<ArrayList<EpisodeData>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<EpisodeData>> e) throws Exception {
                e.onNext(mShowsRepository.getEpisodes(mShowId, mSeasonNumber));
            }
        });

        Consumer<ArrayList<EpisodeData>> consumer = new Consumer<ArrayList<EpisodeData>>() {
            @Override
            public void accept(@NonNull ArrayList<EpisodeData> episodeData) throws Exception {
                mEpisodeData = episodeData;
                mEpisodeView.episodeDataLoaded(episodeData.size());
            }
        };

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
    }

    @Override
    public EpisodeData getEpisodeData(Context context, int position) {
        return mEpisodeData.get(position);
    }

    public Intent getIntentForNewEpisodeActivity(Context context,int index){
        int newSeasonNumber = mSeasonNumbers[index];

        Intent intent = EpisodesActivity.getIntent(context, mShowId, mSeasonNames, mSeasonNumbers, index);

        if(newSeasonNumber == mSeasonNumber)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }

    @Override
    public String[] getSeasonNames() {
        return mSeasonNames;
    }

    @Override
    public boolean downloadExternalIds(final int episodeNumber) {
        if(mExternalIdsTvShow==null) {
            mApiService.getEpisodeExternalIds(String.valueOf(mShowId),
                    String.valueOf(mSeasonNumber),String.valueOf(episodeNumber),BuildConfig.TMDB_API_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ExternalIdsTvShow>() {
                        @Override
                        public void onSubscribe(Disposable d) {}

                        @Override
                        public void onNext(ExternalIdsTvShow externalIdsTvShow) {
                            mExternalIdsTvShow = externalIdsTvShow;
                            mEpisodeView.setIMDBId(mExternalIdsTvShow.getImdbId());
                        }

                        @Override
                        public void onError(Throwable e) {}
                        @Override
                        public void onComplete() {}
                    });
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String getTitle() {
        DetailsData detailsData = mShowsRepository.getShow(mShowId);
        return detailsData.name;
    }

    @Override
    public String getImdbId() {
        return mExternalIdsTvShow.getImdbId();
    }
}
