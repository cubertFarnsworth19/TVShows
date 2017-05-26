package com.example.android.tvshows.ui.find;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;


import com.example.android.tvshows.BuildConfig;
import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.model.search.DiscoverResults;


import com.example.android.tvshows.data.model.search.Result;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.service.DownloadService;
import com.example.android.tvshows.util.Utility;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ResultsPresenter implements ResultsContract.Presenter {

    private ResultsContract.View mResultsView;
    private ApiService mApiService;
    private ShowsRepository mShowsRepository;
    //the list of search results
    private List<Result> mResults;
    private int mPage;
    private int mTotalPages;
    private int mTotalResults;
    private String mLastSortBy;
    private String mLastWithGenres;
    private String mLastWithoutGenres;
    private String mLastMinVoteAverage;
    private String mLastMinVoteCount;
    private String mLastFirstAirDateAfter;
    private String mLastFirstAirDateBefore;
    private ArrayList<Integer> mAllShowIds;

    @Inject
    public ResultsPresenter(@NonNull ResultsContract.View resultsView,ApiService apiService,ShowsRepository showsRepository) {
        mResultsView = resultsView;
        mApiService = apiService;
        mShowsRepository = showsRepository;
        mAllShowIds = mShowsRepository.getAllShowIds();
    }

    @Override
    public void saveSelectedToDatabase(Integer id) {

        Context context = mShowsRepository.getContext();
        Intent intent = new Intent(context,DownloadService.class);
        intent.putExtra(DownloadService.DOWNLOAD_TYPE, DownloadService.RESULTS);
        intent.putExtra(DownloadService.TMDB_ID,id);
        context.startService(intent);

        Log.i("Attempting TMDB id",id.toString());
//        final String strId = id.toString();
//        Observable<TVShowDetailed> observableTvShow = mApiService.getTVShowDetailed(strId,BuildConfig.TMDB_API_KEY)
//                .retryWhen(new RetryUntilDownloaded(2000));
//        Observable<Credits> observableCredits = mApiService.getCredits(strId,BuildConfig.TMDB_API_KEY,"1")
//                .retryWhen(new RetryUntilDownloaded(2000));
//
//
//        Observable<Object[]> observableZipped = Observable.zip(observableTvShow,observableCredits, new BiFunction<TVShowDetailed, Credits, Object[]>(){
//                    @Override
//                    public Object[] apply(@io.reactivex.annotations.NonNull TVShowDetailed tvShowDetailed,
//                                          @io.reactivex.annotations.NonNull Credits credits) throws Exception {
//                        return new Object[]{tvShowDetailed, credits};
//                    }
//                });
//
//        observableZipped.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(new Consumer<Object[]>() {
////                    @Override
////                    public void accept(@io.reactivex.annotations.NonNull Object[] objects) throws Exception {
////                        completeDownload((TVShowDetailed) objects[0],(Credits)objects[1],strId);
////                    }
////                });
//                .subscribe(new Observer<Object[]>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Object[] objects) {
//                        TVShowDetailed tvShowDetailed = (TVShowDetailed) objects[0];
//                        Log.e("Show Details No Error",strId + "  " + tvShowDetailed.getName());
//                        completeDownload((TVShowDetailed) objects[0],(Credits)objects[1],strId);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

    }

//    private void completeDownload(final TVShowDetailed tvShowDetailed, final Credits credits, String strId){
//
//        int seasonsListSize = tvShowDetailed.getSeasonsListSize();
//
//        Observable<Season>[] observableSeasons = new Observable[seasonsListSize];
//
//        for(Integer i=0;i<seasonsListSize;i++){
//            observableSeasons[i] = mApiService
//                    .getSeason(strId,tvShowDetailed.getSeasons().get(i).getSeasonNumber().toString(),BuildConfig.TMDB_API_KEY,"1")
//                    .retryWhen(new RetryUntilDownloaded(2000));
//        }
//
//        Observable<Season[]> observableZipped = Observable.zipArray(new Function<Object[],Season[]>(){
//            @Override
//            public Season[] apply(@io.reactivex.annotations.NonNull Object[] objects) throws Exception {
//                Season[] seasons = new Season[objects.length];
//                for(int i=0;i<objects.length;i++)
//                    seasons[i] = (Season) objects[i];
//                return seasons;
//            }
//        },false,100,observableSeasons);
//
//        observableZipped.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(new Consumer<Season[]>() {
////                    @Override
////                    public void accept(@io.reactivex.annotations.NonNull Season[] seasons) throws Exception {
////                        Log.i("Completed TMDB id",tvShowDetailed.getId().toString());
////                        mShowsRepository.insertShowIntoDatabase(tvShowDetailed,credits,seasons);
////                    }
////                });
//                .subscribe(new Observer<Season[]>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Season[] seasons) {
//                        Log.e("Downloaded No Error",tvShowDetailed.getName());
//                        mShowsRepository.insertShowIntoDatabase(tvShowDetailed,credits,seasons);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

    @Override
    public void makeDiscoverRequest(String sortBy, String withGenres, String withoutGenres, String minVoteAverage,
                                    String minVoteCount, String firstAirDateAfter, String firstAirDateBefore) {
        mLastSortBy = sortBy;
        mLastWithGenres = withGenres;
        mLastWithoutGenres = withoutGenres;
        mLastMinVoteAverage = minVoteAverage;
        mLastMinVoteCount = minVoteCount;
        mLastFirstAirDateAfter = firstAirDateAfter;
        mLastFirstAirDateBefore = firstAirDateBefore;
        getDiscoverPage(1);
    }

    @Override
    public void getDiscoverPage(Integer page) {

        Log.v("Results","Page: "+mPage+", total pages: "+mTotalPages+", total results: "+mTotalResults);
        if(page==1 || mPage < mTotalPages) {
            mApiService.getDiscoverResults(BuildConfig.TMDB_API_KEY, mLastSortBy, mLastWithGenres, mLastWithoutGenres, mLastMinVoteAverage,
                    mLastMinVoteCount, mLastFirstAirDateAfter, mLastFirstAirDateBefore, page.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DiscoverResults>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(DiscoverResults discoverResults) {
                            setDiscoverResultsInfo(discoverResults);
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

    @Override
    public void search(String searchTerm) {
        mApiService.getSearchResults(BuildConfig.TMDB_API_KEY,searchTerm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DiscoverResults>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DiscoverResults discoverResults) {
                        setDiscoverResultsInfo(discoverResults);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getRecommendations(Integer tmdbId) {
        mApiService.getRecommendations(tmdbId.toString(),BuildConfig.TMDB_API_KEY,"1").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DiscoverResults>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DiscoverResults discoverResults) {
                        setDiscoverResultsInfo(discoverResults);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setDiscoverResultsInfo(DiscoverResults discoverResults){
        mPage = discoverResults.getPage();
        mTotalPages = discoverResults.getTotalPages();
        mTotalResults = discoverResults.getTotalResults();
        if(mPage==1)
            mResults = discoverResults.getResults();
        else
            mResults.addAll(discoverResults.getResults());
        mResultsView.setResultsAdapter(mResults.size());
    }

    @Override
    public String getName(int position) {
        return mResults.get(position).getName();
    }

    @Override
    public String getFirstAirDate(int position) {
        String firstAirDate = mResults.get(position).getFirstAirDate();
        if(firstAirDate==null || firstAirDate.length()<4) return "";
        return firstAirDate.substring(0,4);
    }

    @Override
    public int getVoteAverageBackgroundColor(Context context, int position) {
        Double voteAverage = mResults.get(position).getVoteAverage();
        return Utility.getRatingBackgroundColor(context,voteAverage);
    }

    @Override
    public int getVoteAverageTextColor(Context context, int position) {
        Double voteAverage = mResults.get(position).getVoteAverage();
        return Utility.getTextColor(context,voteAverage);
    }

    @Override
    public String getVoteAverage(int position) {
        if(mResults.get(position).getVoteCount() == 0) return "";

        int length = mResults.get(position).getVoteAverage().toString().length();

        if(mResults.get(position).getVoteAverage().toString().substring(0,2).equals("10")) length = 2;
        else if(length>3) length=3;

        return mResults.get(position).getVoteAverage().toString().substring(0,length);
    }

    @Override
    public String getPosterUrl(Context context, int position) {
        return context.getString(R.string.poster_path) + mResults.get(position).getPosterPath();
    }

    @Override
    public boolean showAddButton(int position) {
        return !mAllShowIds.contains(mResults.get(position).getId());
    }

    @Override
    public int getTmdbId(int position) {
        return mResults.get(position).getId();
    }

}
