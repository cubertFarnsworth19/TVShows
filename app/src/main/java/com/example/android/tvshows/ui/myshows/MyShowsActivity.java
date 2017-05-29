package com.example.android.tvshows.ui.myshows;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.android.tvshows.R;
import com.example.android.tvshows.ui.BaseNavigationActivity;
import com.example.android.tvshows.ui.NavigationIconActivity;
import com.example.android.tvshows.ui.myshows.current.CurrentFragment;
import com.example.android.tvshows.ui.myshows.shows.ShowsFragment;
import com.example.android.tvshows.ui.tabs.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyShowsActivity extends NavigationIconActivity {

    @BindView(R.id.tabs)SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.pager) ViewPager mPager;
    @BindView(R.id.nested_scroll_view)NestedScrollView mNestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mNestedScrollView.setFillViewport(true);
        mNestedScrollView.setNestedScrollingEnabled(true);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mSlidingTabLayout.setViewPager(mPager);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer(){
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
        });
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {

        String[] tabs;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.my_shows_tabs);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            if(position==0)
                fragment = ShowsFragment.getInstance();
            else {
                fragment = CurrentFragment.getInstance(position);
            }

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 3;
        }
    }




    // navigation drawer methods
    @Override
    protected int getLayoutResourceId() {
        return R.layout.my_shows_activity;
    }

    @Override
    protected int getViewId() {
        return R.id.navigation_my_shows;
    }

    @Override
    protected DrawerLayout getDrawerLayout() {
        return (DrawerLayout) findViewById(R.id.my_shows_drawer_layout);
    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }


}
