package com.example.android.tvshows.ui.showinfo.seasons;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.episodes.EpisodesActivity;
import com.example.android.tvshows.util.Utility;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SeasonsPresenter implements SeasonsContract.Presenter{

    private SeasonsContract.View mSeasonsView;
    private ShowsRepository mShowsRepository;
    private int tmdbId;
    private Cursor mSeasonsCursor;

    public SeasonsPresenter(SeasonsContract.View seasonsView,ShowsRepository showsRepository,int tmdbId){
        mSeasonsView = seasonsView;
        mShowsRepository = showsRepository;
        this.tmdbId = tmdbId;

    }

    @Override
    public void loadSeasonsData() {
//        mSeasonsCursor = mShowsRepository.getSeasons(tmdbId);
//        mSeasonsView.seasonDataLoaded(mSeasonsCursor.getCount());

        Observable<Cursor> observable = Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> e) throws Exception {
                e.onNext(mShowsRepository.getSeasons(tmdbId));
            }
        });

        Consumer<Cursor> consumer = new Consumer<Cursor>() {
            @Override
            public void accept(@NonNull Cursor cursor) throws Exception {
                mSeasonsCursor = cursor;
                mSeasonsView.seasonDataLoaded(mSeasonsCursor.getCount());
            }
        };

        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
    }

    @Override
    public String getSeasonName(int adapterPosition) {
        mSeasonsCursor.moveToPosition(adapterPosition);
        return mSeasonsCursor.getString(mSeasonsCursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NAME));
    }

    @Override
    public String getPosterUrl(Context context, int adapterPosition) {
        mSeasonsCursor.moveToPosition(adapterPosition);
        String posterPath = mSeasonsCursor.getString(mSeasonsCursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_POSTER_PATH));
        return context.getString(R.string.poster_path) + posterPath;
    }

    @Override
    public String getAirDate(int position) {
        mSeasonsCursor.moveToPosition(position);
        Integer day = mSeasonsCursor.getInt(mSeasonsCursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_AIR_DATE_DAY));
        Integer month = mSeasonsCursor.getInt(mSeasonsCursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_AIR_DATE_MONTH));
        Integer year = mSeasonsCursor.getInt(mSeasonsCursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_AIR_DATE_YEAR));
        return Utility.getDateAsString(day,month,year);
    }

    @Override
    public String getOverview(int position) {
        mSeasonsCursor.moveToPosition(position);
        return mSeasonsCursor.getString(mSeasonsCursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_OVERVIEW));
    }

    @Override
    public String getNumberOfEpisodes(int position) {
        mSeasonsCursor.moveToPosition(position);
        Integer episodes = mSeasonsCursor.getInt(mSeasonsCursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_NUMBER_OF_EPISODES));
        return episodes.toString();
    }

    @Override
    public void startEpisodesActivity(Context context,int adapterPosition) {
        mSeasonsCursor.moveToPosition(adapterPosition);

        Intent intent = new Intent(context, EpisodesActivity.class);
        intent.putExtra(ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY,tmdbId);
        String[] seasonsName = new String[mSeasonsCursor.getCount()];
        int [] seasonNumbers = new int[mSeasonsCursor.getCount()];

        for (int i=0;i<seasonsName.length;i++){
            mSeasonsCursor.moveToPosition(i);
            seasonsName[i] = mSeasonsCursor.getString(mSeasonsCursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NAME));
            seasonNumbers[i] = mSeasonsCursor.getInt(mSeasonsCursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NUMBER));
        }

        intent.putExtra(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NAME,seasonsName);
        intent.putExtra(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NUMBER,seasonNumbers);
        intent.putExtra("adapter_position",adapterPosition);

        context.startActivity(intent);
    }

    @Override
    public void closeCursor() {
        mSeasonsCursor.close();
    }


}
