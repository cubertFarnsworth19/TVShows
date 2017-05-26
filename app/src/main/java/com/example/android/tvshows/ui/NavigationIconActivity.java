package com.example.android.tvshows.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.example.android.tvshows.R;

public abstract class NavigationIconActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbarIcon();
    }

    // sets the hamburger icon and the back arrow
    private void setupToolbarIcon(){
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, getDrawerLayout(), getToolbar(), R.string.drawer_open, R.string.drawer_close
        );
        getDrawerLayout().setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    protected abstract Toolbar getToolbar();

}
