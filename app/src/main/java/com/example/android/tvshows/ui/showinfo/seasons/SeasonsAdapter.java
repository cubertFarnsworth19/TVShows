package com.example.android.tvshows.ui.showinfo.seasons;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.tvshows.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeasonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Parcelable {

    Context mContext;
    SeasonsContract.Presenter mSeasonsPresenter;
    Picasso mPicasso;
    int mSize = 0;

    public SeasonsAdapter(Context context, SeasonsContract.Presenter seasonsPresenter, Picasso picasso){
        mContext = context;
        mSeasonsPresenter = seasonsPresenter;
        mPicasso = picasso;
    }

    public void setVariables(Context context, SeasonsContract.Presenter seasonsPresenter, Picasso picasso){
        mContext = context;
        mSeasonsPresenter = seasonsPresenter;
        mPicasso = picasso;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_info_seasons_list_item,parent,false);
        return new SeasonsAdapter.ViewHolderSeasons(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderSeasons holderSeasons = (ViewHolderSeasons) holder;
        holderSeasons.seasonName.setText(mSeasonsPresenter.getSeasonName(position));
        holderSeasons.airDate.setText(mSeasonsPresenter.getAirDate(position));
        holderSeasons.overview.setText(mSeasonsPresenter.getOverview(position));
        holderSeasons.episodes.setText(mSeasonsPresenter.getNumberOfEpisodes(position,mContext));
        mPicasso.load(mSeasonsPresenter.getPosterUrl(mContext,position)).into(holderSeasons.poster);
    }

    @Override
    public int getItemCount() {
        return mSize;
    }

    public void displaySeasons(int size){
        mSize = size;
        notifyDataSetChanged();
    }

    class ViewHolderSeasons extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.seasons_layout) LinearLayout layout;
        @BindView(R.id.season_name) TextView seasonName;
        @BindView(R.id.poster) ImageView poster;
        @BindView(R.id.season_air_date)TextView airDate;
        @BindView(R.id.season_episodes_number)TextView episodes;
        @BindView(R.id.season_overview)TextView overview;

        public ViewHolderSeasons(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mContext.startActivity(
                    mSeasonsPresenter.getIntentForEpisodesActivity(mContext,getAdapterPosition()));
        }
    }


    protected SeasonsAdapter(Parcel in) {
        mSize = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(mSize);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SeasonsAdapter> CREATOR = new Parcelable.Creator<SeasonsAdapter>() {
        @Override
        public SeasonsAdapter createFromParcel(Parcel in) {
            return new SeasonsAdapter(in);
        }

        @Override
        public SeasonsAdapter[] newArray(int size) {
            return new SeasonsAdapter[size];
        }
    };

}
