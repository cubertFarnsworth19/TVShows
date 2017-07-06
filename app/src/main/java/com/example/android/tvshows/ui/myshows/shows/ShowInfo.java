package com.example.android.tvshows.ui.myshows.shows;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.tvshows.R;

public class ShowInfo implements Parcelable{
    private int id;
    private String title,posterUrl,numberOfSeasons,numberOfEpisodes,inProduction;
    boolean favorite;

    public ShowInfo(Context context, int id, String title, String posterPath, Integer numberOfSeasons, Integer numberOfEpisodes, int inProduction,int favorite) {
        this.id = id;
        this.title = title;
        this.posterUrl = context.getString(R.string.poster_path) + posterPath;
        this.numberOfSeasons = numberOfSeasons.toString();
        this.numberOfEpisodes = numberOfEpisodes.toString();
        this.inProduction = inProduction==1 ? context.getString(R.string.continuing) : context.getString(R.string.finished);
        this.favorite = favorite==1;
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    protected ShowInfo(Parcel in) {
        id = in.readInt();
        title = in.readString();
        posterUrl = in.readString();
        numberOfSeasons = in.readString();
        numberOfEpisodes = in.readString();
        inProduction = in.readString();
        favorite = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(posterUrl);
        dest.writeString(numberOfSeasons);
        dest.writeString(numberOfEpisodes);
        dest.writeString(inProduction);
        dest.writeByte((byte) (favorite ? 0x01 : 0x00));
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
