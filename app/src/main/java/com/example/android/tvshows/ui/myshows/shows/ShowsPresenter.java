package com.example.android.tvshows.ui.myshows.shows;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.showinfo.ShowInfoActivity;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ShowsPresenter implements ShowsContract.Presenter {

    private ShowsRepository mShowsRepository;
    private ShowsContract.View mShowsView;
  //  private Cursor mShowsCursor;
    private ArrayList<ShowInfo> mShowsInfo = new ArrayList<>();

    public ShowsPresenter(ShowsContract.View showsView,ShowsRepository showsRepository){
        mShowsView = showsView;
        mShowsRepository = showsRepository;
    }

    @Override
    public void loadShowsFromDatabase(final Context context) {

//        mShowsCursor = mShowsRepository.getAllShows();
//        mShowsView.showsDataLoaded(mShowsCursor.getCount());

        Observable<Cursor> observable = Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> e) throws Exception {
                e.onNext(mShowsRepository.getAllShows());
            }
        });

        Consumer<Cursor> consumer = new Consumer<Cursor>() {
            @Override
            public void accept(@NonNull Cursor cursor) throws Exception {
               // mShowsCursor = cursor;

                while (cursor.moveToNext()){
                    mShowsInfo.add(new ShowInfo(
                            context,
                            cursor.getInt(cursor.getColumnIndex(ShowsDbContract.ShowsEntry._ID)),
                            cursor.getString(cursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_POSTER_PATH)),
                            cursor.getInt(cursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NUM_SEASONS)),
                            cursor.getInt(cursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NUM_EPISODES)),
                            cursor.getInt(cursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_IN_PRODUCTION))
                    ));
                }


                mShowsView.showsDataLoaded(cursor.getCount());
                cursor.close();
            }
        };

        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);

    }

    @Override
    public String getTitle(int position) {
//        mShowsCursor.moveToPosition(position);
//        return mShowsCursor.getString(mShowsCursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NAME));
        return mShowsInfo.get(position).getTitle();
    }

    @Override
    public String getPosterUrl(int position) {
//        mShowsCursor.moveToPosition(position);
//        String posterUrl = context.getString(R.string.poster_path)
//                + mShowsCursor.getString(mShowsCursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_POSTER_PATH));
//        return posterUrl;
        return mShowsInfo.get(position).getPosterUrl();
    }

    @Override
    public String getNumberOfSeasons(int position) {
//        mShowsCursor.moveToPosition(position);
//        Integer seasons = mShowsCursor.getInt(mShowsCursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NUM_SEASONS));
//        return seasons.toString();
        return mShowsInfo.get(position).getNumberOfSeasons();
    }

    @Override
    public String getNumberOfEpisodes(int position) {
//        mShowsCursor.moveToPosition(position);
//        Integer episodes = mShowsCursor.getInt(mShowsCursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NUM_EPISODES));
//        return episodes.toString();
        return mShowsInfo.get(position).getNumberOfEpisodes();
    }

    @Override
    public String getInProduction(int position) {
//        mShowsCursor.moveToPosition(position);
//        int inProduction = mShowsCursor.getInt(mShowsCursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_IN_PRODUCTION));
//        return inProduction==1 ? context.getString(R.string.continuing) : context.getString(R.string.finished);
        return mShowsInfo.get(position).getInProduction();
    }

    @Override
    public void startShowInfoActivity(Context context, int position) {
//        mShowsCursor.moveToPosition(position);
        Intent intent = new Intent(context, ShowInfoActivity.class);
        intent.putExtra(ShowsDbContract.ShowsEntry._ID,mShowsInfo.get(position).getId());

        intent.putExtra(ShowsDbContract.ShowsEntry.COLUMN_NAME,mShowsInfo.get(position).getTitle());
        context.startActivity(intent);
    }

    @Override
    public void removeShow(int position) {
//        mShowsCursor.moveToPosition(position);
//        Log.v("position"," "+position);
//        Log.v("remove",mShowsCursor.getString(mShowsCursor.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_NAME)));
        int removeId = mShowsInfo.get(position).getId();
        mShowsInfo.remove(position);
        mShowsRepository.deleteShow(removeId);
    }

    class ShowInfo{
        int id;
        String title;
        String posterUrl;
        String numberOfSeasons;
        String numberOfEpisodes;
        String inProduction;

        public ShowInfo(Context context,int id, String title, String posterPath, Integer numberOfSeasons, Integer numberOfEpisodes,int inProduction) {
            this.id = id;
            this.title = title;
            this.posterUrl = context.getString(R.string.poster_path) + posterPath;
            this.numberOfSeasons = numberOfSeasons.toString();
            this.numberOfEpisodes = numberOfEpisodes.toString();
            this.inProduction = inProduction==1 ? context.getString(R.string.continuing) : context.getString(R.string.finished);
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getPosterUrl() {
            return posterUrl;
        }

        public String getNumberOfSeasons() {
            return numberOfSeasons;
        }

        public String getNumberOfEpisodes() {
            return numberOfEpisodes;
        }

        public String getInProduction() {
            return inProduction;
        }
    }
}
