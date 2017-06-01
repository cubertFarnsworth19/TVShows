package com.example.android.tvshows.ui.myshows.shows;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.tvshows.R;
import com.example.android.tvshows.ShowsApplication;
import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.ui.myshows.MyShowsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShowsFragment extends Fragment implements ShowsContract.View{

    private final String OUTSTATE_SHOWS_INFO = "show_info";
    private final String OUTSTATE_ADAPTER = "adapter";

    public static ShowsFragment getInstance(){
        ShowsFragment showsFragment = new ShowsFragment();
        return showsFragment;
    }

    @Inject ShowsAdapter mShowsAdapter;
    @Inject ShowsContract.Presenter mShowsPresenter;

    private boolean mLoaded;

    @BindView(R.id.recyclerview_shows) RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.myshows_shows_fragment,container,false);
        ButterKnife.bind(this,rootview);

        if(savedInstanceState!=null){

            ArrayList<ShowInfo> showInfo = savedInstanceState.getParcelableArrayList(OUTSTATE_SHOWS_INFO);
            ShowsAdapter adapter = savedInstanceState.getParcelable(OUTSTATE_ADAPTER);
            ShowsComponent component = DaggerShowsComponent.builder()
                    .applicationComponent(ShowsApplication.get(getActivity()).getComponent())
                    .showsModule(new ShowsModule(this, this,showInfo,adapter))
                    .build();

            component.inject(this);

            mLoaded = true;
        }
        else {
            ShowsComponent component = DaggerShowsComponent.builder()
                    .applicationComponent(ShowsApplication.get(getActivity()).getComponent())
                    .showsModule(new ShowsModule(this, this))
                    .build();

            component.inject(this);

            mLoaded = false;
        }

        setupRecyclerView();

        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!mLoaded) mShowsPresenter.loadShowsFromDatabase(getActivity(),false,false);
    }

    private void setupRecyclerView(){
        mRecyclerView.setAdapter(mShowsAdapter);
        GridLayoutManager glm = new GridLayoutManager(getActivity(),1);
        mRecyclerView.setLayoutManager(glm);
    }

    public ShowsContract.Presenter getShowsPresenter(){
        return mShowsPresenter;
    }

    @Override
    public void showsDataLoaded(int size) {
        mShowsAdapter.displayShows(size);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(OUTSTATE_SHOWS_INFO,mShowsPresenter.getShowsInfo());
        outState.putParcelable(OUTSTATE_ADAPTER,mShowsAdapter);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(menuVisible){
            MyShowsActivity myShowsActivity = (MyShowsActivity) getActivity();
            if(myShowsActivity!=null) myShowsActivity.setFilterButtonVisible(true);
        }
    }
}
