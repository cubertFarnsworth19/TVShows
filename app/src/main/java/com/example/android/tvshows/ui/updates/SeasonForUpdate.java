package com.example.android.tvshows.ui.updates;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.tvshows.util.Utility;

public class SeasonForUpdate implements Parcelable {

    public int showId;
    public String name;
    public int seasonNumber;
    public int updateDay;
    public int updateMonth;
    public int updateYear;
    public String lastUpdate;

    public SeasonForUpdate(int showId,String name, int seasonNumber, int updateDay, int updateMonth, int updateYear) {
        this.showId = showId;
        this.name = name;
        this.seasonNumber = seasonNumber;
        this.updateDay = updateDay;
        this.updateMonth = updateMonth;
        this.updateYear = updateYear;
        lastUpdate = Utility.getDateAsString(updateDay,updateMonth,updateYear);
    }

    public boolean earlierUpdate(int d,int m,int y){
        if(updateYear<y)
            return true;
        if(updateYear==y && updateMonth<m)
            return true;
        if(updateYear==y && updateMonth==m && updateDay<d)
            return true;
        return false;
    }

    public boolean earlierUpdate(SeasonForUpdate season){
        return earlierUpdate(season.updateDay,season.updateMonth, season.updateYear);
    }


    protected SeasonForUpdate(Parcel in) {
        name = in.readString();
        seasonNumber = in.readInt();
        updateDay = in.readInt();
        updateMonth = in.readInt();
        updateYear = in.readInt();
        lastUpdate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(seasonNumber);
        dest.writeInt(updateDay);
        dest.writeInt(updateMonth);
        dest.writeInt(updateYear);
        dest.writeString(lastUpdate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SeasonForUpdate> CREATOR = new Parcelable.Creator<SeasonForUpdate>() {
        @Override
        public SeasonForUpdate createFromParcel(Parcel in) {
            return new SeasonForUpdate(in);
        }

        @Override
        public SeasonForUpdate[] newArray(int size) {
            return new SeasonForUpdate[size];
        }
    };
}

