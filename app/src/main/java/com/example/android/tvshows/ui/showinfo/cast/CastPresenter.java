package com.example.android.tvshows.ui.showinfo.cast;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.actor.ActorActivity;

import java.util.ArrayList;

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
    private ArrayList<CastInfo> mCastInfo;

    public CastPresenter(CastContract.View castView,ShowsRepository showsRepository,int tmdbId){
        mCastView = castView;
        mShowsRepository = showsRepository;
        this.tmdbId = tmdbId;
    }

    public CastPresenter(CastContract.View castView,ShowsRepository showsRepository,int tmdbId,ArrayList<CastInfo> castInfo){
        mCastView = castView;
        mShowsRepository = showsRepository;
        this.tmdbId = tmdbId;
        mCastInfo = castInfo;
    }

    @Override
    public void loadCastData(final Context context) {

        Observable<Cursor> observable = Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> e) throws Exception {
                e.onNext( mShowsRepository.getCast(tmdbId));
            }
        });

        Consumer<Cursor> consumer = new Consumer<Cursor>() {
            @Override
            public void accept(@NonNull Cursor cursor) throws Exception {

                mCastInfo = new ArrayList();

                while (cursor.moveToNext()){
                    mCastInfo.add(new CastInfo(
                            cursor.getString(cursor.getColumnIndex(ShowsDbContract.CastEntry.COLUMN_CHARACTER)),
                            cursor.getString(cursor.getColumnIndex(ShowsDbContract.CastEntry.COLUMN_NAME)),
                            context.getString(R.string.poster_path) + cursor.getString(cursor.getColumnIndex(ShowsDbContract.CastEntry.COLUMN_PROFILE_PATH)),
                            cursor.getInt(cursor.getColumnIndex(ShowsDbContract.CastEntry.COLUMN_PERSON_ID))
                    ));
                }
                mCastView.castDataLoaded(mCastInfo.size());
            }
        };

        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
    }

    @Override
    public String getCharacterName(int position) {
        return mCastInfo.get(position).getCharacterName();
    }

    @Override
    public String getActorName(int position) {
        return mCastInfo.get(position).getActorName();
    }

    @Override
    public String getPhotoUrl(Context context,int position) {
        return mCastInfo.get(position).getPhotoUrl();
    }

    @Override
    public void startActorActivity(Context context, int position) {
        Intent intent = new Intent(context, ActorActivity.class);
        intent.putExtra(ShowsDbContract.CastEntry.COLUMN_PERSON_ID,mCastInfo.get(position).getPersonId());

        context.startActivity(intent);
    }

    @Override
    public ArrayList<CastInfo> getCastInfo() {
        return mCastInfo;
    }

}
