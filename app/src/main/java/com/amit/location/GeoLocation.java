package com.amit.location;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by AMIT JANGID on 23-Nov-18.
 *
 * this class will help in getting current locations latitude and longitude and address
**/
@SuppressWarnings({"unused", "WeakerAccess", "deprecation"})
public class GeoLocation
{
    private static final String TAG = GeoLocation.class.getSimpleName();

    private static double mLatitude, mLongitude;
    public static final int REQUEST_LOCATION = 1111;

    private final Context mContext;

    private String mAddress, mCity, mState, mCountry, mPostalCode, mKnownName,
            mSubLocality, mSubAdminArea, mThoroughFare, mSubThoroughFare, mPremises;

    // constructor
    public GeoLocation(Context context)
    {
        this.mContext = context;

        // calling ask for locations permission
        askForLocationsPermission();
    }

    /**
     * ask for locations permission method
     * this method will ask for locations permission
     *
     * if locations permission is granted
     * then it will get the current locations latitude and longitude
     *
     * else it will ask for locations permission
    **/
    private void askForLocationsPermission()
    {
        try
        {
            // checking if locations permission is granted or not
            if (ActivityCompat.checkSelfPermission(mContext, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mContext, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                // permission not granted, asking for permission
                ActivityCompat.requestPermissions((Activity) mContext, new String[]
                        {
                                ACCESS_COARSE_LOCATION,
                                ACCESS_FINE_LOCATION

                        }, REQUEST_LOCATION);
            }
            else
            {
                // permission granted
                LocationManager locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

                if (locationManager != null)
                {
                    // checking if gps is enabled or not
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    {
                        // gps is enabled
                        // calling get geo location method
                        getGeoLocation(locationManager);
                    }
                    else
                    {
                        // gps is not enabled
                        Log.e(TAG, "askForLocationsPermission: GPS Service is not enabled.");
                    }
                }
                else
                {
                    Log.e(TAG, "askForLocationsPermission: GPS Service Not available for the device.");
                }
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "askForLocationsPermission: exception while checking for permission");
            e.printStackTrace();
        }
    }

    /**
     * get geo location method
     *
     * this method will get latitude and longitude of current location
     * and will also get address with all address fields
    **/
    private void getGeoLocation(LocationManager locationManager)
    {
        try
        {
            // checking if locations permission is granted or not
            if (ActivityCompat.checkSelfPermission(mContext, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mContext, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                // permission not granted returning nothing
                Log.e(TAG, "getLocation: locations permission was not allowed by the user.");
                return;
            }

            Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location passiveLocation = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            // if gps location is available getting latitude and longitude from gps
            if (gpsLocation != null)
            {
                mLatitude = gpsLocation.getLatitude();
                mLongitude = gpsLocation.getLongitude();
            }
            // if network location is available getting latitude and longitude from network
            else if (networkLocation != null)
            {
                mLatitude = networkLocation.getLatitude();
                mLongitude = networkLocation.getLongitude();
            }
            // if passive location is available getting latitude and longitude from passive
            else if (passiveLocation != null)
            {
                mLatitude = passiveLocation.getLatitude();
                mLongitude = passiveLocation.getLongitude();
            }
            // not able to get location
            else
            {
                Log.e(TAG, "getLocation: Unable to Trace current location:\n");
            }

            // checking if latitude and longitude is available
            if (mLatitude != 0 && mLongitude != 0)
            {
                // latitude and longitude is available
                // calling get geo address method
                getGeoAddress();
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "getLocation: exception while getting location:\n");
            e.printStackTrace();
        }
    }

    /**
     * get geo address method
     *
     * this method will get address of current location
    **/
    private void getGeoAddress()
    {
        try
        {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(mLatitude, mLongitude, 1);

            if (addressList != null && addressList.size() > 0)
            {
                mCity = addressList.get(0).getLocality();
                mState = addressList.get(0).getAdminArea();
                mCountry = addressList.get(0).getCountryName();
                mPostalCode = addressList.get(0).getPostalCode();
                mKnownName = addressList.get(0).getFeatureName();
                mAddress = addressList.get(0).getAddressLine(0);
                mPremises = addressList.get(0).getPremises();
                mSubAdminArea = addressList.get(0).getSubAdminArea();
                mSubLocality = addressList.get(0).getSubLocality();
                mSubThoroughFare = addressList.get(0).getSubThoroughfare();
                mThoroughFare = addressList.get(0).getThoroughfare();
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "getGeoAddress: exception while getting geo location:\n");
            e.printStackTrace();
        }
    }

    /**
     * 2018 October 17 - Wednesday - 02:50 PM
     * is gps enabled method
     *
     * this method will check if gps is enabled or not
     *
     * @param context - context of the application
     *
     * @return true if enabled, false if not enabled.
    **/
    public static boolean isGPSEnabled(Context context)
    {
        int locationMode;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            try
            {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            }
            catch (Settings.SettingNotFoundException e)
            {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        }
        else
        {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /**
     * get latitude method
     *
     * this method will return latitude got from current location
    **/
    public double getLatitude()
    {
        return mLatitude;
    }

    /**
     * get longitude method
     *
     * this method will return longitude got from current location
    **/
    public double getLongitude()
    {
        return mLongitude;
    }

    /**
     * get address method
     *
     * this method will return address got from current location
    **/
    public String getAddress()
    {
        return mAddress;
    }

    /**
     * get city method
     *
     * this method will return city got from current location
    **/
    public String getCity()
    {
        return mCity;
    }

    /**
     * get state method
     *
     * this method will return state got from current location
    **/
    public String getState()
    {
        return mState;
    }

    /**
     * get country method
     *
     * this method will return country got from current location
    **/
    public String getCountry()
    {
        return mCountry;
    }

    /**
     * get postal code method
     *
     * this method will return postal got from current location
    **/
    public String getPostalCode()
    {
        return mPostalCode;
    }

    /**
     * get known name method
     *
     * this method will return known name got from current location
    **/
    public String getKnownName()
    {
        return mKnownName;
    }

    /**
     * get sub locality method
     *
     * this method will return sub locality got from current location
    **/
    public String getSubLocality()
    {
        return mSubLocality;
    }
    /**
     * get sub admin area method
     *
     * this method will return sub admin area got from current location
    **/
    public String getSubAdminArea()
    {
        return mSubAdminArea;
    }

    /**
     * get premises method
     *
     * this method will return premises got from current location
    **/
    public String getPremises()
    {
        return mPremises;
    }

    /**
     * get thorough fare method
     *
     * this method will return thorough fare got from current location
    **/
    public String getThoroughFare()
    {
        return mThoroughFare;
    }

    /**
     * get sub thorough fare method
     *
     * this method will return sub thorough fare got from current location
    **/
    public String getSubThoroughFare()
    {
        return mSubThoroughFare;
    }
}
