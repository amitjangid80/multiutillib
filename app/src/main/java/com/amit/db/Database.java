package com.amit.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
* Created By AMIT JANGID
* 2018 April 17 - Tuesday - 12:10 PM
* Database class for creating database for the application111
* */

public class Database extends SQLiteOpenHelper
{
    private static final String TAG = Database.class.getSimpleName();
    private static final int databaseVersion = 1;
    private static Database mDatabase;

    private Database(Context context, String databaseName)
    {
        super(context, databaseName, null, databaseVersion);
    }

    static Database getDBInstance(Context context, String databaseName)
    {
        if (mDatabase == null)
        {
            synchronized (Database.class)
            {
                if (mDatabase == null)
                {
                    mDatabase = new Database(context, databaseName);
                }
            }
        }

        return mDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    /**
     * 2018 April 17 - Tuesday - 12:11 PM
     * Get Record Count
     *
     * this method will get the count of the record in that table
     * with single record count or count of all records
     *
     * @param query - THIS QUERY WILL CONTAIN A SELECT QUERY WITH OR WITHOUT CONDITION
     *                Ex: SELECT * FROM TABLE_NAME
     *                          OR
     *                    SELECT * FROM TABLE_NAME WHERE ID = 1
    **/
    int getRecordCount(String query)
    {
        try
        {
            int count;
            Cursor cursor = getReadableDatabase().rawQuery(query, null);

            if (cursor != null)
            {
                cursor.moveToFirst();
                count = cursor.getInt(0);
                cursor.close();
                return count;
            }
            else
            {
                Log.e(TAG, "getRecordCount: cursor was null for query: " + query);
                return 0;
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "getRecordCount: while getting count of record in database file:\n");
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
