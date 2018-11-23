package com.amit.db;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amit.utilities.SharedPreferenceData;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by AMIT JANGID
 * 2018 Feb 01 - Thursday - 02:55 PM
 *
 * this class has method for executing db queries
 * like: creating table, inserting into table, deleting table, dropping table
**/
@SuppressWarnings({"unused", "unchecked"})
public class DBHelper
{
    private static final String TAG = DBHelper.class.getSimpleName();

    private final Database db;

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
     *
     * This method is an universal method
     * which will perform all the operations of database
     * like - create, delete, insert, update,
     *
     * parameters to be passed in this method are as follows:
     * the parameters below are numbered and has to passed in the same sequence
     *
     * parameter #1:
     * @param tableName - This will be the name of the table on which the operations has to be performed
     *                  - FOR EXAMPLE: tableName = "User"
     *
     * parameter #2:
     * @param values - This parameter is an object of LinkedHashMap
     *               - this parameter will contain set of Key and Value
     *                 to create table or insert data or update we will have to pass this parameter with data
     *               - FOR EXAMPLE: - values.put("Name", "'Amit'") - this for inserting data and updating data
     *                           - values.put("Name", "TEXT") - this for creating table
     *
     * parameter #3
     * @param operations - Details as follows
     ************************************************************************************************
     *** the values to the parameter operations are as follows:
     *
     *** c - for creating new table
     *       - while doing create operation
     *         the user has to pass the data type also with the field of the table
     *         FOR EXAMPLE: values.put("Name", "TEXT") - this for creating table
     *
     *** d - for deleting values from table
     *       - FOR DELETING SINGLE RECORD OR ROW
     *         - the user has to pass true for the parameter "hasConditions"
     *           because delete operation cannot be performed without where clause or condition
     *           so the user has to pass data to "conditionalValues" parameter
     *           - FOR EXAMPLE: hasConditions = true then conditionalValues.put("ID", "'1'")
     *
     *       - FOR DELETING ALL THE RECORDS OR ROWS
     *         - the user has to pass null of values,
     *           false for hasConditions and null of conditionalValues
     *           - FOR EXAMPLE: values = null, hasConditions = false, conditionalValues = null
     *
     *** dr - for dropping the table
     *
     *** i - for inserting values in the table
     *       - while doing insert operation
     *         - the values that the user has to pass is of LinkedHashMap object
     *           read the comments of values parameter for how to pass the values
     *         - the user has to pass the field name and following with the value of the field
     *           if the field is of type string or text,
     *           then the value should be in single quotes ('')
     *           if the field is of type integer,
     *           then the user can pass the value without the quotes or with quotes
     *           - FOR EXAMPLE: values.put("firstName", "'Amit'"),
     *                          values.put("active", "1") or
     *                          values.put("active", "'1'") - better option this way
     *
     *** u - for updating values of table
     *       - while performing update operation
     *         - pass values with field name of the table and following with values
     *         - pass hasConditions value as true and also pass conditionalValues with data
     *         - FOR EXAMPLE: hasConditions = true and conditionalValue.put("ID", "1") OR
     *                        conditionalValues("ID", "'1'") - this option is better
     *                        values.put("firstName", "'Amit'"),
     *                        values.put("email", "'anythiing@gmail.com'")
     *
     ************************************************************************************************
     *
     * parameter #4
     * @param hasConditions - This parameter is used when the user has to perform Update or Delete operations
     *                      - pass true when doing UPDATE OR DELETE operations
     *                        and pass false if not doing UPDATE OR DELETE operations
     *                        for delete operation false can be passed,
     *                        if entire data of the table is to be removed
     *                      - when passing this parameter with true,
     *                        then you have to pass conditionalValues parameter as well
     *
     * parameter #5
     * @param conditionalValues - This parameter is used when the hasConditions parameter is set to true:
     *                          - If the hasConditions parameter is true
     *                            then conditionalValues should have an Object of LinkedHashMap
     *                          - FOR EXAMPLE: conditionalValues.put("ID", "'1'") - for updating data at this "ID"
     *                          - If the hasConditions parameter is false
     *                            then conditionalValues can be null
     *
     * @return true or false
    **/
    // endregion
    public boolean executeDatabaseOperations(String tableName,
                                             String operations,
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
     *
     * parameters for this method are
     *
     * @param query - query that you want to execute
     *
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
     *
     * parameters for this method are
     *
     * @param tableName - name of the table to perform select operation
     *
     * @param values - values to perform select query on
     *
     * @param hasConditions - if you want to use the where clause in select query
     *                        then this parameter should be set to true
     *                        else this parameter can be false
     *
     * @param conditionalValues - if the hasConditions is set to true
     *                            then the user has to pass conditionalValues
     *                            else it can be null
     *
     * the below lines are not in use so ignore it
     *** s - for selecting values from table
     *     - pass * in values parameter when doing select operations
     *       when you want to select every thing from the table
     *       no matter condition is there or not
     *     - pass values parameters with the name of the columns in the table
     *       when you want to select one or multiple columns from the table
     *       no matter condition is there or not
    **/
    // endregion COMMENTS FOR executeSelectQuery method
    public Cursor executeSelectQuery(String tableName,
                                     String values,
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
     *
     * parameters for this method are
     *
     * @param tableName - name of the table to perform select operation
     *
     * @param values - values to perform select query on
     *                 Ex: "*" or "id, firstName"
     *
     * @param hasConditions - if you want to use the where clause in select query
     *                        then this parameter should be set to true
     *                        else this parameter can be false
     *
     * @param conditionalValues - if the hasConditions is set to true
     *                            then the user has to pass conditionalValues
     *                            else it can be null
     *                            Ex: ID = 1, firstName LIKE '%Your String%'
     *
     * @param tClass - Pass your Model class like this
     *                 Ex: ModelClass.class
     *                 this is required for setting the values
     *
     * @return ArrayList of Type pass as class
     *
    **/
    //#endregion COMMENTS FOR executeSelectQuery method
    public <T> ArrayList<T> executeSelectQuery(String tableName,
                                               String values,
                                               boolean hasConditions,
                                               String conditionalValues,
                                               Class<T> tClass)
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
                    // check ig conditional values is passed
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
                        Object instance = tClass.newInstance();

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

                        tArrayList.add((T) instance);
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
     *
     * this method gets the count of the records in the table
     *
     * parameters to be passed are as follows
     * *********************************************************************************************
     *
     *** @param tableName - pass the name of the table on which you have to perform the operation
     *
     *** @param values - pass either * or just a single name of the field of that table
     *
     *** @param hasConditions - if you want to get the count of a single record with some conditions
     *                          then set this as true else it will be false.
     *
     *                          if this parameter is set to true then
     *                          conditional values has to be provided else it won't work.
     *
     *** @param conditionalValues - pass conditional values in this liked hash map
     *                              it can be null if hasConditions is set to false
     *                              if hasConditions is set to true then this param
     *                              has to be passed.
     *
     * *********************************************************************************************
     *
     *** @return this method will return the count of the record in the table
    **/
    // endregion COMMENTS FOR getRecordCount method
    public int getRecordCount(String tableName,
                              String values,
                              boolean hasConditions,
                              StringBuilder conditionalValues)
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
     *
     * this method will check if a table exists in database
     * if yes is will not execute create table query
     * else it will execute
     *
     * @param tableName - name of the table to check if that table exists or not
     *
     * @return true - if table exists in database
     *         false - if table not exists in database
    **/
    //#endregion COMMENTS FOR isTableExists method
    public boolean isTableExists(String tableName)
    {
        try
        {
            // query for checking if table exists in database
            String query = "SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '" + tableName + "'";

            // executing the query using cursor
            Cursor cursor = db.getReadableDatabase().rawQuery(query, null);

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

    //#region COMMENTS FOR getMaxId method
    /**
     * 2018 August 13 - Monday - 12:34 PM
     * get max field method
     *
     * this method will get max value of the field in the table
     *
     * @param field - primary key of the table to get the max value or
     *             an integer field of the table to get the max value
     *
     * @param tableName - table name from where you need to get max value
     *
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
                Cursor cursor = db.getReadableDatabase().rawQuery(query, null);

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
     *
     * this method is used to execute a query
     * this method will return true if the query is executed successfully
     *
     * @param query - query that you want to execute without getting any particular result.
     *
     * @return - true if query was successful
     *           false if query was not successful.
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
}
