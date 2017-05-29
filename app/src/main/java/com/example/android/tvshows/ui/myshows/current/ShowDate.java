package com.example.android.tvshows.ui.myshows.current;

class ShowDate{
    String date;
    int numberOfShows;
    // the number of shows prior to this day in the adapter
    int cumulativeNumberOfShows;

    public ShowDate(String date,int cumulativeNumberOfShows) {
        this.date = date;
        this.numberOfShows = 1;
        this.cumulativeNumberOfShows = cumulativeNumberOfShows;
    }

    void addShow(){
        numberOfShows++;
    }

    boolean sameDate(String date){
        return this.date.equals(date);
    }
}