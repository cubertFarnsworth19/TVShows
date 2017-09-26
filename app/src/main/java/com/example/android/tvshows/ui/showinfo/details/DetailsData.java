package com.example.android.tvshows.ui.showinfo.details;

import android.database.Cursor;

import com.example.android.tvshows.data.db.ShowsDbContract;


public class DetailsData {

    public String name;
    public String overview;
    public Integer startYear;
    public Double userScore;
    public Integer voteCount;
    public String posterPath;
    public boolean actionAdventure, animation, comedy, crime, documentary, drama, family, kids, mystery, news, reality, sciFiFantasy, soap, talk, warPolitics, western;

    public DetailsData(){}

    public DetailsData(String name, String overview, Integer startYear, Double userScore, Integer voteCount, String posterPath) {
        this.name = name;
        this.overview = overview;
        this.startYear = startYear;
        this.userScore = userScore;
        this.voteCount = voteCount;
        this.posterPath = posterPath;
    }

    public void setGenres(Cursor showDetails) {
        if (showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_ACTION_ADVENTURE)) == 1)
            actionAdventure = true;
        if (showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_ANIMATION)) == 1)
           animation = true;
        if (showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_COMEDY)) == 1)
            comedy = true;
        if (showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_CRIME)) == 1)
            crime = true;
        if (showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_DOCUMENTARY)) == 1)
            documentary = true;
        if (showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_DRAMA)) == 1)
            drama = true;
        if (showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_FAMILY)) == 1)
            family = true;
        if (showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_KIDS)) == 1)
            kids = true;
        if (showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_MYSTERY)) == 1)
            mystery = true;
        if (showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_NEWS)) == 1)
            news = true;
        if (showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_REALITY)) == 1)
            reality = true;
        if (showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_SCI_FI_FANTASY)) == 1)
            sciFiFantasy = true;
        if (showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_SOAP)) == 1)
            soap = true;
        if (showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_TALK)) == 1)
            talk = true;
        if (showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_WAR_POLITICS)) == 1)
            warPolitics = true;
        if (showDetails.getInt(showDetails.getColumnIndex(ShowsDbContract.ShowsEntry.COLUMN_GENRE_WESTERN)) == 1)
            western = true;
    }

}
