package com.amit.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by RAMCHANDRA SINGH on 10-03-2017.
 */
public class MyLocation
{
    private Timer timer1;
    private LocationManager locationManager;
    private LocationResult locationResult;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private Context mContext;

    private String TAG = "MyLocation";
    private int REQUEST_CHECK_SETTINGS = 1;

    private LocationListener locationListenerNetwork = new LocationListener()
    {
        public void onLocationChanged(Location location)
        {
            timer1.cancel();
            locationResult.gotLocation(location);

            if (ActivityCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED)
            {
                Log.e(TAG, "onLocationChanged: permission to access location is not granted.");
                return;
            }

            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerGps);
        }

        public void onProviderDisabled(String provider)
        {

        }

        public void onProviderEnabled(String provider)
        {

        }

        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }
    };

    private LocationListener locationListenerGps = new LocationListener()
    {
        public void onLocationChanged(Location location)
        {
            timer1.cancel();
            locationResult.gotLocation(location);

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }

            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerNetwork);
        }

        public void onProviderDisabled(String provider)
        {

        }

        public void onProviderEnabled(String provider)
        {

        }

        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }
    };

    public boolean getLocation(Context context, LocationResult result)
    {
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        this.mContext = context;
        locationResult = result;

        if (locationManager == null)
        {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        //exceptions will be thrown if provider is not permitted.
        try
        {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch (Exception ex)
        {
            Log.e(TAG, "getLocation: exception while checking for gps provider:\n");
            ex.printStackTrace();
        }

        try
        {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch (Exception ex)
        {
            Log.e(TAG, "getLocation: exception while checking for network provider:\n");
            ex.printStackTrace();
        }

        //don't start listeners if no provider is enabled
        if (!gps_enabled && !network_enabled)
        {
            return false;
        }

        if (ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        {
            return false;
        }

        if (gps_enabled)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
        }

        if (network_enabled)
        {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
        }

        timer1 = new Timer();
        timer1.schedule(new GetLastLocation(), 10000);
        return true;
    }

    public static abstract class LocationResult
    {
        public abstract void gotLocation(Location location);
    }

    class GetLastLocation extends TimerTask
    {
        @Override
        public void run()
        {
            Location net_loc = null, gps_loc = null;

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }

            if (gps_enabled)
            {
                gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (network_enabled)
            {
                net_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            //if there are both values use the latest one
            if (gps_loc != null && net_loc != null)
            {
                if (gps_loc.getTime() > net_loc.getTime())
                {
                    locationResult.gotLocation(gps_loc);
                }
                else
                {
                    locationResult.gotLocation(net_loc);
                }

                return;
            }

            if (gps_loc != null)
            {
                locationResult.gotLocation(gps_loc);
                return;
            }

            if (net_loc != null)
            {
                locationResult.gotLocation(net_loc);
                return;
            }

            locationResult.gotLocation(null);
        }
    }
}
