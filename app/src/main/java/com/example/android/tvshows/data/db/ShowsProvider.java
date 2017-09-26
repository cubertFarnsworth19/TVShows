package com.example.android.tvshows.data.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
//import android.util.Log;
import com.example.android.tvshows.data.db.ShowsDbContract.*;
import com.example.android.tvshows.data.db.ShowsDbContract.ShowsEntry;
import com.example.android.tvshows.data.db.ShowsDbContract.SeasonEntry;
import com.example.android.tvshows.data.db.ShowsDbContract.EpisodeEntry;
import com.example.android.tvshows.data.db.ShowsDbContract.CastEntry;
import com.example.android.tvshows.data.db.ShowsDbContract.CreatorEntry;
import com.example.android.tvshows.data.db.ShowsDbContract.CreatorShowEntry;

public class ShowsProvider extends ContentProvider {

    private ShowsDbHelper mDbHelper;

    private static final int TVSHOW = 100;
    private static final int CAST = 101;
    private static final int SEASONS = 102;
    private static final int EPISODES = 103;
    private static final int CREATOR = 104;
    private static final int CREATOR_SHOW = 105;
    private static final int FULL_SHOW_DELETE = 106;
    private static final int EPISODES_NEXT_MONTH = 107;
    private static final int EPISODES_LAST_MONTH = 108;
    private static final int CREATOR_COUNT = 109;
    private static final int CAST_CREATOR_SHOW_DELETE = 110;
    private static final int SEASONS_DELETE = 111;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        sUriMatcher.addURI(ShowsDbContract.CONTENT_AUTHORITY, ShowsDbContract.PATH_SHOW,TVSHOW);
        sUriMatcher.addURI(ShowsDbContract.CONTENT_AUTHORITY, ShowsDbContract.PATH_SHOW + "/full_delete",FULL_SHOW_DELETE);
        sUriMatcher.addURI(ShowsDbContract.CONTENT_AUTHORITY, ShowsDbContract.PATH_SHOW + "/cast_creator_show_delete",CAST_CREATOR_SHOW_DELETE);
        sUriMatcher.addURI(ShowsDbContract.CONTENT_AUTHORITY, ShowsDbContract.PATH_CAST,CAST);
        sUriMatcher.addURI(ShowsDbContract.CONTENT_AUTHORITY, ShowsDbContract.PATH_SEASON,SEASONS);
        sUriMatcher.addURI(ShowsDbContract.CONTENT_AUTHORITY, ShowsDbContract.PATH_SEASON + "/seasons_delete",SEASONS_DELETE);
        sUriMatcher.addURI(ShowsDbContract.CONTENT_AUTHORITY, ShowsDbContract.PATH_EPISODE,EPISODES);
        sUriMatcher.addURI(ShowsDbContract.CONTENT_AUTHORITY, ShowsDbContract.PATH_EPISODE+"/next_month",EPISODES_NEXT_MONTH);
        sUriMatcher.addURI(ShowsDbContract.CONTENT_AUTHORITY, ShowsDbContract.PATH_EPISODE+"/last_month",EPISODES_LAST_MONTH);
        sUriMatcher.addURI(ShowsDbContract.CONTENT_AUTHORITY, ShowsDbContract.PATH_CREATOR,CREATOR);
        sUriMatcher.addURI(ShowsDbContract.CONTENT_AUTHORITY, ShowsDbContract.PATH_CREATOR_SHOW,CREATOR_SHOW);

