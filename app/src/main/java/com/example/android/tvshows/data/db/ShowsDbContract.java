package com.example.android.tvshows.data.db;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ShowsDbContract {

    private ShowsDbContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.tvshows";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SHOW = "show";
    public static final String PATH_CAST = "cast";
    public static final String PATH_EPISODE = "episode";
    public static final String PATH_SEASON = "season";
    public static final String PATH_CREATOR = "creator";
    public static final String PATH_CREATOR_SHOW = "creator_show";

    public static final class ForeignKeys {
        public static final String COLUMN_SHOW_FOREIGN_KEY = "show_id";
        public static final String COLUMN_CREATOR_FOREIGN_KEY = "creator_id";
    }

    public static final class ShowsEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SHOW);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SHOW;

        public static final String TABLE_NAME = "shows";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_NUM_EPISODES = "number_of_episodes";
        public static final String COLUMN_NUM_SEASONS = "number_of_seasons";
        public static final String COLUMN_HOMEPAGE = "homepage";
        public static final String COLUMN_IN_PRODUCTION = "in_production";
        public static final String COLUMN_FIRST_AIR_DATE_YEAR = "first_air_date_year";
        public static final String COLUMN_FIRST_AIR_DATE_MONTH = "first_air_date_month";
        public static final String COLUMN_FIRST_AIR_DATE_DAY = "first_air_date_day";
        public static final String COLUMN_LAST_AIR_DATE_YEAR = "last_air_date_year";
        public static final String COLUMN_LAST_AIR_DATE_MONTH = "last_air_date_month";
        public static final String COLUMN_LAST_AIR_DATE_DAY = "last_air_date_day";
        public static final String COLUMN_GENRE_ACTION_ADVENTURE = "genre_action_adventure";
        public static final String COLUMN_GENRE_ANIMATION = "genre_animation";
        public static final String COLUMN_GENRE_COMEDY = "genre_comedy";
        public static final String COLUMN_GENRE_CRIME = "genre_crime";
        public static final String COLUMN_GENRE_DOCUMENTARY = "genre_documentary";
        public static final String COLUMN_GENRE_DRAMA = "genre_drama";
        public static final String COLUMN_GENRE_FAMILY = "genre_family";
        public static final String COLUMN_GENRE_KIDS = "genre_kids";
        public static final String COLUMN_GENRE_MYSTERY = "genre_mystery";
        public static final String COLUMN_GENRE_NEWS = "genre_news";
        public static final String COLUMN_GENRE_REALITY = "genre_reality";
        public static final String COLUMN_GENRE_SCI_FI_FANTASY = "genre_sci_fi_fantasy";
        public static final String COLUMN_GENRE_SOAP = "genre_soap";
        public static final String COLUMN_GENRE_TALK = "genre_talk";
        public static final String COLUMN_GENRE_WAR_POLITICS = "genre_war_politics";
        public static final String COLUMN_GENRE_WESTERN = "genre_western";
        public static final String COLUMN_FAVORITE = "favorite";
        public static final String COLUMN_LAST_UPDATE_DAY = "last_update_day";
        public static final String COLUMN_LAST_UPDATE_MONTH = "last_update_month";
        public static final String COLUMN_LAST_UPDATE_YEAR = "last_update_year";
    }

    public static final class CastEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CAST);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CAST;

        public static final String TABLE_NAME = "cast";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PERSON_ID = "person_id";
        public static final String COLUMN_CHARACTER = "character";
        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_PROFILE_PATH = "profile_path";
        public static final String COLUMN_ORDER = "cast_order";

    }

    public static final class SeasonEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SEASON);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SEASON;

        public static final String TABLE_NAME = "seasons";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_AIR_DATE_YEAR = "air_date_year";
        public static final String COLUMN_AIR_DATE_MONTH = "air_date_month";
        public static final String COLUMN_AIR_DATE_DAY = "air_date_day";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_SEASON_NUMBER = "season_number";
        public static final String COLUMN_SEASON_NAME = "name";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_NUMBER_OF_EPISODES = "number_of_episodes";
        public static final String COLUMN_LAST_UPDATE_DAY = "last_update_day";
        public static final String COLUMN_LAST_UPDATE_MONTH = "last_update_month";
        public static final String COLUMN_LAST_UPDATE_YEAR = "last_update_year";
    }

    public static final class EpisodeEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EPISODE);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EPISODE;

        public static final String TABLE_NAME = "episodes";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_AIR_DATE_YEAR = "air_date_year";
        public static final String COLUMN_AIR_DATE_MONTH = "air_date_month";
        public static final String COLUMN_AIR_DATE_DAY = "air_date_day";
        public static final String COLUMN_EPISODE_NAME = "episode_name";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_EPISODE_NUMBER = "episode_number";
        public static final String COLUMN_SEASON_NUMBER = "season_number";
        public static final String COLUMN_STILL_PATH = "still_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
    }

    public static final class CreatorEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CREATOR);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CREATOR;

        public static final String TABLE_NAME = "creators";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_CREATOR_NAME = "name";
        public static final String COLUMN_PROFILE_PATH = "profile_path";

    }

    public static final class CreatorShowEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CREATOR_SHOW);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CREATOR_SHOW;

        public static final String TABLE_NAME = "creator_show";
        public static final String _ID = BaseColumns._ID;

    }


}
