package com.example.android.tvshows.ui.find.discover;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;

import com.example.android.tvshows.R;
import com.example.android.tvshows.util.Genres;

import butterknife.BindView;
import butterknife.ButterKnife;


@SuppressLint("ValidFragment")
public class GenresDialog extends DialogFragment implements View.OnClickListener{

    View rootview;

    @BindView(R.id.genres_grid)GridLayout mGridGenres;

    private DiscoverActivity mDiscoverActivity;
    private boolean[] mGenresSelected;
    private boolean mInclude;

    CheckBox[] mCheckBoxesGenres = new CheckBox[Genres.numberOfGenres()];

    public GenresDialog(DiscoverActivity discoverActivity, boolean[] genresSelected,boolean include){
        mDiscoverActivity = discoverActivity;
        mGenresSelected = genresSelected;
        mInclude = include;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview =  inflater.inflate(R.layout.find_discover_genre_dialog, container, false);
        ButterKnife.bind(this,rootview);
        setCheckedGenres();
        setButtons();
        return rootview;
    }

    // set the genres that have been selected
    private void setCheckedGenres(){
        mCheckBoxesGenres[0] = (CheckBox) rootview.findViewById(R.id.check_box_action_adventure);
        mCheckBoxesGenres[1] = (CheckBox) rootview.findViewById(R.id.check_box_animation);
        mCheckBoxesGenres[2] = (CheckBox) rootview.findViewById(R.id.check_box_comedy);
        mCheckBoxesGenres[3] = (CheckBox) rootview.findViewById(R.id.check_box_crime);
        mCheckBoxesGenres[4] = (CheckBox) rootview.findViewById(R.id.check_box_documentary);
        mCheckBoxesGenres[5] = (CheckBox) rootview.findViewById(R.id.check_box_drama);
        mCheckBoxesGenres[6] = (CheckBox) rootview.findViewById(R.id.check_box_family);
        mCheckBoxesGenres[7] = (CheckBox) rootview.findViewById(R.id.check_box_kids);
        mCheckBoxesGenres[8] = (CheckBox) rootview.findViewById(R.id.check_box_mystery);
        mCheckBoxesGenres[9] = (CheckBox) rootview.findViewById(R.id.check_box_news);
        mCheckBoxesGenres[10] = (CheckBox) rootview.findViewById(R.id.check_box_reality);
        mCheckBoxesGenres[11] = (CheckBox) rootview.findViewById(R.id.check_box_sci_fi_fantasy);
        mCheckBoxesGenres[12] = (CheckBox) rootview.findViewById(R.id.check_box_soap);
        mCheckBoxesGenres[13] = (CheckBox) rootview.findViewById(R.id.check_box_talk);
        mCheckBoxesGenres[14] = (CheckBox) rootview.findViewById(R.id.check_box_war_politics);
        mCheckBoxesGenres[15] = (CheckBox) rootview.findViewById(R.id.check_box_western);

        for (int i=0;i<mCheckBoxesGenres.length;i++){
            mCheckBoxesGenres[i].setChecked(mGenresSelected[i]);
        }
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int columns = (int) (dpWidth / 100);
        columns = columns>=4 ? 4 : 2;
        mGridGenres.setColumnCount(columns);
    }

    //set the buttons at the bottom of the dialog
    private void setButtons(){
        Button selectGenres = (Button) rootview.findViewById(R.id.btn_select_genres);
        selectGenres.setOnClickListener(this);

        if(mInclude) selectGenres.setText(getString(R.string.include_genres));
        else selectGenres.setText(getString(R.string.exclude_genres));

        Button cancel = (Button) rootview.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_select_genres:
                // if the check boxes are checked
                for(int i=0;i<mCheckBoxesGenres.length;i++){
                    mGenresSelected[i] = mCheckBoxesGenres[i].isChecked();
                }
                if(mInclude)
                    mDiscoverActivity.setIncludeGenres(mGenresSelected);
                else
                    mDiscoverActivity.setExcludeGenres(mGenresSelected);

                dismiss();
                break;
            case  R.id.btn_cancel:
                dismiss();
                break;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

}
