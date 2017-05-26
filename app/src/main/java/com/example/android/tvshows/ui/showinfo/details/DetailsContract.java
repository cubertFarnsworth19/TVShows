package com.example.android.tvshows.ui.showinfo.details;

import android.content.Context;

public interface DetailsContract {

    interface View{

        void setUserInterfaceText(String overview,String startYear,
                                  String userScore,String voteCount,String genres,
                                  int userScoreBackgroundColor,int userScoreTextColor);

        void setPoster(String url);
        void creatorDataLoaded(int size);
    }

    interface Presenter{
        void loadShowDetails(Context context);
        String getCreatorName(int position);
        void visitIMDbPage(Context context);
        void visitTMDBPage(Context context);
        void searchGoogle(Context context);
        void searchYouTube(Context context);
        void visitWikipedia(Context context);
    }
}