        sUriMatcher.addURI(ShowsDbContract.CONTENT_AUTHORITY, ShowsDbContract.PATH_CREATOR + "/count",CREATOR_COUNT);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new ShowsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] columns,String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match){
            case TVSHOW:
                cursor = database.query(ShowsEntry.TABLE_NAME,columns, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CAST:
                cursor = database.query(CastEntry.TABLE_NAME,columns, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case SEASONS:
                cursor = database.query(SeasonEntry.TABLE_NAME,columns, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case EPISODES:
                cursor = database.query(EpisodeEntry.TABLE_NAME,columns, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case EPISODES_NEXT_MONTH:
                String rawQueryNextMonth = "SELECT * FROM " + ShowsEntry.TABLE_NAME + " INNER JOIN "  + EpisodeEntry.TABLE_NAME
                        + " ON " + ShowsEntry.TABLE_NAME + "." + ShowsEntry._ID + " = " + EpisodeEntry.TABLE_NAME + "." + ForeignKeys.COLUMN_SHOW_FOREIGN_KEY
                        + " WHERE " + selection + " ORDER BY " + sortOrder;
                cursor = database.rawQuery(rawQueryNextMonth,null);
                break;
            case EPISODES_LAST_MONTH:
                String rawQueryLastMonth = "SELECT * FROM " + ShowsEntry.TABLE_NAME + " INNER JOIN "  + EpisodeEntry.TABLE_NAME
                        + " ON " + ShowsEntry.TABLE_NAME + "." + ShowsEntry._ID + " = " + EpisodeEntry.TABLE_NAME + "." + ForeignKeys.COLUMN_SHOW_FOREIGN_KEY
                        + " WHERE " + selection + " ORDER BY " + sortOrder;
                cursor = database.rawQuery(rawQueryLastMonth,null);
                break;
            case CREATOR:
                String rawQueryCreator = "SELECT * FROM "
                        + CreatorEntry.TABLE_NAME +" INNER JOIN "
                        + CreatorShowEntry.TABLE_NAME
                        + " ON " + CreatorEntry.TABLE_NAME + "." + CreatorEntry._ID
                        + " = " + CreatorShowEntry.TABLE_NAME + "." + ForeignKeys.COLUMN_CREATOR_FOREIGN_KEY
                        + " WHERE " + CreatorShowEntry.TABLE_NAME + "." + ForeignKeys.COLUMN_SHOW_FOREIGN_KEY + " = " + selectionArgs[0];

                cursor = database.rawQuery(rawQueryCreator,null);
                break;
            case CREATOR_COUNT:
                //String string = "SELECT COUNT(creator_id) AS counter FROM creator_show WHERE creator_id = "+ selectionArgs[0] ;
                String string = "SELECT COUNT("+ ForeignKeys.COLUMN_CREATOR_FOREIGN_KEY +") AS counter FROM " + CreatorShowEntry.TABLE_NAME
                        + " WHERE " + ForeignKeys.COLUMN_CREATOR_FOREIGN_KEY + " = " + selectionArgs[0] ;
                cursor = database.rawQuery(string,null);
                cursor.moveToFirst();
                //Log.v("ShowsProvider ", Integer.toString(cursor.getInt(0)));
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match){
            case TVSHOW:
                return ShowsEntry.CONTENT_LIST_TYPE;
            case CAST:
                return CastEntry.CONTENT_LIST_TYPE;
            case SEASONS:
                return SeasonEntry.CONTENT_LIST_TYPE;
            case EPISODES:
                return EpisodeEntry.CONTENT_LIST_TYPE;
            case CREATOR:
                return CreatorEntry.CONTENT_LIST_TYPE;
            case CREATOR_SHOW:
                return CreatorShowEntry.CONTENT_LIST_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        if(match==TVSHOW){
            SQLiteDatabase database = mDbHelper.getWritableDatabase();
            long id = database.insert(ShowsEntry.TABLE_NAME,null,contentValues);
            getContext().getContentResolver().notifyChange(uri,null);

          //  Log.v("ShowsProvider insert",""+id);
            return ContentUris.withAppendedId(uri,id);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final int match = sUriMatcher.match(uri);
        if(match==SEASONS){
            SQLiteDatabase database = mDbHelper.getWritableDatabase();
            //transaction enters all rows with a single transaction
            database.beginTransaction();
            try{
                for(ContentValues v:values){
                    database.insert(SeasonEntry.TABLE_NAME,null,v);
                   // Log.v("ShowsProvider insert","season "+ v.getAsString(SeasonEntry.COLUMN_SEASON_NAME));
                }
                database.setTransactionSuccessful();
            }
            finally
            {database.endTransaction();}

            getContext().getContentResolver().notifyChange(uri,null);
            return 1;
        }
        if(match==EPISODES){
            SQLiteDatabase database = mDbHelper.getWritableDatabase();
            database.beginTransaction();
            try{
                for (ContentValues v : values) {
                    database.insert(EpisodeEntry.TABLE_NAME, null, v);

                  //  Log.v("ShowsProvider insert", "episode " + v.getAsInteger(EpisodeEntry.COLUMN_EPISODE_NUMBER));
                }
                database.setTransactionSuccessful();
            }
            finally
            {database.endTransaction();}

            getContext().getContentResolver().notifyChange(uri,null);

            return 1;
        }
        if(match==CAST){
            SQLiteDatabase database = mDbHelper.getWritableDatabase();
            database.beginTransaction();
            try{
                for(ContentValues v:values){
                    database.insert(CastEntry.TABLE_NAME,null,v);
                 //   Log.v("ShowsProvider insert","cast "+ v.getAsString(CastEntry.COLUMN_NAME));
                }
                database.setTransactionSuccessful();
            }
            finally
            {database.endTransaction();}
            getContext().getContentResolver().notifyChange(uri,null);
            return 1;
        }
        if(match==CREATOR){
            SQLiteDatabase database = mDbHelper.getWritableDatabase();
            database.beginTransaction();
            try{
                for(ContentValues v:values){
                    database.insert(CreatorEntry.TABLE_NAME,null,v);
                  //  Log.v("ShowsProvider insert","creator "+ v.getAsString(CreatorEntry.COLUMN_CREATOR_NAME));
                }
                database.setTransactionSuccessful();
            }
            finally
            {database.endTransaction();}
            getContext().getContentResolver().notifyChange(uri,null);
            return 1;
        }
        if(match==CREATOR_SHOW){
            SQLiteDatabase database = mDbHelper.getWritableDatabase();
            database.beginTransaction();
            try{
                for(ContentValues v:values){
                    database.insert(CreatorShowEntry.TABLE_NAME,null,v);
                }
                database.setTransactionSuccessful();
            }
            finally
            {database.endTransaction();}
            getContext().getContentResolver().notifyChange(uri,null);
            return 1;
        }
        else
            return 0;

    }

    @Override
    public int delete(Uri uri, String where, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted = 0;

        final int match = sUriMatcher.match(uri);

        switch(match){
            case TVSHOW:
                rowsDeleted = database.delete(ShowsEntry.TABLE_NAME,where,selectionArgs);
             //   Log.v("ShowsProvider delete","show "+ selectionArgs[0] );
                break;
            case CAST_CREATOR_SHOW_DELETE:
                rowsDeleted = database.delete(CastEntry.TABLE_NAME,where,selectionArgs);
            //    Log.v("ShowsProvider delete","cast "+ selectionArgs[0] );
                //need to first delete from creators table
                database.delete(CreatorShowEntry.TABLE_NAME,where,selectionArgs);
           //     Log.v("ShowsProvider delete","creator_show "+ selectionArgs[0] );
                break;
            case SEASONS_DELETE:
                database.delete(SeasonEntry.TABLE_NAME,where,selectionArgs);
               // Log.v("ShowsProvider delete","seasons "+ selectionArgs[0] );
                database.delete(EpisodeEntry.TABLE_NAME,where,selectionArgs);
             //   Log.v("ShowsProvider delete","episodes "+ selectionArgs[0] );
                break;
            case FULL_SHOW_DELETE:
                rowsDeleted = database.delete(CastEntry.TABLE_NAME,where,selectionArgs);
            //    Log.v("ShowsProvider delete","cast "+ selectionArgs[0] );
                database.delete(SeasonEntry.TABLE_NAME,where,selectionArgs);
           //     Log.v("ShowsProvider delete","seasons "+ selectionArgs[0] );
                database.delete(EpisodeEntry.TABLE_NAME,where,selectionArgs);
            //    Log.v("ShowsProvider delete","episodes "+ selectionArgs[0] );
                //need to first delete from creators table
                database.delete(CreatorShowEntry.TABLE_NAME,where,selectionArgs);
            //    Log.v("ShowsProvider delete","creator_show "+ selectionArgs[0] );
                break;
            case CREATOR:
                rowsDeleted = database.delete(CreatorEntry.TABLE_NAME,where,selectionArgs);
             //   Log.v("ShowsProvider delete","creators "+ selectionArgs[0] );
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if(rowsDeleted!=0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String where, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        if(match==TVSHOW){
            SQLiteDatabase database = mDbHelper.getWritableDatabase();
            database.update(ShowsEntry.TABLE_NAME,contentValues,where,selectionArgs);
        }
        return 0;
    }



}
