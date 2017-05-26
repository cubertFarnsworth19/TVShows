package com.example.android.tvshows.ui.showinfo.cast;

import android.content.Context;

public interface CastContract {

    interface View{
        void castDataLoaded(int size);
    }

    interface Presenter{
        void loadCastData();
        String getCharacterName(int position);
        String getActorName(int position);
        String getPhotoUrl(Context context, int position);
        void startActorActivity(Context context,int position);
        void closeCursor();
    }
}
