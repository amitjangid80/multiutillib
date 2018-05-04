package com.amit.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings.Secure;
import android.support.v4.util.Pair;
import android.util.Log;

import com.amit.utilities.SharedPreferenceData;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
* Created By AMIT JANGID
* 2018 April 17 - Tuesday - 12:56 PM
**/

public class ApiServices
{
    private static final String TAG = ApiServices.class.getSimpleName();

    public int mResponseCode = 0;
    private String apiPath, mDeviceID;
    private SharedPreferenceData sharedPreferenceData;

    public ApiServices(Context context)
    {
        this.sharedPreferenceData = new SharedPreferenceData(context);

        // set api path before using it
        // this path will consist of the url which is repeated in every api
        // Ex: http://www.example.com/api/
        this.apiPath = sharedPreferenceData.getValue("apiPath");

        // this will get the device id of the device
        this.mDeviceID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    public String getDeviceID()
    {
        return mDeviceID;
    }

    /**
     * get device name method
     *
     * this method will get the name of the device
    **/
    public String getDeviceName()
    {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer))
        {
            return capitalize(model);
        }
        else
        {
            return capitalize(manufacturer + " " + model);
        }
    }

    /**
     * capitalize method
     *
     * this method will capitalize or set the string to upper case
    **/
    private String capitalize(String s)
    {
        if (s == null || s.length() == 0)
        {
            return "";
        }

        char first = s.charAt(0);

        if (Character.isUpperCase(first))
        {
            return s;
        }
        else
        {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    /**
     * Make API Call method
     *
     * this method will handle all the api operations like GET, POST & PUT
     * parameters for this method are as follows
     *
     ************************************************************************************************
     *
     *** parameter #1
     *** @param apiName - this parameter will contain the name of the api with some path or without any path
     *                  - FOR EXAMPLE: with path - MobileAPI/RegisterMobile
     *                            without path - RegisterMobile
     *
     *** parameter #2
     *** @param requestMethod - this parameter will be passed with 3 values
     *                          #1 - POST OR
     *                          #2 - PUT OR
     *                          #3 - GET
     *
     *** parameter #3
     *** @param parameters - this parameter will contain parameters which will be passed to the api
     *                       and required by the api to work properly
     *
     *** parameter #4
     *** @param values - this parameter will hold the parameters in JSON format
     *                   this will be the data which we need to pass along with the api
     *                 - FOR EXAMPLE: MobileNumber = 9999999999, OTP = 9999, etc.
     *
     *** parameter #5
     *** @param hasToken - this parameter should be passed with true or false
     *                   - this parameter will be used if the api requires some token to work with
     *                   - if the api requires token then this has to be true
     *                   - if the api doesn't require token then this has to be false
     *
     * @return String which contains result from API.
     *
     ************************************************************************************************
     * */
    public String makeAPICall(final String apiName, final String requestMethod,
                              final boolean parameters, final JSONObject values,
                              final boolean hasToken)
    {
        try
        {
            String result;
            URL url = new URL(apiPath + apiName);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(requestMethod);
            httpURLConnection.addRequestProperty("Accept", "application/json, text/plain, */*");
            httpURLConnection.addRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpURLConnection.addRequestProperty("IsDeviceMode", "1");

            if (hasToken)
            {
                httpURLConnection.addRequestProperty("x-access-token", sharedPreferenceData.getValue("token"));
                Log.e(TAG, "makeAPICall: x-access-token is: " + sharedPreferenceData.getValue("token"));
            }

            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestProperty("Connection", "close");
            Log.e(TAG, "makeAPICall: api = " + url + "   values = " + values);

            if (parameters)
            {
                httpURLConnection.setDoOutput(true);
                DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                outputStream.writeBytes(values.toString());
                outputStream.flush();
                outputStream.close();
            }

            int responseCode = httpURLConnection.getResponseCode();
            mResponseCode = responseCode;
            Log.e(TAG, "makeAPICall: response code from api is: " + mResponseCode);

            if (responseCode == 200)
            {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }

                bufferedReader.close();
                result = stringBuilder.toString();
                Log.e(TAG, "makeAPICall: result from server is: " + result);
            }
            else
            {
                String responseMessage = httpURLConnection.getResponseMessage();
                Log.e(TAG, "makeAPICall: response message: " + responseMessage);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }

                bufferedReader.close();
                result = stringBuilder.toString();
                httpURLConnection.disconnect();
            }

            return result;
        }
        catch (Exception e)
        {
            Log.e(TAG, "makeAPICall: in web api services class:\n");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get Bitmap Data
     *
     * this method gets bitmap image from the server
     * this can be used to download the image from server or api
     *
     * @param url - this parameter will contain the url where the image has to be downloaded
     * @param requestType - request type will be GET OR POST
     * @param parameter - if any information has to be sent in body then set parameters
     *                    use json object for this parameter.
     **/
    public Pair<Integer, Bitmap> getBitmapData(String url, String requestType, String parameter)
    {
        Bitmap resultVal = null;
        InputStream iStream ;
        int responseCode = 0;

        try
        {
            URL urlConnect = new URL(apiPath + url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlConnect.openConnection();

            urlConnection.setRequestMethod(requestType);
            urlConnection.addRequestProperty("Content-Type", "application/json; charset=utf-8");

            Log.e(TAG, "getBitmapData: url for downloading image is: " + urlConnect);
            urlConnection.addRequestProperty("x-access-token", sharedPreferenceData.getValue("token"));

            Log.e(TAG, "getBitmapData: x-access-token is: " + sharedPreferenceData.getValue("token"));
            urlConnection.setConnectTimeout(30000);

            if (parameter != null)
            {
                urlConnection.setDoOutput(true);
                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                outputStream.writeBytes(parameter);
                outputStream.flush();
                outputStream.close();
            }

            try
            {
                responseCode = urlConnection.getResponseCode();
                Log.e(TAG,"Response Code from api is --  " + responseCode);

                if (responseCode == 200)
                {
                    iStream = urlConnection.getInputStream();

                    /*Creating a bitmap from the stream returned from the url */
                    resultVal = BitmapFactory.decodeStream(iStream);
                    Log.e(TAG, "getBitmapData: result from server is: " + resultVal);
                }
                else
                {
                    String str = urlConnection.getResponseMessage();
                    Log.e(TAG, "getBitmapData: response message from api is:" + str);
                }
            }
            catch (Exception ex)
            {
                Log.e(TAG, "getBitmapData: exception while downloading images from api:\n");
                ex.printStackTrace();
            }
            finally
            {
                urlConnection.disconnect();
                Log.e(TAG, "URL in finally is >" + url + "<-->" + resultVal);
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Exception while geting bitmap:");
            e.printStackTrace();
            return new Pair<>(responseCode, resultVal);
        }

        return new Pair<>(responseCode, resultVal);
    }
}
