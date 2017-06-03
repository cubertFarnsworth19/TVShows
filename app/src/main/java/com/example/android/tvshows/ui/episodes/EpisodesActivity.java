package com.example.android.tvshows.ui.episodes;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.android.tvshows.R;
import com.example.android.tvshows.ShowsApplication;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.BaseNavigationActivity;
import com.example.android.tvshows.ui.tabs.SlidingTabLayout;
import com.example.android.tvshows.util.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

public class EpisodesActivity extends BaseNavigationActivity implements EpisodesContract.View{

    @Inject EpisodesContract.Presenter mEpisodesPresenter;
    @Inject Picasso mPicasso;

    private int mNumberOfEpisodes = 0;
    private int mInitialSpinnerPosition;
    private boolean mInitialised = false;

    @BindView(R.id.tabs)SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.pager) ViewPager mPager;
    @BindView(R.id.nested_scroll_view)NestedScrollView mNestedScrollView;
    @BindView(R.id.spinner_seasons)Spinner mSpinnerSeasons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNestedScrollView.setFillViewport(true);
        mNestedScrollView.setNestedScrollingEnabled(true);

        Intent intent = getIntent();
        int showId = intent.getIntExtra(ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY,-1);
        String[] seasonNames = intent.getStringArrayExtra(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NAME);
        int[] seasonNumbers = intent.getIntArrayExtra(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NUMBER);
        mInitialSpinnerPosition = intent.getIntExtra("adapter_position",0);


        EpisodesComponent component = DaggerEpisodesComponent.builder()
                .applicationComponent(ShowsApplication.get(this).getComponent())
                .episodesModule(new EpisodesModule(this,this,seasonNames,showId,seasonNumbers[mInitialSpinnerPosition],seasonNumbers))
                .build();
        component.inject(this);

        setupToolbar();

        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer(){
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mEpisodesPresenter.loadEpisodesData(this);
    }

    private void setupSpinner(){
        ArrayAdapter<String> adapterSeasons = new ArrayAdapter<>(this,R.layout.episodes_seasons_spinner_item,mEpisodesPresenter.getSeasonNames());
        mSpinnerSeasons.setAdapter(adapterSeasons);
        mSpinnerSeasons.setSelection(mInitialSpinnerPosition);

        mSpinnerSeasons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(mInitialised)
                    mEpisodesPresenter.startNewEpisodesActvity(EpisodesActivity.this,i);

                mInitialised = true;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void episodeDataLoaded(int numberOfEpisodes) {
        mNumberOfEpisodes = numberOfEpisodes;
        mPager.setAdapter(new EpisodesPagerAdapter(getSupportFragmentManager()));
        mSlidingTabLayout.setViewPager(mPager);
        setupSpinner();
    }

    @Override
    public void endActivity() {
        finish();
    }


    public Picasso getPicasso() {
        return mPicasso;
    }

    public void visitIMDbPage(int episodeNumber){
        mEpisodesPresenter.visitIMDbPage(this,episodeNumber);
    }

    public void visitTMDBPage(int tmdbId){
        mEpisodesPresenter.visitTMDBPage(this,tmdbId);
    }

    public void searchGoogle(String episodeName){
        mEpisodesPresenter.searchGoogle(this,episodeName);
    }

    public void searchYouTube(String episodeName){
        mEpisodesPresenter.searchYouTube(this,episodeName);
    }
    // navigation drawer methods
    @Override
    protected int getLayoutResourceId() {
        return R.layout.episodes_activity;
    }

    @Override
    protected int getViewId() {
        return -1;
    }

    @Override
    protected DrawerLayout getDrawerLayout() {
        return (DrawerLayout) findViewById(R.id.episodes_drawer_layout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




    class EpisodesPagerAdapter extends FragmentStatePagerAdapter{

        String[] tabs;

        public EpisodesPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = new String[mNumberOfEpisodes];
            for(int i=0;i<mNumberOfEpisodes;i++){
                int episode = i+1;
                tabs[i] = "e" + episode;
            }
        }

        @Override
        public Fragment getItem(int position) {
            EpisodeData episodeData = mEpisodesPresenter.getEpisodeData(EpisodesActivity.this,position);
            Fragment fragment = new EpisodesFragment();

            Bundle args = new Bundle();
            args.putString("overview",episodeData.getOverview());
            args.putString("poster",episodeData.getStillPhotoUrl());
            args.putParcelable(EpisodeData.episodeData,episodeData);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return mNumberOfEpisodes;
        }
    }


}
