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
    private Cursor mCurrentCursor;
    private int mCurrentType;

    private ArrayList<ShowDate> mDates;

    public CurrentPresenter(CurrentContract.View currentView,ShowsRepository showsRepository,int currentType){
        mCurrentView = currentView;
        mShowsRepository = showsRepository;
        mCurrentType = currentType;
    }

    @Override
    public void loadShowsFromDatabase() {

//        if(mCurrentType == UPCOMING){
//            mCurrentCursor = mShowsRepository.getEpisodesNextMonth();
//        }
//        else{
//            mCurrentCursor = mShowsRepository.getEpisodesLastMonth();
//        }

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
                mCurrentCursor = cursor;
                setDates();
            }
        };

        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);

    }

    private void setDates(){
        mDates = new ArrayList<>();
        while(mCurrentCursor.moveToNext()){
            String date = Utility.getDateAsString(mCurrentCursor.getInt(mCurrentCursor.getColumnIndex(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_DAY)),
                    mCurrentCursor.getInt(mCurrentCursor.getColumnIndex(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_MONTH)),
                    mCurrentCursor.getInt(mCurrentCursor.getColumnIndex(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_YEAR)));

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
    public String getShowPosterUrl(Context context, int dayPosition, int showPosition){
       mCurrentCursor.moveToPosition(getCursorPosition(dayPosition,showPosition));
        String posterUrl = context.getString(R.string.poster_path)
                + mCurrentCursor.getString(mCurrentCursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_POSTER_PATH));
        return posterUrl;
    }

    @Override
    public String getShowName(int dayPosition, int showPosition) {
        mCurrentCursor.moveToPosition(getCursorPosition(dayPosition,showPosition));
        return mCurrentCursor.getString(mCurrentCursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NAME));
    }

    @Override
    public String getEpisodeName(int dayPosition, int showPosition) {
        mCurrentCursor.moveToPosition(getCursorPosition(dayPosition,showPosition));
        return mCurrentCursor.getString(mCurrentCursor.getColumnIndex(ShowsDbContract.EpisodeEntry.COLUMN_EPISODE_NAME));
    }

    @Override
    public String getShowOverview(int dayPosition, int showPosition) {
        mCurrentCursor.moveToPosition(getCursorPosition(dayPosition,showPosition));
        return mCurrentCursor.getString(mCurrentCursor.getColumnIndex(ShowsDbContract.EpisodeEntry.COLUMN_OVERVIEW));
    }

    private int getCursorPosition(int dayPosition, int showPosition) {
        return mDates.get(dayPosition).cumulativeNumberOfShows + showPosition;
    }

    class ShowDate{
        String date;
        int numberOfShows;
        // the number of shows prior to this day in the adapter
        int cumulativeNumberOfShows;

        public ShowDate(String date,int cumulativeNumberOfShows) {
            this.date = date;
            this.numberOfShows = 1;
            this.cumulativeNumberOfShows = cumulativeNumberOfShows;
        }

        void addShow(){
            numberOfShows++;
        }

        boolean sameDate(String date){
            return this.date.equals(date);
        }
    }

}
