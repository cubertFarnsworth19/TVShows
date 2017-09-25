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
   // private Cursor mShowDetails;
    private DetailsData mShowDetails;
    //private Cursor mCreators;
    private ArrayList<String> mCreators;
    private ApiService mApiService;

    public DetailsPresenter(DetailsContract.View detailsView,ShowsRepository showsRepository,ApiService apiService,int tmdbId){
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

        showDetailsObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(showDetailsConsumer);

    }

//    private void loadCreators(final Context context){
//
//        Observable<Cursor> showDetailsObservable = Observable.create(new ObservableOnSubscribe<Cursor>() {
//            @Override
//            public void subscribe(ObservableEmitter<Cursor> e) throws Exception {
//                e.onNext(mShowsRepository.getCreators(tmdbId));
//            }
//        });
//
//        Consumer<Cursor> showDetailsConsumer = new Consumer<Cursor>() {
//            @Override
//            public void accept(@io.reactivex.annotations.NonNull Cursor cursor) throws Exception {
//                mCreators = cursor;
//                setDetailsView(context);
//            }
//        };
//
//        showDetailsObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(showDetailsConsumer);
//    }

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

        showDetailsObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(showDetailsConsumer);
    }

//    private void setDetailsView(Context context){
//        if(mShowDetails.moveToFirst()) {
//            String overview = mShowDetails.getString(mShowDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_OVERVIEW));
//            Integer startYear = mShowDetails.getInt(mShowDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_FIRST_AIR_DATE_YEAR));
//            Double userScore = mShowDetails.getDouble(mShowDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_VOTE_AVERAGE));
//            Integer voteCount = mShowDetails.getInt(mShowDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_VOTE_COUNT));
//
//            mDetailsView.setUserInterfaceText(overview, startYear.toString(), userScore.toString(), voteCount.toString(), getGenres(context,mShowDetails),
//                    Utility.getRatingBackgroundColor(context,userScore),Utility.getTextColor(context,userScore));
//
//            mDetailsView.setPoster(mShowDetails.getString(mShowDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_POSTER_PATH)));
//
//            mDetailsView.creatorDataLoaded(mCreators.getCount());
//        }
//    }

    private void setDetailsView(Context context){
       // if(mShowDetails.moveToFirst()) {
//            String overview = mShowDetails.getString(mShowDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_OVERVIEW));
//            Integer startYear = mShowDetails.getInt(mShowDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_FIRST_AIR_DATE_YEAR));
//            Double userScore = mShowDetails.getDouble(mShowDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_VOTE_AVERAGE));
//            Integer voteCount = mShowDetails.getInt(mShowDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_VOTE_COUNT));

            mDetailsView.setUserInterfaceText(mShowDetails.overview, mShowDetails.startYear.toString(),
                    mShowDetails.userScore.toString(), mShowDetails.voteCount.toString(), getGenres(context,mShowDetails),
                    Utility.getRatingBackgroundColor(context,mShowDetails.userScore),Utility.getTextColor(context,mShowDetails.userScore));

            mDetailsView.setPoster(mShowDetails.posterPath);

            mDetailsView.creatorDataLoaded(mCreators.size());
      //  }
    }

//    @Override
//    public String getCreatorName(int position) {
//        mCreators.moveToPosition(position);
//        return mCreators.getString(mCreators.getColumnIndex(ShowsDbContract.CreatorEntry.COLUMN_CREATOR_NAME));
//    }

    @Override
    public String getCreatorName(int position) {
        return mCreators.get(position);
    }

    @Override
    public void visitIMDbPage(final Context context) {

        mApiService.getTVShowExternalIds(String.valueOf(tmdbId),BuildConfig.TMDB_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ExternalIdsTvShow>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ExternalIdsTvShow externalIdsTvShow) {
                        String imdbId = externalIdsTvShow.getImdbId();
                        if(imdbId!="") {
                            Uri webpage = Uri.parse(context.getString(R.string.imdb_tv_show_webpage) + imdbId);
                            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                            if (intent.resolveActivity(context.getPackageManager()) != null)
                                context.startActivity(intent);
                        }
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
    public void visitTMDBPage(Context context) {
        Uri webpage = Uri.parse(context.getString(R.string.tmdb_tv_show_webpage) + tmdbId);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
    }

//    @Override
//    public void searchGoogle(Context context) {
//        if(mShowDetails.moveToFirst()) {
//            String title = mShowDetails.getString(mShowDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NAME));
//            Uri webpage = Uri.parse(context.getString(R.string.google_search_webpage) + title);
//            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//            if (intent.resolveActivity(context.getPackageManager()) != null)
//                context.startActivity(intent);
//        }
//    }
//
//    @Override
//    public void searchYouTube(Context context) {
//        if(mShowDetails.moveToFirst()) {
//            String title = mShowDetails.getString(mShowDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NAME));
//            Uri webpage = Uri.parse(context.getString(R.string.youtube_search_webpage) + title);
//            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//            if (intent.resolveActivity(context.getPackageManager()) != null)
//                context.startActivity(intent);
//        }
//    }
//
//    @Override
//    public void visitWikipedia(Context context) {
//        if(mShowDetails.moveToFirst()){
//            String title = mShowDetails.getString(mShowDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NAME));
//            Uri webpage = Uri.parse(Utility.getWikipediaTVSeriesWebpage(context,title));
//            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//            if (intent.resolveActivity(context.getPackageManager()) != null)
//                context.startActivity(intent);
//        }
//    }

    @Override
    public void searchGoogle(Context context) {
            String title = mShowDetails.name;
            Uri webpage = Uri.parse(context.getString(R.string.google_search_webpage) + title);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(context.getPackageManager()) != null)
                context.startActivity(intent);

    }

    @Override
    public void searchYouTube(Context context) {

            String title = mShowDetails.name;
            Uri webpage = Uri.parse(context.getString(R.string.youtube_search_webpage) + title);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(context.getPackageManager()) != null)
                context.startActivity(intent);

    }

    @Override
    public void visitWikipedia(Context context) {

            String title = mShowDetails.name;
            Uri webpage = Uri.parse(Utility.getWikipediaTVSeriesWebpage(context,title));
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(context.getPackageManager()) != null)
                context.startActivity(intent);

    }


//    @NonNull
//    private String getGenres(Context context, Cursor showDetails){
//
//        boolean[] genres = new boolean[Genres.numberOfGenres()];
//
//        if(showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_ACTION_ADVENTURE))==1) genres[0] = true;
//        if(showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_ANIMATION))==1) genres[1] = true;
//        if(showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_COMEDY))==1) genres[2] = true;
//        if(showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_CRIME))==1) genres[3] = true;
//        if(showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_DOCUMENTARY))==1) genres[4] = true;
//        if(showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_DRAMA))==1) genres[5] = true;
//        if(showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_FAMILY))==1) genres[6] = true;
//        if(showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_KIDS))==1) genres[7] = true;
//        if(showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_MYSTERY))==1) genres[8] = true;
//        if(showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_NEWS))==1) genres[9] = true;
//        if(showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_REALITY))==1) genres[10] = true;
//        if(showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_SCI_FI_FANTASY))==1) genres[11] = true;
//        if(showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_SOAP))==1) genres[12] = true;
//        if(showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_TALK))==1) genres[13] = true;
//        if(showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_WAR_POLITICS))==1) genres[14] = true;
//        if(showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_WESTERN))==1) genres[15] = true;
//
//        return Genres.getGenresString(genres,context);
//    }

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
