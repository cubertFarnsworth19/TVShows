package com.example.android.tvshows.ui.myshows.shows;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.tvshows.R;

class ShowInfo implements Parcelable{
    private int id;
    private String title,posterUrl,numberOfSeasons,numberOfEpisodes,inProduction;

    public ShowInfo(Context context, int id, String title, String posterPath, Integer numberOfSeasons, Integer numberOfEpisodes, int inProduction) {
        this.id = id;
        this.title = title;
        this.posterUrl = context.getString(R.string.poster_path) + posterPath;
        this.numberOfSeasons = numberOfSeasons.toString();
        this.numberOfEpisodes = numberOfEpisodes.toString();
        this.inProduction = inProduction==1 ? context.getString(R.string.continuing) : context.getString(R.string.finished);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public String getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public String getInProduction() {
        return inProduction;
    }

    protected ShowInfo(Parcel in) {
        id = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ShowInfo> CREATOR = new Parcelable.Creator<ShowInfo>() {
        @Override
        public ShowInfo createFromParcel(Parcel in) {
            return new ShowInfo(in);
        }

        @Override
        public ShowInfo[] newArray(int size) {
            return new ShowInfo[size];
        }
    };
}
