package com.example.android.tvshows.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.tvshows.data.db.ShowsDbContract.*;

public class ShowsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "shows.db";
    private static final int DATABASE_VERSION = 1;

    public ShowsDbHelper(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_SHOWS_TABLE = "CREATE TABLE " + ShowsEntry.TABLE_NAME + "("
                + ShowsEntry._ID + " INTEGER PRIMARY KEY, "
                + ShowsEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + ShowsEntry.COLUMN_POSTER_PATH + " TEXT, "
                + ShowsEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_POPULARITY + " REAL NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_OVERVIEW + " TEXT, "
                + ShowsEntry.COLUMN_NUM_EPISODES + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_NUM_SEASONS + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_HOMEPAGE + " TEXT, "
                + ShowsEntry.COLUMN_IN_PRODUCTION + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_FIRST_AIR_DATE_YEAR + " INTEGER, "
                + ShowsEntry.COLUMN_FIRST_AIR_DATE_MONTH + " INTEGER, "
                + ShowsEntry.COLUMN_FIRST_AIR_DATE_DAY + " INTEGER, "
                + ShowsEntry.COLUMN_LAST_AIR_DATE_YEAR + " INTEGER, "
                + ShowsEntry.COLUMN_LAST_AIR_DATE_MONTH + " INTEGER, "
                + ShowsEntry.COLUMN_LAST_AIR_DATE_DAY + " INTEGER, "
                + ShowsEntry.COLUMN_GENRE_ACTION_ADVENTURE + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_GENRE_ANIMATION + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_GENRE_COMEDY + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_GENRE_CRIME + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_GENRE_DOCUMENTARY + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_GENRE_DRAMA + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_GENRE_FAMILY + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_GENRE_KIDS + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_GENRE_MYSTERY + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_GENRE_NEWS + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_GENRE_REALITY + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_GENRE_SCI_FI_FANTASY + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_GENRE_SOAP + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_GENRE_TALK + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_GENRE_WAR_POLITICS + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_GENRE_WESTERN + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_FAVORITE + " INTEGER NOT NULL DEFAULT 0, "
                + ShowsEntry.COLUMN_LAST_UPDATE_DAY + " INTEGER, "
                + ShowsEntry.COLUMN_LAST_UPDATE_MONTH + " INTEGER, "
                + ShowsEntry.COLUMN_LAST_UPDATE_YEAR + " INTEGER " + ");";

        String SQL_CREATE_CAST_TABLE = "CREATE TABLE " + CastEntry.TABLE_NAME + "("
                + CastEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ForeignKeys.COLUMN_SHOW_FOREIGN_KEY + " INTEGER NOT NULL,"
                + CastEntry.COLUMN_PERSON_ID + " INTEGER, "
                + CastEntry.COLUMN_CHARACTER + " TEXT, "
                + CastEntry.COLUMN_NAME + " TEXT, "
                + CastEntry.COLUMN_PROFILE_PATH + " TEXT, "
                + CastEntry.COLUMN_ORDER + " INTEGER NOT NULL DEFAULT 1000, "
                + " FOREIGN KEY (" + ForeignKeys.COLUMN_SHOW_FOREIGN_KEY + ") REFERENCES "
                + ShowsEntry.TABLE_NAME + " (" + ShowsEntry._ID + "));";

        String SQL_CREATE_SEASON_TABLE = "CREATE TABLE " + SeasonEntry.TABLE_NAME + "("
                + SeasonEntry._ID + " INTEGER PRIMARY KEY, "
                + ForeignKeys.COLUMN_SHOW_FOREIGN_KEY + " INTEGER NOT NULL,"
                + SeasonEntry.COLUMN_AIR_DATE_YEAR + " INTEGER, "
                + SeasonEntry.COLUMN_AIR_DATE_MONTH + " INTEGER, "
                + SeasonEntry.COLUMN_AIR_DATE_DAY + " INTEGER, "
                + SeasonEntry.COLUMN_OVERVIEW + " TEXT, "
                + SeasonEntry.COLUMN_SEASON_NUMBER + " INTEGER, "
                + SeasonEntry.COLUMN_SEASON_NAME + " TEXT, "
                + SeasonEntry.COLUMN_POSTER_PATH + " TEXT, "
                + SeasonEntry.COLUMN_NUMBER_OF_EPISODES + " INTEGER NOT NULL DEFAULT 0,"
                + SeasonEntry.COLUMN_LAST_UPDATE_DAY + " INTEGER, "
                + SeasonEntry.COLUMN_LAST_UPDATE_MONTH + " INTEGER, "
                + SeasonEntry.COLUMN_LAST_UPDATE_YEAR + " INTEGER, "
                + " FOREIGN KEY (" + ForeignKeys.COLUMN_SHOW_FOREIGN_KEY + ") REFERENCES "
                + ShowsEntry.TABLE_NAME + " (" + ShowsEntry._ID + "));";

        String SQL_CREATE_EPISODE_TABLE = "CREATE TABLE " + EpisodeEntry.TABLE_NAME + "("
                + EpisodeEntry._ID + " INTEGER PRIMARY KEY, "
                + ForeignKeys.COLUMN_SHOW_FOREIGN_KEY + " INTEGER NOT NULL,"
                + EpisodeEntry.COLUMN_AIR_DATE_YEAR + " INTEGER, "
                + EpisodeEntry.COLUMN_AIR_DATE_MONTH + " INTEGER, "
                + EpisodeEntry.COLUMN_AIR_DATE_DAY + " INTEGER, "
                + EpisodeEntry.COLUMN_EPISODE_NAME + " TEXT, "
                + EpisodeEntry.COLUMN_OVERVIEW + " TEXT, "
                + EpisodeEntry.COLUMN_EPISODE_NUMBER + " INTEGER, "
                + EpisodeEntry.COLUMN_SEASON_NUMBER + " INTEGER, "
                + EpisodeEntry.COLUMN_STILL_PATH + " TEXT, "
                + EpisodeEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL DEFAULT 0, "
                + EpisodeEntry.COLUMN_VOTE_COUNT + " REAL NOT NULL DEFAULT 0, "
                + " FOREIGN KEY (" + ForeignKeys.COLUMN_SHOW_FOREIGN_KEY + ") REFERENCES "
                + ShowsEntry.TABLE_NAME + " (" + ShowsEntry._ID + "));";

        String SQL_CREATE_CREATOR_TABLE = "CREATE TABLE " + CreatorEntry.TABLE_NAME + "("
                + CreatorEntry._ID + " INTEGER PRIMARY KEY, "
                + CreatorEntry.COLUMN_CREATOR_NAME + " TEXT, "
                + CreatorEntry.COLUMN_PROFILE_PATH + " TEXT" + ");";

        String SQL_CREATE_CREATOR_SHOW_TABLE = "CREATE TABLE " + CreatorShowEntry.TABLE_NAME + "("
                + CreatorShowEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ForeignKeys.COLUMN_CREATOR_FOREIGN_KEY + " INTEGER NOT NULL,"
                + ForeignKeys.COLUMN_SHOW_FOREIGN_KEY + " INTEGER NOT NULL,"
                + " FOREIGN KEY (" + ForeignKeys.COLUMN_CREATOR_FOREIGN_KEY + ") REFERENCES "
                + CreatorEntry.TABLE_NAME + " (" + CreatorEntry._ID + "),"
                + " FOREIGN KEY (" + ForeignKeys.COLUMN_SHOW_FOREIGN_KEY + ") REFERENCES "
                + ShowsEntry.TABLE_NAME + " (" + ShowsEntry._ID + "));";

        sqLiteDatabase.execSQL(SQL_CREATE_SHOWS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CAST_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SEASON_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_EPISODE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CREATOR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CREATOR_SHOW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
