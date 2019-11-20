# multiutillib

[![](https://jitpack.io/v/amitjangid80/multiutillib.svg)](https://jitpack.io/#amitjangid80/multiutillib)

## Setup

#### Step 1. Add the JitPack repository to your build file
 
**Add it in your root build.gradle at the end of repositories:**

```
allprojects{
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

#### Step 2. Add the dependency

```
dependencies {
    ...
    implementation 'com.github.amitjangid80:multiutillib:v1.7.21'
}
```

#### Using maven:

```
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

#### Step 2. Add the dependency

```
<dependency>
   <groupId>com.github.amitjangid80</groupId>
   <artifactId>multiutillib</artifactId>
   <version>v1.7.21</version>
<dependency>
```

**Add compileOptions in your gradle file for supporting this library for Java 8.**
```
compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
}
```

>If you get **Manifest merger failed with multiple errors, see logs** error while running or syncing gradle then you just have to create a class which extends **android.support.v4.content.FileProvider**.

```java
Example:

import android.support.v4.content.FileProvider;

public class MyFileProvider extends FileProvider
{

}

// after you have created this file then you have to register this in your manifest file.
Example: 
<provider
    android:name="android.support.v4.content.FileProvider" // replace this line with the line below
    android:name=".MyFileProvider"
    android:authorities="com.example.file_provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
	android:name="android.support.FILE_PROVIDER_PATHS"
	android:resource="@xml/file_provider_path" />
</provider>
```

## Usage

### ProjectApplication Class

>**You can create an Application class for your project if you want and set up some utils there.**

```java
public class ProjectApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        
        // Set up SharedPreferenceData
        // the key in the shared preference should be of same name as given below.
        // these two are the constant values which will be used in the entire application
        String apiPath = "https://www.example.com/api/";
        
        SharedPreferenceData sharedPreferenceData = new SharedPreferenceData(this);
        sharedPreferenceData.setValue("apiPath", apiPath);
        sharedPreferenceData.setValue("dbName", "YourDatabaseName");
    }
}
```

### SharedPreferenceData 

```java
// use it in the activity or class you want to.
SharedPreferenceData sharedPreferenceData = new SharedPreferenceData(context);

// FOR SETTING DATA TO SHARED PREFERENCE.
// generic method storing string values.
sharedPreferenceData.setValue("keyname", "value for that keyname");

// specific for string values.
sharedPreferenceData.setStrValue("keyname", "value for that keyname");

// specific for int values.
sharedPreferenceData.setIntValue("keyname", 1);

// specific for boolean values.
sharedPreferenceData.setBooleanValue("keyname", true);

// spefici for float values.
sharedPreferenceData.setFloatValue("keyname", 1.0);

// specific for long values.
sharedPreferenceData.setLongValue("keyname", 123456789);

// specific for String set values.
sharedPreferenceData.setStrSetValue("keyname", Set<String> value);

// FOR GETTING DATA FROM SHARED PREFERENCE.
// all the below methods will return some defaults values.

// generic method for getting values returns 0 by default as string type.
sharedPreferenceData.getValue(key);

// generic method for getting values returns defaultValue passed if not found.
sharedPreferenceData.getValue(key, defaultValue);

// specific for getting string values returns 0 by default as string type.
sharedPreferenceData.getStrValue(key);

// generic method for getting values returns defaultValue passed if not found.
sharedPreferenceData.getStrValue(key, defaultValue);

// specific for getting int values returns 0 by default as int type.
sharedPreferenceData.getIntValue(key);

// specific for getting boolean values returns false by default as int type.
sharedPreferenceData.getBooleanValue(key);

// specific for getting float values returns 0f by default as int type.
sharedPreferenceData.getFloatValue(key);

// specific for getting long values returns 0 by default as int type.
sharedPreferenceData.getLongValue(key);

// specific for getting set of string values returns null by default as int type.
sharedPreferenceData.getStrSetValue(key);

```

### ApiServices

>Before using this class first make sure to set up the apiPath into shared preference class. Shown above in **ProjectApplication** Section.

```java
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
ApiServices apiServices = new ApiServices(context);
apiServices.makeApiCall(apiName, requestMethod, parameters, jsonObject, hasToken).second;
```

### DBHelper

>Set the dbName in sharedPreferenceData with key name **'dbName'**. Shown above in **ProjectApplication** section.

```java
// this method will check if a table exists in database
// true - if table exists in database
// false - if table not exists in database
DBHelper dbHelper = new DBHelper(context);
dbHelper.isTableExists(tableName);

// this method will check if a column in a table exists or not
// returns true if column exists in table or false if not
dbHelper.checkIsColumnExists(tableName, columnName);

// parameters to be passed are as follows:
// 
// tableName - table on which query should be performed.
//
// operation - c for creating table, 
//             i - for inserting records in table
//             d - for deleting records in table
//             u - for updating records in table
//             dr - for droping the table
//
// values - it is of type LinkedHashMap<String, String>
//          declaration as below
//          LinkedHashMap<String, String> vauels = new LinkedHashMap<>();
//          now just add your column name and values or data types
//
//          example: values.put("ID", "INTEGER PRIMARY KEY AUTOINCREMENT");
//                   values.put("firstName", "TEXT");
//
//          use this way when creating a table.
//          when inserting a record in the table
//          example: value.put("firstName", "'Amit'"); 
//          notice the single quotes inside the double quotes where name is written.
//          for string value you should pass with single quotes and integer value can be passed directly
//
// hasConditions - set this value as true or false
//                 if you are setting this parameter as true
//                 then you must also pass the conditionalValues parameter as well
//                 else the query won't work
//
//                 this parameter is useful when using conditional queries like update or delete
//                 where you create another linked hash map like the one above can just pass the coloumn name or conditions with values
//                 the values should be passed the way we pass in insert query.
//
// conditionalValues - when passing this parameter make sure
// 
// return - the method will return true or false if the operation is successful or failed.
//
dbHelper.executeDatabaseOperation(tableName, operation, values, hasConditions, conditionalValues);

// this method can be used for inserting bulk data into table using database transaction
// pass the name of the table where you want to insert data
// and also pass the total number of columns for which you want to insert data in.
dbHelper.insertDataWithTransaction("tableName", 10);

Example Usage of above method is:
// first we are generating data for inserting into the table
for (int i = 0; i < 20000; i++)
{
    dbHelper.addDataForTable(new DbData("columnName1", "data"))
            .addDataForTable(new DbData("columnName2", "data"))
            .addDataForTable(new DbData("columnName3", "data"))
            .addDataForTable(new DbData("columnName4", "data"))
            .addDataForTable(new DbData("columnName5", "data"))
            .addDataForTable(new DbData("columnName6", "data"))
            .addDataForTable(new DbData("columnName7", "data"))
            .addDataForTable(new DbData("columnName8", "data"))
            .addDataForTable(new DbData("columnName9", "data"))
            .addDataForTable(new DbData("columnName10", "data"));
}

// remember to put this method outside for loop.
dbHelper.insertDataWithTransaction("tableName", 10);

// for select query use this method
//
// parameters to be passed are as follows:
//
// tableName - table on which query should be performed.
// 
// values - values will be of type string,
//          you can pass * or name of the columns saperated by ,
//
// hasConditions - hasConditions is of type boolean
//                 pass true or false to this parameter
//                 if true then you must pass conditoinalValues parameter
//                 else if it's false then conditionalValues can be passed as null
// 
// conditionalValues - if hasConditions value is true or false
//                     then pass this parameter as linked hash map
//                     it is similar as in executeDatabaseOperation method's conditionalValues parameter.
// 
// return - this method wull return Cursor with values or will return null
dbHelper.executeSelectQuery(tableName, values, hasConditions, conditionalValues);

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
 *                            Ex: ID = 1 OR firstName LIKE '%Your String%'
 *
 * @param tClass - Pass your Model class like this
 *                 Ex: ModelClass.class
 *                 this is required for setting the values
 *
 * @return ArrayList of Type pass as class
 *
**/
//#endregion COMMENTS FOR executeSelectQuery method
dbHelper.executeSelectQuery(tableName, values, hasConditions, conditionalValues, class);

// for select query use this method
//
// parameters to be passed are as follows:
//
// tableName - table on which query should be performed.
// 
// values - values will be of type string,
//          you can pass * or name of the column
//
// hasConditions - hasConditions is of type boolean
//                 pass true or false to this parameter
//                 if true then you must pass conditoinalValues parameter
//                 else if it's false then conditionalValues can be passed as null
// 
// conditionalValues - if hasConditions value is true or false
//                     then pass this parameter as linked hash map
//                     it is similar as in executeDatabaseOperation method's conditionalValues parameter.
// 
// return - this method wull return count of records in the table 
//          either for entire table or for a single column
dbHelper.getRecordCount(tableName, values, hasConditions, conditionalValues);

>**Now creating table and inserting records in database is more simpler.**

**Creating Table**
// this example is with single data type and no constraints
dbHelper.addColumnForTable(new DbColumns("ID" /*this is the name of the column*/, "integer" /*this is the data type for that column*/))
    .createTable("TABLE_NAME");
    
// if you want to create table with constraints then use following example
dbHelper.addColumnForTable(new DbColumns("ID", new String[]{"integer", "primary key", "autoincrement"}))
    .createTable("TABLE_NAME");
    
**Inserting Records**
// this is an example for inserting records
dbHelper.addDataForTable(new DbData("age" /*this is the name of the column*/, 26 /*data for that column*/))
    .insertData("TABLE_NAME");

**Inserting Records With Transaction**
// this is an example for inserting records with transaction
// first create data by using addDataForTable method in a loop
dbHelper.addDataForTable(new DbData("age" /*this is the name of the column*/, 26 /*data for that column*/));

// then call this method outside the loop once data is built
// pass the no of column counts of your table
dbHelper.insertDataWithTransaction(String tableName, int dbColumnCount)

**Altering Table**
// this is an example for altering table
// this method will add new columns to the table
dbHelper.addColumnForTable(new DbColumn("age" /*this is the name of the column*/, int /*datatype for that column*/))
    .alterTable("TABLE_NAME");

**Inserting records using json array or json object**
/**
 * insert data with json method
 *
 * this method will insert data using JSON Array or JSON Object
 *
 * @param tableName - name of the table to insert data in
 *
 * @param object    - JSON Object or JSON Array of records and columns to be inserted
 *
 * @return True or False for success for failure in inserting records
**/
DbHelper dbHelper = new DbHelper(Context context);
dbHelper.insertDataWithJson(String tableName, Object object);

/**
 * insert data with json method
 *
 * this method will insert data using JSON Array or JSON Object
 * this method will user SQLite Database Transaction for inserting records in db
 *
 * @param tableName         - name of the table to insert data in
 *
 * @param object            - JSON Object or JSON Array of records and columns to be inserted
 *
 * @param tableColumnCount  - Count of Number of columns for that table
 *
 * @return True or False for success for failure in inserting records
**/
DbHelper dbHelper = new DbHelper(Context context);
dbHelper.insertDataWithJsonAndTransaction(String tableName, Object object, int tableColumnCount)

**Updating Records**

/**
 * update data method
 *
 * @param tableName      - name of the table on which update query is to be performed
 *
 * @param whereClause    - name of the column to check whether the record is present so the data is updated
 *                         pass this parameter in the way given in example below
 *                         Ex: code = ? or ID = ? etc // this is important
 *
 * @param whereArgs      - data of the column name provided to check if record is present for data update
 *                         here you need to pass the data for the corresponding where clause
 *                         Ex: 1 or 2 etc
 *
 * this method will update records of the table in database
 * this method uses database's update method for updating records
 *
 * parameter whereClause and whereArgs must be passed in the form given
**/
dbHelper.addDataForTable(new DbData("age" /*this is the name of the column*/, 26 /*data for that column*/))
    .updateData(String tableName, String whereClause, String whereArgs)

>**Getting all records directly into you model class:**

/**
 * @param tableName             - name of the table for getting the record
 *
 * @param isAscending           - True for ascending order and False for descending order
 *
 * @param orderByColumnName     - name of the column for getting records in descending order
 *
 * @param tClass                - Pass your Model class like this
 *                                Ex: ModelClass.class this is required for setting the values
 *                                    make sure your model class's setters has same name as database column name
 *                                    Example: columnName: "age", so setter's name should be: "setAge"
 *
 * @return Array list of ModelClass you provided in method
 *
 * this method will get all the records from the table
 * in ascending or descending order as provided by the user
 *
 * this method is a generic method which can be directly bounded to the array list of custom type
**/
// For getting all the records from database
ArrayList<YourModelClassName> arrayList = dbHelper.getAllRecords(
"TABLE_NAME", isAscending: true or false, orderByColumnName: null or name of the column, YourModelClassName.class);

/**
 * @param tableName             - name of the table for getting the record
 *
 * @param conditionalValues     - conditions for selecting records from table in database
 *                                either individual conditions for multiple conditions
 *                                Ex: ID = 1 or code = 1 or firstName = 'FirstName'
 *                                    or ID = 1 AND firstName = 'FirstName'
 *
 * @param isAscending           - True for ascending order and False for descending order
 *
 * @param orderByColumnName     - name of the column for getting records in descending order
 *
 * @param tClass                - Pass your Model class like this
 *                                Ex: ModelClass.class this is required for setting the values
 *                                    make sure your model class's setters has same name as database column name
 *                                    Example: columnName: "age", so setter's name should be: "setAge"
 *
 * @return Array list of ModelClass you provided in method
 *
 * this method will get all the records from the table
 * in ascending or descending order as provided by the user
 *
 * this method is a generic method which can be directly bounded to the array list of custom type
**/
ArrayList<YourModelClassName> arrayList = dbHelper.getAllRecords(
"TABLE_NAME", isAscending: true or false, conditionalValues, orderByColumnName: null or name of the column, YourModelClassName.class);



```

### DatePickerFragment

>**Use this date picker fragment for selecting date.**

```java

/**
 * 2018 October 24 - Wednesday - 03:34 PM
 * show date picker dialog method
 *
 * this method will show the date picker dialog fragment
 *
 * @param context - context of the application
 * @param selectedDate - interface of date picker fragment for getting the selected date value
 * @param selectedDateFormat - format in which you want the date.
 *                             Example: yyyy-MM-dd hh:mm:ss
 *
 * @param isCurrentDateMin - pass true to set current date as minimum date else pass false.
 * you will also have to implement it's interface for getting the selected date.
 * Example: public class YourActivity extends AppCompatActivity implements DatePickerFragment.SelectedDate
**/
DatePickerFragment datePickerFragment = new DatePickerFragment();
datePickerFragment.showDatePickerDialog(this, this, "yyyy/MM/dd HH:mm:ss", false);

// the interface method of date picker is below
@Override
public void selectedDate(String selectedDate)
{
    Log.e(TAG, "selectedDate: selected date is: " + selectedDate);
}

```

### TimePickerFragment

>**Use this time picker fragment for selecting time.**

```java

/**
 * 2018 October 24 - Wednesday - 03:34 PM
 * show time picker dialog method
 *
 * this method will show the time picker dialog fragment
 *
 * @param context               - context of the application
 * @param timeSelected          - interface of time picker fragment for getting the selected time value
 * @param selectedTimeFormat    - format in which you want the time.
 *                                Example: hh:mm
 *
 *
 * you will also have to implement it's interface for getting the selected time.
 * Example: public class YourActivity extends AppCompatActivity implements TimePickerFragment.OnTimeSelected
**/
TimePickerFragment timePickerFragment = new TimePickerFragment();
timePickerFragment.showTimePickerDialog(this, this, "hh:mm");

// the interface method of time picker is below
@Override
public void selectedTime(String selectedTime)
{
    Log.e(TAG, "selectedTime: selected time is: " + selectedTime);
}

```

### SquareImageView

>**Use it the way you use ImageView in android. This makes your image in a perfect square shape.**

```xml
<com.amit.iv.SquareImageView
    android:id="@+id/squareImageView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:scaleType="centerCrop"
    android:src="@drawable/no_image" />
```

### CircularImageView

>**Use it the way you use ImageView in android. Attributes for this image view are as follows:**

**1. highlightEnable - true/false**

**2. highlightColor**

**3. imgStrokeWidth**

**4. strokeColor**

>**example code:**

```xml
<com.amit.iv.CircularImageView
    android:id="@+id/circularImageView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:scaleType="centerCrop"
    android:src="@drawable/no_image"
    app:highlightColor="@color/colorPrimary"
    app:highlightEnable="true"
    app:imgStrokeWidth="2dp"
    app:strokeColor="#000000" />
```

### AvatarImageView

>**Use it the way you use ImageView in android. Attributes for this image view are as follows:**

**1. imgStrokeWidth**

**2. strokeColor**

**3. avatar_backgroundColor**

**4. avatar_text**

**5. avatar_textColor**

**6. avatar_textSize**

**7. avatar_state - IMAGE/INITIAL - use image if you want to show image or use initial if you want to show initial letter of a word.**

>**example code:**

```xml
<com.amit.iv.AvatarImageView
    android:id="@+id/avatarImageView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:scaleType="centerCrop"
    android:src="@drawable/no_image"
    app:avatar_backgroundColor="@color/colorAccent"
    app:avatar_state="INITIAL"
    app:avatar_text="A"
    app:avatar_textColor="#FFFFFF"
    app:avatar_textSize="30sp"
    app:imgStrokeWidth="5dp"
    app:strokeColor="@color/colorPrimary" />
```

### TouchImageView

>**Use it the way you use ImageView in android. This is helpful when you have to add zoom in or zoom out functionality to the imageView with additional code.**


```xml
<com.amit.iv.TouchImageView
    android:id="@+id/imageView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:src="@drawable/no_image" />
```

### SwitchButton

>**Use it in the xml file like this.**

**If you are using typeface then add the font file into assets folder, inside assets folder create another folder named 'fonts'. And then just use the name of your font file like the one in below example.** 

**UI Part**
```xml
<com.amit.ui.SwitchButton
    android:id="@+id/switchButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="10dp"
    android:padding="10dp"
    app:selectedColor="@color/colorPrimary"
    app:selectedTab="0"
    app:strokeRadius="5dp"
    app:strokeWidth="2dp"
    app:switchTabs="@array/gender"
    app:textSize="20sp"
    app:typeface="QuattrocentoSans-Regular.ttf" />
```

**Java code**

```java
// the below code shows how to use the SwitchButton to get the selected values.
SwitchButton switchButton = findViewById(R.id.switchButton);

// this is the on switch listener for switchButton
switchButton.setOnSwitchListener(new SwitchButton.OnSwitchListener() 
{
    @Override
    public void onSwitch(int i, String tabText) 
    {
        Toast.makeText(MainActivity.this, "selected tab text is: " + tabText, Toast.LENGTH_SHORT).show();
    }
});
```

### GeoLocation class

>**This class will  get the current location and address of the user. This class will check and ask for locations permission if not granted**

```java

// this will check whether gps is enabled or not
GeoLocation.isGPSEnabled(Context context); // returns true or false

// following code is for getting latitude and longitude
double latitude = geoLocation.getLatitude();
double longitude = geoLocation.getLongitude();

// this method will get address of current location
// you can also get city, state, country, etc.
String address = geoLocation.getAddress;

String city = geoLocation.getCity();

String state = geoLocation.getState();

String country = geoLocation.getCountry();

String premises = geoLocation.getPremises();

String knownName = geoLocation.getKnownName();

String postalCode = geoLocation.getPostalCode();

String subLocality = geoLocation.getSubLocality();

String subAdminArea = geoLocation.getSubAdminArea();

String thoroughFare = geoLocation.getThoroughFare();

String subThoroughFare = geoLocation.getSubThoroughFare();

// if you want to capture the address and latitude and longitude
// after getting access to locations permission
// then use *onRequestPermissionsResult* of the activity
// and re-call this class as follows.
// considering *geoLocation* is declared globally
geoLocation = new GeoLocation(YourActivity.this);

```

### FontHelper Class

>**This class helps you to apply the font to the entire layout file. This file will apply the required font to every view of the layout file.**

```java
// Example usage
FontHelper.applyFont(context, idOfYourParentLayout, "fonts/QuattrocentoSans-Regular.ttf");
```

### InternetConnection class

>**This class will help you check if internet connection is available or not.**

```java
InternetConnection ic = InternetConnection.getInstanceInternet(context);
if (ic.isNetworkAvailable())
{
  // internet is available
}
else
{
  // internet is not available
}
```

### TextUtils class

>**This class helps you replaces values, like replacing 'null' with empty string, replacing true or false with 1 or 0, etc.**

```java

/**
 * replace null method
 * this method will replace null with empty space
 *
 * @param string - string where you want to replace null
 * @return it will return empty string
**/
// example: for replacing null value from a string.
TextUtils.replaceNullWithEmpty(string); // this will return string value.

/**
 * replace true or false
 * this method will replace true or false with 1 or 0
 *
 * @param string - string to replace true or false with
 * @return it will return 1 or 0
**/
// example: for replacing True or False from a string.
TextUtils.replaceTrueOrFalse(string); // this will return int value.

/**
 * 2018 September 14 - Friday - 12:34 PM
 * replace null with zero method
 *
 * this method will replace null or empty values of string with zero
 *
 * @param stringToReplace - string to replace null with
 * @return it will return 1 or 0 as int
**/
TextUtils.replaceNullWithZero(String stringToReplace)

/**
 * replace one or zero to boolean method
 * this method will replace 1 or 0 value to True or False
 *
 * @param valueToReplace - integer value to be replaced
 * @return True or False
**/
TextUtils.replaceOneOrZeroToBoolean(int valueToReplace)

/**
 * replace null method
 * this method will replace null with empty space
 *
 * @param string - string where you want to replace null
 * @return it will return empty string
**/
TextUtils.replaceNullWithDash(String string)

/**
 * 2018 September 14 - Friday - 12:34 PM
 * remove last char method
 *
 * this method will remove the last character of the string
 *
 * @param stringToRemovedLastCharFrom - string to remove the last character from
 * @return it will return string with last character removed
**/
TextUtils.removeLastChar(String stringToRemovedLastCharFrom)

/**
 * replace null by zero method
 *
 * this method will replace null or empty values of string with zero
 *
 * @param stringToReplace - string to replace null with
 * @return it will return string value passed or 0 as string
**/
TextUtils.replaceNullByZero(String stringToReplace)

/**
 * capitalizeString method
 *
 * this method will capitalizeString or set the string to upper case
 *
 * @param string - string to capitalize
 * return - will return the string which was passed in capitalize form
**/
TextUtils.capitalizeString(String string)

/**
 * format decimal for one places.
 *
 * this method will format decimal value to one places after decimal
 *
 * @param valueToFormat - value in double data type to format
 *
 * @return formatted value in double with one places after decimal
**/
TextUtils.formatDecimalForOnePlaces(double valueToFormat)

/**
 * format decimal for two places.
 *
 * this method will format decimal value to two places after decimal
 *
 * @param valueToFormat - value in double data type to format
 *
 * @return formatted value in double with two places after decimal
**/
TextUtils.formatDecimalForTwoPlaces(double valueToFormat)

/**
 * format decimal for three places.
 *
 * this method will format decimal value to three places after decimal
 *
 * @param valueToFormat - value in double data type to format
 *
 * @return formatted value in double with three places after decimal
**/
TextUtils.formatDecimalForThreePlaces(double valueToFormat)

/**
 * format decimal for four places.
 *
 * this method will format decimal value to four places after decimal
 *
 * @param valueToFormat - value in double data type to format
 *
 * @return formatted value in double with four places after decimal
**/
TextUtils.formatDecimalForFourPlaces(double valueToFormat)

/**
 * format decimal to single digit
 *
 * this method will format decimal value to single digit
 * if you don't want to show 0 after decimal
 *
 * @param valueToFormat - value in double data type to format
 *
 * @return formatted value in double with three places after decimal
**/
TextUtils.formatDecimalToSingleDigit(double valueToFormat)

/**
 * highlight text method
 *
 * this method will highlight the searched string in bold style
**/
TextUtils.highlightSearchedText(CharSequence text, CharSequence wordPrefix)

```

### Validator class

>**This class helps you to validate some common fields like email, mobile number, numbers only, etc.**

```java
// validating an email id, returns true or false.
Validator.validateEmail("example@gmail.com");

// validating mobile no, returns true or false.
Validator.validateMobile("9999955555");

// validating Pan Card (For Indian Format), returns true or false.
Validator.validatePanCard("AAAAA0000A");

// only numbers validation, returns true or false.
Validator.onlyNumbers("123456");

// only characters validation, returns true or false.
Validator.onlyCharacters("abcd");

// atLeastOneLowerCase validation, returns true or false.
Validator.atLeastOneLowerCase("ABcD");

// atLeastOneUpperCase validation, returns true or false.
Validator.atLeastOneUpperCase("abCd");

// atLeastOneNumber validation, returns true or false.
Validator.atLeastOneNumber("abcd1");

// nonEmpty validation, returns true or false.
Validator.nonEmpty("d");

// startsWithNonNumber validation, returns true or false.
Validator.startsWithNonNumber("a12");

// noSpecialCharacters validation, returns true or false.
Validator.noSpecialCharacters("abcd123");

// atLeastOneSpecialCharacters validation, returns true or false.
Validator.atLeastOneSpecialCharacters("abcd@123");
```

### UiUtils

>**This class has utils for ui like setting max length to TextView, EditText, TextInputEditText.**

**Usage: For setting char count on edittext or textinputedittext**

```java
/**
 * set char counter
 * Shows live character counter for the number of characters
 * typed in the parameter {@link android.widget.EditText}
 *
 * @param editText          Characters to count from
 * @param tvCounterView     {@link android.widget.TextView} to show live character count in
 * @param maxCharCount      Max characters that can be typed in into the parameter edit text
 * @param countDown         if true, only the remaining of the max character count will be displayed.
 *                          if false, current character count as well as max character count will be displayed in the UI.
**/
UiUtils.setCharCounter(editText, tvCounterView, maxCharCount, countDown);


/**
 * set char counter
 * Shows live character counter for the number of characters
 *
 * @param textInputEditText          Characters to count from
 * @param tvCounterView     {@link android.widget.TextView} to show live character count in
 * @param maxCharCount      Max characters that can be typed in into the parameter edit text
 * @param countDown         if true, only the remaining of the max character count will be displayed.
 *                          if false, current character count as well as max character count will be displayed in the UI.
**/
UiUtils.setCharCounter(textInputEditText, tvCounterView, maxCharCount, countDown);

/**
 * set max length
 * this method sets max text length for text view
 *
 * @param textView - text view on which you want to set max length
 * @param maxLength - length to set on text view
**/
UiUtils.setMaxLength(textView, maxLength);

/**
 * set max length
 * this method sets max text length for text view
 *
 * @param editText - text view on which you want to set max length
 * @param maxLength - length to set on text view
**/
UiUtils.setMaxLength(editText, maxLength);

/**
 * set max length
 * this method sets max text length for text view
 *
 * @param textInputEditText - text view on which you want to set max length
 * @param maxLength - length to set on text view
**/
UiUtils.setMaxLength(textInputEditText, maxLength);

```

### Utils

>**This utils class helps for generic usages.**

**Usage**

```java

/**
 * is url valid
 * this method will check if the url provided is valid or not
 *
 * @param url - url to check
 * @return - will return true if the url is valid
 *           else will return false
**/
Utils.isUrlValid(url);


/**
 * to Bold
 * this method will convert the normal text to bold
 *
 * @param sourceText - text to convert to bold
 *
 * @return - {@link android.text.SpannableString} in BOLD TypeFace
**/
Utils.toBold(sourceText);


/**
 * to Bold
 * this method will convert a string or a sub string to bold
 *
 * @param string - string in which the sub string has to be converted to bold
 *                 or string to be converted to bold
 *
 * @param subString - The subString within the string to bold.
 *                    Pass null to bold entire string.
 *
 * @return - {@link android.text.SpannableString} in Bold TypeFace
**/
Utils.toBold(string, subString);


/**
 * hide keyboard
 * this method will hide the keyboard
 *
 * @param context - context of the application
**/
Utils.hideKeyboard(context);


/**
 * get sha 512 hash
 * this method will convert a string to byte array.
 *
 * @param stringToHash - string to convert to hash.
 *
 * @return string converted to hash value.
**/
Utils.getSha512Hash(stringToHash);


/**
 * get sha 512 hash
 * this method will convert the byte array to string
 * which is converted to hash
 *
 * @param dataToHash - byte array to convert to hash value
 *
 * @return string converted into hash value.
**/
Utils.getSha512Hash(byte[] dataToHash);

/**
 * get drawable
 * this method will get you the drawables
 *
 * @param context - context of the application
 * @param id - drawable id
 *
 * @return returns drawable
**/
Utils.getDrawable(@NonNull Context context, @DrawableRes int id)

/**
 * get Color wrapper
 * this method will get the color resource based on android version
 *
 * @param context - context of the application
 * @param id - id of the color resource
 *
 * @return int - color in integer.
**/
Utils.getColorWrapper(@NonNull Context context, @ColorRes int id)

/**
 * drawable to bitmap
 * this method will convert a drawable to bitmap
 *
 * @param drawable - drawable to be converted into bitmap
 * @return bitmap
**/
Utils.drawableToBitmap(Drawable drawable)

/**
 * 2018 June 23 - Saturday - 10:30 AM
 * right padding method
 *
 * this method will append empty or blank or spaces
 * after the string for specified length.
 *
 * @param strText - String text to append spaces to the right
 * @param length - length of the string text including spaces and text.
 *
 * @return - returns the string with spaces appended to the right of the string
**/
Utils.rightPadding(String strText, int length);

/**
 * 2018 June 23 - Saturday - 10:30 AM
 * left padding method
 *
 * this method will append empty or blank or spaces
 * after the string for specified length.
 *
 * @param strText - String text to append spaces to the left
 * @param length - length of the string text including spaces and text.
 *
 * @return - returns the string with spaces appended to the left of the string.
**/
Utils.leftPadding(String strText, int length);

/**
 * Schedules the shared element transition to be started immediately
 * after the shared element has been measured and laid out within the
 * activity's view hierarchy. Some common places where it might make
 * sense to call this method are:
 *
 * (1) Inside a Fragment's onCreateView() method (if the shared element
 *     lives inside a Fragment hosted by the called Activity).
 *
 * (2) Inside a Picasso Callback object (if you need to wait for Picasso to
 *     asynchronously load/scale a bitmap before the transition can begin).
 *
 * @param activity - Activity on which shared transition is to be performed
 *
 * @param sharedElement - The view on which the actual animation will be done
**/
Utils.scheduleStartPostponedTransition(final Activity activity, final View sharedElement)

/**
 * make text view resizable method
 *
 * this method will make the text view resizeable
 * when you have a large description and don't want to show entire
 * details completely then you can use this to show a link like text "See More"
 *
 * @param tv - text view to make it resizeable
 *
 * @param maxLine - number of lines to be shown before see more button
 *
 * @param expandText - text you want to show for the user to click
 *                     Example: Read more (THIS IS USED BY DEFAULT)
 *
 * @param viewMore - pass true for this parameter
**/
Utils.makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore)
    
```

### AnimUtil

>**This class can be used for animating activity or a view.**

**Usage**

```java

// for making an activity slide from right to left
AnimUtil.slideActivityFromRightToLeft(context);

// for making an activity slide from left to right
AnimUtil.slideActivityFromLeftToRight(context);

// for making an activity slide from right to left
AnimUtil.slideActivityFromRightToLeft(context);

// for making an activity fade in fade out
AnimUtil.activityFadeInFadeOut(context);

// for making an activity from left with stay
AnimUtil.slideActivityFromLeftWithStay(context);

// for making an activity from right with stay
AnimUtil.slideActivityFromRightWithStay(context);

// for making an activity bottom with stay
AnimUtil.slideActivityFromBottomWithStay(context);

// for making an activity from top with stay
AnimUtil.slideActivityFromUpWithStay(context);

// for making an activity from bottom to up
AnimUtil.slideActivityFromBottomToUp(context);

// for making an activity from up to bottom
AnimUtil.slideActivityFromUpToBottom(context);

// for making an activity from up to bottom
AnimUtil.slideActivityFromUpToBottom(context);

// this method will make an view group to explode or rise 
AnimUtil.explodeTransition(Context context, ViewGroup viewGroup, int duration);

// this method will make and view to slide from right
AnimUtil.slideAnimFromRight(@NonNull Context context, View view, int duration);

// this method will make and view to slide from left
AnimUtil.slideAnimFromLeft(@NonNull Context context, View view, int duration);

// Use your own animation in this method the way you want.
AnimUtil.slideAnim(@NonNull Context context, View view, int duration, @AnimRes int animResId);

// Use this method to make a button or a view bounce
AnimUtil.bounceAnim(Context context, View view);

/**
 * expand view method
 *
 * this method is used to expand the view to its content's height
 *
 * @param v - View that needs to be expanded
**/
AnimUtil.expandView(final View v)

/**
 * collapse view method
 *
 * this method is used to collapse the view to its content's height
 *
 * @param v - View that needs to be collapsed
**/
AnimUtil.collapseView(final View v)

/**
 * expand view method
 *
 * this method is used to expand the view to its content's height
 *
 * @param v             - View that needs to be expanded
 *
 * @param animDuration  - duration of the animation for expanding the view
**/
AnimUtil.expandView(final View v, int animDuration)

/**
 * collapse view method
 *
 * this method is used to collapse the view to its content's height
 *
 * @param v             - View that needs to be collapsed
 *
 * @param animDuration  - duration of the animation for collapsing the view
**/
AnimUtil.collapseView(final View v, int animDuration)

```

### Social Buttons

>**Use Social buttons like you use normal button.**

**Usage**

```xml
<!-- Currently Facebook, Google, Twitter, LinkedIn buttons are available. -->

<!-- Square button with icon and color background and normal usage -->
<com.amit.ui.GoogleButton
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Sign in with Google"
    android:textAlignment="center"
    android:textColor="@color/black"
    app:iconSize="30dp"
    app:iconPadding="30dp"
    tools:ignore="HardcodedText" />

<!-- Round Button with icon only and color background -->
<com.amit.ui.GoogleButton
    android:layout_width="75dp"
    android:layout_height="75dp"
    app:iconSize="30dp"
    app:roundedCorner="true"
    app:roundedCornerRadius="75dp" />

<!-- With Rounded corners and text -->
<com.amit.ui.GoogleButton
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Sign in with Google"
    android:textAlignment="center"
    android:textColor="@color/black"
    app:iconCenterAligned="false"
    app:iconSize="30dp"
    app:roundedCorner="true" />

<!-- Square button with only icon -->
<com.amit.ui.GoogleButton
    android:layout_width="75dp"
    android:layout_height="75dp"
    app:iconSize="30dp" />

<!-- Square button with transparent background -->
<com.amit.ui.GoogleButton
    android:layout_width="75dp"
    android:layout_height="75dp"
    app:iconSize="30dp"
    app:transparentBackground="true" />

<!-- Round button with transparent background -->
<com.amit.ui.GoogleButton
    android:layout_width="75dp"
    android:layout_height="75dp"
    app:iconSize="30dp"
    app:roundedCorner="true"
    app:roundedCornerRadius="75dp"
    app:transparentBackground="true" />
```

### Shine Button

>**Type of like button with some color explosion.**

**Usage**

```xml
<com.amit.shinebtn.ShineButton
    android:id="@+id/shine_button"
    android:layout_width="50dp"
    android:layout_height="50dp"
    android:layout_marginBottom="10dp"
    android:src="@android:color/darker_gray"
    app:allow_random_color="false"
    app:big_shine_color="#FF6666"
    app:btn_color="@android:color/darker_gray"
    app:btn_fill_color="#FF6666"
    app:click_animation_duration="200"
    app:enable_flashing="false"
    app:shine_animation_duration="1500"
    app:shine_count="8"
    app:shine_turn_angle="10"
    app:siShape="@raw/heart"
    app:small_shine_color="#CC9999"
    app:small_shine_offset_angle="20" />
```

**Java Code for Shine Button**

```java
ShineButton shineButton = findViewById(R.id.shine_button);
shineButton.init(MainActivity.this);
```

### DateTimeUtils

**Usage**

```java

/**
 * get current date time method
 *
 * this method will current date time in string type
 *
 * @param inDateTimeFormat - Pass the date or date time format you
 *                           want to get date or date time in format
 *                           Ex: dd-MM-yyyy or dd-MM-yyyy hh:mm
 *
 * @return - date or date time returned
**/
DateTimeUtils.getCurrentDateTime(String inDateTimeFormat)

/**
 * format date time method
 * <p>
 * this method will format date time in to formats user provides
 *
 * @param dateToFormat - date time which you need to format
 *                       EX: 2018-10-09
 *
 * @param inDateTimeFormat - format of the date time in which you want to format given date
 *                           EX: dd-MM-yyyy OR dd-MM-yyyy hh:mm:ss
 *
 * @return date time in format provided
**/
DateTimeUtils.formatDateTime(String dateToFormat, String inDateTimeFormat)

/**
 * format date time method
 * <p>
 * this method will format date time in to formats user provides
 *
 * @param dateToFormat - date time which you need to format
 *                       EX: 2018-10-09
 *
 * @param inDateTimeFormat - format of the date time in which you want to format given date
 *                           EX: dd-MM-yyyy OR dd-MM-yyyy hh:mm:ss
 *
 * @param fromDateTimFormat - format of date time from which you want to format
 *                            EX: yyyy-MM-dd OR dd-MM-yyyy hh:mm:ss
 *
 * @return date time in format provided
**/
DateTimeUtils.formatDateTime(String dateToFormat, String inDateTimeFormat, String fromDateTimFormat)

/**
 * format milli seconds to time method
 *
 * this method formats the string in hh:mm:ss format
**/
DateTimeUtils.formatMilliSecondsToTime(long milliseconds)

/**
 * two digit string method
 *
 * this string formats the given parameter in two digits
 *
 * @param number - number to be formatted in two digits
 *
 * @return returns number in two digits in string format
**/
DateTimeUtils.twoDigitString(long number)

/**
 * convert days in millis method
 *
 * this method will convert days in milliseconds
 *
 * @param days - days value in integer to be converted
 *
 * @return returns milli seconds value of given number of days
**/
DateTimeUtils.convertDaysInMillis(int days)

/**
 * get current fin year method
 *
 * this method will get current fin year in yy-yy format
 *
 * @param context - context of the application or activity
 *
 * @return it will return current fin year in yy-yy format
**/
DateTimeUtils.getCurrentFinYear(Context context)

/**
 * convert to date time from milliseconds method
 *
 * this method will convert milliseconds to date time
 *
 * @param context           - context of the application or activity
 *
 * @param milliseconds      - milli seconds to be converted to date time
 *
 * @param inDateTimeFormat  - format in which you want date time
 *                            Example: yyyy-MM-dd HH:mm:ss
 *
 * @return Date time in specified in inDateTimeFormat
**/
DateTimeUtils.convertToDateTimeFromMilliseconds(Context context, Long milliseconds, String inDateTimeFormat)

```

### DeviceUtils

**Usage**

```java

/**
 * is Sd Card Mounted
 * this method will check if sd card is mounted or not
 *
 * @return - true or false
 *           if sd card available then will return true
 *           else will return false
**/
DeviceUtils.isSdCardMounted()

/**
 * Get IMEI Number method
 *
 * this method gets IMEI number after getting the permission.
 *
 * @return - it will return IMEI number if permission granted
 *           else if no permission granted then will return empty string.
 **/
DeviceUtils.getIMEINumber(@NonNull Context context)

/**
 * 2018 September 18 - Tuesday - 04:54 PM
 * get battery percentage method
 *
 * this method will get the percentage of battery remaining
 *
 * @param context - context of the application
 * @return battery percentage in int or 0
**/
DeviceUtils.getBatteryPercentage(@NonNull Context context)

/**
 * get device id method
 *
 * this method will get the device id
 *
 * @param context - application context
 * @return device id in string
**/
DeviceUtils.getDeviceID(@NonNull Context context)

/**
 * get device name method
 *
 * this method will get the name of the device
 * @return name of the device with manufacturer
**/
DeviceUtils.getDeviceName()

/**
 * 2018 October 04 - Thursday - 02:50 PM
 * get mac address method
 *
 * this method will get mac address of the device
**/
DeviceUtils.getMacAddress(Context context)

```

### ExpandableGridView

>**This expandable grid view will help you to use this inside a scrollview.**

**Usage**

```xml

<com.amit.views.ExpandableGridView
    android:id="@+id/gridView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginStart="@dimen/five_dp"
    android:layout_marginTop="@dimen/five_dp"
    android:layout_marginEnd="@dimen/ten_dp"
    android:numColumns="3" />

```

### ExpandableListView

>**This expandable list view will help you to use this inside a scrollview.**

```xml

<com.amit.views.ExpandableListView
    android:id="@+id/listView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_marginTop="@dimen/five_dp" />

```

### ImagePicker

>**This class will help users to Select image from gallery or capture using camera. This class also helps in croping, compressing the image.**

>**To get all the images you captured in camera or gallery or cropped you can get normally in onActivityResult**

>**The permissions are handled automatically here for Camera and Gallery.**

```java
// Pick image using Gallery:
// this example is to use directly in kotlin
ImagePicker.with(this)
	    .galleryOnly()        //User can only select image from Gallery
	    .start(REQUEST_CODE)		//Default Request Code is ImagePicker.REQUEST_CODE
     
// this example is to use in java
ImagePicker.Companion.with(this)
	    .galleryOnly()        //User can only select image from Gallery
	    .start(REQUEST_CODE)		//Default Request Code is ImagePicker.REQUEST_CODE
     
// Capture image using Camera:
// this example is to use directly in kotlin
ImagePicker.with(this)
	    .cameraOnly()         //User can only capture image using Camera
	    .start(REQUEST_CODE)		//Default Request Code is ImagePicker.REQUEST_CODE
     
// this example is to use in java
ImagePicker.Companion.with(this)
	    .cameraOnly()         //User can only capture image using Camera
	    .start(REQUEST_CODE)		//Default Request Code is ImagePicker.REQUEST_CODE
     
// Crop image
// in kotlin
ImagePicker.with(this)
	    .crop(16f, 9f)	   //Crop image with 16:9 aspect ratio
	    .start()
     
// in java
ImagePicker.Companion.with(this)
	    .crop(16f, 9f)	   //Crop image with 16:9 aspect ratio
	    .start()
     
// Crop square image(e.g for profile)
// in kotlin
ImagePicker.with(this)
       .cropSquare()	   //Crop square image, its same as crop(1f, 1f)
       .start()
       
// in java
ImagePicker.Companion.with(this)
       .cropSquare()	   //Crop square image, its same as crop(1f, 1f)
       .start()
       
// Compress image size(e.g image should be maximum 1 MB)
// in kotlin
ImagePicker.with(this)
	    .compress(1024)	   //Final image size will be less than 1 MB
	    .start()
     
// in java
ImagePicker.Companion.with(this)
	    .compress(1024)	   //Final image size will be less than 1 MB
	    .start()
     
// Resize image resolution
// in kotlin
ImagePicker.with(this)
    	//Final image resolution will be less than 620 x 620
	    .maxResultSize(620, 620)	   
	    .start()
     
// in java
ImagePicker.Companion.with(this)
    	//Final image resolution will be less than 620 x 620
	    .maxResultSize(620, 620)
     .start()
```

### PermissionHelper Class

>**PermissionHelper class will help developers for requesting permissions.**

```java
// For requesting all permissions 
// **in Kotlin**
PermissionHelper.requestAllPermissions(this, requestCode);

// **in Java**
PermissionHelper.Companion.requestAllPermissions(this, requestCode);

// For requesting single permission
// **in Java**
PermissionHelper.Companion.requestPermission(this, requestCode, Manifest.permission.CAMERA);

// **in Kotlin**
PermissionHelper.requestPermission(this, requestCode, Manifest.permission.CAMERA)

// For requesting multiple permissions
// **in Kotlin**
val requestPermissionsList = arrayOf(
                Manifest.permission.CAMERA, 
                Manifest.permission.WRITE_CONTACTS, 
                Manifest.permission.READ_CONTACTS, 
                Manifest.permission.READ_CALENDAR, 
                Manifest.permission.WRITE_CALENDAR)
        
PermissionHelper.requestPermission(this, requestCode, requestPermissionsList)

// **in Java**
String[] requestPermissionsList = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR};
		
PermissionHelper.Companion.requestPermission(this, requestCode, requestPermissionsList);
```

### ASpinner
>****

```xml
// XML
<com.amit.ui.ASpinner
        android:id="@+id/mySpinner"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="10dp"
        android:hint="@string/select_gender"
        android:textColor="@color/black_shade"
        app:backgroundTint="@color/colorPrimary"
        app:spinner_dialog_icon_tint="@color/colorPrimary"
        app:spinner_dialog_title="@string/select_gender"
        app:spinner_dialog_title_color="@color/colorPrimary" />

// Code
ASpinner mySpinner = findViewById(R.id.mySpinner);
mySpinner.setTitle(getResources().getString(R.string.select_gender));
mySpinner.setItems(getResources().getStringArray(R.array.gender_array));

mySpinner.setOnItemClickListener((selectedItem, position) ->
{
    Log.e(TAG, "onItemClick: selected item is: " + selectedItem);
    Log.e(TAG, "onItemClick: selected item's position is: " + position);
});
```
