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
        Observable<ArrayList<ShowInfo>> observable = Observable.create(new ObservableOnSubscribe<ArrayList<ShowInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<ShowInfo>> e) throws Exception {
                e.onNext(mShowsRepository.getAllShows(continuing,favorite));
            }
        });

        Consumer<ArrayList<ShowInfo>> consumer = new Consumer<ArrayList<ShowInfo>>() {
            @Override
            public void accept(@NonNull ArrayList<ShowInfo> showsInfo) throws Exception {
                mShowsInfo = showsInfo;
                mShowsView.showsDataLoaded(showsInfo.size());
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
    public void removeShow(int position) {
        final int removeId = mShowsInfo.get(position).getId();
        mShowsInfo.remove(position);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mShowsRepository.deleteShow(removeId);
            }
        });
        thread.start();
    }

    @Override
    public ArrayList<ShowInfo> getShowsInfo() {
        return mShowsInfo;
    }

    @Override
    public Intent getIntentForShowInfoActivity(Context context, int position) {
        return ShowInfoActivity.getIntent(context, mShowsInfo.get(position).getId(),getTitle(position));
    }
}
