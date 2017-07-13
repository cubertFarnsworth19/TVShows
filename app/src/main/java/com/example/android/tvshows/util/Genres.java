package com.example.android.tvshows.util;

import android.content.Context;
import android.support.annotation.NonNull;


import com.example.android.tvshows.R;


public class Genres {

    public static int getGenreInteger(String string, Context context){

        if(string.equals(context.getString(R.string.genre_action_adventure))) return 10759;
        else if(string.equals(context.getString(R.string.genre_animation))) return 16;
        else if(string.equals(context.getString(R.string.genre_comedy))) return 35;
        else if(string.equals(context.getString(R.string.genre_crime))) return 80;
        else if(string.equals(context.getString(R.string.genre_documentary))) return 99;
        else if(string.equals(context.getString(R.string.genre_drama))) return 18;
        else if(string.equals(context.getString(R.string.genre_family))) return 10751;
        else if(string.equals(context.getString(R.string.genre_kids))) return 10762;
        else if(string.equals(context.getString(R.string.genre_mystery))) return 9648;
        else if(string.equals(context.getString(R.string.genre_news))) return 10763;
        else if(string.equals(context.getString(R.string.genre_reality))) return 10764;
        else if(string.equals(context.getString(R.string.genre_sci_fi_fantasy))) return 10765;
        else if(string.equals(context.getString(R.string.genre_soap))) return 10766;
        else if(string.equals(context.getString(R.string.genre_talk))) return 10767;
        else if(string.equals(context.getString(R.string.genre_war_politics))) return 10768;
        else return 37;

    }

    public static int getGenreInteger(int index){
        if(index==0) return 10759;
        else if(index==1) return 16;
        else if(index==2) return 35;
        else if(index==3) return 80;
        else if(index==4) return 99;
        else if(index==5) return 18;
        else if(index==6) return 10751;
        else if(index==7) return 10762;
        else if(index==8) return 9648;
        else if(index==9) return 10763;
        else if(index==10) return 10764;
        else if(index==11) return 10765;
        else if(index==12) return 10766;
        else if(index==13) return 10767;
        else if(index==14) return 10768;
        else return 37;
    }

    @NonNull
    public static String getGenreString(int genre, Context context){
        if(genre == 10759)return context.getString(R.string.genre_action_adventure);
        else if(genre ==  16) return context.getString(R.string.genre_animation);
        else if(genre ==  35) return context.getString(R.string.genre_comedy);
        else if(genre ==  80) return context.getString(R.string.genre_crime);
        else if(genre == 99) return context.getString(R.string.genre_documentary);
        else if(genre ==  18) return context.getString(R.string.genre_drama);
        else if(genre ==  10751) return context.getString(R.string.genre_family);
        else if(genre ==  10762) return context.getString(R.string.genre_kids);
        else if(genre ==  9648) return context.getString(R.string.genre_mystery);
        else if(genre ==  10763) return context.getString(R.string.genre_news);
        else if(genre ==  10764) return context.getString(R.string.genre_reality);
        else if(genre ==  10765) return context.getString(R.string.genre_sci_fi_fantasy);
        else if(genre ==  10766) return context.getString(R.string.genre_soap);
        else if(genre ==  10767) return context.getString(R.string.genre_talk);
        else if(genre == 10768) return context.getString(R.string.genre_war_politics);
        else return context.getString(R.string.genre_western);
    }

    @NonNull
    public static String getGenreStringFromIndex(int index, Context context){
        return getGenreString(getGenreInteger(index),context);
    }

    public static int numberOfGenres(){
        return 16;
    }

    @NonNull
    public static String getGenresString(boolean[] selectedGenres, Context context){
        StringBuilder stringSelected = new StringBuilder();
        boolean firstSelected = false;
        for(int i=0;i<selectedGenres.length;i++){
            if(selectedGenres[i]){
                if(firstSelected)
                    stringSelected.append(", "+Genres.getGenreStringFromIndex(i,context));
                else {
                    stringSelected.append(Genres.getGenreStringFromIndex(i,context));
                    firstSelected = true;
                }
            }
        }
        return stringSelected.toString();
    }

    // convert the boolean selected genres into a comma separated list of integer ids
    public static String genresToStringInts(boolean[] selectedGenres){
        StringBuilder stringSelected = new StringBuilder();
        boolean firstSelected = false;
        for(int i=0;i<selectedGenres.length;i++){
            if(selectedGenres[i]){
                if(firstSelected)
                    stringSelected.append(","+ getGenreInteger(i));
                else {
                    stringSelected.append(getGenreInteger(i));
                    firstSelected = true;
                }
            }
        }
        return stringSelected.toString();
    }

}
