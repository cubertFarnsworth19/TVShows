package com.example.android.tvshows.ui.find;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.tvshows.data.model.search.Result;

import java.util.ArrayList;
import java.util.List;

public class SaveResultsPresenterState implements Parcelable{

    private List<Result> mResults;
    private int mPage;
    private int mTotalPages;
    private int mTotalResults;
    private String mLastSortBy;
    private String mLastWithGenres;
    private String mLastWithoutGenres;
    private String mLastMinVoteAverage;
    private String mLastMinVoteCount;
    private String mLastFirstAirDateAfter;
    private String mLastFirstAirDateBefore;
    private ArrayList<Integer> mAllShowIds;

    public SaveResultsPresenterState(List<Result> results, int page, int totalPages, int totalResults,
                                     String lastSortBy, String lastWithGenres, String lastWithoutGenres,
                                     String lastMinVoteAverage, String lastMinVoteCount, String lastFirstAirDateAfter,
                                     String lastFirstAirDateBefore, ArrayList<Integer> allShowIds) {
        mResults = results;
        mPage = page;
        mTotalPages = totalPages;
        mTotalResults = totalResults;
        mLastSortBy = lastSortBy;
        mLastWithGenres = lastWithGenres;
        mLastWithoutGenres = lastWithoutGenres;
        mLastMinVoteAverage = lastMinVoteAverage;
        mLastMinVoteCount = lastMinVoteCount;
        mLastFirstAirDateAfter = lastFirstAirDateAfter;
        mLastFirstAirDateBefore = lastFirstAirDateBefore;
        mAllShowIds = allShowIds;
    }

    public List<Result> getResults() {
        return mResults;
    }

    public int getPage() {
        return mPage;
    }

    public int getTotalPages() {
        return mTotalPages;
    }

    public int getTotalResults() {
        return mTotalResults;
    }

    public String getLastSortBy() {
        return mLastSortBy;
    }

    public String getLastWithGenres() {
        return mLastWithGenres;
    }

    public String getLastWithoutGenres() {
        return mLastWithoutGenres;
    }

    public String getLastMinVoteAverage() {
        return mLastMinVoteAverage;
    }

    public String getLastMinVoteCount() {
        return mLastMinVoteCount;
    }

    public String getLastFirstAirDateAfter() {
        return mLastFirstAirDateAfter;
    }

    public String getLastFirstAirDateBefore() {
        return mLastFirstAirDateBefore;
    }

    public ArrayList<Integer> getAllShowIds() {
        return mAllShowIds;
    }


    protected SaveResultsPresenterState(Parcel in) {
        if (in.readByte() == 0x01) {
            mResults = new ArrayList<Result>();
            in.readList(mResults, Result.class.getClassLoader());
        } else {
            mResults = null;
        }
        mPage = in.readInt();
        mTotalPages = in.readInt();
        mTotalResults = in.readInt();
        mLastSortBy = in.readString();
        mLastWithGenres = in.readString();
        mLastWithoutGenres = in.readString();
        mLastMinVoteAverage = in.readString();
        mLastMinVoteCount = in.readString();
        mLastFirstAirDateAfter = in.readString();
        mLastFirstAirDateBefore = in.readString();
        if (in.readByte() == 0x01) {
            mAllShowIds = new ArrayList<Integer>();
            in.readList(mAllShowIds, Integer.class.getClassLoader());
        } else {
            mAllShowIds = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mResults == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mResults);
        }
        dest.writeInt(mPage);
        dest.writeInt(mTotalPages);
        dest.writeInt(mTotalResults);
        dest.writeString(mLastSortBy);
        dest.writeString(mLastWithGenres);
        dest.writeString(mLastWithoutGenres);
        dest.writeString(mLastMinVoteAverage);
        dest.writeString(mLastMinVoteCount);
        dest.writeString(mLastFirstAirDateAfter);
        dest.writeString(mLastFirstAirDateBefore);
        if (mAllShowIds == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mAllShowIds);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SaveResultsPresenterState> CREATOR = new Parcelable.Creator<SaveResultsPresenterState>() {
        @Override
        public SaveResultsPresenterState createFromParcel(Parcel in) {
            return new SaveResultsPresenterState(in);
        }

        @Override
        public SaveResultsPresenterState[] newArray(int size) {
            return new SaveResultsPresenterState[size];
        }
    };
}
