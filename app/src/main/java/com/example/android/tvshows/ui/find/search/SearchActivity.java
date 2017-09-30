package com.example.android.tvshows.ui.find.search;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.android.tvshows.R;
import com.example.android.tvshows.ui.BaseNavigationActivity;
import com.example.android.tvshows.ui.NavigationIconActivity;
import com.example.android.tvshows.ui.find.ResultsFragment;
import com.example.android.tvshows.ui.find.discover.DiscoverFragment;

import butterknife.BindView;

public class SearchActivity extends NavigationIconActivity{

    private final String FRAGMENT = "Results Fragment";

    private ResultsFragment mResultsFragment;

   //@BindView(R.id.search_view)
    SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(null);

        if(savedInstanceState != null){
            mResultsFragment = (ResultsFragment) getSupportFragmentManager().getFragment(savedInstanceState,FRAGMENT);
        }
        else {

            mResultsFragment = new ResultsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.find_search_content, mResultsFragment,FRAGMENT);
            fragmentTransaction.commit();
        }

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mResultsFragment.search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.find_search_activity;
    }

    @Override
    protected int getViewId() {
        return R.id.navigation_search;
    }

    @Override
    protected DrawerLayout getDrawerLayout() {
        return (DrawerLayout) findViewById(R.id.search_drawer_layout);
    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT,mResultsFragment);
    }



}
