package com.amit.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
* Created By AMIT JANGID
* 2018 April 17 - Tuesday - 12:50 PM
* */

public class InternetConnection
{
    private Context con;
    private static InternetConnection ic = null;

    public static synchronized InternetConnection getInstanceInternet(Context con)
    {
        if (ic == null)
        {
            ic = new InternetConnection(con);
        }

        return ic;
    }

    private InternetConnection(Context con)
    {
        this.con = con;
    }

    public boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
