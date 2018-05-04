package com.amit.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/*
 * Created by AMIT JANGID
 * 2018 April 17 - Tuesday - 12:20 PM
 *
 * this class is useful for saving data in shared preferences
*/

public class SharedPreferenceData
{
    private SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mEditor;

    /**
     * Constructor of the class
     *
     * @param context - this will take the context of the application
    **/
    public SharedPreferenceData(Context context)
    {
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPreference.edit();
    }

    /**
     * Set value method
     *
     * this method is used for saving the data in shared preferences
     *
     * @param key - key is the name of the value will store data for and use it later to retrieve
     *              key will be unique for every data you need to save.
     *
     * @param value - value will the data for the key.
    **/
    public void setValue(String key, String value)
    {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    /**
     * Get value method
     *
     * this method is used for getting the data from shared preferences
     *
     * @param key - you need to just pass the key name of the data you want to get.
     *
     * @return 0 - this method will return 0 as default value
     *             if not record found for the key or key not found
     **/
    public String getValue(String key)
    {
        return mSharedPreference.getString(key, "0");
    }
}
