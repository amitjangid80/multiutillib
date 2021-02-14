package com.amit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.amit.utilities.SharedPreferenceData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TreeSet;

/**
 * Created by AMIT JANGID
 * 2018 Feb 01 - Thursday - 02:55 PM
 * <p>
 * this class has method for executing db queries
 * like: creating table, inserting into table, deleting table, dropping table
**/
public class DBHelper
{
    private static final String TAG = DBHelper.class.getSimpleName();

    private final Database db;

    private ArrayList<DbData> dbDataArrayList = new ArrayList<>();
    private ArrayList<DbColumn> dbColumnArrayList = new ArrayList<>();

    /**
     * Constructor of the class
     * you have to set the db name first before using this class.
     *
     * @param context - context
     **/
    public DBHelper(Context context)
    {
        SharedPreferenceData sharedPreferenceData = new SharedPreferenceData(context);
        db = Database.getDBInstance(context, sharedPreferenceData.getValue("dbName"));
    }

    // region COMMENTS OF EXECUTE DATABASE OPERATIONS METHOD

    /**
     * Created By AMIT JANGID
     * 2018 Feb 01 - Thursday - 02:57 PM
     * Execute Database Operations - method name
     * <p>
     * This method is an universal method
     * which will perform all the operations of database
     * like - create, delete, insert, update,
     * <p>
     * parameters to be passed in this method are as follows:
     * the parameters below are numbered and has to passed in the same sequence
     * <p>
     * parameter #1:
     *
     * @param tableName         - This will be the name of the table on which the operations has to be performed
     *                          - FOR EXAMPLE: tableName = "User"
     *                          <p>
     *                          parameter #2:
     * @param values            - This parameter is an object of LinkedHashMap
     *                          - this parameter will contain set of Key and Value
     *                          to create table or insert data or update we will have to pass this parameter with data
     *                          - FOR EXAMPLE: - values.put("Name", "'Amit'") - this for inserting data and updating data
     *                          - values.put("Name", "TEXT") - this for creating table
     *                          <p>
     *                          parameter #3
     * @param operations        - Details as follows
     *                          ***********************************************************************************************
     *                          ** the values to the parameter operations are as follows:
     *                          <p>
     *                          ** c - for creating new table
     *                          - while doing create operation
     *                          the user has to pass the data type also with the field of the table
     *                          FOR EXAMPLE: values.put("Name", "TEXT") - this for creating table
     *                          <p>
     *                          ** d - for deleting values from table
     *                          - FOR DELETING SINGLE RECORD OR ROW
     *                          - the user has to pass true for the parameter "hasConditions"
     *                          because delete operation cannot be performed without where clause or condition
     *                          so the user has to pass data to "conditionalValues" parameter
     *                          - FOR EXAMPLE: hasConditions = true then conditionalValues.put("ID", "'1'")
     *                          <p>
     *                          - FOR DELETING ALL THE RECORDS OR ROWS
     *                          - the user has to pass null of values,
     *                          false for hasConditions and null of conditionalValues
     *                          - FOR EXAMPLE: values = null, hasConditions = false, conditionalValues = null
     *                          <p>
     *                          ** dr - for dropping the table
     *                          <p>
     *                          ** i - for inserting values in the table
     *                          - while doing insert operation
     *                          - the values that the user has to pass is of LinkedHashMap object
     *                          read the comments of values parameter for how to pass the values
     *                          - the user has to pass the field name and following with the value of the field
     *                          if the field is of type string or text,
     *                          then the value should be in single quotes ('')
     *                          if the field is of type integer,
     *                          then the user can pass the value without the quotes or with quotes
     *                          - FOR EXAMPLE: values.put("firstName", "'Amit'"),
     *                          values.put("active", "1") or
     *                          values.put("active", "'1'") - better option this way
     *                          <p>
     *                          ** u - for updating values of table
     *                          - while performing update operation
     *                          - pass values with field name of the table and following with values
     *                          - pass hasConditions value as true and also pass conditionalValues with data
     *                          - FOR EXAMPLE: hasConditions = true and conditionalValue.put("ID", "1") OR
     *                          conditionalValues("ID", "'1'") - this option is better
     *                          values.put("firstName", "'Amit'"),
     *                          values.put("email", "'anythiing@gmail.com'")
     *                          <p>
     *                          ***********************************************************************************************
     *                          <p>
     *                          parameter #4
     * @param hasConditions     - This parameter is used when the user has to perform Update or Delete operations
     *                          - pass true when doing UPDATE OR DELETE operations
     *                          and pass false if not doing UPDATE OR DELETE operations
     *                          for delete operation false can be passed,
     *                          if entire data of the table is to be removed
     *                          - when passing this parameter with true,
     *                          then you have to pass conditionalValues parameter as well
     *                          <p>
     *                          parameter #5
     * @param conditionalValues - This parameter is used when the hasConditions parameter is set to true:
     *                          - If the hasConditions parameter is true
     *                          then conditionalValues should have an Object of LinkedHashMap
     *                          - FOR EXAMPLE: conditionalValues.put("ID", "'1'") - for updating data at this "ID"
     *                          - If the hasConditions parameter is false
     *                          then conditionalValues can be null
     * @return true or false
     **/
    // endregion
    public boolean executeDatabaseOperations(String tableName, String operations,
                                             LinkedHashMap<String, String> values,
                                             boolean hasConditions,
                                             LinkedHashMap<String, String> conditionalValues)
    {
        try
        {
            String query = "";

            switch (operations.toLowerCase())
            {
                // this case will perform create table operations
                // and this case will generate create table query
                case "c":

                    // in create query values array list cannot be null
                    // checking if values array list is not null
                    // if not null then converting the array list into string
                    // after converting, replacing square brackets in the string
                    // and then passing the string to query string for db operation
                    if (values != null)
                    {
                        Log.e(TAG, "executeDatabaseOperations: map of values for create query is: " + values.toString());

                        // extracting all the values from hash map
                        // removing brackets from hash map
                        // and replacing = sign with space
                        String strValues = values.toString();
                        strValues = strValues.replace("{", "");
                        strValues = strValues.replace("}", "");
                        strValues = strValues.replace("=", " ");

                        query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + strValues + ")";
                        Log.e(TAG, "executeDatabaseOperations: create query: " + query);
                    }
                    else
                    {
                        Log.e(TAG, "executeDatabaseOperations: Values was null for creating table in.....");
                        return false;
                    }

                    break;

                // this case will perform delete operation
                // and this case will generate delete query
                case "d":

                    // while performing delete where clause is compulsory if deleting single record
                    //
                    // -- if you want to delete single row then values can be null,
                    //    hasCondition has to be true and conditional values cannot be null.
                    //
                    // -- if you want to delete all the records of the table
                    //    then just pass the table name and operation as "d"
                    //    rest of the parameters like values can be null,
                    //    has condition can be false and conditional values can be false
                    //
                    // checking if has conditions is set to true or not
                    // if yes then checking if conditional values array list is not null
                    // if not null then converting the conditional values array list to string
                    // after converting, replacing the square brackets in the string
                    // and then passing the query string for delete operations
                    if (hasConditions)
                    {
                        // checking if conditional values array list if not null
                        if (conditionalValues != null)
                        {
                            // extracting all the values from hash map
                            // removing brackets from hash map
                            String strConditionalValues = conditionalValues.toString();
                            strConditionalValues = strConditionalValues.replace("{", "");
                            strConditionalValues = strConditionalValues.replace("}", "");

                            query = "DELETE FROM " + tableName + " WHERE " + strConditionalValues;
                            Log.e(TAG, "executeDatabaseOperations: delete query: " + query);
                        }
                        else
                        {
                            Log.e(TAG, "executeDatabaseOperations: Conditional values was null for Delete query.....");
                            return false;
                        }
                    }
                    else
                    {
                        query = "DELETE FROM " + tableName;
                        Log.e(TAG, "executeDatabaseOperations: delete query: " + query);
                    }

                    break;

                // this case will perform drop operation
                // and this case will generate drop query
                case "dr":

                    // this query will drop the table if and only if the table exists
                    query = "DROP TABLE IF EXISTS '" + tableName + "'";
                    Log.e(TAG, "executeDatabaseOperations: drop query: " + query);

                    break;

                // this case will perform insert operation
                // and this case will generate insert query
                case "i":

                    // in insert query the values array list cannot be null
                    // checking if values array list is not null
                    // if not null then converting values array list to string
                    // after converting, replacing the square brackets in the string
                    // then passing the string to query string
                    if (values != null)
                    {
                        Log.e(TAG, "executeDatabaseOperations: map of values for insert query is: " + values.toString());
                        ArrayList<String> strValuesList = new ArrayList<>();

                        for (String k : values.keySet())
                        {
                            strValuesList.add(values.get(k));
                        }

                        // the below code extracts all the key set from hash map
                        // removing brackets from hash map
                        String fields = values.keySet().toString();
                        fields = fields.replace("[", "");
                        fields = fields.replace("]", "");

                        // extracting all the values from hash map
                        // removing brackets from hash map
                        String strValues = strValuesList.toString();
                        strValues = strValues.replace("[", "");
                        strValues = strValues.replace("]", "");

                        query = "INSERT INTO " + tableName + " (" + fields + ")" + " VALUES (" + strValues + ")";
                        Log.e(TAG, "executeDatabaseOperations: insert query: " + query);
                    }
                    else
                    {
                        Log.e(TAG, "executeDatabaseOperations: Values was null while inserting data in table.....");
                        return false;
                    }

                    break;

                case "u":

                    // while performing update operation has condition value should be true
                    // checking if has conditions is set to true
                    // if yes then checking values and conditional values array list are not null
                    // if not null then converting them to strings
                    // after converting, replacing square brackets in the string
                    // then passing the string to query string
                    if (hasConditions)
                    {
                        if (values != null && conditionalValues != null)
                        {
                            // extracting all the values from hash map
                            // removing brackets from hash map
                            String strValues = values.toString();
                            strValues = strValues.replace("{", "");
                            strValues = strValues.replace("}", "");

                            // extracting all the values from hash map
                            // removing brackets from hash map
                            String strConditionalValues = conditionalValues.toString();
                            strConditionalValues = strConditionalValues.replace("{", "");
                            strConditionalValues = strConditionalValues.replace("}", "");

                            query = "UPDATE " + tableName + " SET " + strValues + " WHERE " + strConditionalValues;
                            Log.e(TAG, "executeDatabaseOperations: update query: " + query);
                        }
                        else
                        {
                            Log.e(TAG, "executeDatabaseOperations: Values or Conditional values was null for update query.....");
                            return false;
                        }
                    }
                    else
                    {
                        Log.e(TAG, "executeDatabaseOperations: False passed for has conditions parameter while performing update query.....");
                        return false;
                    }

                    break;
            }

            // executing the query generated in above cases
            // if successful then it will return true
            // return true;
            db.getWritableDatabase().execSQL(query);

            return true;
        }
        catch (Exception e)
        {
            Log.e(TAG, "executeDatabaseOperations: in database helper class:\n");
            e.printStackTrace();
            return false;
        }
    }

