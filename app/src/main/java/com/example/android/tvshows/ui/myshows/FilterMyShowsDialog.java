package com.example.android.tvshows.ui.myshows;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.android.tvshows.R;
import com.example.android.tvshows.ui.myshows.current.CurrentContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("ValidFragment")
public class FilterMyShowsDialog extends DialogFragment {

    View rootview;
    @BindView(R.id.check_box_continuing) CheckBox mCheckBoxContinuing;
    @BindView(R.id.check_box_favorites) CheckBox mCheckBoxFavorites;

    private MyShowsActivity mMyShowsActivity;
    private CurrentContract.Presenter mPresenter;
    private boolean mContinuing;
    private boolean mFavorite;

    FilterMyShowsDialog(MyShowsActivity myShowsActivity,CurrentContract.Presenter presenter,boolean continuing,boolean favorite){
        mMyShowsActivity = myShowsActivity;
        mPresenter = presenter;
        mContinuing = continuing;
        mFavorite = favorite;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview =  inflater.inflate(R.layout.myshows_shows_filters_dialog, container, false);
        ButterKnife.bind(this,rootview);
        mCheckBoxContinuing.setChecked(mContinuing);
        mCheckBoxFavorites.setChecked(mFavorite);
        return rootview;
    }

    @OnClick({R.id.add_filters,R.id.dismiss})
    void click(Button button){
        if(button.getId()==R.id.add_filters){
            mMyShowsActivity.setContinuing(mCheckBoxContinuing.isChecked());
            mMyShowsActivity.setFavorite(mCheckBoxFavorites.isChecked());
        }
        else if(button.getId()==R.id.dismiss)
            dismiss();
    }
}
