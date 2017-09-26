package com.example.android.tvshows.ui.updates;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.Pair;
import android.util.Log;

import com.example.android.tvshows.BuildConfig;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.model.credits.Credits;
import com.example.android.tvshows.data.model.season.Season;
import com.example.android.tvshows.data.model.tvshowdetailed.TVShowDetailed;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.service.DownloadService;
import com.example.android.tvshows.ui.RetryUntilDownloaded;
import com.example.android.tvshows.ui.myshows.FilterMyShowsDialog;
import com.example.android.tvshows.util.Utility;

import java.util.ArrayList;
import java.util.Hashtable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
/*
    Parcelable has not been implemented properly but works for device rotation
 */
public class UpdatesPresenter implements UpdatesContract.Presenter{

    private UpdatesContract.View mUpdatesView;
    private ShowsRepository mShowsRepository;
    private ArrayList<TVShow> mTVShows;
    // key is the show id and data is the array of seasons
    private Hashtable<Integer,ArrayList<SeasonForUpdate>> mHashtableSeasons;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadShowsFromDatabase(true);
        }
    };

    public UpdatesPresenter(UpdatesContract.View updatesView, ShowsRepository showsRepository) {
        mUpdatesView = updatesView;
        mShowsRepository = showsRepository;
        registerReceivers();
    }

    private void registerReceivers(){
        LocalBroadcastManager.getInstance(mUpdatesView.getActivity()).registerReceiver((mBroadcastReceiver),
                new IntentFilter(ShowsRepository.UPDATE_COMPLETE));
    }

    @Override
    public void loadShowsFromDatabase(boolean update) {
        mTVShows = mShowsRepository.getAllShowsUpdate();

        setHashtableSeasons(mShowsRepository.getAllSeasons());

        display(update);
    }

    private void display(boolean update){
        if (update) mUpdatesView.displayUpdate();
        else mUpdatesView.showsDataLoaded(mTVShows.size());
    }

    private void setHashtableSeasons(ArrayList<SeasonForUpdate> seasonsForUpdate){
        mHashtableSeasons = new Hashtable<>();

        for(int i=0;i<seasonsForUpdate.size();){

            ArrayList<SeasonForUpdate> seasons = new ArrayList<>();
            do{
                seasons.add(seasonsForUpdate.get(i));
            }
            while(++i<seasonsForUpdate.size() && seasonsForUpdate.get(i-1).showId==seasonsForUpdate.get(i).showId);

            mHashtableSeasons.put(seasonsForUpdate.get(i-1).showId,seasons);
        }

    }

    @Override
    public int getShowId(int position) {
        return mTVShows.get(position).id;
    }

    @Override
    public int getNumberOfSeasons(int position) {
        Log.v("position",""+position);
        return mHashtableSeasons.get(mTVShows.get(position).id).size();
    }

    @Override
    public String getShowName(int position) {
        return mTVShows.get(position).name;
    }

    //the last update for the most recent season/ tv details update
    @Override
    public String getLastUpdate(int position) {
        ArrayList<SeasonForUpdate> seasons = mHashtableSeasons.get(mTVShows.get(position).id);

        SeasonForUpdate earliestUpdate = seasons.get(0);
        for(int i=1;i<seasons.size();i++){
            if(!earliestUpdate.earlierUpdate(seasons.get(i))) earliestUpdate = seasons.get(i);
        }

        if(!earliestUpdate.earlierUpdate(mTVShows.get(position).updateDay,mTVShows.get(position).updateMonth,mTVShows.get(position).updateYear))
            return mTVShows.get(position).lastUpdate;
        else
            return earliestUpdate.lastUpdate;
    }

    @Override
    public String getShowLastUpdate(int position) {
        return mTVShows.get(position).lastUpdate;
    }

    @Override
    public String getSeasonName(Integer showId,int position) {
        return mHashtableSeasons.get(showId).get(position).name;
    }

    @Override
    public String getSeasonLastUpdate(Integer showId,int position) {
        return mHashtableSeasons.get(showId).get(position).lastUpdate;
    }

    @Override
    public void makeUpdatesRequest(Context context, ArrayList<Pair<Boolean,ArrayList<Boolean>>> checked) {

        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            for (int i = 0; i < checked.size(); i++) {
                if (checked.get(i).first) {
                    Intent intent = new Intent(context, DownloadService.class);
                    intent.putExtra(DownloadService.DOWNLOAD_TYPE, DownloadService.UPDATE_DETAILS);
                    intent.putExtra(DownloadService.TMDB_ID, mTVShows.get(i).id);
                    intent.putExtra(DownloadService.NUMBER_OF_SEASONS, checked.get(i).second.size());
                    context.startService(intent);
                }

                ArrayList<Integer> seasonsNumber = new ArrayList<>();
                for (int j = 0; j < checked.get(i).second.size(); j++) {
                    if (checked.get(i).second.get(j))
                        seasonsNumber.add(mHashtableSeasons.get(mTVShows.get(i).id).get(j).seasonNumber);
                }

                if (seasonsNumber.size() > 0) {
                    Intent intent = new Intent(context, DownloadService.class);
                    intent.putExtra(DownloadService.DOWNLOAD_TYPE, DownloadService.UPDATE_SEASONS);
                    intent.putExtra(DownloadService.TMDB_ID, mTVShows.get(i).id);
                    intent.putExtra(DownloadService.SEASONS_NUMBER, seasonsNumber);
                    context.startService(intent);
                }
            }
        }else{
            mUpdatesView.noConnection();
        }

    }


    protected UpdatesPresenter(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UpdatesPresenter> CREATOR = new Parcelable.Creator<UpdatesPresenter>() {
        @Override
        public UpdatesPresenter createFromParcel(Parcel in) {
            return new UpdatesPresenter(in);
        }

        @Override
        public UpdatesPresenter[] newArray(int size) {
            return new UpdatesPresenter[size];
        }
    };

}
