package com.example.android.tvshows.ui.myshows.current;


public class CurrentDatabaseLoad {

    public String showName,episodeName,overview,posterPath;
    public int airDateDay,airDateMonth,airDateYear;

    public CurrentDatabaseLoad(String showName, String episodeName, String overview, String posterPath, int airDateDay, int airDateMonth, int airDateYear) {
        this.showName = showName;
        this.episodeName = episodeName;
        this.overview = overview;
        this.posterPath = posterPath;
        this.airDateDay = airDateDay;
        this.airDateMonth = airDateMonth;
        this.airDateYear = airDateYear;
    }
}
