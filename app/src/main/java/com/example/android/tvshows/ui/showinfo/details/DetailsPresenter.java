package com.example.android.tvshows.ui.showinfo.details;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.android.tvshows.BuildConfig;
import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.model.ExternalIdsTvShow;
import com.example.android.tvshows.data.rest.ApiService;
import com.example.android.tvshows.util.Genres;
import com.example.android.tvshows.util.Utility;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DetailsPresenter implements DetailsContract.Presenter{

    private DetailsContract.View mDetailsView;
    private ShowsRepository mShowsRepository;
    private int tmdbId;
    private DetailsData mShowDetails;
    private ArrayList<String> mCreators;
    private ApiService mApiService;
    private ExternalIdsTvShow mExternalIdsTvShow;

    public DetailsPresenter(DetailsContract.View detailsView,ShowsRepository showsRepository,
                            ApiService apiService,int tmdbId){
        mDetailsView = detailsView;
        mShowsRepository = showsRepository;
        this.tmdbId = tmdbId;
        mApiService = apiService;
    }

    @Override
    public void loadShowDetails(final Context context) {
        Observable<DetailsData> showDetailsObservable = Observable.create(new ObservableOnSubscribe<DetailsData>() {
            @Override
            public void subscribe(ObservableEmitter<DetailsData> e) throws Exception {
                e.onNext(mShowsRepository.getShow(tmdbId));
            }
        });
        Consumer<DetailsData> showDetailsConsumer = new Consumer<DetailsData>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull DetailsData detailsData) throws Exception {
                mShowDetails = detailsData;
                loadCreators(context);
            }
        };
        showDetailsObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(showDetailsConsumer);
    }

    private void loadCreators(final Context context){

        Observable<ArrayList<String>> showDetailsObservable = Observable.create(new ObservableOnSubscribe<ArrayList<String>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<String>> e) throws Exception {
                e.onNext(mShowsRepository.getCreators(tmdbId));
            }
        });

        Consumer<ArrayList<String>> showDetailsConsumer = new Consumer<ArrayList<String>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull ArrayList<String> creators) throws Exception {
                mCreators = creators;
                setDetailsView(context);
            }
        };

        showDetailsObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(showDetailsConsumer);
    }

    private void setDetailsView(Context context){
        mDetailsView.setUserInterfaceText(mShowDetails.overview,
                mShowDetails.startYear.toString(), mShowDetails.userScore.toString(),
                mShowDetails.voteCount.toString(), getGenres(context,mShowDetails),
                Utility.getRatingBackgroundColor(context,mShowDetails.userScore),
                Utility.getTextColor(context,mShowDetails.userScore));

        mDetailsView.setPoster(mShowDetails.posterPath);

        mDetailsView.creatorDataLoaded(mCreators.size());
    }

    @Override
    public String getCreatorName(int position) {
        return mCreators.get(position);
    }

    @Override
    public String getTitle() {
        return mShowDetails.name;
    }

    @Override
    public boolean downloadExternalIds() {
        if(mExternalIdsTvShow==null) {
            mApiService.getTVShowExternalIds(String.valueOf(tmdbId), BuildConfig.TMDB_API_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ExternalIdsTvShow>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(ExternalIdsTvShow externalIdsTvShow) {
                            mExternalIdsTvShow = externalIdsTvShow;
                            mDetailsView.setIMDBid(mExternalIdsTvShow.getImdbId());
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String getImdbId() {
        return mExternalIdsTvShow.getImdbId();
    }

    @NonNull
    private String getGenres(Context context, DetailsData showDetails){

        boolean[] genres = new boolean[Genres.numberOfGenres()];

        if(showDetails.actionAdventure) genres[0] = true;
        if(showDetails.animation) genres[1] = true;
        if(showDetails.comedy) genres[2] = true;
        if(showDetails.crime) genres[3] = true;
        if(showDetails.documentary) genres[4] = true;
        if(showDetails.drama) genres[5] = true;
        if(showDetails.family) genres[6] = true;
        if(showDetails.kids) genres[7] = true;
        if(showDetails.mystery) genres[8] = true;
        if(showDetails.news) genres[9] = true;
        if(showDetails.reality) genres[10] = true;
        if(showDetails.sciFiFantasy) genres[11] = true;
        if(showDetails.soap) genres[12] = true;
        if(showDetails.talk) genres[13] = true;
        if(showDetails.warPolitics) genres[14] = true;
        if(showDetails.western) genres[15] = true;

        return Genres.getGenresString(genres,context);
    }

}
