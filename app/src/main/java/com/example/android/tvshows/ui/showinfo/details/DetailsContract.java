package com.example.android.tvshows.ui.showinfo.details;

import android.content.Context;

public interface DetailsContract {

    interface View{

        void setUserInterfaceText(String overview,String startYear,
                                  String userScore,String voteCount,String genres,
                                  int userScoreBackgroundColor,int userScoreTextColor);

        void setPoster(String url);
        void creatorDataLoaded(int size);
        void setIMDBid(String id);
    }

    interface Presenter{
        void loadShowDetails(Context context);
        String getCreatorName(int position);
        String getTitle();
        boolean downloadExternalIds();
        String getImdbId();
    }
}
