package com.example.android.tvshows.ui.showinfo.seasons;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.tvshows.util.Utility;

class SeasonInfo implements Parcelable {
    String seasonName,airDate,posterUrl,overview,numberOfEpisodes;
    int seasonNumber;

    public SeasonInfo(String seasonName,Integer day ,Integer month,Integer year, String posterUrl, String overview, Integer numberOfEpisodes, int seasonNumber) {
        this.seasonName = seasonName;
        this.airDate = Utility.getDateAsString(day,month,year);
        this.posterUrl = posterUrl;
        this.overview = overview;
        this.numberOfEpisodes = numberOfEpisodes.toString();
        this.seasonNumber = seasonNumber;
    }

    protected SeasonInfo(Parcel in) {
        seasonNumber = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(seasonNumber);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SeasonInfo> CREATOR = new Parcelable.Creator<SeasonInfo>() {
        @Override
        public SeasonInfo createFromParcel(Parcel in) {
            return new SeasonInfo(in);
        }

        @Override
        public SeasonInfo[] newArray(int size) {
            return new SeasonInfo[size];
        }
    };

}
