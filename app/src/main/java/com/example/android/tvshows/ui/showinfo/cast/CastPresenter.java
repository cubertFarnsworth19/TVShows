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

public class CastPresenter implements CastContract.Presenter, Parcelable {

    private CastContract.View mCastView;
    private ShowsRepository mShowsRepository;
    private int tmdbId;
    private ArrayList<CastInfo> mCastInfo = new ArrayList();

    public CastPresenter(CastContract.View castView,ShowsRepository showsRepository,int tmdbId){
        mCastView = castView;
        mShowsRepository = showsRepository;
        this.tmdbId = tmdbId;

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
        return mCastInfo.get(position).characterName;
    }

    @Override
    public String getActorName(int position) {
        return mCastInfo.get(position).actorName;
    }

    @Override
    public String getPhotoUrl(Context context,int position) {
        return mCastInfo.get(position).photoUrl;
    }

    @Override
    public void startActorActivity(Context context, int position) {
        Intent intent = new Intent(context, ActorActivity.class);
        intent.putExtra(ShowsDbContract.CastEntry.COLUMN_PERSON_ID,mCastInfo.get(position).personId);

        mCastView.setSave(false);

        context.startActivity(intent);
    }

    protected CastPresenter(Parcel in) {
        mCastView = (CastContract.View) in.readValue(CastContract.View.class.getClassLoader());
        mShowsRepository = (ShowsRepository) in.readValue(ShowsRepository.class.getClassLoader());
        tmdbId = in.readInt();
        if (in.readByte() == 0x01) {
            mCastInfo = new ArrayList<CastInfo>();
            in.readList(mCastInfo, CastInfo.class.getClassLoader());
        } else {
            mCastInfo = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mCastView);
        dest.writeValue(mShowsRepository);
        dest.writeInt(tmdbId);
        if (mCastInfo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mCastInfo);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CastPresenter> CREATOR = new Parcelable.Creator<CastPresenter>() {
        @Override
        public CastPresenter createFromParcel(Parcel in) {
            return new CastPresenter(in);
        }

        @Override
        public CastPresenter[] newArray(int size) {
            return new CastPresenter[size];
        }
    };


//    @Override
//    public void closeCursor() {
//        mCastCursor.close();
//    }
    class CastInfo{
        String characterName, actorName, photoUrl;
        int personId;

        public CastInfo(String characterName, String actorName, String photoUrl,int personId) {
            this.characterName = characterName;
            this.actorName = actorName;
            this.photoUrl = photoUrl;
            this.personId = personId;
        }
    }

}
