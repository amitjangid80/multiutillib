# multiutillib

## Setup

#### Step 1. Add the JitPack repository to your build file 
>**Add it in your root build.gradle at the end of repositories:**
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
    implementation 'com.github.amitjangid80:multiutillib:v1.1.2'
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
   version>v1.1.2</version>
dependency>
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
sharedPreferenceData.setValue("keyname", "value for that keyname");
```

### ApiServices

>Before using this class first make sure to set up the apiPath into shared preference class. Shown above in **ProjectApplication** Section.

```java
// apiName - this will the part of the api url
//
// requestMethod - this will GET, POST OR PUT
//
// parameters - this is a boolean value
//              if you want to send parameters in body then this should be set to true.
//              else it should be false.
//              if you don't want to pass data in body 
//              then it should be set to false 
//              and send data as parameters by appending it to the api name
//
// jsonObject - this will be the data which you want to send by body to the api.
//
// hasToken - this will take boolean vaule true or false.
//            if you have verification in your api where you are using token then
//            you should set it to true otherwise to false.
//            but set the token into sharedPreferenceData
//            with key as 'token' to get the token
//
// return - this method will return result from server in string format

ApiServices apiServices = new ApiServices(context);
apiServices.makeApiCall(apiName, requestMethod, parameters, jsonObject, hasToken);
```

### DBHelper

>Set the dbName in sharedPreferenceData with key name **'dbName'**. Shown above in **ProjectApplication** section.

```java
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
 
DBHelper dbHelper = new DBHelper(context);
dbHelper.executeDatabaseOperation(tableName, operation, values, hasConditions, conditionalValues);
```
```java
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
```
```
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
```

### SquareImageView
>**Use it the way you use ImageView in android. This makes your image in a perfect square shape.**

```xml
<com.amit.imgview.SquareImageView
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
<com.amit.imgview.CircularImageView
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
<com.amit.imgview.AvatarImageView
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
<com.amit.imgview.TouchImageView
    android:id="@+id/imageView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:src="@drawable/no_image" />
```

### SwitchButton
>**Use it in the xml file like this.**

**If you are using typeface then add the font file into assets folder, inside assets folder create another folder named 'fonts'. And then just use the name of your font file like the one in below example.** 

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

### MyLocation class

>**This class will  get the current location of the user.**
```java
// Declare this class globally inside your activity or class.
private double latitude = 0.0, longitude = 0.0;
private MyLocation myLocation = new MyLocation();

// Use myLocation's class's Location listener which will return latitude and longitude of current location.
private MyLocation.LocationResult locationResult = new Mylocation.LocationResult()
{
    @Override
    public void gotLocation(Location location)
    {
        try
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
                
            Log.e(TAG, "gotLocation: Latitude of the location is: " + latitude);
            Log.e(TAG, "gotLocation: Longitude of the location is: " + longitude);
        }
        catch(Exception e)
        {
            Log.e(TAG, "gotLocation: exception while getting location lat, long:\n");
            e.printStackTrace();
        }
    }
};

// all the above code should be used globally.
// now use the myLocation's class's object as below inside onCreate method.
myLocation.getLocation(this, locationResult);
```

### LocationAddress class

>**This class helps your to get the address of the current location or by using latitude and longitude.**
```java
// use the following code to get the address of the latitude and longitude.
// declare this inside onCreate() method or anywhere you would like to use it.
// on button click or anywhere.

LocationAddress.getAddressFromLocation(
        mLatitude, mLongitude, this,
        new GeocoderHandler());

// this is the GeocoderHandler class where you'll get the address
private class GeocoderHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 1:

                    Bundle bundle = msg.getData();
                    
                    // note that subThoroughFare may return null sometimes.
                    // so you should manage it accordling.
                    String address = bundle.getString("subThoroughFare") + ", " +
                                     bundle.getString("thoroughFare");

                    String subLocality = bundle.getString("subLocality");
                    String subAdminArea = bundle.getString("subAdminArea");
                    String postalCode = bundle.getString("postalCode");
                    
                    String city = bundle.getString("city");
                    String state = bundle.getString("state");
                    String country = bundle.getString("country");

                    break;

                default:
                    
                    // address not found.
                    
                    break;
            }
        }
    }
```

### FontHelper Class
>**This class helps you to apply the font to the entire layout file. This file will apply the required font to every view of the layout file.**

```java
// Example usage
FontHelper.applyFont(context, idOfYourParentLayout, "fonts/QuattrocentoSans-Regular.ttf");
```

### InternetConection class
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

### TextUtitlities class

>**This class helps you replaces values, like replacing 'null' with empty string, replacing true or false with 1 or 0, etc.**

```java
// example: for replacing null value from a string.
TextUtilities.replaceNull(string); // this will return string value.

// example: for replacing True or False from a string.
TextUtilities.replaceTrueOrFalse(string); // this will return int value.
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
