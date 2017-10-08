package com.example.android.tvshows.ui.episodes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.util.ExternalLinks;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EpisodesFragment extends Fragment{

    @BindView(R.id.still_photo) ImageView mStillPhoto;
    @BindView(R.id.episode_name) TextView mEpisodeName;
    @BindView(R.id.overview) TextView mOverview;
    @BindView(R.id.original_air_date)TextView mAirDate;
    private int mTMDbId;
    private int mEpisodeNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.episodes_fragment,container,false);
        ButterKnife.bind(this,rootview);

        EpisodeData episodeData = getArguments().getParcelable(EpisodeData.episodeData);
        mEpisodeNumber = episodeData.getEpisodeNumber();
        mTMDbId = episodeData.getTmdbId();

        ((EpisodesActivity)getActivity()).getPicasso().load(episodeData.getStillPhotoUrl()).into(mStillPhoto);

        mEpisodeName.setText(episodeData.getEpisodeName());
        mAirDate.setText(episodeData.getOriginalAirDate());
        mOverview.setText(episodeData.getOverview());

        return rootview;
    }



    @OnClick({R.id.link_imdb,R.id.link_tmdb,R.id.link_google_search,R.id.link_youtube_search})
    void visitLink(View view){
        if(view.getId() == R.id.link_imdb){
            if(((EpisodesActivity)getActivity()).mEpisodesPresenter.downloadExternalIds(mEpisodeNumber))
                ExternalLinks.vistIMDBShowPage(this.getContext(),((EpisodesActivity)getActivity()).mEpisodesPresenter.getImdbId());
        }
        else if(view.getId() == R.id.link_tmdb){
            ExternalLinks.visitTMDBPage(getContext(),mTMDbId);
        }
        else if(view.getId() == R.id.link_google_search){
            ExternalLinks.searchGoogle(getContext(),((EpisodesActivity)getActivity()).mEpisodesPresenter.getTitle()+" "+mEpisodeName.getText().toString());
        }
        else if(view.getId() == R.id.link_youtube_search){
            ExternalLinks.searchYoutube(getContext(),((EpisodesActivity)getActivity()).mEpisodesPresenter.getTitle()+" "+mEpisodeName.getText().toString());
        }

    }


}
