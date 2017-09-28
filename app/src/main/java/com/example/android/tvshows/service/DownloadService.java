package com.example.android.tvshows.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.tvshows.*;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.model.credits.Credits;
import com.example.android.tvshows.data.model.season.Season;
import com.example.android.tvshows.data.model.tvshowdetailed.TVShowDetailed;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.ui.*;
//import com.example.android.tvshows.ui.find.service.DaggerServiceComponent;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class DownloadService extends Service {

    public static final String DOWNLOAD_TYPE = "download_type";
    public static final String TMDB_ID = "tmdb_id";
    public static final String NUMBER_OF_SEASONS = "seasons";
    public static final String SEASONS_NUMBER = "seasons_number";
    public static final int RESULTS = 1;
    public static final int UPDATE_DETAILS = 2;
    public static final int UPDATE_SEASONS = 3;

    @Inject ShowsRepository mShowsRepository;
    @Inject ApiService mApiService;

    @Override
    public void onCreate() {
        super.onCreate();

        ShowsApplication showsApplication = (ShowsApplication) getApplication();

//        ServiceComponent component = DaggerServiceComponent.builder()
//                .applicationComponent(ShowsApplication.get(this).getComponent())
//                .serviceModule(new ServiceModule(this))
//                .build();

        ServiceComponent component = DaggerServiceComponent.builder()
                .applicationComponent(showsApplication.get(this).getComponent())
                .serviceModule(showsApplication.getServiceModule(this))
                .build();

        component.inject(this);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int downloadType = intent.getIntExtra(DOWNLOAD_TYPE,0);

        if(downloadType==RESULTS) {
            Integer id = intent.getIntExtra(TMDB_ID,-1);

            if (id!=-1) downloadResults(id);
        }
        else if(downloadType==UPDATE_DETAILS){
            Integer id = intent.getIntExtra(TMDB_ID,-1);
            int numberOfSeasons = intent.getIntExtra(NUMBER_OF_SEASONS ,1);
            if (id!=-1) updateShowDetails(id,numberOfSeasons);
        }
        else if(downloadType==UPDATE_SEASONS){
            Integer id = intent.getIntExtra(TMDB_ID,-1);
            ArrayList<Integer> seasonsNumber = intent.getIntegerArrayListExtra(SEASONS_NUMBER);
            if (id!=-1) updateSeasons(id,seasonsNumber);
        }

        return START_NOT_STICKY;
    }

    private void downloadResults(Integer id){

        final String strId = id.toString();
        Observable<TVShowDetailed> observableTvShow = mApiService.getTVShowDetailed(strId, BuildConfig.TMDB_API_KEY)
                .retryWhen(new RetryUntilDownloaded(2000));
        Observable<Credits> observableCredits = mApiService.getCredits(strId,BuildConfig.TMDB_API_KEY,"1")
                .retryWhen(new RetryUntilDownloaded(2000));


        Observable<Object[]> observableZipped = Observable.zip(observableTvShow,observableCredits, new BiFunction<TVShowDetailed, Credits, Object[]>(){
            @Override
            public Object[] apply(@io.reactivex.annotations.NonNull TVShowDetailed tvShowDetailed,
                                  @io.reactivex.annotations.NonNull Credits credits) throws Exception {
                return new Object[]{tvShowDetailed, credits};
            }
        });

        observableZipped.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object[] objects) {
                        TVShowDetailed tvShowDetailed = (TVShowDetailed) objects[0];
                        Log.e("Show Details No Error",strId + "  " + tvShowDetailed.getName());
                        completeDownload((TVShowDetailed) objects[0],(Credits)objects[1],strId);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void completeDownload(final TVShowDetailed tvShowDetailed, final Credits credits, String strId){

        int seasonsListSize = tvShowDetailed.getSeasonsListSize();

        Observable<Season>[] observableSeasons = new Observable[seasonsListSize];

        for(Integer i=0;i<seasonsListSize;i++){
            observableSeasons[i] = mApiService
                    .getSeason(strId,tvShowDetailed.getSeasons().get(i).getSeasonNumber().toString(),BuildConfig.TMDB_API_KEY,"1")
                    .retryWhen(new RetryUntilDownloaded(2000));
        }

        Observable<Season[]> observableZipped = Observable.zipArray(new Function<Object[],Season[]>(){
            @Override
            public Season[] apply(@io.reactivex.annotations.NonNull Object[] objects) throws Exception {
                Season[] seasons = new Season[objects.length];
                for(int i=0;i<objects.length;i++)
                    seasons[i] = (Season) objects[i];
                return seasons;
            }
        },false,100,observableSeasons);

        observableZipped.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Season[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Season[] seasons) {
                        Log.e("Downloaded No Error",tvShowDetailed.getName());
                        mShowsRepository.insertShowIntoDatabase(tvShowDetailed,credits,seasons);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updateShowDetails(final Integer id, final int numberOfSeasons){
        final String strId = id.toString();
        Observable<TVShowDetailed> observableTvShow = mApiService.getTVShowDetailed(strId, BuildConfig.TMDB_API_KEY)
                .retryWhen(new RetryUntilDownloaded(2000));
        Observable<Credits> observableCredits = mApiService.getCredits(strId,BuildConfig.TMDB_API_KEY,"1")
                .retryWhen(new RetryUntilDownloaded(2000));

        Observable<Object[]> observableZipped = Observable.zip(observableTvShow,observableCredits, new BiFunction<TVShowDetailed, Credits, Object[]>(){
            @Override
            public Object[] apply(@io.reactivex.annotations.NonNull TVShowDetailed tvShowDetailed,
                                  @io.reactivex.annotations.NonNull Credits credits) throws Exception {
                return new Object[]{tvShowDetailed, credits};
            }
        });

        observableZipped.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext( Object[] objects) {
                        TVShowDetailed tvShowDetailed = (TVShowDetailed)objects[0];
                        mShowsRepository.updateTVShowDetails(tvShowDetailed,(Credits)objects[1]);

                        if(tvShowDetailed.getSeasonsListSize()>numberOfSeasons)
                            downloadNewSeasons(numberOfSeasons,tvShowDetailed);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    // download any additional seasons since the last update
    private void downloadNewSeasons(int numberOfSeasons,final TVShowDetailed tvShowDetailed){
        int seasonsListSize = tvShowDetailed.getSeasonsListSize() - numberOfSeasons;

        Observable<Season>[] observableSeasons = new Observable[seasonsListSize];

        for(Integer i=numberOfSeasons;i<tvShowDetailed.getSeasonsListSize();i++){
            observableSeasons[i-numberOfSeasons] = mApiService
                    .getSeason(tvShowDetailed.getId().toString(),tvShowDetailed.getSeasons().get(i).getSeasonNumber().toString(),BuildConfig.TMDB_API_KEY,"1")
                    .retryWhen(new RetryUntilDownloaded(2000));
        }

        Observable<Season[]> observableZipped = Observable.zipArray(new Function<Object[],Season[]>(){
            @Override
            public Season[] apply(@io.reactivex.annotations.NonNull Object[] objects) throws Exception {
                Season[] seasons = new Season[objects.length];
                for(int i=0;i<objects.length;i++)
                    seasons[i] = (Season) objects[i];
                return seasons;
            }
        },false,100,observableSeasons);

        observableZipped.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Season[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Season[] seasons) {
                        Log.e("Downloaded No Error","");
                        mShowsRepository.insertAdditionalSeasonsIntoDatabase(tvShowDetailed.getId(),seasons);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void updateSeasons(final Integer id,ArrayList<Integer> seasonsNumber){

        Observable<Season>[] observableSeasons = new Observable[seasonsNumber.size()];

        for(Integer i=0;i<observableSeasons.length;i++){
            observableSeasons[i] = mApiService.getSeason(id.toString(),seasonsNumber.get(i).toString(),BuildConfig.TMDB_API_KEY,"1")
                    .retryWhen(new RetryUntilDownloaded(2000));
        }

        Observable<Season[]> observableZipped = Observable.zipArray(new Function<Object[],Season[]>(){
            @Override
            public Season[] apply(@io.reactivex.annotations.NonNull Object[] objects) throws Exception {
                Season[] seasons = new Season[objects.length];
                for(int i=0;i<objects.length;i++)
                    seasons[i] = (Season) objects[i];
                return seasons;
            }
        },false,100,observableSeasons);

        observableZipped.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Season[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Season[] seasons) {
                        mShowsRepository.updateSeasons(id,seasons);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
