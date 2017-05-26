package com.example.android.tvshows.ui.actor;


import android.content.Context;

public interface ActorContract {

    interface View{
        void setImage(String url);
        void setName(String name);
        void setBiography(String biography);
        void displayCredits(int size);
    }

    interface Presenter{
        void downloadActorData();
        void goToImdbPage(Context context);
        String getCharacterName(int position);
        String getTVShowTitle(int position);
    }
}
