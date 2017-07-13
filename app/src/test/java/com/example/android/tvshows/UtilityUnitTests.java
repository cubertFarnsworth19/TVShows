package com.example.android.tvshows;

import android.content.Context;
import android.os.Parcelable;
import android.test.AndroidTestCase;

import com.example.android.tvshows.data.model.tvshowdetailed.Genre;
import com.example.android.tvshows.util.Genres;
import com.example.android.tvshows.util.Utility;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class UtilityUnitTests{

    @Mock
    Context mContext;

    @Test
    public void utility_getDateAsString(){
        String date = "10 JAN 2017";
        assertTrue(date.equals(Utility.getDateAsString(10,1,2017)));
        date = "6 OCT 2015 ";
        assertTrue(date.equals(Utility.getDateAsString(6,10,2015)));
    }

    @Test
    public void genres_getGenresAsString(){

        MockitoAnnotations.initMocks(this);
        when(mContext.getString(R.string.genre_documentary)).thenReturn("Documentary");
        when(mContext.getString(R.string.genre_drama)).thenReturn("Drama");

        String genres = "Documentary, Drama";
        boolean[] selectedGenres = new boolean[16];
        selectedGenres[4] = true;
        selectedGenres[5] = true;
        assertTrue(genres.equals(Genres.getGenresString(selectedGenres,mContext)));
    }

    @Test
    public void genres_genresToStringInts(){

        String genres = "18,10766";

        boolean[] selected = new boolean[16];
        selected[5] = true;
        selected[12] = true;

        assertTrue(genres.equals(Genres.genresToStringInts(selected)));
    }

}
