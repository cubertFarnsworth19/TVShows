package com.example.android.tvshows.ui.myshows.current;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.Pair;

import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.myshows.shows.ShowsContract;
import com.example.android.tvshows.util.Utility;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CurrentPresenter implements CurrentContract.Presenter{

    private static final int UPCOMING = 1;
    private static final int RECENT = 2;

    private ShowsRepository mShowsRepository;
    private CurrentContract.View mCurrentView;
    private int mCurrentType;
    // list of episodes during the time period, ordered by date (nearest to current date first)
    private ArrayList<CurrentInfo> mCurrent;
    // list of dates that have shows
    private ArrayList<ShowDate> mDates;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadShowsFromDatabase(context);
        }
    };

    public CurrentPresenter(CurrentContract.View currentView,ShowsRepository showsRepository,int currentType){
        mCurrentView = currentView;
        mShowsRepository = showsRepository;
        mCurrentType = currentType;
        registerReceivers();
    }

    public CurrentPresenter(CurrentContract.View currentView,ShowsRepository showsRepository,int currentType,
                            ArrayList<CurrentInfo> current, ArrayList<ShowDate> dates){
        mCurrentView = currentView;
        mShowsRepository = showsRepository;
        mCurrentType = currentType;
        mCurrent = current;
        mDates = dates;
        registerReceivers();
    }

    private void registerReceivers(){
        LocalBroadcastManager.getInstance(mCurrentView.getActivity()).registerReceiver((mBroadcastReceiver),
                new IntentFilter(ShowsRepository.INSERT_COMPLETE));
        LocalBroadcastManager.getInstance(mCurrentView.getActivity()).registerReceiver((mBroadcastReceiver),
                new IntentFilter(ShowsRepository.DELETE_COMPLETE));
    }

    @Override
    public void loadShowsFromDatabase(final Context context) {

        Observable<ArrayList<CurrentDatabaseLoad>> observable = Observable.create(new ObservableOnSubscribe<ArrayList<CurrentDatabaseLoad>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<CurrentDatabaseLoad>> e) throws Exception {
                if(mCurrentType == UPCOMING){
                    e.onNext(mShowsRepository.getEpisodesNextMonth());
                }
                else{
                    e.onNext(mShowsRepository.getEpisodesLastMonth());
                }
            }
        });

        Consumer<ArrayList<CurrentDatabaseLoad>> consumer = new Consumer<ArrayList<CurrentDatabaseLoad>>() {
            @Override
            public void accept(@NonNull ArrayList<CurrentDatabaseLoad> episodeData) throws Exception {

                setCurrent(episodeData,context);
                setDates(episodeData);
            }
        };

        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);

    }

    public void setCurrent(ArrayList<CurrentDatabaseLoad> currentDatabaseLoad,Context context){

        mCurrent = new ArrayList<>(currentDatabaseLoad.size());

        for(CurrentDatabaseLoad episode : currentDatabaseLoad ){
            mCurrent.add(new CurrentInfo(episode.showName,episode.episodeName,episode.overview,
                    context.getString(R.string.poster_path) + episode.posterPath));
        }

    }

    public void setDates(ArrayList<CurrentDatabaseLoad> currentDatabaseLoad){
        mDates = new ArrayList<>();

        for(CurrentDatabaseLoad episode : currentDatabaseLoad ){
            String date = Utility.getDateAsString(episode.airDateDay,episode.airDateMonth,episode.airDateYear);

            if(mDates.size()==0 || !mDates.get(mDates.size()-1).sameDate(date)) {
                if(mDates.size()==0)
                    mDates.add(new ShowDate(date,0));
                else
                    mDates.add(new ShowDate(date,mDates.get(mDates.size()-1).cumulativeNumberOfShows+mDates.get(mDates.size()-1).numberOfShows));
            }
            else{
                mDates.get(mDates.size()-1).addShow();
            }
        }


        mCurrentView.showsDataLoaded(mDates.size());
    }

    @Override
    public String getDate(int position) {
        return mDates.get(position).date;
    }

    @Override
    public int getNumberOfShowsOnDate(int position) {
        return mDates.get(position).numberOfShows;
    }

    @Override
    public String getShowPosterUrl(int dayPosition, int showPosition){
        return mCurrent.get(getCursorPosition(dayPosition,showPosition)).getPosterUrl();
    }

    @Override
    public String getShowName(int dayPosition, int showPosition) {
        return mCurrent.get(getCursorPosition(dayPosition,showPosition)).getShowName();
    }

    @Override
    public String getEpisodeName(int dayPosition, int showPosition) {
        return mCurrent.get(getCursorPosition(dayPosition,showPosition)).getEpisodeName();
    }

    @Override
    public String getShowOverview(int dayPosition, int showPosition) {
        return mCurrent.get(getCursorPosition(dayPosition,showPosition)).getOverview();
    }

    @Override
    public ArrayList<CurrentInfo> getCurrentInfo() {
        return mCurrent;
    }

    @Override
    public ArrayList<ShowDate> getDates() {
        return mDates;
    }

    private int getCursorPosition(int dayPosition, int showPosition) {
        return mDates.get(dayPosition).cumulativeNumberOfShows + showPosition;
    }

}
