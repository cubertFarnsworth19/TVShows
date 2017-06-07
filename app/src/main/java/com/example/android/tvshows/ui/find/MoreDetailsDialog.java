package com.example.android.tvshows.ui.find;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tvshows.R;
import com.example.android.tvshows.data.model.tvshowdetailed.Genre;
import com.example.android.tvshows.data.model.tvshowdetailed.TVShowDetailed;
import com.example.android.tvshows.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("ValidFragment")
public class MoreDetailsDialog extends DialogFragment{

    private View rootview;
    @BindView(R.id.title)TextView mTitle;
    @BindView(R.id.start_year) TextView mStartYear;
    @BindView(R.id.in_production) TextView mInProduction;
    @BindView(R.id.user_score) TextView mUserScore;
    @BindView(R.id.vote_count) TextView mVoteCount;
    @BindView(R.id.genres_list) TextView mGenresList;
    @BindView(R.id.creators_list) TextView mCreatorsList;
    @BindView(R.id.overview) TextView mOverview;

    private TVShowDetailed mTvShowDetailed;
    private ResultsContract.Presenter mPresenter;
    private final int mAdapterPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview =  inflater.inflate(R.layout.find_moredetails_dialog, container, false);
        ButterKnife.bind(this,rootview);
        setText();
        return rootview;
    }

    public MoreDetailsDialog(TVShowDetailed tvShowDetailed,ResultsContract.Presenter presenter,int adapterPosition) {
        mTvShowDetailed = tvShowDetailed;
        mPresenter = presenter;
        mAdapterPosition = adapterPosition;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    public void setText(){
        mTitle.setText(mTvShowDetailed.getName());
        mStartYear.setText(mTvShowDetailed.getFirstAirDateYearString());
        mInProduction.setText(mTvShowDetailed.getStatus());
        mUserScore.setText(mTvShowDetailed.getVoteAverageString());
        mVoteCount.setText(mTvShowDetailed.getVoteCount().toString());
        String genresList = "";
        for(int i=0;i<mTvShowDetailed.getGenres().size();i++){
            if (i!=0) genresList = genresList + ", ";
            genresList = genresList + mTvShowDetailed.getGenres().get(i).getName();
        }
        mGenresList.setText(genresList);

        String creatorsList = "";
        for(int i=0;i<mTvShowDetailed.getCreatedBy().size();i++){
            if (i!=0) creatorsList = creatorsList + ", ";
            creatorsList = creatorsList + mTvShowDetailed.getCreatedBy().get(i).getName();
        }

        mCreatorsList.setText(creatorsList);

        mOverview.setText(mTvShowDetailed.getOverview());

        GradientDrawable voteAverageCircle = (GradientDrawable) mUserScore.getBackground();
        Double voteAverage = mTvShowDetailed.getVoteAverage();
        voteAverageCircle.setColor(Utility.getRatingBackgroundColor(getContext(),voteAverage));
        mUserScore.setTextColor(Utility.getTextColor(getContext(),voteAverage));
    }

    @OnClick({R.id.add_show,R.id.dismiss})
    void click(Button button){
        if(button.getId()==R.id.add_show){
            mPresenter.showAdded();
            mPresenter.saveSelectedToDatabase(getContext(),mTvShowDetailed.getId());
            Toast.makeText(getContext(),"Downloading " + mTvShowDetailed.getName(),Toast.LENGTH_SHORT).show();
            dismiss();
        }
        else if(button.getId()==R.id.dismiss)
            dismiss();
    }


    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

}
