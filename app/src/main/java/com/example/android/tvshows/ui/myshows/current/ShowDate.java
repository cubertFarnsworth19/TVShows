package com.example.android.tvshows.ui.myshows.current;

import android.os.Parcel;
import android.os.Parcelable;

public class ShowDate implements Parcelable{
    String date;
    // the number of shows for this date
    int numberOfShows;
    // the number of shows prior to this day in the adapter
    int cumulativeNumberOfShows;

    public ShowDate(String date,int cumulativeNumberOfShows) {
        this.date = date;
        this.numberOfShows = 1;
        this.cumulativeNumberOfShows = cumulativeNumberOfShows;
    }

    public void addShow(){
        numberOfShows++;
    }

    public boolean sameDate(String date){
        return this.date.equals(date);
    }

    @Override
    public boolean equals(Object obj) {
        ShowDate showDate = (ShowDate) obj;

        return showDate.sameDate(this.date) && showDate.numberOfShows == this.numberOfShows && showDate.cumulativeNumberOfShows == this.cumulativeNumberOfShows;
    }

    protected ShowDate(Parcel in) {
        date = in.readString();
        numberOfShows = in.readInt();
        cumulativeNumberOfShows = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeInt(numberOfShows);
        dest.writeInt(cumulativeNumberOfShows);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ShowDate> CREATOR = new Parcelable.Creator<ShowDate>() {
        @Override
        public ShowDate createFromParcel(Parcel in) {
            return new ShowDate(in);
        }

        @Override
        public ShowDate[] newArray(int size) {
            return new ShowDate[size];
        }
    };
}