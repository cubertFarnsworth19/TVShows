package com.example.android.tvshows.ui.showinfo.cast;

import android.content.Context;
import android.os.Parcelable;

import java.util.ArrayList;

public interface CastContract {

    interface View{
        void castDataLoaded(int size);
    }

    interface Presenter{
        void loadCastData(final Context context);
        String getCharacterName(int position);
        String getActorName(int position);
        String getPhotoUrl(Context context, int position);
        void startActorActivity(Context context,int position);
        ArrayList<CastInfo> getCastInfo();
    }
}
