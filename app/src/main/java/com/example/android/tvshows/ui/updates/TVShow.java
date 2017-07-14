package com.example.android.tvshows.ui.updates;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.tvshows.util.Utility;

public class TVShow implements Parcelable {

    public Integer id;
    public String name;
    public int updateDay;
    public int updateMonth;
    public int updateYear;
    public String lastUpdate;

    public TVShow(Integer id, String name, int updateDay, int updateMonth, int updateYear) {
        this.id = id;
        this.name = name;
        this.updateDay = updateDay;
        this.updateMonth = updateMonth;
        this.updateYear = updateYear;
        lastUpdate = Utility.getDateAsString(updateDay,updateMonth,updateYear);
    }


    protected TVShow(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        name = in.readString();
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
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeInt(updateDay);
        dest.writeInt(updateMonth);
        dest.writeInt(updateYear);
        dest.writeString(lastUpdate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TVShow> CREATOR = new Parcelable.Creator<TVShow>() {
        @Override
        public TVShow createFromParcel(Parcel in) {
            return new TVShow(in);
        }

        @Override
        public TVShow[] newArray(int size) {
            return new TVShow[size];
        }
    };
}



