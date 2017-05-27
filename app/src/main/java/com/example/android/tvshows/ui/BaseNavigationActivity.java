package com.example.android.tvshows.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.tvshows.R;
import com.example.android.tvshows.ui.find.discover.DiscoverActivity;
import com.example.android.tvshows.ui.find.search.SearchActivity;
import com.example.android.tvshows.ui.myshows.MyShowsActivity;
import com.example.android.tvshows.ui.updates.UpdatesActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class BaseNavigationActivity extends AppCompatActivity {

    @BindView(R.id.navigation_drawer) View mNaviagationDrawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);
        mNaviagationDrawer.setPadding(0,getStatusBarHeight(),0,0);
    }

    //the layout of the sub class
    protected abstract int getLayoutResourceId();

    // the id of the view relating to the activity currently being displayed
    protected abstract int getViewId();

    // the drawer layout in the activity
    protected abstract DrawerLayout getDrawerLayout();

    @OnClick({R.id.navigation_my_shows,R.id.navigation_discover,R.id.navigation_search,R.id.navigation_updates})
    public void selectNavigation(TextView textView){
        if(textView.getId() == getViewId()){
            getDrawerLayout().closeDrawer(mNaviagationDrawer);
        }
        else {
            startNewActivity(textView);
        }

    }

    protected void startNewActivity(TextView textView){
        if(textView.getId() == R.id.navigation_my_shows){
            Intent intent = new Intent(this,MyShowsActivity.class);
            startActivity(intent);
        }
        else if(textView.getId() == R.id.navigation_discover){
            Intent intent = new Intent(this,DiscoverActivity.class);
            startActivity(intent);
        }
        else if(textView.getId() == R.id.navigation_search){
            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);
        }
        else if(textView.getId() == R.id.navigation_updates){
            Intent intent = new Intent(this,UpdatesActivity.class);
            startActivity(intent);
        }
    }


    @OnClick(R.id.tmdb_link)
    public void openTmDbWebpage(){
        Uri webpage = Uri.parse("https://www.themoviedb.org/?language=en");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
