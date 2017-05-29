package com.example.android.tvshows.ui.showinfo.cast;

import android.os.Parcel;
import android.os.Parcelable;

class CastInfo implements Parcelable{
    private String characterName, actorName, photoUrl;
    private int personId;

    public CastInfo(String characterName, String actorName, String photoUrl,int personId) {
        this.characterName = characterName;
        this.actorName = actorName;
        this.photoUrl = photoUrl;
        this.personId = personId;
    }

    public String getCharacterName() {
        return characterName;
    }

    public String getActorName() {
        return actorName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int getPersonId() {
        return personId;
    }

    protected CastInfo(Parcel in) {
        personId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(personId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CastInfo> CREATOR = new Parcelable.Creator<CastInfo>() {
        @Override
        public CastInfo createFromParcel(Parcel in) {
            return new CastInfo(in);
        }

        @Override
        public CastInfo[] newArray(int size) {
            return new CastInfo[size];
        }
    };

}