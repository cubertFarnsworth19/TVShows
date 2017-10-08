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

        Observable<ArrayList<CastInfo>> observable = Observable.create(new ObservableOnSubscribe<ArrayList<CastInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<CastInfo>> e) throws Exception {
                e.onNext(mShowsRepository.getCast(tmdbId));
            }
        });

        Consumer<ArrayList<CastInfo>> consumer = new Consumer<ArrayList<CastInfo>>() {
            @Override
            public void accept(@NonNull ArrayList<CastInfo> castInfo) throws Exception {
                mCastInfo = castInfo;
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
    public Intent getIntentForActorActivity(Context context, int position) {
        return ActorActivity.getIntent(context,mCastInfo.get(position).getPersonId());
    }

    @Override
    public ArrayList<CastInfo> getCastInfo() {
        return mCastInfo;
    }

}
