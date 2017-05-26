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
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShowsFragment extends Fragment implements ShowsContract.View{

    public static ShowsFragment getInstance(){
        ShowsFragment showsFragment = new ShowsFragment();
        return showsFragment;
    }

    @Inject ShowsAdapter mShowsAdapter;
    @Inject ShowsContract.Presenter mShowsPresenter;

    @BindView(R.id.recyclerview_shows) RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.myshows_shows_fragment,container,false);
        ButterKnife.bind(this,rootview);

        ShowsComponent component = DaggerShowsComponent.builder()
                .applicationComponent(ShowsApplication.get(getActivity()).getComponent())
                .showsModule(new ShowsModule(this,this))
                .build();

        component.inject(this);

        setupRecyclerView();

        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        mShowsPresenter.loadShowsFromDatabase(getActivity());
    }

    private void setupRecyclerView(){
        mRecyclerView.setAdapter(mShowsAdapter);
        GridLayoutManager glm = new GridLayoutManager(getActivity(),1);
        mRecyclerView.setLayoutManager(glm);
    }

    @Override
    public void showsDataLoaded(int size) {
        mShowsAdapter.displayShows(size);
    }

}
