package com.cocube;

import android.content.Context;
import android.content.SharedPreferences;

import com.cocube.parser.ParserInfo;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 10. 17
 * Time: 오후 4:57
 * To change this template use File | Settings | File Templates.
 */
public class LolTvPreference {


    static final String PREFERNECE_NAME = "loltv_pref";
    static final String KEY_ORDER_BY = "orderby";


    static SharedPreferences.Editor mEdit;
    static SharedPreferences mPref;
    private static Context mContext = null;


    public LolTvPreference() {
        mContext = null;
    }





    public static void edit(Context context) {

        mEdit = context.getSharedPreferences(LolTvPreference.PREFERNECE_NAME, Context.MODE_PRIVATE).edit();
    }






    public static int getOrderBy(Context context){
        SharedPreferences pref =
                context.getSharedPreferences(LolTvPreference.PREFERNECE_NAME, Context.MODE_PRIVATE);
        return pref.getInt(KEY_ORDER_BY, ParserInfo.SORT_TYPE_LATEST_FIRST);
    }



    public static void putInt(String key, int value){
        mEdit.putInt(key, value);

    }


    public static void putOrderBy(int b) {
        mEdit.putInt(KEY_ORDER_BY, b);
    }


    /**
     * set* methods do put* method with {@ref edit} and {@ref commit} methods.
     *
     * @param context
     * @param b
     */
    public static void setKeyOrderBy(Context context, int orderBy) {

        LolTvPreference.edit(context);
        LolTvPreference.putOrderBy(orderBy);
        LolTvPreference.commit();
    }



    private static void commit() {
        mEdit.commit();
    }


}
