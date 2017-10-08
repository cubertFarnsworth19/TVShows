package com.example.android.tvshows.ui.showinfo.details;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.tvshows.R;
import com.example.android.tvshows.ShowsApplication;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.util.ExternalLinks;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsFragment extends Fragment implements DetailsContract.View {

    private static int mTmdbId;

    public static DetailsFragment getInstance(int tmdbId){
        mTmdbId = tmdbId;
        return new DetailsFragment();
    }

    @Inject DetailsContract.Presenter mDetailsPresenter;
    @Inject Picasso mPicasso;
    @Inject CreatorAdapter mCreatorAdapter;

    @BindView(R.id.poster) ImageView mPoster;
    @BindView(R.id.start_year) TextView mStartYear;
    @BindView(R.id.user_score) TextView mUserScore;
    @BindView(R.id.vote_count) TextView mVoteCount;
    @BindView(R.id.overview) TextView mOverview;
    @BindView(R.id.genres) TextView mGenres;
    @BindView(R.id.recyclerview_creator)RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.show_info_details_fragment,container,false);
        ButterKnife.bind(this,rootview);

        ShowsApplication showsApplication = (ShowsApplication) getActivity().getApplication();

        DetailsComponent component = DaggerDetailsComponent.builder()
                .applicationComponent(showsApplication.get(getActivity()).getComponent())
                .detailsModule(showsApplication.getDetailsModule(this,this,mTmdbId))
                .build();

        component.inject(this);

        setupRecyclerView();

        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        mDetailsPresenter.loadShowDetails(getActivity());
    }

    private void setupRecyclerView(){
        mRecyclerView.setAdapter(mCreatorAdapter);
        LinearLayoutManager glm = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void setUserInterfaceText(String overview, String startYear,
                                     String userScore, String voteCount, String genres,
                                     int userScoreBackgroundColor,int userScoreTextColor) {
        mOverview.setText(overview);
        mStartYear.setText(startYear);
        mUserScore.setText(userScore);
        mVoteCount.setText(voteCount);
        mGenres.setText(genres);
        GradientDrawable voteAverageCircle = (GradientDrawable) mUserScore.getBackground();
        voteAverageCircle.setColor(userScoreBackgroundColor);
        mUserScore.setTextColor(userScoreTextColor);
    }

    @Override
    public void setPoster(String url) {
        String posterUrl = getActivity().getString(R.string.poster_path) + url;
        mPicasso.load(posterUrl).into(mPoster);
    }

    @Override
    public void creatorDataLoaded(int size) {
        mCreatorAdapter.displayCreators(size);
    }

    @Override
    public void setIMDBid(String id) {
        ExternalLinks.vistIMDBShowPage(this.getContext(),mDetailsPresenter.getImdbId());
    }

    @OnClick({R.id.link_imdb,R.id.link_tmdb,R.id.link_google_search,R.id.link_youtube_search,R.id.link_wikipedia})
    void visitLink(View view){
        if(view.getId() == R.id.link_imdb){
            if(mDetailsPresenter.downloadExternalIds()) ExternalLinks.vistIMDBShowPage(this.getContext(),mDetailsPresenter.getImdbId());
        }
        else if(view.getId() == R.id.link_tmdb){
            ExternalLinks.visitTMDBPage(this.getContext(),mTmdbId);
        }
        else if(view.getId() == R.id.link_google_search){
            ExternalLinks.searchGoogle(this.getContext(),mDetailsPresenter.getTitle());
        }
        else if(view.getId() == R.id.link_youtube_search){
            ExternalLinks.searchYoutube(this.getContext(),mDetailsPresenter.getTitle());
        }
        else if(view.getId() == R.id.link_wikipedia){
            ExternalLinks.visitWikipedia(this.getContext(),mDetailsPresenter.getTitle());
        }
    }

}
