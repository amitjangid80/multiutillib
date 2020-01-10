package com.amit.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import android.util.Log;

import com.amit.utilities.SharedPreferenceData;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created By AMIT JANGID
 * 2018 April 17 - Tuesday - 12:56 PM
**/
@SuppressWarnings("unused")
public class ApiServices
{
    private static final String TAG = ApiServices.class.getSimpleName();

    private String apiPath;
    private SharedPreferenceData sharedPreferenceData;

    public ApiServices(@NonNull Context context)
    {
        this.sharedPreferenceData = new SharedPreferenceData(context);

        // set api path before using it
        // this path will consist of the url which is repeated in every api
        // Ex: http://www.example.com/api/
        this.apiPath = sharedPreferenceData.getValue("apiPath");
    }

    /**
     * Make API Call method
     *
     * this method will handle all the api operations like GET OR POST OR PUT
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
     * @return Pair of integer and string which contains response value and response code from the server
     *
     ************************************************************************************************
    **/
    public Pair<Integer, String> makeAPICall(final String apiName, final String requestMethod,
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

            // httpURLConnection.setConnectTimeout(10000);
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
                httpURLConnection.disconnect();
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

            return new Pair<>(responseCode, result);
        }
        catch (Exception e)
        {
            Log.e(TAG, "makeAPICall: in web api services class:\n");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get Bitmap Image from Server method
     *
     * this method gets bitmap image from the server
     * this can be used to download the image from server or api
     *
     * @param apiPath - this parameter will contain the apiPath where the image has to be downloaded
     *              this apiPath parameter will contain the entire path for the image to be downloaded.
     *
     * @param requestType - request type will be GET OR POST
     *
     * @param parameter - if any information has to be sent in body then set parameters
     *                    use json object for this parameter.
     *
     * return           - pair of integer and bitmap value
     *                    where integer will be the response code
     *                    and bitmap will be the file uploaded.
     *
     * will be removed in next release
    **/
    @Deprecated
    public Pair<Integer, Bitmap> getBitmapImageFromServer(String apiPath, String requestType, String parameter)
    {
        Bitmap resultVal = null;
        InputStream iStream ;
        int responseCode = 0;

        try
        {
            URL urlConnect = new URL(apiPath);
            HttpURLConnection urlConnection = (HttpURLConnection) urlConnect.openConnection();

            urlConnection.setRequestMethod(requestType);
            urlConnection.addRequestProperty("Content-Type", "application/json; charset=utf-8");

            Log.e(TAG, "getBitmapData: apiPath for downloading image is: " + urlConnect);
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

                    /*Creating a bitmap from the stream returned from the apiPath */
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
                Log.e(TAG, "URL in finally is >" + apiPath + "<-->" + resultVal);
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
