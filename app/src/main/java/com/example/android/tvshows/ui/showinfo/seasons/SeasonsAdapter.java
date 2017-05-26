package com.example.android.tvshows.ui.showinfo.seasons;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.ui.episodes.EpisodesActivity;
import com.example.android.tvshows.ui.showinfo.ShowInfoActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeasonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context mContext;
    SeasonsContract.Presenter mSeasonsPresenter;
    Picasso mPicasso;
    int mSize = 0;

    SeasonsAdapter(Context context, SeasonsContract.Presenter seasonsPresenter, Picasso picasso){
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
        holderSeasons.episodes.setText(mSeasonsPresenter.getNumberOfEpisodes(position));
        holderSeasons.overview.setText(mSeasonsPresenter.getOverview(position));
        holderSeasons.episodes.setText(mSeasonsPresenter.getNumberOfEpisodes(position));
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
            mSeasonsPresenter.startEpisodesActivity(mContext,getAdapterPosition());
        }
    }

}
