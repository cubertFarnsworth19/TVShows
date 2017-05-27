package com.example.android.tvshows.ui.updates;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

public class UpdatesPresenter implements UpdatesContract.Presenter{

    private UpdatesContract.View mUpdatesView;
    //private ApiService mApiService;
    private ShowsRepository mShowsRepository;
    private ArrayList<TVShow> mTVShows = new ArrayList<>();
    private Hashtable<Integer,ArrayList<SeasonForUpdate>> mHashtableSeasons = new Hashtable<>();

    public UpdatesPresenter(UpdatesContract.View updatesView, ShowsRepository showsRepository) {
        mUpdatesView = updatesView;
        //mApiService = apiService;
        mShowsRepository = showsRepository;
    }

    @Override
    public void loadShowsFromDatabase() {

        Cursor cursor = mShowsRepository.getAllShows();

        while (cursor.moveToNext()){
            mTVShows.add(new TVShow(
                    cursor.getInt(cursor.getColumnIndex(ShowsDbContract.ShowsEntry._ID)),
                    cursor.getString(cursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NAME)),
                    cursor.getInt(cursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_LAST_UPDATE_DAY)),
                    cursor.getInt(cursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_LAST_UPDATE_MONTH)),
                    cursor.getInt(cursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_LAST_UPDATE_YEAR))));
        }

        cursor.close();

        Cursor seasonsCursor = mShowsRepository.getAllSeasons();

        seasonsCursor.moveToFirst();

        while(!seasonsCursor.isAfterLast()){

            ArrayList<SeasonForUpdate> seasons = new ArrayList<>();

            Integer currentShowId = seasonsCursor.getInt(seasonsCursor.getColumnIndex(ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY));

            do{
                seasons.add(new SeasonForUpdate(seasonsCursor.getString(seasonsCursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NAME)),
                        seasonsCursor.getInt(seasonsCursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NUMBER)),
                        seasonsCursor.getInt(seasonsCursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_LAST_UPDATE_DAY)),
                        seasonsCursor.getInt(seasonsCursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_LAST_UPDATE_MONTH)),
                        seasonsCursor.getInt(seasonsCursor.getColumnIndex(ShowsDbContract.SeasonEntry.COLUMN_LAST_UPDATE_YEAR))));

            }while(seasonsCursor.moveToNext() && currentShowId == seasonsCursor.getInt(seasonsCursor.getColumnIndex(ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY)));

            mHashtableSeasons.put(currentShowId,seasons);

        }

        mUpdatesView.showsDataLoaded(mTVShows.size());
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
        ArrayList<SeasonForUpdate> seasons = mHashtableSeasons.get(showId);
        return mHashtableSeasons.get(showId).get(position).name;
    }

    @Override
    public String getSeasonLastUpdate(Integer showId,int position) {
        return mHashtableSeasons.get(showId).get(position).lastUpdate;
    }

    @Override
    public void makeUpdatesRequest(ArrayList<Pair<Boolean,ArrayList<Boolean>>> checked) {
        for(int i=0;i<checked.size();i++){
            if(checked.get(i).first){
                Context context = mShowsRepository.getContext();
                Intent intent = new Intent(context,DownloadService.class);
                intent.putExtra(DownloadService.DOWNLOAD_TYPE, DownloadService.UPDATE_DETAILS);
                intent.putExtra(DownloadService.TMDB_ID,mTVShows.get(i).id);
                intent.putExtra(DownloadService.NUMBER_OF_SEASONS,checked.get(i).second.size());
                context.startService(intent);
            }


            ArrayList<Integer> seasonsNumber = new ArrayList<>();
            for(int j=0;j<checked.get(i).second.size();j++){
                if(checked.get(i).second.get(j))
                    seasonsNumber.add(mHashtableSeasons.get(mTVShows.get(i).id).get(j).seasonNumber);
            }

            if(seasonsNumber.size()>0) {
                //updateSeasons(mTVShows.get(i).id, seasonsNumber);
                Context context = mShowsRepository.getContext();
                Intent intent = new Intent(context,DownloadService.class);
                intent.putExtra(DownloadService.DOWNLOAD_TYPE, DownloadService.UPDATE_SEASONS);
                intent.putExtra(DownloadService.TMDB_ID,mTVShows.get(i).id);
                intent.putExtra(DownloadService.SEASONS_NUMBER,seasonsNumber);
                context.startService(intent);
            }

        }
    }

    private class TVShow{
        Integer id;
        String name;
        int updateDay;
        int updateMonth;
        int updateYear;
        String lastUpdate;

        public TVShow(Integer id, String name, int updateDay, int updateMonth, int updateYear) {
            this.id = id;
            this.name = name;
            this.updateDay = updateDay;
            this.updateMonth = updateMonth;
            this.updateYear = updateYear;
            lastUpdate = Utility.getDateAsString(updateDay,updateMonth,updateYear);
        }
    }

    private class SeasonForUpdate{
        String name;
        int seasonNumber;
        int updateDay;
        int updateMonth;
        int updateYear;
        String lastUpdate;

        public SeasonForUpdate(String name, int seasonNumber, int updateDay, int updateMonth, int updateYear) {
            this.name = name;
            this.seasonNumber = seasonNumber;
            this.updateDay = updateDay;
            this.updateMonth = updateMonth;
            this.updateYear = updateYear;
            lastUpdate = Utility.getDateAsString(updateDay,updateMonth,updateYear);
        }

        public boolean earlierUpdate(int d,int m,int y){
            if(updateYear<y)
                return true;
            if(updateYear==y && updateMonth<m)
                return true;
            if(updateYear==y && updateMonth==m && updateDay<d)
                return true;
            return false;
        }

        public boolean earlierUpdate(SeasonForUpdate season){
            return earlierUpdate(season.updateDay,season.updateMonth, season.updateYear);
        }
    }

}
