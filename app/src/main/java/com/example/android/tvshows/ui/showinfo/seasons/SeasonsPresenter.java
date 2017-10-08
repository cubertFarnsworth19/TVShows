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
        Observable<ArrayList<SeasonInfo>> observable = Observable.create(new ObservableOnSubscribe<ArrayList<SeasonInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<SeasonInfo>> e) throws Exception {
                e.onNext(mShowsRepository.getSeasons(tmdbId));
            }
        });
        Consumer<ArrayList<SeasonInfo>> consumer = new Consumer<ArrayList<SeasonInfo>>() {
            @Override
            public void accept(@NonNull ArrayList<SeasonInfo> seasonsInfo) throws Exception {
                mSeasonsInfo = seasonsInfo;
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
    public Intent getIntentForEpisodesActivity(Context context, int adapterPosition) {
        String[] seasonsName = new String[mSeasonsInfo.size()];
        int [] seasonNumbers = new int[mSeasonsInfo.size()];

        for (int i=0;i<seasonsName.length;i++){
            seasonsName[i] = mSeasonsInfo.get(i).seasonName;
            seasonNumbers[i] = mSeasonsInfo.get(i).seasonNumber;
        }

        return EpisodesActivity.getIntent(context,tmdbId,seasonsName,seasonNumbers,adapterPosition);
    }

    @Override
    public ArrayList<SeasonInfo> getSeasonsInfo() {
        return mSeasonsInfo;
    }


}



