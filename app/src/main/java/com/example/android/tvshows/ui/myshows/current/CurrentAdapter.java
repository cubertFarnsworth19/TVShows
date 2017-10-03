package com.example.android.tvshows.ui.myshows.current;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.android.tvshows.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CurrentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Parcelable {

    private Context mContext;
    private Picasso mPicasso;
    private CurrentContract.Presenter mCurrentPresenter;
    private int mSize = 0;

    public CurrentAdapter(Context context, CurrentContract.Presenter currentPresenter, Picasso picasso){
        mContext = context;
        mCurrentPresenter = currentPresenter;
        mPicasso = picasso;
    }

    public void setVariables(Context context, CurrentContract.Presenter currentPresenter, Picasso picasso){
        mContext = context;
        mCurrentPresenter = currentPresenter;
        mPicasso = picasso;
    }

    public void displayShows(int size){
        mSize = size;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myshows_current_list_item,parent,false);
        return new CurrentAdapter.ViewHolderDate(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderDate holderDate = (ViewHolderDate) holder;
        holderDate.date.setText(mCurrentPresenter.getDate(position));
        holderDate.displayShows(mCurrentPresenter.getNumberOfShowsOnDate(position),position);
    }

    @Override
    public int getItemCount() {
        return mSize;
    }

    class ViewHolderDate extends RecyclerView.ViewHolder {

        @BindView(R.id.shows_date) TextView date;
        @BindView(R.id.recyclerview_day_shows) RecyclerView recyclerView;
        DayShowsAdapter dayShowsAdapter;

        public ViewHolderDate(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            dayShowsAdapter = new DayShowsAdapter();
            recyclerView.setAdapter(dayShowsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setNestedScrollingEnabled(false);
        }

        void displayShows(int size,int dayPosition){
            dayShowsAdapter.displayShows(size,dayPosition);
        }
    }

    class DayShowsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        int mSize;
        int mDayPosition;

        public DayShowsAdapter(){
            mSize = 0;
            mDayPosition = 0;
        }

        public void displayShows(int size,int dayPosition){
            mSize = size;
            mDayPosition = dayPosition;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.myshows_current_day_shows_list_item,parent,false);
            return new DayShowsAdapter.ViewHolderShows(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolderShows holderShows = (ViewHolderShows) holder;
            holderShows.title.setText(mCurrentPresenter.getShowName(mDayPosition,position));
            mPicasso.load(mCurrentPresenter.getShowPosterUrl(mDayPosition,position)).into(holderShows.poster);
            holderShows.overview.setText(mCurrentPresenter.getShowOverview(mDayPosition,position));
            holderShows.episodeName.setText(mCurrentPresenter.getEpisodeName(mDayPosition,position));
        }

        @Override
        public int getItemCount() {
            return mSize;
        }

        class ViewHolderShows extends RecyclerView.ViewHolder {

            @BindView(R.id.poster)ImageView poster;
            @BindView(R.id.title)TextView title;
            @BindView(R.id.episode_name)TextView episodeName;
            @BindView(R.id.overview)TextView overview;

            public ViewHolderShows(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }

    protected CurrentAdapter(Parcel in) {
        mSize = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mSize);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CurrentAdapter> CREATOR = new Parcelable.Creator<CurrentAdapter>() {
        @Override
        public CurrentAdapter createFromParcel(Parcel in) {
            return new CurrentAdapter(in);
        }

        @Override
        public CurrentAdapter[] newArray(int size) {
            return new CurrentAdapter[size];
        }
    };

}
