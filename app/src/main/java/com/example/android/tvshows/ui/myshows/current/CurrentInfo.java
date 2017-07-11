package com.example.android.tvshows.ui.myshows.current;


import android.os.Parcel;
import android.os.Parcelable;

public class CurrentInfo implements Parcelable{

    private String showName,episodeName,overview,posterUrl;

    public CurrentInfo(String showName, String episodeName, String overview, String posterUrl) {
        this.showName = showName;
        this.episodeName = episodeName;
        this.overview = overview;
        this.posterUrl = posterUrl;
    }

    public String getShowName() {
        return showName;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    @Override
    public boolean equals(Object obj) {
        CurrentInfo currentInfo = (CurrentInfo) obj;
        return this.showName.equals(currentInfo.getShowName()) && this.episodeName.equals(currentInfo.getEpisodeName())
                && this.overview.equals(currentInfo.getOverview()) && this.posterUrl.equals(currentInfo.getPosterUrl());
    }

    protected CurrentInfo(Parcel in) {
        showName = in.readString();
        episodeName = in.readString();
        overview = in.readString();
        posterUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(showName);
        dest.writeString(episodeName);
        dest.writeString(overview);
        dest.writeString(posterUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CurrentInfo> CREATOR = new Parcelable.Creator<CurrentInfo>() {
        @Override
        public CurrentInfo createFromParcel(Parcel in) {
            return new CurrentInfo(in);
        }

        @Override
        public CurrentInfo[] newArray(int size) {
            return new CurrentInfo[size];
        }
    };
}
