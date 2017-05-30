package com.example.android.tvshows.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExternalIds implements Parcelable{

    @SerializedName("facebook_id")
    @Expose
    private Object facebookId;
    @SerializedName("freebase_mid")
    @Expose
    private String freebaseMid;
    @SerializedName("freebase_id")
    @Expose
    private String freebaseId;
    @SerializedName("imdb_id")
    @Expose
    private String imdbId;
    @SerializedName("instagram_id")
    @Expose
    private Object instagramId;
    @SerializedName("tvrage_id")
    @Expose
    private Integer tvrageId;
    @SerializedName("twitter_id")
    @Expose
    private Object twitterId;
    @SerializedName("id")
    @Expose
    private Integer id;

    public Object getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(Object facebookId) {
        this.facebookId = facebookId;
    }

    public String getFreebaseMid() {
        return freebaseMid;
    }

    public void setFreebaseMid(String freebaseMid) {
        this.freebaseMid = freebaseMid;
    }

    public String getFreebaseId() {
        return freebaseId;
    }

    public void setFreebaseId(String freebaseId) {
        this.freebaseId = freebaseId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public Object getInstagramId() {
        return instagramId;
    }

    public void setInstagramId(Object instagramId) {
        this.instagramId = instagramId;
    }

    public Integer getTvrageId() {
        return tvrageId;
    }

    public void setTvrageId(Integer tvrageId) {
        this.tvrageId = tvrageId;
    }

    public Object getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(Object twitterId) {
        this.twitterId = twitterId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    protected ExternalIds(Parcel in) {
        facebookId = (Object) in.readValue(Object.class.getClassLoader());
        freebaseMid = in.readString();
        freebaseId = in.readString();
        imdbId = in.readString();
        instagramId = (Object) in.readValue(Object.class.getClassLoader());
        tvrageId = in.readByte() == 0x00 ? null : in.readInt();
        twitterId = (Object) in.readValue(Object.class.getClassLoader());
        id = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(facebookId);
        dest.writeString(freebaseMid);
        dest.writeString(freebaseId);
        dest.writeString(imdbId);
        dest.writeValue(instagramId);
        if (tvrageId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(tvrageId);
        }
        dest.writeValue(twitterId);
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ExternalIds> CREATOR = new Parcelable.Creator<ExternalIds>() {
        @Override
        public ExternalIds createFromParcel(Parcel in) {
            return new ExternalIds(in);
        }

        @Override
        public ExternalIds[] newArray(int size) {
            return new ExternalIds[size];
        }
    };
}
