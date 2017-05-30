package com.example.android.tvshows.ui.myshows.current;

import android.content.Context;
import android.database.Cursor;
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
    private ArrayList<CurrentInfo> mCurrent;
    private ArrayList<ShowDate> mDates;

    public CurrentPresenter(CurrentContract.View currentView,ShowsRepository showsRepository,int currentType){
        mCurrentView = currentView;
        mShowsRepository = showsRepository;
        mCurrentType = currentType;
    }

    public CurrentPresenter(CurrentContract.View currentView,ShowsRepository showsRepository,int currentType,
                            ArrayList<CurrentInfo> current, ArrayList<ShowDate> dates){
        mCurrentView = currentView;
        mShowsRepository = showsRepository;
        mCurrentType = currentType;
        mCurrent = current;
        mDates = dates;
    }

    @Override
    public void loadShowsFromDatabase(final Context context) {

        Observable<Cursor> observable = Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> e) throws Exception {
                if(mCurrentType == UPCOMING){
                    e.onNext(mShowsRepository.getEpisodesNextMonth());
                }
                else{
                    e.onNext(mShowsRepository.getEpisodesLastMonth());
                }
            }
        });

        Consumer<Cursor> consumer = new Consumer<Cursor>() {
            @Override
            public void accept(@NonNull Cursor cursor) throws Exception {

                mCurrent = new ArrayList<>(cursor.getCount());

                while (cursor.moveToNext()){
                    mCurrent.add(new CurrentInfo(
                            cursor.getString(cursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndex(ShowsDbContract.EpisodeEntry.COLUMN_EPISODE_NAME)),
                            cursor.getString(cursor.getColumnIndex(ShowsDbContract.EpisodeEntry.COLUMN_OVERVIEW)),
                            context.getString(R.string.poster_path) + cursor.getString(cursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_POSTER_PATH))));
                }

                setDates(cursor);
            }
        };

        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);

    }

    private void setDates(Cursor cursor){
        mDates = new ArrayList<>();
        cursor.moveToPosition(-1);
        while(cursor.moveToNext()){
            String date = Utility.getDateAsString(cursor.getInt(cursor.getColumnIndex(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_DAY)),
                    cursor.getInt(cursor.getColumnIndex(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_MONTH)),
                    cursor.getInt(cursor.getColumnIndex(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_YEAR)));

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

        cursor.close();
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
    public String getShowPosterUrl(Context context, int dayPosition, int showPosition){
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