    // region COMMENTS FOR executeSelectQuery method

    /**
     * 2018 Feb 01 - Thursday - 03:52 PM
     * Execute Select Query
     * <p>
     * parameters for this method are
     *
     * @param query - query that you want to execute
     * @return cursor with records from the table
     **/
    // endregion COMMENTS FOR executeSelectQuery method
    public Cursor executeSelectQuery(String query)
    {
        try
        {
            // query execution
            Cursor cursor = db.getWritableDatabase().rawQuery(query, null);

            // if cursor is not null then moving the position to first
            // and returning the cursor
            if (cursor != null)
            {
                cursor.moveToFirst();
            }
            else
            {
                Log.e(TAG, "executeSelectQuery: cursor was null. No data found.");
                return null;
            }

            return cursor;
        }
        catch (Exception e)
        {
            Log.e(TAG, "executeSelectQuery: in database helper class:\n");
            e.printStackTrace();
            return null;
        }
    }

    // region COMMENTS FOR executeSelectQuery method

    /**
     * 2018 Feb 01 - Thursday - 03:52 PM
     * Execute Select Query
     * <p>
     * parameters for this method are
     *
     * @param tableName         - name of the table to perform select operation
     * @param values            - values to perform select query on
     * @param hasConditions     - if you want to use the where clause in select query
     *                          then this parameter should be set to true
     *                          else this parameter can be false
     * @param conditionalValues - if the hasConditions is set to true
     *                          then the user has to pass conditionalValues
     *                          else it can be null
     *                          <p>
     *                          the below lines are not in use so ignore it
     *                          ** s - for selecting values from table
     *                          - pass * in values parameter when doing select operations
     *                          when you want to select every thing from the table
     *                          no matter condition is there or not
     *                          - pass values parameters with the name of the columns in the table
     *                          when you want to select one or multiple columns from the table
     *                          no matter condition is there or not
     **/
    // endregion COMMENTS FOR executeSelectQuery method
    public Cursor executeSelectQuery(String tableName, String values,
                                     boolean hasConditions,
                                     StringBuilder conditionalValues)
    {
        try
        {
            Cursor cursor;

            if (values != null)
            {
                String query;

                // check if has condition is tru
                // if yes the conditional values should not be null
                if (hasConditions)
                {
                    // check ig conditional values is passed
                    // it should be of string builder type
                    // where user has to pass values to be passed in where clause
                    //
                    // FOR EX: firstName = 'FirstNameValue' OR
                    //         firstName LIKE %Term to be searched%
                    if (conditionalValues != null)
                    {
                        // building conditional query
                        query = "SELECT " + values + " FROM " + tableName + " WHERE " + conditionalValues.toString() + "";
                        Log.e(TAG, "executeSelectQuery: Select query with conditions is: " + query);
                    }
                    else
                    {
                        Log.e(TAG, "executeSelectQuery: conditional values is null.");
                        return null;
                    }
                }
                else
                {
                    // building non conditional values
                    query = "SELECT " + values + " FROM " + tableName;
                    Log.e(TAG, "executeSelectQuery: Select query without conditions is: " + query);
                }

                // executing query
                cursor = db.getWritableDatabase().rawQuery(query, null);

                // if cursor is not null then moving the position to first
                // and returning the cursor
                if (cursor != null)
                {
                    cursor.moveToFirst();
                }
                else
                {
                    Log.e(TAG, "executeSelectQuery: cursor was null. No data found.");
                    return null;
                }
            }
            else
            {
                Log.e(TAG, "executeSelectQuery: values was null for select query.");
                return null;
            }

            return cursor;
        }
        catch (Exception e)
        {
            Log.e(TAG, "executeSelectQuery: in database helper class:\n");
            e.printStackTrace();
            return null;
        }
    }

    //#region COMMENTS FOR executeSelectQuery method

    /**
     * execute Select Query method
     * this method is used for selecting data from database
     * <p>
     * parameters for this method are
     *
     * @param tableName         - name of the table to perform select operation
     * @param values            - values to perform select query on
     *                          Ex: "*" or "id, firstName"
     * @param hasConditions     - if you want to use the where clause in select query
     *                          then this parameter should be set to true
     *                          else this parameter can be false
     * @param conditionalValues - if the hasConditions is set to true
     *                          then the user has to pass conditionalValues
     *                          else it can be null
     *                          Ex: ID = 1, firstName LIKE '%Your String%'
     * @param tClass            - Pass your Model class like this
     *                          Ex: ModelClass.class
     *                          this is required for setting the values
     * @return ArrayList of Type pass as class
     **/
    //#endregion COMMENTS FOR executeSelectQuery method
    public <T> ArrayList<T> executeSelectQuery(String tableName, String values,
                                               boolean hasConditions,
                                               String conditionalValues, Class<T> tClass)
    {
        try
        {
            Cursor cursor;
            ArrayList<T> tArrayList = new ArrayList<>();

            if (values != null)
            {
                String query;

                // check if has condition is tru
                // if yes the conditional values should not be null
                if (hasConditions)
                {
                    // check if conditional values is passed
                    // it should be of string builder type
                    // where user has to pass values to be passed in where clause
                    //
                    // FOR EX: firstName = 'FirstNameValue' OR
                    //         firstName LIKE %Term to be searched%
                    if (conditionalValues != null)
                    {
                        // generating query with conditions
                        query = "SELECT " + values + " FROM " + tableName + " WHERE " + conditionalValues;
                        Log.e(TAG, "executeSelectQuery: Select query with conditions is: " + query);
                    }
                    else
                    {
                        // conditional values was not passed
                        Log.e(TAG, "executeSelectQuery: conditional values is null.");
                        return null;
                    }
                }
                else
                {
                    // generating query without conditions
                    query = "SELECT " + values + " FROM " + tableName;
                    Log.e(TAG, "executeSelectQuery: Select query without conditions is: " + query);
                }

                // executing query
                cursor = db.getWritableDatabase().rawQuery(query, null);

                // if cursor is not null then moving the position to first
                // and returning the cursor
                if (cursor != null && cursor.moveToFirst())
                {
                    //#region LOOP FOR EXTRACTING DATA FROM DATABASE
                    for (int i = 0; i < cursor.getCount(); i++)
                    {
                        // setting new instance of the class passed
                        // for invoking the values returned from database
                        T instance = tClass.newInstance();

                        //#region LOOP FOR COUNT OF COLUMNS
                        for (int j = 0; j < cursor.getColumnCount(); j++)
                        {
                            try
                            {
                                //#region LOOP FOR GETTING ALL USER DECLARED METHODS
                                for (Method method : tClass.getDeclaredMethods())
                                {
                                    // getting column name from database
                                    String columnName = cursor.getColumnName(j).toLowerCase();

                                    // getting name of the methods which are user declared or created
                                    String methodName = method.getName().toLowerCase();

                                    // checking for set method only for setting the value
                                    // with prefix set followed by the name of column from database
                                    if (methodName.contains("set" + columnName))
                                    {
                                        // getting name of the methods which are user declared or created
                                        // with parameter types for setting value
                                        method = tClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
                                        String parameterType = method.getParameterTypes()[0].toString();

                                        // checking if parameter type is int
                                        if (int.class == method.getParameterTypes()[0])
                                        {
                                            // getting int value from database
                                            method.invoke(instance, cursor.getInt(j));
                                        }
                                        // checking if parameter type is boolean
                                        else if (boolean.class == method.getParameterTypes()[0])
                                        {
                                            // getting string value from database
                                            method.invoke(instance, cursor.getString(j));
                                        }
                                        // checking if parameter type is float
                                        else if (float.class == method.getParameterTypes()[0])
                                        {
                                            // getting float value from database
                                            method.invoke(instance, cursor.getFloat(j));
                                        }
                                        // checking if parameter type is double
                                        else if (double.class == method.getParameterTypes()[0])
                                        {
                                            // getting double value from database
                                            method.invoke(instance, String.valueOf(cursor.getDouble(j)));
                                        }
                                        // checking if parameter type is byte array
                                        else if (byte[].class == method.getParameterTypes()[0])
                                        {
                                            method.invoke(instance, cursor.getBlob(j));
                                        }
                                        // any other data type will be get string from database
                                        else
                                        {
                                            // getting string value from database
                                            method.invoke(instance, String.valueOf(cursor.getString(j)));
                                        }
                                    }
                                }
                                //#region LOOP FOR GETTING ALL USER DECLARED METHODS
                            }
                            catch (Exception e)
                            {
                                Log.e(TAG, "executeSelectQuery: exception while type casting:\n");
                                e.printStackTrace();
                            }
                        }
                        //#endregion LOOP FOR COUNT OF COLUMNS

                        tArrayList.add(instance);
                        cursor.moveToNext();
                    }
                    //#endregion LOOP FOR EXTRACTING DATA FROM DATABASE

                    cursor.close();
                    return tArrayList;
                }
                else
                {
                    Log.e(TAG, "executeSelectQuery: cursor was null. No data found.");
                    return null;
                }
            }
            else
            {
                Log.e(TAG, "executeSelectQuery: values was null for select query.");
                return null;
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "executeSelectQuery: in database helper class:\n");
            e.printStackTrace();

            return null;
        }
    }

