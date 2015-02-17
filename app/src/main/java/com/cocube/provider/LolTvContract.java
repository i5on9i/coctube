package com.cocube.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Constants for the LikeItems table
 * of the lentitems provider.
 */
public final class LolTvContract {

    /**
     * The authority of the provider. Please check {ref: AndroidManifest.xml}
     */
    public static final String AUTHORITY =
            "com.cocube";
    /**
     * The content URI for the top-level
     * lentitems authority.
     */
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    /**
     * Constants for the LikeItems table
     * of the lentitems provider.
     */
    public static final class LikeItems
            implements CommonColumns {
        /**
         * The content URI for this table.
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(LolTvContract.CONTENT_URI, "likes");
        /**
         * The mime type of a directory of items.
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/like_items";
        /**
         * The mime type of a single item.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/like_items";
        /**
         * A projection of all columns
         * in the items table.
         */
        public static final String TITLE = "title";
        public static final String DESC = "desc";
        public static final String YOUTUBE_ID = "youtubeId";
        public static final String UPDATED = "updated";
        public static final String DURATION = "duration";
        public static final String VIEW_COUNT = "viewCount";

        public static final String[] PROJECTION_ALL ={DESC, TITLE, YOUTUBE_ID, UPDATED, DURATION, VIEW_COUNT};
        /**
         * The default sort order for
         * queries containing DESC fields.
         */
        public static final String SORT_ORDER_DEFAULT = "rowid ASC";
    }


    ////////////////////////////////////////////////////////////
    ////
    public static final class Tags implements CommonColumns {
        /**
         * The Content URI for this table.
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(LolTvContract.CONTENT_URI, "tags");
        /**
         * The mime type of a directory of download_ids
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/tags";
        /**
         * The mime type of a single download_id.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/tags";
        /**
         * The data column of this download_id.
         */
        public static final String USER_ID = "userId";
        public static final String TAG = "tag";
        public static final String ORDERBY = "orderBy";
        /**
         * A projection of all columns in the download_id table.
         */
        public static final String[] PROJECTION_ALL = {USER_ID, TAG, ORDERBY};

        public static final String SORT_ORDER_DEFAULT = "rowid ASC";
    }


    ////////////////////////////////////////////////////////////
    ////
    public static final class PlayLists implements CommonColumns {
        /**
         * The Content URI for this table.
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(LolTvContract.CONTENT_URI, "playlists");
        /**
         * The mime type of a directory of download_ids
         */
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/playlists";
        /**
         * The mime type of a single download_id.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/playlists";
        /**
         * The data column of this download_id.
         */
        public static final String LIST_CATEGORY = "category";
        public static final String LIST_NAME = "listName";
        public static final String LIST_URL = "listUrl";
        public static final String LIST_SELECTED = "selected";

        /**
         * A projection of all columns in the download_id table.
         */
        public static final String[] PROJECTION_ALL = {LIST_NAME, LIST_URL, LIST_SELECTED};

        public static final String SORT_ORDER_DEFAULT = "rowid ASC";
    }

    /**
     * Constants for a joined view of LikeItems and
     * Photos. The _id of this joined view is
     * the _id of the LikeItems table.
     */
    public static final class ItemEntities
            implements CommonColumns {

    }

    /**
     * This interface defines common columns
     * found in multiple tables.
     */
    public static interface CommonColumns
            extends BaseColumns {

    }
}