package com.example.android.tvshows.ui.find.discover;

import android.support.v7.widget.RecyclerView;

import com.example.android.tvshows.ui.find.EndlessRecyclerViewScrollListener;
import com.example.android.tvshows.ui.find.ResultsFragment;



public class DiscoverFragment extends ResultsFragment {

    @Override
    public void onStart() {
        super.onStart();
       mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mResultsPresenter.getDiscoverPage(getActivity(),page+1);
            }
        });
    }

}