    // region COMMENTS FOR getRecordCount method

    /**
     * 2018 Feb 02 - Friday - 01:36 PM
     * Get Record Count
     * <p>
     * this method gets the count of the records in the table
     * <p>
     * parameters to be passed are as follows
     * *********************************************************************************************
     * <p>
     * ** @param tableName - pass the name of the table on which you have to perform the operation
     * <p>
     * ** @param values - pass either * or just a single name of the field of that table
     * <p>
     * ** @param hasConditions - if you want to get the count of a single record with some conditions
     * then set this as true else it will be false.
     * <p>
     * if this parameter is set to true then
     * conditional values has to be provided else it won't work.
     * <p>
     * ** @param conditionalValues - pass conditional values in this liked hash map
     * it can be null if hasConditions is set to false
     * if hasConditions is set to true then this param
     * has to be passed.
     * <p>
     * *********************************************************************************************
     * <p>
     * ** @return this method will return the count of the record in the table
     **/
    // endregion COMMENTS FOR getRecordCount method
    public int getRecordCount(String tableName, String values,
                              boolean hasConditions, StringBuilder conditionalValues)
    {
        try
        {
            String query;

            // check if has condition is true
            // if yes then conditional values should be passed
            if (hasConditions)
            {
                // checking if conditional values is not null
                // if yes then then building query with conditions
                if (conditionalValues != null)
                {
                    // building conditional query
                    query = "SELECT COUNT(" + values + ") FROM " + tableName + " WHERE " + conditionalValues.toString() + "";
                    Log.e(TAG, "getRecordCount: query with condition is: " + query);
                }
                else
                {
                    // building non conditional query
                    Log.e(TAG, "getRecordCount: conditional value was null.");
                    return 0;
                }
            }
            else
            {
                query = "SELECT COUNT(" + values + ") FROM " + tableName + "";
                Log.e(TAG, "getRecordCount: query without condition is: " + query);
            }

            if (!query.equalsIgnoreCase(""))
            {
                return db.getRecordCount(query);
            }
            else
            {
                Log.e(TAG, "getRecordCount: query was not generated.");
                return 0;
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "getRecordCount: in database helper class:\n");
            e.printStackTrace();
            return 0;
        }
    }

    //#region COMMENTS FOR isTableExists method

