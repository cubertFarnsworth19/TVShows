package com.example.android.tvshows.ui.showinfo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.android.tvshows.R;
import com.example.android.tvshows.data.db.ShowsDbContract;
import com.example.android.tvshows.ui.BaseNavigationActivity;

import com.example.android.tvshows.ui.showinfo.cast.CastFragment;
import com.example.android.tvshows.ui.showinfo.details.DetailsFragment;
import com.example.android.tvshows.ui.showinfo.recommendations.RecommendationsFragment;
import com.example.android.tvshows.ui.showinfo.seasons.SeasonsFragment;
import com.example.android.tvshows.ui.tabs.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowInfoActivity extends BaseNavigationActivity {

    @BindView(R.id.tabs)SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.pager) ViewPager mPager;
    @BindView(R.id.nested_scroll_view)NestedScrollView mNestedScrollView;
    @BindView(R.id.show_name)TextView mShowName;
    private int tmdbId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mNestedScrollView.setFillViewport(true);
        mNestedScrollView.setNestedScrollingEnabled(true);

        mPager.setAdapter(new ShowInfoPagerAdapter(getSupportFragmentManager()));
        mSlidingTabLayout.setViewPager(mPager);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer(){
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
        });

        getWindow().setBackgroundDrawable(null);

        setipToolbar();

        Intent intent = getIntent();
        tmdbId = intent.getIntExtra(ShowsDbContract.ShowsEntry._ID,-1);
        mShowName.setText(intent.getStringExtra(ShowsDbContract.ShowsEntry.COLUMN_NAME));

    }

    private void setipToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    class ShowInfoPagerAdapter extends FragmentStatePagerAdapter {

        String[] tabs;

        public ShowInfoPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.show_info_tabs);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            if(position==0)
                fragment = DetailsFragment.getInstance(tmdbId);
            else if(position == 1)
                fragment = SeasonsFragment.getInstance(tmdbId);
            else if(position == 2)
                fragment = CastFragment.getInstance(tmdbId);
            else
                fragment = RecommendationsFragment.getInstance(tmdbId);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    // navigation drawer methods
    @Override
    protected int getLayoutResourceId() {
        return R.layout.show_info_activity;
    }

    @Override
    protected int getViewId() {
        return -1;
    }

    @Override
    protected DrawerLayout getDrawerLayout() {
        return (DrawerLayout) findViewById(R.id.show_info_drawer_layout);
    }

    public static Intent getIntent(Context context,int id,String title) {
        Intent intent = new Intent(context, ShowInfoActivity.class);
        intent.putExtra(ShowsDbContract.ShowsEntry._ID, id);
        intent.putExtra(ShowsDbContract.ShowsEntry.COLUMN_NAME, title);
        return intent;
    }

}
