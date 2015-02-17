package com.cocube.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;


/**
 * The below source is written in the basis of Grokking Android source{@ref http://goo.gl/14Gkdx}
 */
public class CocubeProvider extends ContentProvider {

    private DatabaseHelper mDbHelper;

    private static final int LIKE_LIST = 1;
    private static final int LIKE_ID = 2;
    private static final int TAG_ID_LIST = 3;
    private static final int TAG_ID = 4;
    private static final int PLAYLIST_LIST = 5;
    private static final int PLAYLIST_ID = 6;


    private static final UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        mUriMatcher.addURI(LolTvContract.AUTHORITY, "likes", LIKE_LIST);
        mUriMatcher.addURI(LolTvContract.AUTHORITY, "likes/#", LIKE_ID);
        mUriMatcher.addURI(LolTvContract.AUTHORITY, "tags", TAG_ID_LIST);
        mUriMatcher.addURI(LolTvContract.AUTHORITY, "tags/#", TAG_ID);

        mUriMatcher.addURI(LolTvContract.AUTHORITY, "playlists", PLAYLIST_LIST);
        mUriMatcher.addURI(LolTvContract.AUTHORITY, "playlists/#", PLAYLIST_ID);
    }

    private final ThreadLocal<Boolean> mIsInBatchMode = new ThreadLocal<Boolean>(); // works like a lock

    @Override
    public boolean onCreate() {
        mDbHelper = new DatabaseHelper(getContext());

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        boolean useAuthorityUri = false;

        int uriType = mUriMatcher.match(uri);
        switch (uriType) {
            case LIKE_LIST:
                useAuthorityUri = true;
                queryBuilder.setTables(DatabaseHelper.TABLE_NAME_LIKE_LIST);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = LolTvContract.LikeItems.SORT_ORDER_DEFAULT;
                }

                break;
            case LIKE_ID:
                useAuthorityUri = true;
                queryBuilder.setTables(DatabaseHelper.TABLE_NAME_LIKE_LIST);
                queryBuilder.appendWhere(LolTvContract.LikeItems._ID + " = " +
                        uri.getLastPathSegment());

                break;
            case TAG_ID_LIST:
                useAuthorityUri = true;
                queryBuilder.setTables(DatabaseHelper.TABLE_NAME_TAG_LIST);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = LolTvContract.Tags.SORT_ORDER_DEFAULT;
                }

                break;
            case TAG_ID:
                useAuthorityUri = true;
                queryBuilder.setTables(DatabaseHelper.TABLE_NAME_TAG_LIST);
                queryBuilder.appendWhere(LolTvContract.Tags._ID + " = " +
                        uri.getLastPathSegment());


            case PLAYLIST_LIST:
                useAuthorityUri = true;
                queryBuilder.setTables(DatabaseHelper.TABLE_NAME_PLAYLIST);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = LolTvContract.PlayLists.SORT_ORDER_DEFAULT;
                }

                break;
            case PLAYLIST_ID:
                useAuthorityUri = true;
                queryBuilder.setTables(DatabaseHelper.TABLE_NAME_PLAYLIST);
                queryBuilder.appendWhere(LolTvContract.PlayLists._ID + " = " +
                        uri.getLastPathSegment());

                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);

        if (useAuthorityUri) {
            // Make sure that potential listeners are getting notified
            cursor.setNotificationUri(getContext().getContentResolver(),
                    LolTvContract.CONTENT_URI);
        } else {
            cursor.setNotificationUri(getContext().getContentResolver(),
                    uri);
        }


        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case LIKE_LIST:
                return LolTvContract.LikeItems.CONTENT_TYPE;
            case LIKE_ID:
                return LolTvContract.LikeItems.CONTENT_ITEM_TYPE;
            case TAG_ID_LIST:
                return LolTvContract.Tags.CONTENT_TYPE;
            case TAG_ID:
                return LolTvContract.Tags.CONTENT_ITEM_TYPE;
            case PLAYLIST_LIST:
                return LolTvContract.PlayLists.CONTENT_TYPE;
            case PLAYLIST_ID:
                return LolTvContract.PlayLists.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        int uriType = mUriMatcher.match(uri);

        SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();

        long rowId = 0;
        switch (uriType) {
            case LIKE_LIST:
                rowId = sqlDB.insertOrThrow(DatabaseHelper.TABLE_NAME_LIKE_LIST, null, values);
                break;
            case TAG_ID_LIST:
                rowId = sqlDB.insertOrThrow(DatabaseHelper.TABLE_NAME_TAG_LIST, null, values);
                break;
            case PLAYLIST_LIST:
                rowId = sqlDB.insertOrThrow(DatabaseHelper.TABLE_NAME_PLAYLIST, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        return getUriForId(rowId, uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = mUriMatcher.match(uri);

        SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();
        int delCount = 0;
        String where;
        String idStr;

        switch (uriType) {
            case LIKE_LIST:
                delCount = sqlDB.delete(DatabaseHelper.TABLE_NAME_LIKE_LIST, selection,
                        selectionArgs);
                break;
            case LIKE_ID:
                delCount = sqlDB.delete(DatabaseHelper.TABLE_NAME_LIKE_LIST, selection,
                        selectionArgs);

                idStr = uri.getLastPathSegment();
                where = LolTvContract.LikeItems._ID + " = " + idStr;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }

                delCount = sqlDB.delete(DatabaseHelper.TABLE_NAME_LIKE_LIST,
                        where,
                        selectionArgs);
                break;
            case TAG_ID_LIST:
                delCount = sqlDB.delete(DatabaseHelper.TABLE_NAME_TAG_LIST, selection,
                        selectionArgs);
                break;
            case TAG_ID:
                delCount = sqlDB.delete(DatabaseHelper.TABLE_NAME_TAG_LIST, selection,
                        selectionArgs);

                idStr = uri.getLastPathSegment();
                where = LolTvContract.Tags._ID + " = " + idStr;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }

                delCount = sqlDB.delete(DatabaseHelper.TABLE_NAME_TAG_LIST,
                        where,
                        selectionArgs);
                break;
            case PLAYLIST_LIST:
                delCount = sqlDB.delete(DatabaseHelper.TABLE_NAME_PLAYLIST, selection,
                        selectionArgs);
                break;
            case PLAYLIST_ID:
                delCount = sqlDB.delete(DatabaseHelper.TABLE_NAME_PLAYLIST, selection,
                        selectionArgs);

                idStr = uri.getLastPathSegment();
                where = LolTvContract.PlayLists._ID + " = " + idStr;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }

                delCount = sqlDB.delete(DatabaseHelper.TABLE_NAME_PLAYLIST,
                        where,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (delCount > 0 && !isInBatchMode()) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return delCount;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = mUriMatcher.match(uri);

        SQLiteDatabase sqlDB = mDbHelper.getWritableDatabase();
        int updateCount = 0;
        String id;
        String where;
        switch (uriType) {
            case LIKE_LIST:
                updateCount = sqlDB.update(DatabaseHelper.TABLE_NAME_LIKE_LIST,
                        values,
                        selection,
                        selectionArgs);
                break;
            case LIKE_ID:
                id = uri.getLastPathSegment();
                where = LolTvContract.LikeItems._ID + "=" + id;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }

                updateCount = sqlDB.update(DatabaseHelper.TABLE_NAME_LIKE_LIST,
                        values,
                        where,
                        selectionArgs);
                break;

            case TAG_ID_LIST:
                updateCount = sqlDB.update(DatabaseHelper.TABLE_NAME_TAG_LIST,
                        values,
                        selection,
                        selectionArgs);
                break;
            case TAG_ID:
                id = uri.getLastPathSegment();
                where = LolTvContract.Tags._ID + "=" + id;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }

                updateCount = sqlDB.update(DatabaseHelper.TABLE_NAME_TAG_LIST,
                        values,
                        where,
                        selectionArgs);
                break;
            case PLAYLIST_LIST:
                updateCount = sqlDB.update(DatabaseHelper.TABLE_NAME_PLAYLIST,
                        values,
                        selection,
                        selectionArgs);
                break;
            case PLAYLIST_ID:
                id = uri.getLastPathSegment();
                where = LolTvContract.PlayLists._ID + "=" + id;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }

                updateCount = sqlDB.update(DatabaseHelper.TABLE_NAME_PLAYLIST,
                        values,
                        where,
                        selectionArgs);

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        if (updateCount > 0 && !isInBatchMode()) {
            getContext().getContentResolver().notifyChange(uri, null);
        }


        return updateCount;
    }


    private Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            if (!isInBatchMode()) {
                getContext().getContentResolver().notifyChange(itemUri, null);
            }
            return itemUri;
        }

        // s.th. went wrong:
        throw new SQLException("Problem while inserting into uri: " + uri);
    }

//    @Override
//    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
//            throws OperationApplicationException {
//
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//        mIsInBatchMode.set(true);
//        // the next line works because SQLiteDatabase
//        // uses a thread local SQLiteSession object for
//        // all manipulations
//        db.beginTransaction();
//        try {
//            final ContentProviderResult[] retResult = super.applyBatch(operations);
//            db.setTransactionSuccessful();
//            getContext().getContentResolver().notifyChange(LolTvContract.CONTENT_URI, null);
//            return retResult;
//        }
//        finally {
//            mIsInBatchMode.remove();
//            db.endTransaction();
//        }
//    }

    private boolean isInBatchMode() {
        return mIsInBatchMode.get() != null && mIsInBatchMode.get();
    }

    public DatabaseHelper getOpenHelperForTest() {
        return mDbHelper;
    }

}
