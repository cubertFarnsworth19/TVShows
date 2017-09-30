package com.example.android.tvshows.ui.find.discover;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.tvshows.R;
import com.example.android.tvshows.ui.BaseNavigationActivity;
import com.example.android.tvshows.ui.NavigationIconActivity;
import com.example.android.tvshows.ui.find.MoreDetailsDialog;
import com.example.android.tvshows.ui.find.ResultsFragment;
import com.example.android.tvshows.util.Genres;
import com.example.android.tvshows.util.Utility;
import com.github.aakira.expandablelayout.ExpandableWeightLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiscoverActivity extends NavigationIconActivity {

    private final String FRAGMENT = "Results Fragment";
    private final String OUTSTATE_INCLUDE_GENRES = "include_genres";
    private final String OUTSTATE_EXCLUDE_GENRES = "exclude_genres";
    private final String OUTSTATE_EXPANDED = "expanded";

    @BindView(R.id.discover_filters) View mViewDiscoverFilters;
    @BindView(R.id.find_discover_activity)  LinearLayout mRootLayout;
    @BindView(R.id.edit_text_vote_average_min) EditText mEditTextVoteAverage;
    @BindView(R.id.edit_text_vote_count_min) EditText mEditTextVoteCount;
    @BindView(R.id.spinner_sort_by) Spinner mSpinnerSortBy;
    @BindView(R.id.spinner_first_air_date_min) Spinner mSpinnerFirstAirDateAfter;
    @BindView(R.id.spinner_first_air_date_max) Spinner mSpinnerFirstAirDateBefore;
    @BindView(R.id.layout_include_genres) LinearLayout mLayoutIncludeGenres;
    @BindView(R.id.layout_exclude_genres) LinearLayout mLayoutExcludeGenres;
    @BindView(R.id.txt_include_genres) TextView mTextViewIncludeGenres;
    @BindView(R.id.txt_exclude_genres) TextView mTextViewExcludeGenres;
    @BindView(R.id.expandable_layout)ExpandableWeightLayout mExpandableWeightLayout;

    private ResultsFragment mResultsFragment;

    // the genres to be included and excluded from the search
    // the 16 genres are in alphabetical order
    boolean[] mIncludeGenres = new boolean[Genres.numberOfGenres()];
    boolean[] mExcludeGenres = new boolean[Genres.numberOfGenres()];

    boolean mExpanded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        getWindow().setBackgroundDrawable(null);

        setupSpinners();

        if(savedInstanceState != null){
            mResultsFragment = (DiscoverFragment) getSupportFragmentManager().getFragment(savedInstanceState,FRAGMENT);
            mIncludeGenres = savedInstanceState.getBooleanArray(OUTSTATE_INCLUDE_GENRES);
            mExcludeGenres = savedInstanceState.getBooleanArray(OUTSTATE_EXCLUDE_GENRES);
            mTextViewIncludeGenres.setText(Genres.getGenresString(mIncludeGenres,this));
            mTextViewExcludeGenres.setText(Genres.getGenresString(mExcludeGenres,this));
            mExpanded = savedInstanceState.getBoolean(OUTSTATE_EXPANDED);
            ImageButton button = (ImageButton) findViewById(R.id.btn_expand);
            if(mExpanded)
                button.setImageResource(R.drawable.ic_expand_less_white_24dp);
            else
                button.setImageResource(R.drawable.ic_expand_more_white_24dp);
        }
        else {
            mResultsFragment = new DiscoverFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.add(R.id.find_discover_content,mResultsFragment,FRAGMENT);
            fragmentTransaction.commit();

            mExpanded = false;
        }

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.find_discover_activity;
    }

    @Override
    protected int getViewId() {
        return R.id.navigation_discover;
    }

    @Override
    protected DrawerLayout getDrawerLayout() {
        return (DrawerLayout) findViewById(R.id.discover_drawer_layout);
    }

    @Override
    protected Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    @OnClick(R.id.btn_expand)
    public void clickExpandButtonImage(ImageButton button){
       // mExpandableWeightLayout.setExpandWeight(1.5);
        mExpandableWeightLayout.toggle();
        setExpandButtonImage(button);
    }

    private void setExpandButtonImage(ImageButton button){
        if(mExpandableWeightLayout.getHeight()==0) {
            button.setImageResource(R.drawable.ic_expand_less_white_24dp);
            mExpanded = true;
        }
        else {
            button.setImageResource(R.drawable.ic_expand_more_white_24dp);
            mExpanded = false;
        }
    }

    @OnClick(R.id.btn_find)
    public void clickFind(){
        // set the filters and send to presenter to make request
        mResultsFragment.setFilters(
                getSortByString(mSpinnerSortBy.getSelectedItemPosition()),
                Genres.genresToStringInts(mIncludeGenres),
                Genres.genresToStringInts(mExcludeGenres),
                mEditTextVoteAverage.getText().toString(),
                mEditTextVoteCount.getText().toString(),
                mSpinnerFirstAirDateAfter.getSelectedItem().toString(),
                mSpinnerFirstAirDateBefore.getSelectedItem().toString());
    }

    // open the dialog to select genres
    @OnClick({R.id.layout_include_genres,R.id.layout_exclude_genres})
    public void setGenres(View layout){
        FragmentManager fm = this.getSupportFragmentManager();
        GenresDialog genresDialog;
        if(layout.getId() == mLayoutIncludeGenres.getId()){
            genresDialog = new GenresDialog(this,mIncludeGenres,true);
            genresDialog.show(fm,"dialog_include_genres");
        }
        else{
            genresDialog = new GenresDialog(this,mExcludeGenres,false);
            genresDialog.show(fm,"dialog_exclude_genres");
        }

    }

    public void setIncludeGenres(boolean[] includeGenres){
        mIncludeGenres = includeGenres;
        mTextViewIncludeGenres.setText(Genres.getGenresString(includeGenres,this));
        mTextViewIncludeGenres.invalidate();
    }

    public void setExcludeGenres(boolean[] excludeGenres){
        mExcludeGenres = excludeGenres;
        mTextViewExcludeGenres.setText(Genres.getGenresString(excludeGenres,this));
        mTextViewExcludeGenres.invalidate();
    }

    //set the data for the 3 spinners
    private void setupSpinners(){
        ArrayAdapter<CharSequence> adapterSortBy = ArrayAdapter.createFromResource(this,
                R.array.sort_by_options, R.layout.find_discover_spinnner_item);
        adapterSortBy.setDropDownViewResource(R.layout.find_discover_spinnner_item);
        mSpinnerSortBy.setAdapter(adapterSortBy);

        ArrayList<String> years = Utility.getYearsArray();
        ArrayAdapter<String> adapterYears = new ArrayAdapter<>(this,R.layout.find_discover_spinnner_item,years);
        adapterSortBy.setDropDownViewResource(R.layout.find_discover_spinnner_item);
        mSpinnerFirstAirDateAfter.setAdapter(adapterYears);
        mSpinnerFirstAirDateBefore.setAdapter(adapterYears);
    }

    private String getSortByString(int index){
        String[] sortByOptions = getResources().getStringArray(R.array.sort_by_options_request);
        return sortByOptions[index];
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT,mResultsFragment);
        outState.putBooleanArray(OUTSTATE_INCLUDE_GENRES,mIncludeGenres);
        outState.putBooleanArray(OUTSTATE_EXCLUDE_GENRES,mExcludeGenres);
        outState.putBoolean(OUTSTATE_EXPANDED,mExpanded);
    }

}
