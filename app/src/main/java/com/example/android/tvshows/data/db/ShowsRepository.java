package com.example.android.tvshows.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.android.tvshows.data.model.credits.Credits;
import com.example.android.tvshows.data.model.season.Season;
import com.example.android.tvshows.data.model.tvshowdetailed.TVShowDetailed;
import com.example.android.tvshows.util.Utility;

import java.util.ArrayList;

public class ShowsRepository {

    private Context mContext;

    public ShowsRepository(Context context){
        mContext = context;
    }

    public void insertShowIntoDatabase(final TVShowDetailed tvShowDetailed, final Credits credits, final Season[] seasons){
        Log.v("ShowsRepository","insert");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Integer todayDay = Utility.getDay();
                Integer todayMonth = Utility.getMonth();
                Integer todayYear = Utility.getYear();

                ContentValues showsValues = new ContentValues();
                showsValues.put(ShowsDbContract.ShowsEntry._ID,tvShowDetailed.getId());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_NAME,tvShowDetailed.getName());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_POSTER_PATH,tvShowDetailed.getPosterPath());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_VOTE_AVERAGE,tvShowDetailed.getVoteAverage());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_VOTE_COUNT,tvShowDetailed.getVoteCount());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_POPULARITY,tvShowDetailed.getPopularity());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_OVERVIEW,tvShowDetailed.getOverview());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_NUM_EPISODES,tvShowDetailed.getNumberOfEpisodes());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_NUM_SEASONS,tvShowDetailed.getSeasonsListSize());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_HOMEPAGE,tvShowDetailed.getHomepage());
                if(tvShowDetailed.getInProduction()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_IN_PRODUCTION,1);
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_FIRST_AIR_DATE_YEAR,tvShowDetailed.getFirstAirDateYear());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_FIRST_AIR_DATE_MONTH,tvShowDetailed.getFirstAirDateMonth());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_FIRST_AIR_DATE_DAY,tvShowDetailed.getFirstAirDateDay());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_LAST_AIR_DATE_YEAR,tvShowDetailed.getLastAirDateYear());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_LAST_AIR_DATE_MONTH,tvShowDetailed.getLastAirDateMonth());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_LAST_AIR_DATE_DAY,tvShowDetailed.getLastAirDateDay());
                if(tvShowDetailed.isActionAdventure()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_ACTION_ADVENTURE,1);
                if(tvShowDetailed.isAnimation()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_ANIMATION,1);
                if(tvShowDetailed.isComedy()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_COMEDY,1);
                if(tvShowDetailed.isCrime()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_CRIME,1);
                if(tvShowDetailed.isDocumentary()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_DOCUMENTARY,1);
                if(tvShowDetailed.isDrama()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_DRAMA,1);
                if(tvShowDetailed.isFamily()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_FAMILY,1);
                if(tvShowDetailed.isKids()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_KIDS,1);
                if(tvShowDetailed.isMystery()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_MYSTERY,1);
                if(tvShowDetailed.isNews()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_NEWS,1);
                if(tvShowDetailed.isReality()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_REALITY,1);
                if(tvShowDetailed.isSciFiFantasy()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_SCI_FI_FANTASY,1);
                if(tvShowDetailed.isSoap()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_SOAP,1);
                if(tvShowDetailed.isTalk()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_TALK,1);
                if(tvShowDetailed.isWarPolitics()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_WAR_POLITICS,1);
                if(tvShowDetailed.isWestern()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_WESTERN,1);
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_LAST_UPDATE_DAY,todayDay);
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_LAST_UPDATE_MONTH,todayMonth);
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_LAST_UPDATE_YEAR,todayYear);
                Log.v("insertIntoDatabase",tvShowDetailed.getName()+","+credits.getId());


                int numEpisodes = 0;
                for(int i=0;i<seasons.length;i++){
                    numEpisodes += seasons[i].numberOfEpisodes();
                }
                ContentValues[] seasonsValues = new ContentValues[seasons.length];
                ContentValues[] episodesValues = new ContentValues[numEpisodes];

                int episodesIndex = 0;
                for(int i=0;i<seasons.length;i++){
                    seasonsValues[i] = new ContentValues();
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry._ID,seasons[i].getId());
                    seasonsValues[i].put(ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY,tvShowDetailed.getId());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NUMBER,seasons[i].getSeasonNumber());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_AIR_DATE_YEAR,seasons[i].getAirDateYear());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_AIR_DATE_MONTH,seasons[i].getAirDateMonth());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_AIR_DATE_DAY,seasons[i].getAirDateDay());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NAME,seasons[i].getName());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_OVERVIEW,seasons[i].getOverview());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_POSTER_PATH,seasons[i].getPosterPath());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_NUMBER_OF_EPISODES,seasons[i].getEpisodes().size());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_LAST_UPDATE_DAY,todayDay);
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_LAST_UPDATE_MONTH,todayMonth);
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_LAST_UPDATE_YEAR,todayYear);

                    for(int j=0;j<seasons[i].numberOfEpisodes();j++){
                        episodesValues[episodesIndex] = new ContentValues();
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry._ID,seasons[i].getEpisodes().get(j).getId());
                        episodesValues[episodesIndex].put(ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY,tvShowDetailed.getId());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_EPISODE_NAME,seasons[i].getEpisodes().get(j).getName());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_OVERVIEW,seasons[i].getEpisodes().get(j).getOverview());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_EPISODE_NUMBER,seasons[i].getEpisodes().get(j).getEpisodeNumber());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_SEASON_NUMBER,seasons[i].getEpisodes().get(j).getSeasonNumber());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_STILL_PATH,seasons[i].getEpisodes().get(j).getStillPath());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_VOTE_AVERAGE,seasons[i].getEpisodes().get(j).getVoteAverage());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_VOTE_COUNT,seasons[i].getEpisodes().get(j).getVoteCount());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_YEAR,seasons[i].getEpisodes().get(j).getAirDateYear());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_MONTH,seasons[i].getEpisodes().get(j).getAirDateMonth());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_DAY,seasons[i].getEpisodes().get(j).getAirDateDay());
                        episodesIndex++;
                    }

                }

                ContentValues[] castValues = new ContentValues[credits.getNumberOfCast()];
                for(int i=0;i<castValues.length;i++){
                    castValues[i] = new ContentValues();
                    castValues[i].put(ShowsDbContract.CastEntry.COLUMN_PERSON_ID,credits.getCast().get(i).getId());
                    castValues[i].put(ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY,tvShowDetailed.getId());
                    castValues[i].put(ShowsDbContract.CastEntry.COLUMN_CHARACTER,credits.getCast().get(i).getCharacter());
                    castValues[i].put(ShowsDbContract.CastEntry.COLUMN_NAME,credits.getCast().get(i).getName());
                    castValues[i].put(ShowsDbContract.CastEntry.COLUMN_PROFILE_PATH,credits.getCast().get(i).getProfilePath());
                    castValues[i].put(ShowsDbContract.CastEntry.COLUMN_ORDER,credits.getCast().get(i).getOrder());
                }

                ContentValues[] creatorValues = new ContentValues[tvShowDetailed.numberOfCreators()];
                ContentValues[] creatorShowValues = new ContentValues[tvShowDetailed.numberOfCreators()];

                for (int i=0;i<creatorValues.length;i++){
                    creatorValues[i] = new ContentValues();
                    creatorValues[i].put(ShowsDbContract.CreatorEntry._ID,tvShowDetailed.getCreatedBy().get(i).getId());
                    creatorValues[i].put(ShowsDbContract.CreatorEntry.COLUMN_CREATOR_NAME,tvShowDetailed.getCreatedBy().get(i).getName());
                    creatorValues[i].put(ShowsDbContract.CreatorEntry.COLUMN_PROFILE_PATH,tvShowDetailed.getCreatedBy().get(i).getProfilePath());

                    creatorShowValues[i] = new ContentValues();
                    creatorShowValues[i].put(ShowsDbContract.ForeignKeys.COLUMN_CREATOR_FOREIGN_KEY,tvShowDetailed.getCreatedBy().get(i).getId());
                    creatorShowValues[i].put(ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY,tvShowDetailed.getId());
                }

                completeShowInsert(showsValues,castValues,seasonsValues,episodesValues,creatorValues,creatorShowValues);

            }
        });

        thread.start();

    }

    private synchronized void completeShowInsert(ContentValues showsValues,ContentValues[] castValues,ContentValues[] seasonsValues,
                                    ContentValues[] episodesValues,ContentValues[] creatorValues,ContentValues[] creatorShowValues){
        mContext.getContentResolver().insert(ShowsDbContract.ShowsEntry.CONTENT_URI,showsValues);
        mContext.getContentResolver().bulkInsert(ShowsDbContract.CastEntry.CONTENT_URI,castValues);
        mContext.getContentResolver().bulkInsert(ShowsDbContract.SeasonEntry.CONTENT_URI,seasonsValues);
        mContext.getContentResolver().bulkInsert(ShowsDbContract.EpisodeEntry.CONTENT_URI,episodesValues);
        mContext.getContentResolver().bulkInsert(ShowsDbContract.CreatorEntry.CONTENT_URI,creatorValues);
        mContext.getContentResolver().bulkInsert(ShowsDbContract.CreatorShowEntry.CONTENT_URI,creatorShowValues);
    }

    public void insertAdditionalSeasonsIntoDatabase(final int tmdbId, final Season[] seasons){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Integer todayDay = Utility.getDay();
                Integer todayMonth = Utility.getMonth();
                Integer todayYear = Utility.getYear();

                int numEpisodes = 0;
                for(int i=0;i<seasons.length;i++){
                    numEpisodes += seasons[i].numberOfEpisodes();
                }
                ContentValues[] seasonsValues = new ContentValues[seasons.length];
                ContentValues[] episodesValues = new ContentValues[numEpisodes];

                int episodesIndex = 0;
                for(int i=0;i<seasons.length;i++){
                    seasonsValues[i] = new ContentValues();
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry._ID,seasons[i].getId());
                    seasonsValues[i].put(ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY,tmdbId);
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NUMBER,seasons[i].getSeasonNumber());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_AIR_DATE_YEAR,seasons[i].getAirDateYear());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_AIR_DATE_MONTH,seasons[i].getAirDateMonth());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_AIR_DATE_DAY,seasons[i].getAirDateDay());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NAME,seasons[i].getName());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_OVERVIEW,seasons[i].getOverview());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_POSTER_PATH,seasons[i].getPosterPath());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_NUMBER_OF_EPISODES,seasons[i].getEpisodes().size());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_LAST_UPDATE_DAY,todayDay);
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_LAST_UPDATE_MONTH,todayMonth);
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_LAST_UPDATE_YEAR,todayYear);

                    for(int j=0;j<seasons[i].numberOfEpisodes();j++){
                        episodesValues[episodesIndex] = new ContentValues();
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry._ID,seasons[i].getEpisodes().get(j).getId());
                        episodesValues[episodesIndex].put(ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY,tmdbId);
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_EPISODE_NAME,seasons[i].getEpisodes().get(j).getName());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_OVERVIEW,seasons[i].getEpisodes().get(j).getOverview());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_EPISODE_NUMBER,seasons[i].getEpisodes().get(j).getEpisodeNumber());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_SEASON_NUMBER,seasons[i].getEpisodes().get(j).getSeasonNumber());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_STILL_PATH,seasons[i].getEpisodes().get(j).getStillPath());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_VOTE_AVERAGE,seasons[i].getEpisodes().get(j).getVoteAverage());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_VOTE_COUNT,seasons[i].getEpisodes().get(j).getVoteCount());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_YEAR,seasons[i].getEpisodes().get(j).getAirDateYear());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_MONTH,seasons[i].getEpisodes().get(j).getAirDateMonth());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_DAY,seasons[i].getEpisodes().get(j).getAirDateDay());
                        episodesIndex++;
                    }

                }

                completeAdditionalSeasonsInsert(seasonsValues,episodesValues);

            }
        });

        thread.start();
    }

    private synchronized void completeAdditionalSeasonsInsert(ContentValues[] seasonsValues,ContentValues[] episodesValues){
        mContext.getContentResolver().bulkInsert(ShowsDbContract.SeasonEntry.CONTENT_URI,seasonsValues);
        mContext.getContentResolver().bulkInsert(ShowsDbContract.EpisodeEntry.CONTENT_URI,episodesValues);
    }

    public void deleteShow(final Integer tmdbId){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                //the creators need to be deleted first, as the creator_show table is required
                deleteCreators(tmdbId);
                completeDelete(tmdbId);

            }
        });
        thread.start();
    }

    private synchronized void deleteCreators(Integer tmdbId) {
        //first query the database;
        // the cursor returns a creator, creator_show inner join
        // returning all creators of the given show
        String[] selectionArgs = {tmdbId.toString()};
        Cursor creatorsCursor = mContext.getContentResolver().query(ShowsDbContract.CreatorEntry.CONTENT_URI, null, null, selectionArgs, null);
        // go through each creator and determine if they are a creator for another show
        // if not delete the creator from the creators table
        while (creatorsCursor.moveToNext()) {
            // i is the creator id , i.e. from the first column
            Integer i = creatorsCursor.getInt(0);
            selectionArgs[0] = i.toString();
            Cursor countCursor = mContext.getContentResolver().query(Uri.parse(ShowsDbContract.CreatorEntry.CONTENT_URI + "/count"), null, null, selectionArgs, null);
            countCursor.moveToFirst();
            if (countCursor.getInt(0) < 2) {
                String where = ShowsDbContract.CreatorEntry._ID + "=?";
                mContext.getContentResolver().delete(ShowsDbContract.CreatorEntry.CONTENT_URI, where, selectionArgs);
            }
        }
        creatorsCursor.close();
    }

    private synchronized void completeDelete(Integer tmdbId){
        //delete the show in the shows table for te given id
        String where = ShowsDbContract.ShowsEntry._ID + "=?";
        String[] selectionArgs = {tmdbId.toString()};
        mContext.getContentResolver().delete(ShowsDbContract.ShowsEntry.CONTENT_URI, where, selectionArgs);

        // delete all show details
        String fullWhere = ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY + "=?";
        mContext.getContentResolver().delete(Uri.parse(ShowsDbContract.ShowsEntry.CONTENT_URI + "/full_delete"), fullWhere, selectionArgs);
    }

    // update if has been selected/ deselected as favorite
    public void setFavorite(final Integer tmdbId,final boolean favorite){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int fav = favorite ? 1 : 0;
                ContentValues showsValues = new ContentValues();
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_FAVORITE, fav);
                String[] selectionArgs = {tmdbId.toString()};
                String where = ShowsDbContract.ShowsEntry._ID + " =?";
                mContext.getContentResolver().update(ShowsDbContract.ShowsEntry.CONTENT_URI, showsValues, where, selectionArgs);
            }
        });

        thread.start();

    }

    public Cursor getAllShows(boolean continuing,boolean favorites){
        if(!continuing && !favorites)
            return mContext.getContentResolver().query(ShowsDbContract.ShowsEntry.CONTENT_URI,null,null,null,null);

        if(continuing && favorites){
            String where = ShowsDbContract.ShowsEntry.COLUMN_IN_PRODUCTION + " =? AND " + ShowsDbContract.ShowsEntry.COLUMN_FAVORITE + " =?";
            String[] selectionArgs = {"1","1"};
            return mContext.getContentResolver().query(ShowsDbContract.ShowsEntry.CONTENT_URI,null,where,selectionArgs,null);
        }
        else{
            String[] selectionArgs = {"1"};
            String where;
            if(continuing)
                where = ShowsDbContract.ShowsEntry.COLUMN_IN_PRODUCTION + " =?";
            else
                where = ShowsDbContract.ShowsEntry.COLUMN_FAVORITE + " =?";
            return mContext.getContentResolver().query(ShowsDbContract.ShowsEntry.CONTENT_URI,null,where,selectionArgs,null);
        }
    }

    public ArrayList<Integer> getAllShowIds(){
        String[] columns = {ShowsDbContract.ShowsEntry._ID};
        Cursor cursor = mContext.getContentResolver().query(ShowsDbContract.ShowsEntry.CONTENT_URI,columns,null,null,null);
        ArrayList<Integer> allShowIds = new ArrayList<>(cursor.getColumnCount());
        while (cursor.moveToNext()){
            allShowIds.add(cursor.getInt(cursor.getColumnIndex(ShowsDbContract.ShowsEntry._ID)));
        }
        return allShowIds;
    }

    public Cursor getShow(Integer tmdbId){
        String where = ShowsDbContract.ShowsEntry._ID + "=?";
        String[] selectionArgs = {tmdbId.toString()};
        return mContext.getContentResolver().query(ShowsDbContract.ShowsEntry.CONTENT_URI,null,where,selectionArgs,null);
    }

    public Cursor getCast(Integer tmdbId){
        String where = ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY + "=?";
        String[] selectionArgs = {tmdbId.toString()};
        return mContext.getContentResolver().query(ShowsDbContract.CastEntry.CONTENT_URI,null,where,selectionArgs,null);
    }

    public Cursor getSeasons(Integer tmdbId){
        String where = ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY + "=?";
        String[] selectionArgs = {tmdbId.toString()};
        String sortOrder = ShowsDbContract.SeasonEntry.COLUMN_SEASON_NUMBER + " DESC";
        return mContext.getContentResolver().query(ShowsDbContract.SeasonEntry.CONTENT_URI,null,where,selectionArgs,sortOrder);
    }

    public Cursor getEpisodes(Integer tmdbId,Integer season){
        String where = ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY + "=? AND " + ShowsDbContract.EpisodeEntry.COLUMN_SEASON_NUMBER + "=?";
        String[] selectionArgs = {tmdbId.toString(),season.toString()};
        String sortOrder = ShowsDbContract.EpisodeEntry.COLUMN_EPISODE_NUMBER + " ASC";
        return mContext.getContentResolver().query(ShowsDbContract.EpisodeEntry.CONTENT_URI,null,where,selectionArgs,sortOrder);
    }

    public Cursor getEpisodesLastMonth(){
        Integer day = Utility.getDay();
        Integer month = Utility.getMonth();
        Integer year = Utility.getYear();
        Integer prevYear;
        Integer prevMonth;

        if(month==1) {
            prevYear = year-1;
            prevMonth = 12;
        }
        else{
            prevYear = year;
            prevMonth = month-1;
        }

        String where = "("+ShowsDbContract.EpisodeEntry.TABLE_NAME + "." + ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_YEAR + "=" + year.toString() + " AND "
                + ShowsDbContract.EpisodeEntry.TABLE_NAME + "." + ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_MONTH + "=" + month.toString() + " AND "
                + ShowsDbContract.EpisodeEntry.TABLE_NAME + "." + ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_DAY + "<" + day.toString() + ") OR ("
                + ShowsDbContract.EpisodeEntry.TABLE_NAME + "." +  ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_YEAR + "=" + prevYear.toString() + " AND "
                + ShowsDbContract.EpisodeEntry.TABLE_NAME + "." + ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_MONTH + "=" + prevMonth.toString() + " AND "
                + ShowsDbContract.EpisodeEntry.TABLE_NAME + "." + ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_DAY + ">=" + day.toString() + ")";


        String sortOrder = ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_YEAR + " DESC, "
                + ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_MONTH + " DESC, "
                + ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_DAY + " DESC";

        return mContext.getContentResolver().query(Uri.parse(ShowsDbContract.EpisodeEntry.CONTENT_URI+"/last_month"),null,where,null,sortOrder);
    }

    public Cursor getEpisodesNextMonth(){
        Integer day = Utility.getDay();
        Integer month = Utility.getMonth();
        Integer year = Utility.getYear();
        Integer nextYear;
        Integer nextMonth;

        if(month==12) {
            nextYear = year+1;
            nextMonth = 1;
        }
        else{
            nextYear = year;
            nextMonth = month+1;
        }

        String where = "("+ShowsDbContract.EpisodeEntry.TABLE_NAME + "." + ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_YEAR + "=" + year.toString() + " AND "
                + ShowsDbContract.EpisodeEntry.TABLE_NAME + "." + ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_MONTH + "=" + month.toString() + " AND "
                + ShowsDbContract.EpisodeEntry.TABLE_NAME + "." + ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_DAY + ">" + day.toString() + ") OR ("
                + ShowsDbContract.EpisodeEntry.TABLE_NAME + "." + ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_YEAR + "=" + nextYear.toString() + " AND "
                + ShowsDbContract.EpisodeEntry.TABLE_NAME + "." + ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_MONTH + "=" + nextMonth.toString() + " AND "
                + ShowsDbContract.EpisodeEntry.TABLE_NAME + "." + ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_DAY + "<=" + day.toString() + ")";

        String sortOrder = ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_YEAR + " ASC, "
                + ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_MONTH + " ASC, "
                + ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_DAY + " ASC";

        return mContext.getContentResolver().query(Uri.parse(ShowsDbContract.EpisodeEntry.CONTENT_URI+"/next_month"),null,where,null,sortOrder);
    }

    public Cursor getCreators(Integer tmdbId){
        //String where = ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY + "=?";
        String[] selectionArgs = {tmdbId.toString()};
        return mContext.getContentResolver().query(ShowsDbContract.CreatorEntry.CONTENT_URI,null,null,selectionArgs,null);
    }

    public Cursor getAllSeasons(){
        String sortOrder = ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY + " ASC, " + ShowsDbContract.SeasonEntry.COLUMN_SEASON_NUMBER + " DESC";
        return mContext.getContentResolver().query(ShowsDbContract.SeasonEntry.CONTENT_URI,null,null,null,sortOrder);
    }

    public void updateTVShowDetails(final TVShowDetailed tvShowDetailed, final Credits credits){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Integer todayDay = Utility.getDay();
                Integer todayMonth = Utility.getMonth();
                Integer todayYear = Utility.getYear();

                ContentValues showsValues = new ContentValues();
                showsValues.put(ShowsDbContract.ShowsEntry._ID,tvShowDetailed.getId());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_NAME,tvShowDetailed.getName());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_POSTER_PATH,tvShowDetailed.getPosterPath());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_VOTE_AVERAGE,tvShowDetailed.getVoteAverage());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_VOTE_COUNT,tvShowDetailed.getVoteCount());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_POPULARITY,tvShowDetailed.getPopularity());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_OVERVIEW,tvShowDetailed.getOverview());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_NUM_EPISODES,tvShowDetailed.getNumberOfEpisodes());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_NUM_SEASONS,tvShowDetailed.getSeasonsListSize());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_HOMEPAGE,tvShowDetailed.getHomepage());
                if(tvShowDetailed.getInProduction()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_IN_PRODUCTION,1);
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_FIRST_AIR_DATE_YEAR,tvShowDetailed.getFirstAirDateYear());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_FIRST_AIR_DATE_MONTH,tvShowDetailed.getFirstAirDateMonth());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_FIRST_AIR_DATE_DAY,tvShowDetailed.getFirstAirDateDay());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_LAST_AIR_DATE_YEAR,tvShowDetailed.getLastAirDateYear());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_LAST_AIR_DATE_MONTH,tvShowDetailed.getLastAirDateMonth());
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_LAST_AIR_DATE_DAY,tvShowDetailed.getLastAirDateDay());
                if(tvShowDetailed.isActionAdventure()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_ACTION_ADVENTURE,1);
                if(tvShowDetailed.isAnimation()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_ANIMATION,1);
                if(tvShowDetailed.isComedy()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_COMEDY,1);
                if(tvShowDetailed.isCrime()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_CRIME,1);
                if(tvShowDetailed.isDocumentary()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_DOCUMENTARY,1);
                if(tvShowDetailed.isDrama()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_DRAMA,1);
                if(tvShowDetailed.isFamily()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_FAMILY,1);
                if(tvShowDetailed.isKids()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_KIDS,1);
                if(tvShowDetailed.isMystery()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_MYSTERY,1);
                if(tvShowDetailed.isNews()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_NEWS,1);
                if(tvShowDetailed.isReality()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_REALITY,1);
                if(tvShowDetailed.isSciFiFantasy()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_SCI_FI_FANTASY,1);
                if(tvShowDetailed.isSoap()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_SOAP,1);
                if(tvShowDetailed.isTalk()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_TALK,1);
                if(tvShowDetailed.isWarPolitics()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_WAR_POLITICS,1);
                if(tvShowDetailed.isWestern()) showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_GENRE_WESTERN,1);
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_LAST_UPDATE_DAY,todayDay);
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_LAST_UPDATE_MONTH,todayMonth);
                showsValues.put(ShowsDbContract.ShowsEntry.COLUMN_LAST_UPDATE_YEAR,todayYear);
                Log.v("insertIntoDatabase",tvShowDetailed.getName()+","+credits.getId());

                ContentValues[] castValues = new ContentValues[credits.getNumberOfCast()];
                for(int i=0;i<castValues.length;i++){
                    castValues[i] = new ContentValues();
                    castValues[i].put(ShowsDbContract.CastEntry.COLUMN_PERSON_ID,credits.getCast().get(i).getId());
                    castValues[i].put(ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY,tvShowDetailed.getId());
                    castValues[i].put(ShowsDbContract.CastEntry.COLUMN_CHARACTER,credits.getCast().get(i).getCharacter());
                    castValues[i].put(ShowsDbContract.CastEntry.COLUMN_NAME,credits.getCast().get(i).getName());
                    castValues[i].put(ShowsDbContract.CastEntry.COLUMN_PROFILE_PATH,credits.getCast().get(i).getProfilePath());
                    castValues[i].put(ShowsDbContract.CastEntry.COLUMN_ORDER,credits.getCast().get(i).getOrder());
                }

                ContentValues[] creatorValues = new ContentValues[tvShowDetailed.numberOfCreators()];
                ContentValues[] creatorShowValues = new ContentValues[tvShowDetailed.numberOfCreators()];

                for (int i=0;i<creatorValues.length;i++){
                    creatorValues[i] = new ContentValues();
                    creatorValues[i].put(ShowsDbContract.CreatorEntry._ID,tvShowDetailed.getCreatedBy().get(i).getId());
                    creatorValues[i].put(ShowsDbContract.CreatorEntry.COLUMN_CREATOR_NAME,tvShowDetailed.getCreatedBy().get(i).getName());
                    creatorValues[i].put(ShowsDbContract.CreatorEntry.COLUMN_PROFILE_PATH,tvShowDetailed.getCreatedBy().get(i).getProfilePath());

                    creatorShowValues[i] = new ContentValues();
                    creatorShowValues[i].put(ShowsDbContract.ForeignKeys.COLUMN_CREATOR_FOREIGN_KEY,tvShowDetailed.getCreatedBy().get(i).getId());
                    creatorShowValues[i].put(ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY,tvShowDetailed.getId());
                }

                completeShowDetailsUpdate(showsValues,castValues,creatorValues,creatorShowValues);
            }
        });

        thread.start();

    }

    private synchronized void completeShowDetailsUpdate(ContentValues showsValues,ContentValues[] castValues,
                                                        ContentValues[] creatorValues,ContentValues[] creatorShowValues){

        Integer tmdbId = showsValues.getAsInteger(ShowsDbContract.ShowsEntry._ID);
        //the creators need to be deleted first, as the creator_show table is required
        deleteCreators(tmdbId);
        //delete the show in the shows table for te given id
        String where = ShowsDbContract.ShowsEntry._ID + "=?";
        String[] selectionArgs = {tmdbId.toString()};
        mContext.getContentResolver().delete(ShowsDbContract.ShowsEntry.CONTENT_URI, where, selectionArgs);

        // delete all show details
        String fullWhere = ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY + "=?";
        mContext.getContentResolver().delete(Uri.parse(ShowsDbContract.ShowsEntry.CONTENT_URI + "/cast_creator_show_delete"), fullWhere, selectionArgs);

        mContext.getContentResolver().insert(ShowsDbContract.ShowsEntry.CONTENT_URI,showsValues);
        mContext.getContentResolver().bulkInsert(ShowsDbContract.CastEntry.CONTENT_URI,castValues);
        mContext.getContentResolver().bulkInsert(ShowsDbContract.CreatorEntry.CONTENT_URI,creatorValues);
        mContext.getContentResolver().bulkInsert(ShowsDbContract.CreatorShowEntry.CONTENT_URI,creatorShowValues);
    }

    public void updateSeasons(final Integer showId,final Season[] seasons){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Integer todayDay = Utility.getDay();
                Integer todayMonth = Utility.getMonth();
                Integer todayYear = Utility.getYear();

                int numEpisodes = 0;
                for(int i=0;i<seasons.length;i++){
                    numEpisodes += seasons[i].numberOfEpisodes();
                }
                ContentValues[] seasonsValues = new ContentValues[seasons.length];
                ContentValues[] episodesValues = new ContentValues[numEpisodes];

                int episodesIndex = 0;
                for(int i=0;i<seasons.length;i++){
                    seasonsValues[i] = new ContentValues();
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry._ID,seasons[i].getId());
                    seasonsValues[i].put(ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY,showId);
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NUMBER,seasons[i].getSeasonNumber());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_AIR_DATE_YEAR,seasons[i].getAirDateYear());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_AIR_DATE_MONTH,seasons[i].getAirDateMonth());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_AIR_DATE_DAY,seasons[i].getAirDateDay());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_SEASON_NAME,seasons[i].getName());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_OVERVIEW,seasons[i].getOverview());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_POSTER_PATH,seasons[i].getPosterPath());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_NUMBER_OF_EPISODES,seasons[i].getEpisodes().size());
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_LAST_UPDATE_DAY,todayDay);
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_LAST_UPDATE_MONTH,todayMonth);
                    seasonsValues[i].put(ShowsDbContract.SeasonEntry.COLUMN_LAST_UPDATE_YEAR,todayYear);

                    for(int j=0;j<seasons[i].numberOfEpisodes();j++){
                        episodesValues[episodesIndex] = new ContentValues();
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry._ID,seasons[i].getEpisodes().get(j).getId());
                        episodesValues[episodesIndex].put(ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY,showId);
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_EPISODE_NAME,seasons[i].getEpisodes().get(j).getName());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_OVERVIEW,seasons[i].getEpisodes().get(j).getOverview());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_EPISODE_NUMBER,seasons[i].getEpisodes().get(j).getEpisodeNumber());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_SEASON_NUMBER,seasons[i].getEpisodes().get(j).getSeasonNumber());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_STILL_PATH,seasons[i].getEpisodes().get(j).getStillPath());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_VOTE_AVERAGE,seasons[i].getEpisodes().get(j).getVoteAverage());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_VOTE_COUNT,seasons[i].getEpisodes().get(j).getVoteCount());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_YEAR,seasons[i].getEpisodes().get(j).getAirDateYear());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_MONTH,seasons[i].getEpisodes().get(j).getAirDateMonth());
                        episodesValues[episodesIndex].put(ShowsDbContract.EpisodeEntry.COLUMN_AIR_DATE_DAY,seasons[i].getEpisodes().get(j).getAirDateDay());
                        episodesIndex++;
                    }

                }

                completeSeasonsUpdate(showId,seasons,seasonsValues,episodesValues);
            }
        });

        thread.start();
    }

    private synchronized void completeSeasonsUpdate(Integer showId,final Season[] seasons,ContentValues[] seasonsValues,ContentValues[] episodesValues){
        String[] selectionArgs = new String[seasons.length+1];
        String fullWhere = ShowsDbContract.ForeignKeys.COLUMN_SHOW_FOREIGN_KEY + "=?" + " AND (";

        selectionArgs[0] = showId.toString();
        for(int i=0;i<seasons.length;i++){
            selectionArgs[i+1] = seasons[i].getSeasonNumber().toString();
            if(i>0) fullWhere += " OR ";
            fullWhere += ShowsDbContract.SeasonEntry.COLUMN_SEASON_NUMBER + "=?";
        }

        fullWhere += ");";

        mContext.getContentResolver().delete(Uri.parse(ShowsDbContract.SeasonEntry.CONTENT_URI + "/seasons_delete"), fullWhere, selectionArgs);

        mContext.getContentResolver().bulkInsert(ShowsDbContract.SeasonEntry.CONTENT_URI,seasonsValues);
        mContext.getContentResolver().bulkInsert(ShowsDbContract.EpisodeEntry.CONTENT_URI,episodesValues);
    }

}
