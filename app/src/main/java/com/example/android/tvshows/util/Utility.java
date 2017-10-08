package com.example.android.tvshows.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.example.android.tvshows.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Utility {

    public static ArrayList<String> getYearsArray(){
        SimpleDateFormat dayFormat = new SimpleDateFormat("y");
        long time = System.currentTimeMillis();
        Integer year = Integer.valueOf(dayFormat.format(time));

        ArrayList<String> arrayYears = new ArrayList<>();
        arrayYears.add("");
        do {
            arrayYears.add(year.toString());
            year--;
        } while(year>1970);

        return arrayYears;
    }

    public static Integer getDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("d");
        long time = System.currentTimeMillis();
        return Integer.valueOf(sdf.format(time));
    }

    public static Integer getMonth(){
        SimpleDateFormat sdf = new SimpleDateFormat("M");
        long time = System.currentTimeMillis();
        Integer month = Integer.valueOf(sdf.format(time));

        return month;
    }

    public static Integer getYear(){
        SimpleDateFormat sdf = new SimpleDateFormat("y");
        long time = System.currentTimeMillis();
        return Integer.valueOf(sdf.format(time));
    }

    public static int getRatingBackgroundColor(Context context,Double rating){
        int color;
        if(rating==0)
            color = R.color.colorBlack;
        else if(rating>=8)
            color = R.color.colorUserScoreExcellent;
        else if(rating>=7)
            color = R.color.colorUserScoreVeryGood;
        else if (rating>=6)
            color = R.color.colorUserScoreGood;
        else if (rating>=5)
            color = R.color.colorUserScoreAverage;
        else if (rating>=4)
            color = R.color.colorUserScoreBad;
        else if (rating>=3)
            color = R.color.colorUserScoreVeryBad;
        else
            color = R.color.colorUserScoreDreadful;

        return ContextCompat.getColor(context,color);
    }

    public static int getTextColor(Context context,Double rating){
        int color;
        if(rating >=3)
            color = R.color.colorBlack;
        else
            color = R.color.colorWhite;

        return ContextCompat.getColor(context,color);
    }

    public static String getDateAsString(Integer day,Integer month,Integer year){

        if(day == -1 || month == -1 || year == -1)
            return "           ";
        String m;
        if(month==1)
            m = "JAN";
        else if(month==2)
            m = "FEB";
        else if(month==3)
            m = "MAR";
        else if(month==4)
            m = "APR";
        else if(month==5)
            m = "MAY";
        else if(month==6)
            m = "JUN";
        else if(month==7)
            m = "JUL";
        else if(month==8)
            m = "AUG";
        else if(month==9)
            m = "SEP";
        else if(month==10)
            m = "OCT";
        else if(month==11)
            m = "NOV";
        else
            m = "DEC";
        String date = day.toString()+" "+m+" "+year.toString();
        if (day<10) date += " ";
        return date;
    }

}
