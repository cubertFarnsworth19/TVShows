package com.example.android.tvshows.ui.myshows;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.tvshows.R;
import com.example.android.tvshows.idlingResource.SimpleIdlingResource;
import com.example.android.tvshows.ui.BaseNavigationActivity;
import com.example.android.tvshows.ui.NavigationIconActivity;
import com.example.android.tvshows.ui.find.discover.GenresDialog;
import com.example.android.tvshows.ui.myshows.current.CurrentFragment;
import com.example.android.tvshows.ui.myshows.shows.ShowsFragment;
import com.example.android.tvshows.ui.tabs.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyShowsActivity extends NavigationIconActivity {

    private final String OUTSTATE_CONTINUING = "continuing";
    private final String OUTSTATE_FAVORITE = "favorite";

    @BindView(R.id.tabs)SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.pager) ViewPager mPager;
    @BindView(R.id.nested_scroll_view)NestedScrollView mNestedScrollView;
    @BindView(R.id.btn_filter) ImageButton mButtonFilter;

    // if filter includes
    private boolean mContinuing;
    private boolean mFavorite;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        if(savedInstanceState!=null){
            mContinuing = savedInstanceState.getBoolean(OUTSTATE_CONTINUING,false);
            mFavorite = savedInstanceState.getBoolean(OUTSTATE_FAVORITE,false);
        }
        else{
            mContinuing = false;
            mFavorite = false;
        }

        mNestedScrollView.setFillViewport(true);
        mNestedScrollView.setNestedScrollingEnabled(true);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
              //  Log.v("Pager","onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                Log.v("Pager","onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //if(state==1) setNotIdle();
                Log.v("Pager","onPageScrollStateChanged " + state);
            }
        });
        mSlidingTabLayout.setViewPager(mPager);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer(){
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
        });

        getWindow().setBackgroundDrawable(null);
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
            if(position==0) {
                fragment = ShowsFragment.getInstance();
            }
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

    public void setFilterButtonVisible(boolean visible){
        Log.v("Pager","setFilterButtonVisible");
        if(visible) mButtonFilter.setVisibility(View.VISIBLE);
        else  mButtonFilter.setVisibility(View.INVISIBLE);

        if (mIdlingResource != null) mIdlingResource.setIdleState(true);
    }

    @OnClick(R.id.btn_filter)
    public void clickButton() {
        FragmentManager fm = this.getSupportFragmentManager();
        FilterMyShowsDialog filterMyShowsDialog = new FilterMyShowsDialog(this,mContinuing,mFavorite);
        filterMyShowsDialog.show(fm,"dialog_myshows_filter");
    }

    public void setContinuing(boolean continuing) {
        mContinuing = continuing;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(OUTSTATE_CONTINUING,mContinuing);
        outState.putBoolean(OUTSTATE_FAVORITE,mFavorite);
    }


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @VisibleForTesting
    public void setNotIdle(){
        if (mIdlingResource != null) mIdlingResource.setIdleState(false);
    }


}
