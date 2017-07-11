package com.example.android.tvshows.ui.myshows.shows;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.myshows.FilterMyShowsDialog;
import com.example.android.tvshows.ui.showinfo.ShowInfoActivity;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ShowsPresenter implements ShowsContract.Presenter {

    private ShowsRepository mShowsRepository;
    private ShowsContract.View mShowsView;
    private ArrayList<ShowInfo> mShowsInfo;
    private BroadcastReceiver mBroadcastReceiver;
    private boolean mContinuing = false;
    private boolean mFavorite = false;

    public ShowsPresenter(ShowsContract.View showsView,ShowsRepository showsRepository){
        mShowsView = showsView;
        mShowsRepository = showsRepository;
        setBroadcastReceiver();
        registerReceivers();
    }

    public ShowsPresenter(ShowsContract.View showsView,ShowsRepository showsRepository,ArrayList<ShowInfo> showsInfo){
        mShowsView = showsView;
        mShowsRepository = showsRepository;
        mShowsInfo = showsInfo;
        setBroadcastReceiver();
        registerReceivers();
    }

    private void setBroadcastReceiver(){
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(ShowsRepository.INSERT_COMPLETE))
                    loadShowsFromDatabase(context,mContinuing,mFavorite);
                else if((intent.getAction().equals(FilterMyShowsDialog.FILTER_SHOWS))){
                    mContinuing = intent.getBooleanExtra(FilterMyShowsDialog.CONTINUING,false);
                    mFavorite = intent.getBooleanExtra(FilterMyShowsDialog.FAVORITE,false);
                    loadShowsFromDatabase(context,mContinuing,mFavorite);
                }
            }
        };
    }

    private void registerReceivers(){
        LocalBroadcastManager.getInstance(mShowsView.getActivity()).registerReceiver((mBroadcastReceiver),
                new IntentFilter(ShowsRepository.INSERT_COMPLETE));
        LocalBroadcastManager.getInstance(mShowsView.getActivity()).registerReceiver((mBroadcastReceiver),
                new IntentFilter(FilterMyShowsDialog.FILTER_SHOWS));
    }

    @Override
    public void loadShowsFromDatabase(final Context context, final boolean continuing, final boolean favorite) {

        Observable<Cursor> observable = Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> e) throws Exception {
                e.onNext(mShowsRepository.getAllShows(continuing,favorite));
            }
        });

        Consumer<Cursor> consumer = new Consumer<Cursor>() {
            @Override
            public void accept(@NonNull Cursor cursor) throws Exception {
                mShowsInfo = new ArrayList<>();
                while (cursor.moveToNext()){
                    mShowsInfo.add(new ShowInfo(
                            context,
                            cursor.getInt(cursor.getColumnIndex(ShowsDbContract.ShowsEntry._ID)),
                            cursor.getString(cursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_POSTER_PATH)),
                            cursor.getInt(cursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NUM_SEASONS)),
                            cursor.getInt(cursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NUM_EPISODES)),
                            cursor.getInt(cursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_IN_PRODUCTION)),
                            cursor.getInt(cursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_FAVORITE))
                    ));
                }

                mShowsView.showsDataLoaded(cursor.getCount());
                cursor.close();
            }
        };

        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);

    }

    @Override
    public String getTitle(int position) {
        return mShowsInfo.get(position).getTitle();
    }

    @Override
    public String getPosterUrl(int position) {
        return mShowsInfo.get(position).getPosterUrl();
    }

    @Override
    public String getNumberOfSeasons(Context context,int position) {
        String seasons = mShowsInfo.get(position).getNumberOfSeasons();
        if(seasons.equals("1")) seasons += context.getString(R.string.seasons1);
        else seasons += context.getString(R.string.seasons);
        return seasons;
    }

    @Override
    public String getNumberOfEpisodes(Context context,int position) {
        String episodes = mShowsInfo.get(position).getNumberOfEpisodes();
        if(episodes.equals("1")) episodes += context.getString(R.string.episodes1);
        else episodes += context.getString(R.string.episodes);
        return episodes;
    }

    @Override
    public String getInProduction(int position) {
        return mShowsInfo.get(position).getInProduction();
    }

    @Override
    public boolean isFavorite(int position) {
        return mShowsInfo.get(position).isFavorite();
    }

    @Override
    public void setFavorite(int position, boolean favorite) {
        mShowsInfo.get(position).setFavorite(favorite);
        mShowsRepository.setFavorite(mShowsInfo.get(position).getId(),favorite);
    }

    @Override
    public void startShowInfoActivity(Context context, int position) {
        Intent intent = new Intent(context, ShowInfoActivity.class);
        intent.putExtra(ShowsDbContract.ShowsEntry._ID,mShowsInfo.get(position).getId());

        intent.putExtra(ShowsDbContract.ShowsEntry.COLUMN_NAME,mShowsInfo.get(position).getTitle());
        context.startActivity(intent);
    }

    @Override
    public void removeShow(int position) {
        int removeId = mShowsInfo.get(position).getId();
        mShowsInfo.remove(position);
        mShowsRepository.deleteShow(removeId);
    }

    @Override
    public ArrayList<ShowInfo> getShowsInfo() {
        return mShowsInfo;
    }

}
