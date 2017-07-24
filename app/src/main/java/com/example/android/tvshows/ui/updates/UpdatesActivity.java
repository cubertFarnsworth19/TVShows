package com.example.android.tvshows.ui.updates;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.tvshows.R;
import com.example.android.tvshows.ui.NavigationIconActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdatesActivity extends NavigationIconActivity {

    private UpdatesFragment mUpdatesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        getWindow().setBackgroundDrawable(null);

        mUpdatesFragment = new UpdatesFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.updates_content,mUpdatesFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.updates_activity;
    }

    @Override
    protected int getViewId() {
        return R.id.navigation_updates;
    }

    @Override
    protected DrawerLayout getDrawerLayout() {
        return (DrawerLayout) findViewById(R.id.updates_drawer_layout);
    }
    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    @OnClick(R.id.btn_update)
    void updateSelected(){
        mUpdatesFragment.updateSelected();
        mUpdatesFragment.unCheckAll();
    }

}
