
package com.example.android.tvshows.data.model.actortvcredits;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActorTVCredits implements Parcelable{

    @SerializedName("cast")
    @Expose
    private List<Cast> cast = null;
    @SerializedName("crew")
    @Expose
    private List<Crew> crew = null;
    @SerializedName("id")
    @Expose
    private Integer id;

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    protected ActorTVCredits(Parcel in) {
        if (in.readByte() == 0x01) {
            cast = new ArrayList<Cast>();
            in.readList(cast, Cast.class.getClassLoader());
        } else {
            cast = null;
        }
        id = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (cast == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(cast);
        }
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ActorTVCredits> CREATOR = new Parcelable.Creator<ActorTVCredits>() {
        @Override
        public ActorTVCredits createFromParcel(Parcel in) {
            return new ActorTVCredits(in);
        }

        @Override
        public ActorTVCredits[] newArray(int size) {
            return new ActorTVCredits[size];
        }
    };
}
