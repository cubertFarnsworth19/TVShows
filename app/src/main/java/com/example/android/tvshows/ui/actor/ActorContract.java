package com.example.android.tvshows.ui.actor;


import android.content.Context;

import com.example.android.tvshows.data.model.Actor;
import com.example.android.tvshows.data.model.ExternalIds;
import com.example.android.tvshows.data.model.actortvcredits.ActorTVCredits;

public interface ActorContract {

    interface View{
        void setImage(String url);
        void setName(String name);
        void setBiography(String biography);
        void displayCredits(int size);
        void noConnection();
        void startingDownload();
    }

    interface Presenter{
        void downloadActorData(Context context);
        void setActorData();
        ActorTVCredits getActorTVCredits();
        ExternalIds getExternalIds();
        Actor getActor();
        String getActorIMDBId();
        String getCharacterName(int position);
        String getTVShowTitle(int position);
    }
}
