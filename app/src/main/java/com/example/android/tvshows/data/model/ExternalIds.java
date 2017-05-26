package com.example.android.tvshows.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExternalIds {

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

}
