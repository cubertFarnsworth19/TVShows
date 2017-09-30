package com.example.android.tvshows.ui.find;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tvshows.R;
import com.example.android.tvshows.data.model.search.DiscoverResults;
import com.example.android.tvshows.data.model.search.Result;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Parcelable {

    Context mContext;
    ResultsContract.Presenter mResultsPresenter;
    Picasso mPicasso;
    private int mSize;
    private boolean mWide = false;

    @Inject
    public ResultsAdapter(Context context,ResultsContract.Presenter resultsPresenter,Picasso picasso){
        mContext = context;
        mResultsPresenter = resultsPresenter;
        mPicasso = picasso;
    }

    public void setVariables(Context context,ResultsContract.Presenter resultsPresenter,Picasso picasso){
        mContext = context;
        mResultsPresenter = resultsPresenter;
        mPicasso = picasso;
    }

    public void updateDiscoverResults(int size){
        mSize = size;
        notifyDataSetChanged();
    }

    public void setLayoutType(boolean wide){
        mWide = wide;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       int layout = mWide ? R.layout.find_results_list_item_wide : R.layout.find_results_list_item;

            View view = LayoutInflater.from(parent.getContext())
            .inflate(layout,parent,false);
            return new ViewHolderResults(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderResults holderResults = (ViewHolderResults) holder;
        holderResults.setAddButtonVisible(mResultsPresenter.showAddButton(position));
        holderResults.title.setText(mResultsPresenter.getName(position));
        holderResults.year.setText(mResultsPresenter.getFirstAirDate(position));
        holderResults.userScore.setText(mResultsPresenter.getVoteAverage(position));
        GradientDrawable voteAverageCircle = (GradientDrawable) holderResults.userScore.getBackground();
        voteAverageCircle.setColor(mResultsPresenter.getVoteAverageBackgroundColor(mContext,position));
        holderResults.userScore.setTextColor(mResultsPresenter.getVoteAverageTextColor(mContext,position));
        mPicasso.load(mResultsPresenter.getPosterUrl(mContext,position)).into(holderResults.poster);
        holderResults.setTmdbId(mResultsPresenter.getTmdbId(position));
    }

    @Override
    public int getItemCount() {
        return mSize;
    }

    class ViewHolderResults extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.title) TextView title;
        @BindView(R.id.poster) ImageView poster;
        @BindView(R.id.start_year) TextView year;
        @BindView(R.id.user_score) TextView userScore;
        @BindView(R.id.button_add) ImageButton buttonAdd;
        @BindView(R.id.show_more_details) TextView showMoreDetails;

        private int tmdbId;

        public ViewHolderResults(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            buttonAdd.setOnClickListener(this);
            showMoreDetails.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == buttonAdd.getId()){
                buttonAdd.setVisibility(View.INVISIBLE);
                showMoreDetails.setVisibility(View.INVISIBLE);
                Toast.makeText(mContext,"Downloading " + title.getText().toString(),Toast.LENGTH_SHORT).show();
                mResultsPresenter.saveSelectedToDatabase(mContext,tmdbId);
            }
            else if(view.getId() == showMoreDetails.getId()){
                mResultsPresenter.openMoreDetailsDialog(mContext,getAdapterPosition());
            }
        }

        public void setTmdbId(int tmdbId) {
            this.tmdbId = tmdbId;
        }

        public void setAddButtonVisible(boolean visible){
            if(visible) {
                buttonAdd.setVisibility(View.VISIBLE);
                showMoreDetails.setVisibility(View.VISIBLE);
            }
            else {
                buttonAdd.setVisibility(View.INVISIBLE);
                showMoreDetails.setVisibility(View.INVISIBLE);
            }
        }
    }

    protected ResultsAdapter(Parcel in) {
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
    public static final Parcelable.Creator<ResultsAdapter> CREATOR = new Parcelable.Creator<ResultsAdapter>() {
        @Override
        public ResultsAdapter createFromParcel(Parcel in) {
            return new ResultsAdapter(in);
        }

        @Override
        public ResultsAdapter[] newArray(int size) {
            return new ResultsAdapter[size];
        }
    };

}
