package com.cocube.like;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.cocube.provider.LolTvContract;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 12
 * Time: 오전 10:18
 */

public class LikeLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {


    private final Context mContext;

    public LikeLoaderCallback(Context context) {
        mContext = context;
    }


    /////////////////////////////////////////////////////////////////////////////////
    ///   LoaderManager.LoaderCallbacks<Cursor>
    ///

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new CursorLoader(mContext, LolTvContract.LikeItems.CONTENT_URI,
                LolTvContract.LikeItems.PROJECTION_ALL, null, null, null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        LikeSingleton.getInstance().setData(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

}
