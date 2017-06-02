package com.example.android.tvshows.ui.showinfo.seasons;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.episodes.EpisodesActivity;
import com.example.android.tvshows.util.Utility;

import java.util.ArrayList;

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
    private ArrayList<SeasonInfo> mSeasonsInfo;

    public SeasonsPresenter(SeasonsContract.View seasonsView,ShowsRepository showsRepository,int tmdbId){
        mSeasonsView = seasonsView;
        mShowsRepository = showsRepository;
        this.tmdbId = tmdbId;
    }

    public SeasonsPresenter(SeasonsContract.View seasonsView,ShowsRepository showsRepository,int tmdbId,ArrayList<SeasonInfo> seasonsInfo){
        mSeasonsView = seasonsView;
        mShowsRepository = showsRepository;
        this.tmdbId = tmdbId;
        mSeasonsInfo = seasonsInfo;
    }

    @Override
    public void loadSeasonsData(final Context context) {

        Observable<Cursor> observable = Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> e) throws Exception {
                e.onNext(mShowsRepository.getSeasons(tmdbId));
            }
        });

        Consumer<Cursor> consumer = new Consumer<Cursor>() {
            @Override
            public void accept(@NonNull Cursor cursor) throws Exception {

                mSeasonsInfo = new ArrayList<>();

                while (cursor.moveToNext()){

                    mSeasonsInfo.add(new SeasonInfo(
                            cursor.getString(cursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NAME)),
                            cursor.getInt(cursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_AIR_DATE_DAY)),
                            cursor.getInt(cursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_AIR_DATE_MONTH)),
                            cursor.getInt(cursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_AIR_DATE_YEAR)),
                            context.getString(R.string.poster_path) + cursor.getString(cursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_POSTER_PATH)),
                            cursor.getString(cursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_OVERVIEW)),
                            cursor.getInt(cursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_NUMBER_OF_EPISODES)),
                            cursor.getInt(cursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NUMBER))));
                }

                cursor.close();
                mSeasonsView.seasonDataLoaded(mSeasonsInfo.size());
            }
        };

        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
    }

    @Override
    public String getSeasonName(int adapterPosition) {
        return mSeasonsInfo.get(adapterPosition).seasonName;
    }

    @Override
    public String getPosterUrl(Context context, int adapterPosition) {
        return mSeasonsInfo.get(adapterPosition).posterUrl;
    }

    @Override
    public String getAirDate(int adapterPosition) {
        return mSeasonsInfo.get(adapterPosition).airDate;
    }

    @Override
    public String getOverview(int adapterPosition) {
        return mSeasonsInfo.get(adapterPosition).overview;
    }

    @Override
    public String getNumberOfEpisodes(int adapterPosition,Context context) {
        String episodes = mSeasonsInfo.get(adapterPosition).numberOfEpisodes;

        if(episodes.equals("1")) episodes += context.getString(R.string.episodes1);
        else episodes += context.getString(R.string.episodes);

        return episodes;
    }

    @Override
    public void startEpisodesActivity(Context context,int adapterPosition) {

        Intent intent = new Intent(context, EpisodesActivity.class);
        intent.putExtra(ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY,tmdbId);
        String[] seasonsName = new String[mSeasonsInfo.size()];
        int [] seasonNumbers = new int[mSeasonsInfo.size()];

        for (int i=0;i<seasonsName.length;i++){
            seasonsName[i] = mSeasonsInfo.get(i).seasonName;
            seasonNumbers[i] = mSeasonsInfo.get(i).seasonNumber;
        }

        intent.putExtra(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NAME,seasonsName);
        intent.putExtra(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NUMBER,seasonNumbers);
        intent.putExtra("adapter_position",adapterPosition);


        context.startActivity(intent);
    }

    @Override
    public ArrayList<SeasonInfo> getSeasonsInfo() {
        return mSeasonsInfo;
    }


}



