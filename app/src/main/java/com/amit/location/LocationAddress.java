package com.amit.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.List;
import java.util.Locale;

/**
 * 2018 April 21 - Saturday - 04:27 PM
 * Location Address class
 *
 * this class helps to get the address of the current location
 * using the latitude and longitude provided
**/
public class LocationAddress
{
    private static final String TAG = LocationAddress.class.getSimpleName();

    /**
     * 2018 April 21 - Saturday - 04:28 PM
     * Get Address from location method
     *
     * this method will get the address from current location
     * using the latitude and longitude and
     * will return the complete address of current location
    **/
    public static void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final Handler handler)
    {
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                String result;
                Address address = null;
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                try
                {
                    // getting the list of address from latitude and longitude
                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);

                    if (addressList != null && addressList.size() > 0)
                    {
                        address = addressList.get(0);
                    }
                }
                catch (Exception e)
                {
                    Log.e(TAG, "getAddressFromLocation:run: exception while getting address from location");
                    e.printStackTrace();
                }
                finally
                {
                    Message message = Message.obtain();
                    message.setTarget(handler);

                    if (address != null)
                    {
                        message.what = 1;
                        Bundle bundle = new Bundle();

                        bundle.putString("thoroughFare", address.getThoroughfare());
                        bundle.putString("subThoroughFare", address.getSubThoroughfare());
                        bundle.putString("city", address.getLocality());
                        bundle.putString("state", address.getAdminArea());
                        bundle.putString("country", address.getCountryName());
                        bundle.putString("postalCode", address.getPostalCode());
                        bundle.putString("subAdminArea", address.getSubAdminArea());
                        bundle.putString("subLocality", address.getSubLocality());
                        message.setData(bundle);
                    }
                    else
                    {
                        message.what = 1;
                        Bundle bundle = new Bundle();

                        result = "Latitude: " + latitude + "Longitude: " + longitude +
                                "\n Unable to get address for this location.";

                        bundle.putString("address", result);
                        message.setData(bundle);
                    }

                    message.sendToTarget();
                }
            }
        };

        thread.start();
    }
}
