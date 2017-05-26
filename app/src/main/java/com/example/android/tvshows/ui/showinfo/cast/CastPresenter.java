package com.example.android.tvshows.ui.showinfo.cast;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.actor.ActorActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CastPresenter implements CastContract.Presenter {

    private CastContract.View mCastView;
    private ShowsRepository mShowsRepository;
    private int tmdbId;
    private Cursor mCastCursor;

    public CastPresenter(CastContract.View castView,ShowsRepository showsRepository,int tmdbId){
        mCastView = castView;
        mShowsRepository = showsRepository;
        this.tmdbId = tmdbId;

    }

    @Override
    public void loadCastData() {
//        mCastCursor = mShowsRepository.getCast(tmdbId);
//        mCastView.castDataLoaded(mCastCursor.getCount());

        Observable<Cursor> observable = Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> e) throws Exception {
                e.onNext( mShowsRepository.getCast(tmdbId));
            }
        });

        Consumer<Cursor> consumer = new Consumer<Cursor>() {
            @Override
            public void accept(@NonNull Cursor cursor) throws Exception {
                mCastCursor = cursor;
                mCastView.castDataLoaded(mCastCursor.getCount());
            }
        };

        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
    }

    @Override
    public String getCharacterName(int position) {
        mCastCursor.moveToPosition(position);
        return mCastCursor.getString(mCastCursor.getColumnIndex(ShowsDbContract.CastEntry.COLUMN_CHARACTER));
    }

    @Override
    public String getActorName(int position) {
        mCastCursor.moveToPosition(position);
        return mCastCursor.getString(mCastCursor.getColumnIndex(ShowsDbContract.CastEntry.COLUMN_NAME));
    }

    @Override
    public String getPhotoUrl(Context context,int position) {
        mCastCursor.moveToPosition(position);
        String profilePath = mCastCursor.getString(mCastCursor.getColumnIndex(ShowsDbContract.CastEntry.COLUMN_PROFILE_PATH));
        return context.getString(R.string.poster_path) + profilePath;
    }

    @Override
    public void startActorActivity(Context context, int position) {
        mCastCursor.moveToPosition(position);
        Intent intent = new Intent(context, ActorActivity.class);
        intent.putExtra(ShowsDbContract.CastEntry.COLUMN_PERSON_ID,mCastCursor.getInt(mCastCursor.getColumnIndex(ShowsDbContract.CastEntry.COLUMN_PERSON_ID)));
        context.startActivity(intent);
    }

    @Override
    public void closeCursor() {
        mCastCursor.close();
    }


}
