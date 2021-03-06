package com.piusvelte.sonet.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.piusvelte.sonet.Sonet;
import com.piusvelte.sonet.social.Client;

/**
 * Created by bemmanuel on 3/22/15.
 */
public class Accounts implements BaseColumns {

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.piusvelte.accounts";

    public static final long INVALID_ACCOUNT_ID = -1;

    public static final String USERNAME = "username";
    public static final String TOKEN = "token";
    public static final String SECRET = "secret";
    public static final String SERVICE = "service";
    public static final String EXPIRY = "expiry";
    // service id for posting and linking
    public static final String SID = "sid";

    public static final String TABLE = "accounts";

    public static String ACCOUNTS_QUERY;

    static {
        ACCOUNTS_QUERY = "(case";

        for (Client.Network network : Client.Network.values()) {
            ACCOUNTS_QUERY += " when " + SERVICE + "=" + network.ordinal() + " then '" + network.name() + ": '";
        }

        ACCOUNTS_QUERY += " else '' end)||" + USERNAME + " as " + USERNAME;
    }

    private Accounts() {
    }

    public static Uri getContentUri(Context context) {
        return Uri.parse("content://" + Sonet.getAuthority(context) + "/accounts");
    }

    public static void createTable(@NonNull SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE
                + " (" + Accounts._ID + " integer primary key autoincrement, "
                + Accounts.USERNAME + " text, "
                + Accounts.TOKEN + " text, "
                + Accounts.SECRET + " text, "
                + Accounts.SERVICE + " integer, "
                + Accounts.EXPIRY + " integer, "
                + Accounts.SID + " text);");
    }
}