    /**
     * 2018 September 07 - Friday - 05:39 PM
     * is table exists method
     * <p>
     * this method will check if a table exists in database
     * if yes is will not execute create table query
     * else it will execute
     *
     * @param tableName - name of the table to check if that table exists or not
     * @return true - if table exists in database
     * false - if table not exists in database
     **/
    //#endregion COMMENTS FOR isTableExists method
    public boolean isTableExists(String tableName)
    {
        try
        {
            // query for checking if table exists in database
            String query = "SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '" + tableName + "'";

            // executing the query using cursor
            Cursor cursor = db.getWritableDatabase().rawQuery(query, null);

            // checking if cursor is not null
            if (cursor != null)
            {
                // cursor is not null, moving the cursor position to first
                cursor.moveToFirst();

                // getting the count from cursor
                int count = cursor.getCount();

                // closing the cursor
                cursor.close();

                // returning true if table exists in database
                // else false if table does not exists in database
                return count > 0;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "isTableExists: exception in is table exists method:\n");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 2019 November 19 - Tuesday - 12:49 PM
     * check if column exists method
     * <p>
     * this method will check if a column in a table exists or not
     *
     * @param tableName  - table name to check for column name
     * @param columnName - column name to check if exists or not
     * @return returns true if column exists in table or false if not
     **/
    public boolean checkIsColumnExists(String tableName, String columnName)
    {
        try
        {
            boolean isExists = false;

            String query = "PRAGMA TABLE_INFO('" + tableName + "')";
            Cursor cursor = db.getWritableDatabase().rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst())
            {
                for (int i = 0; i < cursor.getCount(); i++)
                {
                    String currentColumnName = cursor.getString(cursor.getColumnIndex("name"));

                    if (currentColumnName.equalsIgnoreCase(columnName))
                    {
                        isExists = true;
                        Log.e(TAG, "checkIsColumnExists: " + columnName + " found in table " + tableName);
                    }

                    cursor.moveToNext();
                }

                cursor.close();
            }

            return isExists;
        }
        catch (Exception e)
        {
            Log.e(TAG, "checkIsColumnExists: exception while checking if column exists or not\n");
            e.printStackTrace();

            return false;
        }
    }

    //#region COMMENTS FOR getMaxId method

    /**
     * 2018 August 13 - Monday - 12:34 PM
     * get max field method
     * <p>
     * this method will get max value of the field in the table
     *
     * @param field     - primary key of the table to get the max value or
     *                  an integer field of the table to get the max value
     * @param tableName - table name from where you need to get max value
     * @return - max value of the field passed
     **/
    //#endregion COMMENTS FOR getMaxId method
    public int getMaxId(String field, String tableName)
    {
        try
        {
            if (field != null && field.length() != 0)
            {
                if (tableName.length() == 0)
                {
                    Log.e(TAG, "getMaxId: table name is required which was not passed");
                    return 0;
                }

                String query = "SELECT MAX(" + field + ") AS ID FROM " + tableName;
                Cursor cursor = db.getWritableDatabase().rawQuery(query, null);

                if (cursor != null)
                {
                    cursor.moveToFirst();
                    int id = cursor.getInt(cursor.getColumnIndex("ID"));

                    cursor.close();
                    return id;
                }
                else
                {
                    return 0;
                }
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "getMaxId: exception while getting max field from table:\n");
            e.printStackTrace();
            return 0;
        }
    }

    //#region COMMENTS FOR executeQuery method

    /**
     * 2018 August 20 - Monday - 04:01 PM
     * execute query method
     * <p>
     * this method is used to execute a query
     * this method will return true if the query is executed successfully
     *
     * @param query - query that you want to execute without getting any particular result.
     * @return - true if query was successful
     * false if query was not successful.
     **/
    //#endregion COMMENTS FOR executeQuery method
    public boolean executeQuery(String query)
    {
        try
        {
            if (query != null && !query.equalsIgnoreCase(""))
            {
                db.getWritableDatabase().execSQL(query);

                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, "executeQuery: exception while executing query:\n");
            ex.printStackTrace();
            return false;
        }
    }

    //#region COMMENTS FOR addColumnForTable method

    /**
     * 2019 January 08 - Tuesday - 03:32 PM
     * add column for table method
     *
     * @param dbColumn - Db Column is a model class
     *                 used for defining column name and data types
     *                 <p>
     *                 this method will help user for adding the columns to the table
     **/
    //#endregion COMMENTS FOR addColumnForTable method
    public DBHelper addColumnForTable(DbColumn dbColumn)
    {
        // checking if db columns was provided or not
        if (dbColumn == null || dbColumn.toString().isEmpty())
        {
            Log.e(TAG, "addColumnForTable: Db Column was null or empty, Cannot proceed with null or empty Db Column.");
            dbColumnArrayList = new ArrayList<>();
            return this;
        }

        dbColumnArrayList.add(dbColumn);
        return this;
    }

    //#region COMMENTS FOR addDataForTable method

    /**
     * 2019 January 08 - Tuesday - 03:32 PM
     * add column for table method
     *
     * @param dbData - Db data is a model class
     *               used for defining column name and data for the columns
     *               <p>
     *               this method will help in adding data to the table for respective column name in the table
     **/
    //#endregion COMMENTS FOR addDataForTable method
    public DBHelper addDataForTable(DbData dbData)
    {
        // checking if data was provided or not
        if (dbData == null || dbData.toString().isEmpty())
        {
            Log.e(TAG, "addDataForTable: Db Data was null or empty, Cannot proceed with null or empty Db Data.");
            dbDataArrayList = new ArrayList<>();
            return this;
        }

        dbDataArrayList.add(dbData);
        return this;
    }

    //#region COMMENTS FOR createTable method

    /**
     * 2019 January 08 - Tuesday - 03:52 PM
     * create table method
     *
     * @param tableName - name of the table which is to be created
     *                  <p>
     *                  this method is responsible for creating the table
     *                  with the name and columns and data types provided
     **/
    //#endregion COMMENTS FOR createTable method
    public DBHelper createTable(String tableName)
    {
        // checking if table name was provided or not
        if (tableName == null || tableName.isEmpty())
        {
            if (dbColumnArrayList != null)
            {
                dbColumnArrayList.clear();
            }

            Log.e(TAG, "createTable: Table name was null or empty.");
            return this;
        }

        // checking if columns were provided or not for creating table
        if (dbColumnArrayList == null || dbColumnArrayList.size() == 0)
        {
            Log.e(TAG, "createTable: No columns provided for creating table.");
            return this;
        }

        // string builder for generating create table query
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");

        // loop for getting all the columns and their respective data types
        for (int i = 0; i < dbColumnArrayList.size(); i++)
        {
            query.append(dbColumnArrayList.get(i).columnName)
                    .append(" ")
                    .append(dbColumnArrayList.get(i).columnDataType);

            // checking if the loop is at the end of all the columns added
            // if yes the appending brackets
            // else appending comma for separating two columns
            if (i == dbColumnArrayList.size() - 1)
            {
                query.append(")");
            }
            else
            {
                query.append(" , ");
            }
        }

        Log.e(TAG, "createTable: Create table query is: " + query.toString());

        db.getWritableDatabase().execSQL(query.toString());
        dbColumnArrayList = new ArrayList<>();

        return this;
    }

    //#region COMMENTS FOR alterTable method

    /**
     * 2019 April 15 - Monday - 01:13 PM
     * alter table method
     *
     * @param tableName - name of the table where column is to be added
     *                  <p>
     *                  this method will alter the table and will add new column to the table
     **/
    //#endregion COMMENTS FOR alterTable method
    public DBHelper alterTable(String tableName)
    {
        try
        {
            if (dbColumnArrayList == null || dbColumnArrayList.size() == 0)
            {
                Log.e(TAG, "alterTable: No Db Columns were provided.");
                return this;
            }

            for (int i = 0; i < dbColumnArrayList.size(); i++)
            {
                String columnName = dbColumnArrayList.get(i).columnName;
                String columnDataType = dbColumnArrayList.get(i).columnDataType;

                String query = "SELECT COUNT(*) FROM pragma_table_info('" + tableName + "') " +
                        "WHERE name = '" + columnName + "'";

                int count = db.getRecordCount(query);

                if (count == 0)
                {
                    query = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnDataType;
                    Log.e(TAG, "alterTable: query for adding new column or altering table is: " + query);

                    db.getWritableDatabase().execSQL(query);
                }
                else
                {
                    Log.e(TAG, "alterTable: " + columnName + " already exists in " + tableName);
                }
            }

            dbColumnArrayList = new ArrayList<>();
        }
        catch (Exception e)
        {
            dbColumnArrayList = new ArrayList<>();

            Log.e(TAG, "alterTable: exception while altering table:\n");
            e.printStackTrace();
        }

        return this;
    }

    //#region COMMENTS FOR insertData method

    /**
     * 2019 January 08 - Tuesday - 04:03 PM
     * insert data method
     *
     * @param tableName - name of the table for inserting records
     *                  <p>
     *                  this method is used for inserting records into table
     **/
    //#endregion COMMENTS FOR insertData method
    public DBHelper insertData(String tableName)
    {
        try
        {
            // checking if table name was provided or not
            if (tableName == null || tableName.isEmpty())
            {
                if (dbDataArrayList != null)
                {
                    dbDataArrayList.clear();
                }

                Log.e(TAG, "insertData: Table name was null or empty.");
                return this;
            }

            // checking if data was provided or not for inserting in database
            if (dbDataArrayList == null || dbDataArrayList.size() == 0)
            {
                Log.e(TAG, "insertData: No data provided for inserting.");
                return this;
            }

            // content values for putting column name
            // and data for inserting into database table
            ContentValues contentValues = new ContentValues();

            // loop for no of data to be inserted
            for (int i = 0; i < dbDataArrayList.size(); i++)
            {
                if (dbDataArrayList.get(i).imageData != null && dbDataArrayList.get(i).imageData.length > 0)
                {
                    // adding column names and column data into content values
                    contentValues.put(dbDataArrayList.get(i).columnName, dbDataArrayList.get(i).imageData);
                }
                else
                {
                    // adding column names and column data into content values
                    contentValues.put(dbDataArrayList.get(i).columnName, dbDataArrayList.get(i).columnData.toString());
                }
            }

            // executing inserting statement for inserting records in table
            db.getWritableDatabase().insert(tableName, null, contentValues);
            dbDataArrayList = new ArrayList<>();
        }
        catch (Exception e)
        {
            dbDataArrayList = new ArrayList<>();

            Log.e(TAG, "insertData: exception while inserting data into table: " + tableName + ":\n");
            e.printStackTrace();
        }

        return this;
    }

    //#region COMMENTS FOR insertData method

    /**
     * 2019 January 08 - Tuesday - 04:03 PM
     * insert data with return id method
     * <p>
     * this method is used for inserting records into table
     *
     * @param tableName - name of the table for inserting records
     * @return inserted row id if successful or -1 if not inserted
     **/
    //#endregion COMMENTS FOR insertData method
    public long insertDataWithReturnId(String tableName)
    {
        try
        {
            // checking if table name was provided or not
            if (tableName == null || tableName.isEmpty())
            {
                if (dbDataArrayList != null)
                {
                    dbDataArrayList.clear();
                }

                Log.e(TAG, "insertData: Table name was null or empty.");
                return -1;
            }

            // checking if data was provided or not for inserting in database
            if (dbDataArrayList == null || dbDataArrayList.size() == 0)
            {
                Log.e(TAG, "insertData: No data provided for inserting.");
                return -1;
            }

            // content values for putting column name
            // and data for inserting into database table
            ContentValues contentValues = new ContentValues();

            // loop for no of data to be inserted
            for (int i = 0; i < dbDataArrayList.size(); i++)
            {
                if (dbDataArrayList.get(i).imageData != null && dbDataArrayList.get(i).imageData.length > 0)
                {
                    // adding column names and column data into content values
                    contentValues.put(dbDataArrayList.get(i).columnName, dbDataArrayList.get(i).imageData);
                }
                else
                {
                    // adding column names and column data into content values
                    contentValues.put(dbDataArrayList.get(i).columnName, dbDataArrayList.get(i).columnData.toString());
                }
            }

            // executing inserting statement for inserting records in table
            long insertedId = db.getWritableDatabase().insert(tableName, null, contentValues);
            dbDataArrayList = new ArrayList<>();

            return insertedId;
        }
        catch (Exception e)
        {
            dbDataArrayList = new ArrayList<>();

            Log.e(TAG, "insertDataWithReturnId: exception while inserting data with return id for table: " + tableName + ":\n");
            e.printStackTrace();

            return 0L;
        }
    }

    //#region COMMENTS FOR insertDataWithTransaction method

    /**
     * 2019 January 09 - Wednesday - 06:49 PM
     * insert data with transaction method
     *
     * @param tableName - name of the table where the data is to be inserted
     *                  <p>
     *                  this method will insert data into table using database transaction
     *                  this method is useful for inserting bulk records into table in less time
     **/
    //#endregion COMMENTS FOR insertDataWithTransaction method
    @Deprecated
    public void insertDataWithTransaction(String tableName)
    {
        try
        {
            if (dbDataArrayList == null || dbDataArrayList.size() == 0)
            {
                Log.e(TAG, "insertDataWithTransaction: Db Data was not provided. Cannot insert data in table.");
                return;
            }

            // tree set is used for removing duplicate column name from data array list
            TreeSet<String> treeSet = new TreeSet<>();

            // this array list will hold unique column name from data array list
            ArrayList<String> columnsArrayList = new ArrayList<>();

            // loop for removing duplicate values from data array list
            // for (int i = 0; i < dbDataArrayList.size(); i++)
            for (int i = 0; i < dbDataArrayList.size(); i++)
            {
                for (DbData item : dbDataArrayList)
                {
                    // checking if tree set contains columns from data array list
                    // if not contains then adding it to columns array list
                    if (!treeSet.contains(item.columnName))
                    {
                        // column name not present in columns array list
                        // adding to columns array list and tree set
                        treeSet.add(item.columnName);
                        columnsArrayList.add(item.columnName);
                    }
                }
            }

            // getting columns count for generating insert query
            // and inserting records into corresponding columns
            int columnCount = columnsArrayList.size();

            // this string builder is used to append names of columns for the query
            // for saving records into corresponding columns
            StringBuilder queryBuilder = new StringBuilder();

            // this string builder is used to append indexes for the query
            StringBuilder indexesBuilder = new StringBuilder();

            // generating insert query
            queryBuilder.append("INSERT INTO ").append(tableName).append(" (");
            indexesBuilder.append(" VALUES (");

            // loop for generating insert query with columns name and indexes
            for (int i = 0; i < columnCount; i++)
            {
                indexesBuilder.append("?");
                queryBuilder.append(columnsArrayList.get(i));

                // checking if column's count is equals to i
                // if yes then appending brackets
                // else appending comma
                if (i == columnCount - 1)
                {
                    queryBuilder.append(")");
                    indexesBuilder.append(")");
                }
                else
                {
                    queryBuilder.append(" , ");
                    indexesBuilder.append(" , ");
                }
            }

            // this is final query
            String query = queryBuilder.toString() + indexesBuilder.toString();
            Log.e(TAG, "insertDataWithTransaction: Insert query with transaction is: " + query);

            // starting database transaction for inserting records
            db.getWritableDatabase().beginTransaction();

            // compiling insert query with indexes
            SQLiteStatement statement = db.getWritableDatabase().compileStatement(query);

            // this position is used for SQLite statement
            // for binding data with columns
            int position = 0;

            // loop for inserting records with statement
            for (int i = 0; i <= dbDataArrayList.size(); i++)
            {
                // checking if position is equals to column count
                // this check will make sure that only those records get inserted
                // for which the columns are passed
                // irrespective of no of columns table has
                // if yes then executing the statement
                if (position == columnCount)
                {
                    position = 0;
                    statement.execute();
                    statement.clearBindings();
                }

                // checking if i is equals to data array list's size
                // if yes then breaking loop so the below code is not executed
                // this check will ensure that last records are inserted
                // and no index out of bound exception occurs
                if (i == dbDataArrayList.size())
                {
                    continue;
                }

                // increasing the position value by 1 for mapping data with column
                position += 1;

                // retrieving data from data array list
                Object columnData = dbDataArrayList.get(i).columnData;

                // checking the type of data and binding to corresponding type
                if (columnData instanceof Integer)
                {
                    statement.bindLong(position, Integer.parseInt(columnData.toString()));
                }
                else if (columnData instanceof String)
                {
                    statement.bindString(position, columnData.toString());
                }
                else if (columnData instanceof Double || columnData instanceof Float)
                {
                    statement.bindDouble(position, Double.parseDouble(columnData.toString()));
                }
            }

            db.getWritableDatabase().setTransactionSuccessful();
            db.getWritableDatabase().endTransaction();

            dbDataArrayList = new ArrayList<>();
        }
        catch (Exception e)
        {
            Log.e(TAG, "insertDataWithTransaction: exception while inserting records with transaction into table: " + tableName + ":\n");
            e.printStackTrace();

            if (db.getWritableDatabase().inTransaction())
            {
                db.getWritableDatabase().setTransactionSuccessful();
                db.getWritableDatabase().endTransaction();
            }

            dbDataArrayList = new ArrayList<>();
        }
    }

    //#region COMMENTS FOR insertDataWithTransaction method

    /**
     * 2019 January 09 - Wednesday - 06:49 PM
     * insert data with transaction method
     *
     * @param tableName     - name of the table where the data is to be inserted
     * @param dbColumnCount - total number of columns in the table you want to insert data
     *                      <p>
     *                      this method will insert data into table using database transaction
     *                      this method is useful for inserting bulk records into table in less time
     **/
    //#endregion COMMENTS FOR insertDataWithTransaction method
    public void insertDataWithTransaction(String tableName, int dbColumnCount)
    {
        try
        {
            if (dbDataArrayList == null || dbDataArrayList.size() == 0)
            {
                Log.e(TAG, "insertDataWithTransaction: Db Data was not provided. Cannot insert data in table.");
                return;
            }

            // tree set is used for removing duplicate column name from data array list
            TreeSet<String> treeSet = new TreeSet<>();

            // this array list will hold unique column name from data array list
            ArrayList<String> columnsArrayList = new ArrayList<>();

            // loop for removing duplicate values from data array list
            // for (int i = 0; i < dbDataArrayList.size(); i++)
            for (int i = 0; i < dbColumnCount; i++)
            {
                for (DbData item : dbDataArrayList)
                {
                    if (columnsArrayList.size() == dbColumnCount)
                    {
                        break;
                    }

                    // checking if tree set contains columns from data array list
                    // if not contains then adding it to columns array list
                    if (!treeSet.contains(item.columnName))
                    {
                        // column name not present in columns array list
                        // adding to columns array list and tree set
                        treeSet.add(item.columnName);
                        columnsArrayList.add(item.columnName);
                    }
                }
            }

            // getting columns count for generating insert query
            // and inserting records into corresponding columns
            int columnCount = columnsArrayList.size();

            // this string builder is used to append names of columns for the query
            // for saving records into corresponding columns
            StringBuilder queryBuilder = new StringBuilder();

            // this string builder is used to append indexes for the query
            StringBuilder indexesBuilder = new StringBuilder();

            // generating insert query
            queryBuilder.append("INSERT INTO ").append(tableName).append(" (");
            indexesBuilder.append(" VALUES (");

            // loop for generating insert query with columns name and indexes
            for (int i = 0; i < columnCount; i++)
            {
                indexesBuilder.append("?");
                queryBuilder.append(columnsArrayList.get(i));

                // checking if column's count is equals to i
                // if yes then appending brackets
                // else appending comma
                if (i == columnCount - 1)
                {
                    queryBuilder.append(")");
                    indexesBuilder.append(")");
                }
                else
                {
                    queryBuilder.append(" , ");
                    indexesBuilder.append(" , ");
                }
            }

            // this is final query
            String query = queryBuilder.toString() + indexesBuilder.toString();
            Log.e(TAG, "insertDataWithTransaction: Insert query with transaction is: " + query);

            // starting database transaction for inserting records
            db.getWritableDatabase().beginTransaction();

            // compiling insert query with indexes
            SQLiteStatement statement = db.getWritableDatabase().compileStatement(query);

            // this position is used for SQLite statement
            // for binding data with columns
            int position = 0;

            // loop for inserting records with statement
            for (int i = 0; i <= dbDataArrayList.size(); i++)
            {
                // checking if position is equals to column count
                // this check will make sure that only those records get inserted
                // for which the columns are passed
                // irrespective of no of columns table has
                // if yes then executing the statement
                if (position == columnCount)
                {
                    position = 0;
                    statement.execute();
                    statement.clearBindings();
                }

                // checking if i is equals to data array list's size
                // if yes then breaking loop so the below code is not executed
                // this check will ensure that last records are inserted
                // and no index out of bound exception occurs
                if (i == dbDataArrayList.size())
                {
                    continue;
                }

                // increasing the position value by 1 for mapping data with column
                position += 1;

                // retrieving data from data array list
                Object columnData = dbDataArrayList.get(i).columnData;

                // checking the type of data and binding to corresponding type
                if (columnData instanceof Integer)
                {
                    statement.bindLong(position, Integer.parseInt(columnData.toString()));
                }
                else if (columnData instanceof String)
                {
                    statement.bindString(position, columnData.toString());
                }
                else if (columnData instanceof Double || columnData instanceof Float)
                {
                    statement.bindDouble(position, Double.parseDouble(columnData.toString()));
                }
            }

            db.getWritableDatabase().setTransactionSuccessful();
            db.getWritableDatabase().endTransaction();

            dbDataArrayList = new ArrayList<>();
        }
        catch (Exception e)
        {
            Log.e(TAG, "insertDataWithTransaction: exception while inserting records with transaction in table: " + tableName + ":\n");
            e.printStackTrace();

            if (db.getWritableDatabase().inTransaction())
            {
                db.getWritableDatabase().setTransactionSuccessful();
                db.getWritableDatabase().endTransaction();
            }

            dbDataArrayList = new ArrayList<>();
        }
    }

    //#region COMMENTS FOR insertDataWithJson method

    /**
     * 2019 Apr 25 - Thursday - 12:25 PM
     * insert data with json method
     * <p>
     * this method will insert data using JSON Array or JSON Object
     *
     * @param tableName - name of the table to insert data in
     * @param object    - JSON Object or JSON Array of records and columns to be inserted
     * @return True or False for success for failure in inserting records
     **/
    //#endregion COMMENTS FOR insertDataWithJson method
    public boolean insertDataWithJson(String tableName, Object object)
    {
        try
        {
            JSONArray jsonArray = new JSONArray();

            if (object == null)
            {
                Log.e(TAG, "insertData: object value cannot be null.");
                return false;
            }

            if (object instanceof ArrayList)
            {
                Log.e(TAG, "insertDataWithJson: cannot parse array list, you can use json object or json array.");
                return false;
            }

            if (object instanceof JSONObject)
            {
                Iterator<String> iterator = ((JSONObject) object).keys();

                while (iterator.hasNext())
                {
                    String key = iterator.next();
                    jsonArray = ((JSONObject) object).getJSONArray(key);

                    Log.e(TAG, "insertData: json array for " + key + " is: " + jsonArray);
                }
            }
            else if (object instanceof JSONArray)
            {
                jsonArray = (JSONArray) object;
            }

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Iterator<String> iterator = jsonObject.keys();

                while (iterator.hasNext())
                {
                    String columnName = iterator.next();
                    String columnData = jsonObject.getString(columnName);

                    this.addDataForTable(new DbData(columnName, columnData));
                }
            }

            this.insertData(tableName);
            return true;
        }
        catch (Exception e)
        {
            Log.e(TAG, "insertData: exception while inserting data using json:\n");
            e.printStackTrace();

            return false;
        }
    }

