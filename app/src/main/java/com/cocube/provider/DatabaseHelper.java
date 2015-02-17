package com.cocube.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class DatabaseHelper {
    private static final String DATABASE_NAME = "loltv.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_NAME_LIKE_LIST = "likelist";
    public static final String TABLE_NAME_TAG_LIST = "taglist";
    public static final String TABLE_NAME_PLAYLIST = "playlist";



    public static final String _ID = BaseColumns._ID;
    public static final String TITLE = LolTvContract.LikeItems.TITLE;
    public static final String DESC = LolTvContract.LikeItems.DESC;
    public static final String UPDATED = LolTvContract.LikeItems.UPDATED;
    public static final String YOUTUBE_ID = LolTvContract.LikeItems.YOUTUBE_ID;
    public static final String DURATION = LolTvContract.LikeItems.DURATION;
    public static final String VIEW_COUNT = LolTvContract.LikeItems.VIEW_COUNT;

    public static final String USER_ID = LolTvContract.Tags.USER_ID;
    public static final String TAG = LolTvContract.Tags.TAG;

    private static String LIST_CATEGORY = LolTvContract.PlayLists.LIST_CATEGORY;
    private static String LIST_NAME = LolTvContract.PlayLists.LIST_NAME;
    private static String LIST_URL = LolTvContract.PlayLists.LIST_URL;
    private static String LIST_SELECTED = LolTvContract.PlayLists.LIST_SELECTED;


    public static final int COLINDEX_FILENAME = 0;
    public static final int COLINDEX_DOWNLOAD_ID = 1;


    private static class OpenHelper extends SQLiteOpenHelper {


        /**
         * Sql example :
         * CREATE TABLE downloading
         * (
         * id INTEGER PRIMARY KEY AUTOINCREMENT,
         * filename TEXT NOT NULL,
         * download_id INTEGER
         * );
         */
        private static final String CREATE_TABLE_LIKELIST =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_LIKE_LIST + " ("
                        + _ID + " INTEGER PRIMARY KEY ASC, "
                        + TITLE + " DATETIME NOT NULL, "
                        + DESC + " TEXT, "
                        + UPDATED + " TEXT, "
                        + DURATION + " TEXT, "
                        + YOUTUBE_ID + " TEXT, "
                        + VIEW_COUNT + " TEXT"
                        + ");";

        private static final String CREATE_TABLE_PLAYLIST =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_PLAYLIST + " ("
                        + _ID + " INTEGER PRIMARY KEY ASC, "
                        + LIST_CATEGORY + " TEXT, "
                        + LIST_NAME + " TEXT NOT NULL, "
                        + LIST_URL + " TEXT NOT NULL, "
                        + LIST_SELECTED + " INTEGER," // boolean : 0(false)
                        + "UNIQUE(" + LIST_URL + ") ON CONFLICT REPLACE"
                        + ");";


        private static final String CREATE_TABLE_TAGLIST =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_TAG_LIST + " ("
                        + _ID + " INTEGER PRIMARY KEY ASC, "
                        + USER_ID + " TEXT NOT NULL, "
                        + TAG + " INTEGER"
                        + ");";


        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {


            db.execSQL(CREATE_TABLE_LIKELIST);
            db.execSQL(CREATE_TABLE_PLAYLIST);
            db.execSQL(CREATE_TABLE_TAGLIST);


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LIKE_LIST);
            onCreate(db);
        }
    }


    private SQLiteDatabase mWritableDb;
    private SQLiteDatabase mReadableDb;

    public SQLiteDatabase getWritableDatabase() {
        return mWritableDb;
    }

    public SQLiteDatabase getReadableDatabase() {
        return mReadableDb;
    }


    public DatabaseHelper(Context context) {

        OpenHelper openHelper = new OpenHelper(context);

        // getWritableDatabase()
        // : open or create depending on the DATABASE_VERSION
        mWritableDb = openHelper.getWritableDatabase();
        mReadableDb = openHelper.getReadableDatabase();
    }


}
