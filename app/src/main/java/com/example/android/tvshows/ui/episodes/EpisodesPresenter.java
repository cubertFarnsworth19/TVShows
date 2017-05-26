package com.example.android.tvshows.ui.episodes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.android.tvshows.BuildConfig;
import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.model.ExternalIdsTvShow;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.data.rest.ApiUtils;
import com.example.android.tvshows.util.Utility;

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
    private Cursor mEpisodesCursor;
    private ApiService mApiService;

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
    public void loadEpisodesData() {
        //mEpisodesCursor = mShowsRepository.getEpisodes(mShowId, mSeasonNumber);

        Observable<Cursor> observable = Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> e) throws Exception {
                e.onNext(mShowsRepository.getEpisodes(mShowId, mSeasonNumber));
            }
        });

        Consumer<Cursor> consumer = new Consumer<Cursor>() {
            @Override
            public void accept(@NonNull Cursor cursor) throws Exception {
                mEpisodesCursor = cursor;
                mEpisodeView.episodeDataLoaded(mEpisodesCursor.getCount());
            }
        };

        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);

        //mEpisodeView.episodeDataLoaded(mEpisodesCursor.getCount());
    }

    @Override
    public EpisodeData getEpisodeData(Context context, int position) {

        mEpisodesCursor.moveToPosition(position);
        int tmdbId = mEpisodesCursor.getInt(mEpisodesCursor.getColumnIndex(ShowsDbContract.EpisodeEntry._ID));
        String name = mEpisodesCursor.getString(mEpisodesCursor.getColumnIndex(ShowsDbContract.EpisodeEntry.COLUMN_EPISODE_NAME));
        String overview = mEpisodesCursor.getString(mEpisodesCursor.getColumnIndex(ShowsDbContract.EpisodeEntry.COLUMN_OVERVIEW));
        String stillPath = mEpisodesCursor.getString(mEpisodesCursor.getColumnIndex(ShowsDbContract.EpisodeEntry.COLUMN_STILL_PATH));

        Integer day = mEpisodesCursor.getInt(mEpisodesCursor.getColumnIndex(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_DAY));
        Integer month = mEpisodesCursor.getInt(mEpisodesCursor.getColumnIndex(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_MONTH));
        Integer year = mEpisodesCursor.getInt(mEpisodesCursor.getColumnIndex(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_YEAR));

        EpisodeData episodeData = new EpisodeData(tmdbId,name,overview,context.getString(R.string.poster_path) +stillPath, Utility.getDateAsString(day,month,year),position);
        return episodeData;
    }

    @Override
    public void startNewEpisodesActvity(Context context,int index) {
        int newSeasonNumber = mSeasonNumbers[index];
        if(newSeasonNumber != mSeasonNumber){
            Intent intent = new Intent(context, EpisodesActivity.class);
            intent.putExtra(ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY,mShowId);
            intent.putExtra(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NAME,mSeasonNames);
            intent.putExtra(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NUMBER,mSeasonNumbers);
            intent.putExtra("adapter_position",index);
            context.startActivity(intent);
            mEpisodeView.endActivity();
        }
    }

    @Override
    public String[] getSeasonNames() {
        return mSeasonNames;
    }

    @Override
    public void visitIMDbPage(final Context context,final int episodeNumber) {

        mApiService.getEpisodeExternalIds(String.valueOf(mShowId),String.valueOf(mSeasonNumber),String.valueOf(episodeNumber),BuildConfig.TMDB_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ExternalIdsTvShow>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(ExternalIdsTvShow externalIdsTvShow) {
                        String imdbId = externalIdsTvShow.getImdbId();
                        if(imdbId!="") {
                            Uri webpage = Uri.parse(context.getString(R.string.imdb_tv_show_webpage) + imdbId);
                            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                            if (intent.resolveActivity(context.getPackageManager()) != null)
                                context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {}
                    @Override
                    public void onComplete() {}
                });
    }

    @Override
    public void visitTMDBPage(Context context, int tmdbId) {
        Uri webpage = Uri.parse(context.getString(R.string.tmdb_tv_show_webpage) + tmdbId);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
    }

    @Override
    public void searchGoogle(Context context, String episodeName) {
        Cursor showCursor = mShowsRepository.getShow(mShowId);
        if (showCursor.moveToFirst()) {
            String showName = showCursor.getString(showCursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NAME));
            Uri webpage = Uri.parse(context.getString(R.string.google_search_webpage) + showName + " " + episodeName);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(context.getPackageManager()) != null)
                context.startActivity(intent);
        }
        showCursor.close();
    }

    @Override
    public void searchYouTube(Context context, String episodeName) {
        Cursor showCursor = mShowsRepository.getShow(mShowId);
        if (showCursor.moveToFirst()) {
            String showName = showCursor.getString(showCursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NAME));
            Uri webpage = Uri.parse(context.getString(R.string.youtube_search_webpage)  + showName + " " + episodeName);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(context.getPackageManager()) != null)
                context.startActivity(intent);
        }
        showCursor.close();
    }

    @Override
    public void closeCursor() {
        mEpisodesCursor.close();
    }

}
