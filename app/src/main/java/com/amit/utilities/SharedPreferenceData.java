package com.amit.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

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
     * this method is used for saving the data in shared preferences
     *
     * @param key - key is the name of the value will store data for and use it later to retrieve
     *              key will be unique for every data you need to save.
     *
     * @param value - value will the data for the key as a string format.
     **/
    public void setValue(String key, String value)
    {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    /**
     * Get value method
     * this method is used for getting the data from shared preferences
     *
     * @param key - you need to just pass the key name of the data you want to get.
     *
     * @return 0 - this method will return 0 as default value
     *             if not record found for the key or key not found
     *             this will return the data in string format
     **/
    public String getValue(String key)
    {
        return mSharedPreference.getString(key, "0");
    }

    /**
     * Set String value method
     * this method is used for saving the data in shared preferences
     *
     * @param key - key is the name of the value will store data for and use it later to retrieve
     *              key will be unique for every data you need to save.
     *
     * @param value - value will the data for the key as a string format.
    **/
    public void setStrValue(String key, String value)
    {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    /**
     * Get String value method
     * this method is used for getting the data from shared preferences
     *
     * @param key - you need to just pass the key name of the data you want to get.
     *
     * @return 0 - this method will return 0 as default value
     *             if not record found for the key or key not found
     *             this will return the data in string format
    **/
    public String getStrValue(String key)
    {
        return mSharedPreference.getString(key, "0");
    }

    /**
     * Set Int value method
     * this method is used for saving the data in shared preferences
     *
     * @param key - key is the name of the value will store data for and use it later to retrieve
     *              key will be unique for every data you need to save.
     *
     * @param value - value will the data for the key in integer format.
    **/
    public void setIntValue(String key, int value)
    {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    /**
     * Get int value method
     * this method is used for getting the data from shared preferences
     *
     * @param key - you need to just pass the key name of the data you want to get.
     *
     * @return 0 - this method will return 0 as default value
     *             if not record found for the key or key not found
     *             this will return data in integer format.
    **/
    public int getIntValue(String key)
    {
        return mSharedPreference.getInt(key, 0);
    }

    /**
     * Set boolean value method
     * this method is used for saving the data in shared preferences
     *
     * @param key - key is the name of the value will store data for and use it later to retrieve
     *              key will be unique for every data you need to save.
     *
     * @param value - value will the data for the key in boolean format.
     **/
    public void setBooleanValue(String key, boolean value)
    {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    /**
     * Get boolean value method
     * this method is used for getting the data from shared preferences
     *
     * @param key - you need to just pass the key name of the data you want to get.
     *
     * @return 0 - this method will return 0 as default value
     *             if not record found for the key or key not found
     *             this will return data in boolean format.
     **/
    public boolean getBooleanValue(String key)
    {
        return mSharedPreference.getBoolean(key, false);
    }

    /**
     * Set float value method
     * this method is used for saving the data in shared preferences
     *
     * @param key - key is the name of the value will store data for and use it later to retrieve
     *              key will be unique for every data you need to save.
     *
     * @param value - value will the data for the key in float format.
     **/
    public void setFloatValue(String key, float value)
    {
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    /**
     * Get float value method
     * this method is used for getting the data from shared preferences
     *
     * @param key - you need to just pass the key name of the data you want to get.
     *
     * @return 0 - this method will return 0 as default value
     *             if not record found for the key or key not found
     *             this will return data in float format.
     **/
    public float getFloatValue(String key)
    {
        return mSharedPreference.getFloat(key, 0f);
    }

    /**
     * Set Long value method
     * this method is used for saving the data in shared preferences
     *
     * @param key - key is the name of the value will store data for and use it later to retrieve
     *              key will be unique for every data you need to save.
     *
     * @param value - value will the data for the key in Long format.
     **/
    public void setLongValue(String key, long value)
    {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    /**
     * Get Long value method
     * this method is used for getting the data from shared preferences
     *
     * @param key - you need to just pass the key name of the data you want to get.
     *
     * @return 0 - this method will return 0 as default value
     *             if not record found for the key or key not found
     *             this will return data in Long format.
     **/
    public long getLongValue(String key)
    {
        return mSharedPreference.getLong(key, 0);
    }

    /**
     * Set String set value method
     * this method is used for saving the data in shared preferences
     *
     * @param key - key is the name of the value will store data for and use it later to retrieve
     *              key will be unique for every data you need to save.
     *
     * @param value - value will the data for the key in string set format.
     **/
    public void setStrSetValue(String key, Set<String> value)
    {
        mEditor.putStringSet(key, value);
        mEditor.commit();
    }

    /**
     * Get string set value method
     * this method is used for getting the data from shared preferences
     *
     * @param key - you need to just pass the key name of the data you want to get.
     *
     * @return 0 - this method will return 0 as default value
     *             if not record found for the key or key not found
     *             this will return data in string set format.
     **/
    public Set<String> getStrSetValue(String key)
    {
        return mSharedPreference.getStringSet(key, null);
    }
}