    //#region COMMENTS FOR insertDataWithJson method

    /**
     * 2019 Apr 25 - Thursday - 12:25 PM
     * insert data with json with return id method
     * <p>
     * this method will insert data using JSON Array or JSON Object
     *
     * @param tableName - name of the table to insert data in
     * @param object    - JSON Object or JSON Array of records and columns to be inserted
     * @return True or False for success for failure in inserting records
     **/
    //#endregion COMMENTS FOR insertDataWithJson method
    public long insertDataWithJsonWithReturnId(String tableName, Object object)
    {
        try
        {
            JSONArray jsonArray = new JSONArray();

            if (object == null)
            {
                Log.e(TAG, "insertData: object value cannot be null.");
                return -1;
            }

            if (object instanceof ArrayList)
            {
                Log.e(TAG, "insertDataWithJson: cannot parse array list, you can use json object or json array.");
                return -1;
            }

            if (object instanceof JSONObject)
            {
                Iterator<String> iterator = ((JSONObject) object).keys();

                while (iterator.hasNext())
                {
                    String key = iterator.next();
                    jsonArray = ((JSONObject) object).getJSONArray(key);

                    Log.e(TAG, "insertData: json array for " + key + " is: " + jsonArray);
                }
            }
            else if (object instanceof JSONArray)
            {
                jsonArray = (JSONArray) object;
            }

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Iterator<String> iterator = jsonObject.keys();

                while (iterator.hasNext())
                {
                    String columnName = iterator.next();
                    String columnData = jsonObject.getString(columnName);

                    this.addDataForTable(new DbData(columnName, columnData));
                }
            }

            return this.insertDataWithReturnId(tableName);
        }
        catch (Exception e)
        {
            Log.e(TAG, "insertData: exception while inserting data using json:\n");
            e.printStackTrace();

            return -1;
        }
    }

    //#region COMMENTS FOR insertDataWithJsonAndTransaction method

    /**
     * 2019 Apr 25 - Thursday - 12:25 PM
     * insert data with json method
     * <p>
     * this method will insert data using JSON Array or JSON Object
     * this method will user SQLite Database Transaction for inserting records in db
     *
     * @param tableName        - name of the table to insert data in
     * @param object           - JSON Object or JSON Array of records and columns to be inserted
     * @param tableColumnCount - Count of Number of columns for that table
     * @return True or False for success for failure in inserting records
     **/
    //#endregion COMMENTS FOR insertDataWithJsonAndTransaction method
    public boolean insertDataWithJsonAndTransaction(String tableName, Object object, int tableColumnCount)
    {
        try
        {
            JSONArray jsonArray = new JSONArray();

            if (object == null)
            {
                Log.e(TAG, "insertData: object value cannot be null.");
                return false;
            }

            if (object instanceof JSONObject)
            {
                Iterator<String> iterator = ((JSONObject) object).keys();

                while (iterator.hasNext())
                {
                    String key = iterator.next();
                    jsonArray = ((JSONObject) object).getJSONArray(key);

                    // Log.e(TAG, "insertData: json array for " + key + " is: " + jsonArray);
                }
            }
            else if (object instanceof JSONArray)
            {
                jsonArray = (JSONArray) object;
            }

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Iterator<String> iterator = jsonObject.keys();

                while (iterator.hasNext())
                {
                    String columnName = iterator.next();
                    String columnData = jsonObject.getString(columnName);
                    this.addDataForTable(new DbData(columnName, columnData));
                }
            }

            this.insertDataWithTransaction(tableName, tableColumnCount);
            return true;
        }
        catch (Exception e)
        {
            Log.e(TAG, "insertData: exception while inserting data using json:\n");
            e.printStackTrace();

            return false;
        }
    }

    //#region COMMENTS FOR updateData method

    /**
     * 2019 January 08 - Tuesday - 04:28 PM
     * update data method
     *
     * @param tableName   - name of the table on which update query is to be performed
     * @param whereClause - name of the column to check whether the record is present so the data is updated
     *                    pass this parameter in the way given in example below
     *                    Ex: code = ? or ID = ? etc // this is important
     *                    <p>
     *                    this method will update records of the table in database
     *                    this method uses database's update method for updating records
     *                    <p>
     *                    parameter whereClause and whereArgs must be passed in the form given
     **/
    //#endregion COMMENTS FOR updateData method
    public DBHelper updateData(String tableName, String whereClause)
    {
        try
        {
            // checking if table name was provided or not
            if (tableName == null || tableName.isEmpty())
            {
                if (dbDataArrayList != null)
                {
                    dbDataArrayList.clear();
                }

                Log.e(TAG, "updateData: Table name was null or empty.");
                return this;
            }

            // checking if column name was provided or not
            if (whereClause == null || whereClause.isEmpty())
            {
                if (dbDataArrayList != null)
                {
                    dbDataArrayList.clear();
                }

                Log.e(TAG, "updateData: Column name was null or empty.");
                return this;
            }

            // checking if data was provided or not
            if (dbDataArrayList == null || dbDataArrayList.size() == 0)
            {
                Log.e(TAG, "updateData: Data was not provided for updating records.");
                return this;
            }

            // content values for putting column name
            // and data for inserting into database table
            ContentValues contentValues = new ContentValues();

            // loop for no of data provided
            for (int i = 0; i < dbDataArrayList.size(); i++)
            {
                if (dbDataArrayList.get(i).imageData != null && dbDataArrayList.get(i).imageData.length > 0)
                {
                    // adding column names and column data into content values
                    contentValues.put(dbDataArrayList.get(i).columnName, dbDataArrayList.get(i).imageData);
                }
                else
                {
                    // adding column names and column data into content values
                    contentValues.put(dbDataArrayList.get(i).columnName, dbDataArrayList.get(i).columnData.toString());
                }
            }

            // you can directly pass the values to where clause
            db.getWritableDatabase().update(tableName, contentValues, whereClause, null);
            dbDataArrayList = new ArrayList<>();
        }
        catch (Exception e)
        {
            Log.e(TAG, "updateData: exception while updating records in table: " + tableName + ":\n");
            e.printStackTrace();

            dbDataArrayList = new ArrayList<>();
        }

        return this;
    }

    //#region COMMENTS FOR updateData method

    /**
     * 2019 January 08 - Tuesday - 04:28 PM
     * update data method
     *
     * @param tableName   - name of the table on which update query is to be performed
     * @param whereClause - name of the column to check whether the record is present so the data is updated
     *                    pass this parameter in the way given in example below
     *                    Ex: code = ? or ID = ? etc // this is important
     * @param whereArgs   - data of the column name provided to check if record is present for data update
     *                    here you need to pass the data for the corresponding where clause
     *                    Ex: 1 or 2 etc
     *                    <p>
     *                    this method will update records of the table in database
     *                    this method uses database's update method for updating records
     *                    <p>
     *                    parameter whereClause and whereArgs must be passed in the form given
     **/
    //#endregion COMMENTS FOR updateData method
    public DBHelper updateData(String tableName, String whereClause, String whereArgs)
    {
        try
        {
            // checking if table name was provided or not
            if (tableName == null || tableName.isEmpty())
            {
                if (dbDataArrayList != null)
                {
                    dbDataArrayList.clear();
                }

                Log.e(TAG, "updateData: Table name was null or empty.");
                return this;
            }

            // checking if column name was provided or not
            if (whereClause == null || whereClause.isEmpty())
            {
                if (dbDataArrayList != null)
                {
                    dbDataArrayList.clear();
                }

                Log.e(TAG, "updateData: Column name was null or empty.");
                return this;
            }

            // checking if data was provided or not
            if (dbDataArrayList == null || dbDataArrayList.size() == 0)
            {
                Log.e(TAG, "updateData: Data was not provided for updating records.");
                return this;
            }

            // content values for putting column name
            // and data for inserting into database table
            ContentValues contentValues = new ContentValues();

            // loop for no of data provided
            for (int i = 0; i < dbDataArrayList.size(); i++)
            {
                if (dbDataArrayList.get(i).imageData != null && dbDataArrayList.get(i).imageData.length > 0)
                {
                    // adding column names and column data into content values
                    contentValues.put(dbDataArrayList.get(i).columnName, dbDataArrayList.get(i).imageData);
                }
                else
                {
                    // adding column names and column data into content values
                    contentValues.put(dbDataArrayList.get(i).columnName, dbDataArrayList.get(i).columnData.toString());
                }
            }

            // checking if column data was provided or not
            if (whereArgs != null && !whereArgs.isEmpty())
            {
                db.getWritableDatabase().update(tableName, contentValues, whereClause, new String[]{whereArgs});
            }
            else
            {
                // you can directly pass the values to where clause
                db.getWritableDatabase().update(tableName, contentValues, whereClause, null);
            }

            dbDataArrayList = new ArrayList<>();
        }
        catch (Exception e)
        {
            Log.e(TAG, "updateData: exception while updating records in table: " + tableName + ":\n");
            e.printStackTrace();

            dbDataArrayList = new ArrayList<>();
        }

        return this;
    }

    //#region COMMENTS FOR updateData method

    /**
     * 2019 January 08 - Tuesday - 04:28 PM
     * update data with return id method
     *
     * @param tableName   - name of the table on which update query is to be performed
     * @param whereClause - name of the column to check whether the record is present so the data is updated
     *                    pass this parameter in the way given in example below
     *                    Ex: code = ? or ID = ? etc // this is important
     * @param whereArgs   - data of the column name provided to check if record is present for data update
     *                    here you need to pass the data for the corresponding where clause
     *                    Ex: 1 or 2 etc
     *                    <p>
     *                    this method will update records of the table in database
     *                    this method uses database's update method for updating records
     *                    <p>
     *                    parameter whereClause and whereArgs must be passed in the form given
     * @return -1 if failed to update the record
     **/
    //#endregion COMMENTS FOR updateData method
    public long updateDataWithReturnId(String tableName, String whereClause, String whereArgs)
    {
        try
        {
            // checking if table name was provided or not
            if (tableName == null || tableName.isEmpty())
            {
                if (dbDataArrayList != null)
                {
                    dbDataArrayList.clear();
                }

                Log.e(TAG, "updateData: Table name was null or empty.");
                return -1;
            }

            // checking if column name was provided or not
            if (whereClause == null || whereClause.isEmpty())
            {
                if (dbDataArrayList != null)
                {
                    dbDataArrayList.clear();
                }

                Log.e(TAG, "updateData: Column name was null or empty.");
                return -1;
            }

            // checking if data was provided or not
            if (dbDataArrayList == null || dbDataArrayList.size() == 0)
            {
                Log.e(TAG, "updateData: Data was not provided for updating records.");
                return -1;
            }

            // content values for putting column name
            // and data for inserting into database table
            ContentValues contentValues = new ContentValues();

            // loop for no of data provided
            for (int i = 0; i < dbDataArrayList.size(); i++)
            {
                if (dbDataArrayList.get(i).imageData != null && dbDataArrayList.get(i).imageData.length > 0)
                {
                    // adding column names and column data into content values
                    contentValues.put(dbDataArrayList.get(i).columnName, dbDataArrayList.get(i).imageData);
                }
                else
                {
                    // adding column names and column data into content values
                    contentValues.put(dbDataArrayList.get(i).columnName, dbDataArrayList.get(i).columnData.toString());
                }
            }

            long updatedId;

            // checking if column data was provided or not
            if (whereArgs != null && whereArgs.isEmpty())
            {
                updatedId = db.getWritableDatabase().update(tableName, contentValues, whereClause, new String[]{whereArgs});
            }
            else
            {
                // you can directly pass the values to where clause
                updatedId = db.getWritableDatabase().update(tableName, contentValues, whereClause, null);
            }

            dbDataArrayList = new ArrayList<>();

            return updatedId;
        }
        catch (Exception e)
        {
            dbDataArrayList = new ArrayList<>();

            Log.e(TAG, "updateDataWithReturnId: exception while updating records with return id for table: " + tableName + ":\n");
            e.printStackTrace();

            return -1;
        }
    }

    //#region COMMENTS FOR deleteTable method

    /**
     * 2019 January 08 - Tuesday - 04:40 PM
     * delete table method
     *
     * @param tableName - name of the table to be deleted
     *                  <p>
     *                  this method will delete the table from database
     **/
    //#endregion COMMENTS FOR deleteTable method
    public boolean deleteTable(String tableName)
    {
        try
        {
            // checking if table name was provided or not
            if (tableName == null || tableName.isEmpty())
            {
                Log.e(TAG, "deleteTable: Table name was null or empty.");
                return false;
            }

            String query = "DELETE TABLE IF EXISTS " + tableName;

            db.getWritableDatabase().execSQL(query);

            return true;
        }
        catch (Exception e)
        {
            Log.e(TAG, "deleteTable: exception while deleting table:\n");
            e.printStackTrace();
            return false;
        }
    }

    //#region COMMENTS FOR getAllRecords method

    /**
     * 2019 January 08 - Tuesday - 04:44 PM
     * get all records method
     *
     * @param tableName         - name of the table for getting the record
     * @param isAscending       - True for ascending order and False for descending order
     * @param orderByColumnName - name of the column for getting records in descending order
     * @param tClass            - Pass your Model class like this
     *                          Ex: ModelClass.class this is required for setting the values
     * @return Array list of ModelClass you provided in method
     * <p>
     * this method will get all the records from the table
     * in ascending or descending order as provided by the user
     * <p>
     * this method is a generic method which can be directly bounded to the array list of custom type
     * Ex: ArrayList<YourModelClass> arrayList = getAllRecords
     **/
    //#endregion COMMENTS FOR getAllRecords method
    public <T> ArrayList<T> getAllRecords(String tableName, boolean isAscending,
                                          String orderByColumnName, Class<T> tClass)

    {
        try
        {
            Cursor cursor;
            String orderBy;
            ArrayList<T> tArrayList = new ArrayList<>();

            // checking if table name is provided or not
            if (tableName == null || tableName.isEmpty())
            {
                Log.e(TAG, "getAllRecords: Table name was null or empty.");
                return null;
            }

            // checking if order by column name is null or is empty for ascending order
            if (orderByColumnName == null || orderByColumnName.isEmpty())
            {
                Log.e(TAG, "getAllRecords: order by column name was null or empty.");
                return null;
            }

            // checking if isAscending is false
            // and order by column name is not null and not empty for descending order
            if (!isAscending)
            {
                orderBy = " ORDER BY " + orderByColumnName + " DESC";
            }
            else
            {
                orderBy = " ORDER BY " + orderByColumnName + " ASC";
            }

            // checking if model class was provided or not
            // it not then not proceeding further
            if (tClass == null)
            {
                Log.e(TAG, "getAllRecords: Model class parameter was null. Please provide model class parameter");
                return null;
            }

            String query = "SELECT * FROM " + tableName + orderBy;
            Log.e(TAG, "getAllRecords: Select query for getting all records is: " + query);

            // executing generated select query
            cursor = db.getWritableDatabase().rawQuery(query, null);

            // checking if cursor is not null and cursor has moved to first position
            if (cursor != null && cursor.moveToFirst())
            {
                //#region LOOP FOR EXTRACTING DATA FROM DATABASE
                for (int i = 0; i < cursor.getCount(); i++)
                {
                    // setting new instance of the class passed
                    // for invoking the values returned from database
                    T instance = tClass.newInstance();

                    //#region LOOP FOR COUNT OF COLUMNS
                    for (int j = 0; j < cursor.getColumnCount(); j++)
                    {
                        try
                        {
                            //#region LOOP FOR GETTING ALL USER DECLARED METHODS
                            for (Method method : tClass.getDeclaredMethods())
                            {
                                // getting column name from database
                                String columnName = cursor.getColumnName(j).toLowerCase();

                                // getting name of the methods which are user declared or created
                                String methodName = method.getName().toLowerCase();

                                // checking for set method only for setting the value
                                // with prefix set followed by the name of column from database
                                if (methodName.contains("set" + columnName))
                                {
                                    // getting name of the methods which are user declared or created
                                    // with parameter types for setting value
                                    method = tClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
                                    String parameterType = method.getParameterTypes()[0].toString();

                                    // checking if parameter type is int
                                    if (int.class == method.getParameterTypes()[0])
                                    {
                                        // getting int value from database
                                        method.invoke(instance, cursor.getInt(j));
                                    }
                                    // checking if parameter type is boolean
                                    else if (boolean.class == method.getParameterTypes()[0])
                                    {
                                        // getting string value from database
                                        method.invoke(instance, cursor.getString(j));
                                    }
                                    // checking if parameter type is float
                                    else if (float.class == method.getParameterTypes()[0])
                                    {
                                        // getting float value from database
                                        method.invoke(instance, cursor.getFloat(j));
                                    }
                                    // checking if parameter type is double
                                    else if (double.class == method.getParameterTypes()[0])
                                    {
                                        // getting double value from database
                                        method.invoke(instance, String.valueOf(cursor.getDouble(j)));
                                    }
                                    // checking if parameter type is byte array
                                    else if (byte[].class == method.getParameterTypes()[0])
                                    {
                                        method.invoke(instance, cursor.getBlob(j));
                                    }
                                    // any other data type will be get string from database
                                    else
                                    {
                                        // getting string value from database
                                        method.invoke(instance, String.valueOf(cursor.getString(j)));
                                    }
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            Log.e(TAG, "getAllRecords: exception while type casting:\n");
                            e.printStackTrace();
                        }
                    }
                    //#endregion LOOP FOR COUNT OF COLUMNS

                    tArrayList.add(instance);
                    cursor.moveToNext();
                }
                //#endregion LOOP FOR EXTRACTING DATA FROM DATABASE

                cursor.close();
                return tArrayList;
            }
            else
            {
                Log.e(TAG, "getAllRecords: Cursor was null or empty.");
                return null;
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "getAllRecords: exception while getting all records:\n");
            e.printStackTrace();
            return null;
        }
    }

    //#region COMMENTS FOR getAllRecords method

    /**
     * 2019 January 08 - Tuesday - 04:44 PM
     * get all records with conditions method
     *
     * @param tableName         - name of the table for getting the record
     * @param conditionalValues - conditions for selecting records from table in database
     *                          either individual conditions for multiple conditions
     *                          Ex: ID = 1 or code = 1 or firstName = 'FirstName'
     *                          or ID = 1 AND firstName = 'FirstName'
     * @param isAscending       - True for ascending order and False for descending order
     * @param orderByColumnName - name of the column for getting records in descending order
     * @param tClass            - Pass your Model class like this
     *                          Ex: ModelClass.class this is required for setting the values
     * @return Array list of ModelClass you provided in method
     * <p>
     * this method will get all the records from the table
     * in ascending or descending order as provided by the user
     * <p>
     * this method is a generic method which can be directly bounded to the array list of custom type
     * Ex: ArrayList<YourModelClass> arrayList = getAllRecords
     **/
    //#endregion COMMENTS FOR getAllRecordsWithConditions method
    public <T> ArrayList<T> getAllRecordsWithConditions(String tableName, boolean isAscending,
                                                        String conditionalValues,
                                                        String orderByColumnName, Class<T> tClass)
    {
        try
        {
            Cursor cursor;
            String orderBy, whereClause = "";
            ArrayList<T> tArrayList = new ArrayList<>();

            // checking if table name is provided or not
            if (tableName == null || tableName.isEmpty())
            {
                Log.e(TAG, "getAllRecords: Table name was null or empty.");
                return null;
            }

            if (conditionalValues != null && !conditionalValues.isEmpty())
            {
                whereClause = " WHERE " + conditionalValues;
            }
            else
            {
                Log.e(TAG, "getAllRecords: conditional value was null or empty");
                return null;
            }

            // checking if order by column name is null or is empty for ascending order
            if (orderByColumnName == null || orderByColumnName.isEmpty())
            {
                Log.e(TAG, "getAllRecords: order by column name was null or empty.");
                return null;
            }

            // checking if isAscending is false
            // and order by column name is not null and not empty for descending order
            if (!isAscending)
            {
                orderBy = " ORDER BY " + orderByColumnName + " DESC";
            }
            else
            {
                orderBy = " ORDER BY " + orderByColumnName + " ASC";
            }

            // checking if model class was provided or not
            // it not then not proceeding further
            if (tClass == null)
            {
                Log.e(TAG, "getAllRecords: Model class parameter was null. Please provide model class parameter");
                return null;
            }

            String query = "SELECT * FROM " + tableName + whereClause + orderBy;
            Log.e(TAG, "getAllRecords: Select query for getting all records is: " + query);

            // executing generated select query
            cursor = db.getWritableDatabase().rawQuery(query, null);

            // checking if cursor is not null and cursor has moved to first position
            if (cursor != null && cursor.moveToFirst())
            {
                //#region LOOP FOR EXTRACTING DATA FROM DATABASE
                for (int i = 0; i < cursor.getCount(); i++)
                {
                    // setting new instance of the class passed
                    // for invoking the values returned from database
                    T instance = tClass.newInstance();

                    //#region LOOP FOR COUNT OF COLUMNS
                    for (int j = 0; j < cursor.getColumnCount(); j++)
                    {
                        try
                        {
                            //#region LOOP FOR GETTING ALL USER DECLARED METHODS
                            for (Method method : tClass.getDeclaredMethods())
                            {
                                // getting column name from database
                                String columnName = cursor.getColumnName(j).toLowerCase();

                                // getting name of the methods which are user declared or created
                                String methodName = method.getName().toLowerCase();

                                // checking for set method only for setting the value
                                // with prefix set followed by the name of column from database
                                if (methodName.contains("set" + columnName))
                                {
                                    // getting name of the methods which are user declared or created
                                    // with parameter types for setting value
                                    method = tClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
                                    String parameterType = method.getParameterTypes()[0].toString();

                                    // checking if parameter type is int
                                    if (int.class == method.getParameterTypes()[0])
                                    {
                                        // getting int value from database
                                        method.invoke(instance, cursor.getInt(j));
                                    }
                                    // checking if parameter type is boolean
                                    else if (boolean.class == method.getParameterTypes()[0])
                                    {
                                        // getting string value from database
                                        method.invoke(instance, cursor.getString(j));
                                    }
                                    // checking if parameter type is float
                                    else if (float.class == method.getParameterTypes()[0])
                                    {
                                        // getting float value from database
                                        method.invoke(instance, cursor.getFloat(j));
                                    }
                                    // checking if parameter type is double
                                    else if (double.class == method.getParameterTypes()[0])
                                    {
                                        // getting double value from database
                                        method.invoke(instance, String.valueOf(cursor.getDouble(j)));
                                    }
                                    // checking if parameter type is byte array
                                    else if (byte[].class == method.getParameterTypes()[0])
                                    {
                                        method.invoke(instance, cursor.getBlob(j));
                                    }
                                    // any other data type will be get string from database
                                    else
                                    {
                                        // getting string value from database
                                        method.invoke(instance, String.valueOf(cursor.getString(j)));
                                    }
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            Log.e(TAG, "getAllRecords: exception while type casting:\n");
                            e.printStackTrace();
                        }
                    }
                    //#endregion LOOP FOR COUNT OF COLUMNS

                    tArrayList.add(instance);
                    cursor.moveToNext();
                }
                //#endregion LOOP FOR EXTRACTING DATA FROM DATABASE

                cursor.close();
                return tArrayList;
            }
            else
            {
                Log.e(TAG, "getAllRecords: Cursor was null or empty.");
                return null;
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "getAllRecords: exception while getting all records:\n");
            e.printStackTrace();
            return null;
        }
    }
}
