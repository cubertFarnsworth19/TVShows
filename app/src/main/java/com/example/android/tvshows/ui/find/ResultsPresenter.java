package com.example.android.tvshows.ui.find;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;


import com.example.android.tvshows.BuildConfig;
import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.model.search.DiscoverResults;


import com.example.android.tvshows.data.model.search.Result;
import com.example.android.tvshows.data.model.tvshowdetailed.TVShowDetailed;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.idlingResource.SimpleIdlingResource;
import com.example.android.tvshows.service.DownloadService;
import com.example.android.tvshows.ui.RetryUntilDownloaded;
import com.example.android.tvshows.util.Genres;
import com.example.android.tvshows.util.Utility;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
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
    //the ids of all shows stored in the database
    private ArrayList<Integer> mAllShowIds;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Inject
    public ResultsPresenter(@NonNull ResultsContract.View resultsView,ApiService apiService,ShowsRepository showsRepository) {
        mResultsView = resultsView;
        mApiService = apiService;
        mShowsRepository = showsRepository;
        mAllShowIds = mShowsRepository.getAllShowIds();
    }

    public ResultsPresenter(@NonNull ResultsContract.View resultsView,ApiService apiService,ShowsRepository showsRepository,SaveResultsPresenterState saveResultsPresenterState){
        mResultsView = resultsView;
        mApiService = apiService;
        mShowsRepository = showsRepository;
        mAllShowIds = saveResultsPresenterState.getAllShowIds();
        mResults = saveResultsPresenterState.getResults();
        mPage = saveResultsPresenterState.getPage();
        mTotalPages = saveResultsPresenterState.getTotalPages();
        mTotalResults = saveResultsPresenterState.getTotalResults();
        mLastSortBy = saveResultsPresenterState.getLastSortBy();
        mLastWithGenres = saveResultsPresenterState.getLastWithGenres();
        mLastWithoutGenres = saveResultsPresenterState.getLastWithoutGenres();
        mLastMinVoteAverage = saveResultsPresenterState.getLastMinVoteAverage();
        mLastMinVoteCount = saveResultsPresenterState.getLastMinVoteCount();
        mLastFirstAirDateAfter = saveResultsPresenterState.getLastFirstAirDateAfter();
        mLastFirstAirDateBefore = saveResultsPresenterState.getLastFirstAirDateBefore();
    }

    @Override
    public void saveSelectedToDatabase(Context context, Integer id) {

        mAllShowIds.add(id);
        Intent intent = new Intent(context,DownloadService.class);
        intent.putExtra(DownloadService.DOWNLOAD_TYPE, DownloadService.RESULTS);
        intent.putExtra(DownloadService.TMDB_ID,id);
        context.startService(intent);

        Log.i("Attempting TMDB id",id.toString());
    }

    @Override
    public void makeDiscoverRequest(Context context, String sortBy, String withGenres, String withoutGenres, String minVoteAverage,
                                    String minVoteCount, String firstAirDateAfter, String firstAirDateBefore) {
        mLastSortBy = sortBy;
        mLastWithGenres = withGenres;
        mLastWithoutGenres = withoutGenres;
        mLastMinVoteAverage = minVoteAverage;
        mLastMinVoteCount = minVoteCount;
        mLastFirstAirDateAfter = firstAirDateAfter;
        mLastFirstAirDateBefore = firstAirDateBefore;
        getDiscoverPage(context,1);
    }

    @Override
    public void getDiscoverPage(Context context,final Integer page) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.v("Results", "Page: " + mPage + ", total pages: " + mTotalPages + ", total results: " + mTotalResults);
            if (page == 1 || mPage < mTotalPages) {
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
                                mResultsView.noConnectionRetryNewPage(page);
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
            }
        }else{
            if(page==1)
                mResultsView.noConnection();
            else
                mResultsView.noConnectionRetryNewPage(page);
        }
    }

    @Override
    public void search(Context context, String searchTerm) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mApiService.getSearchResults(BuildConfig.TMDB_API_KEY, searchTerm)
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
        }else{
            mResultsView.noConnection();
        }
    }

    @Override
    public void getRecommendations(Context context,final Integer tmdbId) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mApiService.getRecommendations(tmdbId.toString(), BuildConfig.TMDB_API_KEY, "1").subscribeOn(Schedulers.io())
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
                            mResultsView.noConnectionWithRetry(tmdbId);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else{
            mResultsView.noConnectionWithRetry(tmdbId);
        }
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

    @Override
    public void openMoreDetailsDialog(Context context,final int position) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            mApiService.getTVShowDetailed(mResults.get(position).getId().toString(), BuildConfig.TMDB_API_KEY)
                    .retryWhen(new RetryUntilDownloaded(2000)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<TVShowDetailed>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(TVShowDetailed tvShowDetailed) {
                            FragmentManager fm = mResultsView.getFragmentManagerForDialog();
                            MoreDetailsDialog moreDetailsDialog = new MoreDetailsDialog(tvShowDetailed, ResultsPresenter.this, position);
                            moreDetailsDialog.show(fm, "dialog_more_details");
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else{
            mResultsView.noConnection();
        }
    }

    @Override
    public void showAdded() {
        mResultsView.updateAdapter();
    }

    @Override
    public SaveResultsPresenterState getSaveResultsPresenterState() {
        return new SaveResultsPresenterState(mResults,mPage,mTotalPages,mTotalResults,mLastSortBy,
                mLastWithGenres,mLastWithoutGenres,mLastMinVoteAverage,mLastMinVoteCount,
                mLastFirstAirDateAfter,mLastFirstAirDateBefore,mAllShowIds);
    }

}