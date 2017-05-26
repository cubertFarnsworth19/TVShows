package com.example.android.tvshows.ui.episodes;


import android.os.Parcel;
import android.os.Parcelable;

public class EpisodeData implements Parcelable {

    public static final String episodeData = "episode data";

    private int tmdbId;
    private String episodeName;
    private String overview;
    private String stillPhotoUrl;
    private String originalAirDate;
    private int episodeNumber;

    public EpisodeData(int tmdbId, String episodeName, String overview, String stillPhotoUrl, String originalAirDate, int position) {
        this.tmdbId = tmdbId;
        this.episodeName = episodeName;
        this.overview = overview;
        this.originalAirDate = originalAirDate;
        this.stillPhotoUrl = stillPhotoUrl;
        this.episodeNumber = position+1;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public String getStillPhotoUrl() {
        return stillPhotoUrl;
    }

    public String getOriginalAirDate() {
        return originalAirDate;
    }

    public String getOverview() {
        return overview;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    protected EpisodeData(Parcel in) {
        episodeName = in.readString();
        overview = in.readString();
        stillPhotoUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(episodeName);
        dest.writeString(overview);
        dest.writeString(stillPhotoUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<EpisodeData> CREATOR = new Parcelable.Creator<EpisodeData>() {
        @Override
        public EpisodeData createFromParcel(Parcel in) {
            return new EpisodeData(in);
        }

        @Override
        public EpisodeData[] newArray(int size) {
            return new EpisodeData[size];
        }
    };
}