package com.amit.utilities;

import android.provider.UserDictionary;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2018 May 04 - Friday - 12:49 PM
 * Created By AMIT JANGID
 *
 * this class will help the validate the strings like
 * email, password, pan number, etc.
**/
@SuppressWarnings("unused")
public class Validator
{
    private UserDictionary userDictionary;

    private static final String TAG = Validator.class.getSimpleName();

    /**
     * Validate email
     * this method will validate the email for entered string
     *
     * @param email - email to be validated
     * @return this will return true or false
    **/
    public static boolean validateEmail(String email)
    {
        try
        {
            String expression = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" +
                                "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +
                                "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." +
                                "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +
                                "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" +
                                "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
        catch (Exception e)
        {
            Log.e(TAG, "validateEmail: exception while validating email:\n");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validate mobile
     * this method will validate for mobile number to be numbers for entered string
     *
     * @param mobile - mobile number to be validated
     * @return this will return true or false
    **/
    public static boolean validateMobile(String mobile)
    {
        try
        {
            return mobile.matches("\\d+");
        }
        catch (Exception e)
        {
            Log.e(TAG, "validateMobile: exception while validating for mobile:\n");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validate pan card
     * this method will validate the pan number for entered string
     *
     * @param panNumber - pan card number to be validated
     * @return this will return true or false
    **/
    public static boolean validatePanCard(String panNumber)
    {
        try
        {
            Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
            Matcher matcher = pattern.matcher(panNumber.trim());
            return matcher.matches();
        }
        catch (Exception e)
        {
            Log.e(TAG, "validatePanCard: exception while validating pan number.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * only numbers
     * this method will validate the entered string to be numeric only
     *
     * @param number - string to be validated for numeric values
     * @return this will return true or false
    **/
    public static boolean onlyNumbers(String number)
    {
        try
        {
            return number.matches("\\d+");
        }
        catch (Exception e)
        {
            Log.e(TAG, "validateMobile: exception while validating for mobile:\n");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * only characters
     * this method will validate the entered string for characters only
     *
     * @param string - string to be validated
     * @return this will return true or false
    **/
    public static boolean onlyCharacters(String string)
    {
        try
        {
            return string.matches("^[A-Za-z]+$");
        }
        catch (Exception e)
        {
            Log.e(TAG, "onlyCharacters: exception while validating for characters:\n");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * at least one lower case
     * this method will check for at least one lower case value
     *
     * @param string - string to be validated
     * @return this will return true or false
     *         if true is returned then string has lower case value
     *         else if false is returned then string has no lower case value
    **/
    public static boolean atLeastOneLowerCase(String string)
    {
        try
        {
            return !string.equals(string.toUpperCase());
        }
        catch (Exception e)
        {
            Log.e(TAG, "atLeastOneLowerCase: exception while validating for at least one lower case.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * at least one upper case
     * this method will check for at least one lower case value
     *
     * @param string - string to be validated
     * @return this will return true or false
     *         if true is returned then string has upper case value
     *         else if false is returned then string has no upper case value
    **/
    public static boolean atLeastOneUpperCase(String string)
    {
        try
        {
            return !string.equals(string.toLowerCase());
        }
        catch (Exception e)
        {
            Log.e(TAG, "atLeastOneUpperCase: exception while validating for at least one upper case.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * at least one number
     * this method will validate the string for at least one number
     *
     * @param string - string to be validated
     * @return this will return true or false
    **/
    public static boolean atLeastOneNumber(String string)
    {
        try
        {
            return string.matches(".*\\d.*");
        }
        catch (Exception e)
        {
            Log.e(TAG, "atLeastOnNumber: exception while validating for at least one number value.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * non empty
     * this method will validate the string for non empty
     *
     * @param string - string to be validated
     * @return this will return true or false
     *         if it returns true then the string is not empty
     *         else if it returns false then the string is empty
    **/
    public static boolean nonEmpty(String string)
    {
        try
        {
            return !string.isEmpty();
        }
        catch (Exception e)
        {
            Log.e(TAG, "nonEmpty: exception while validating non empty string\n");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * starts with non number
     * this method will validate the string for non number starting of string
     *
     * @param string - string to be validated
     * @return this will return true or false
     *         if true then string starts with number
     *         else if false then string doesn't start with number
    **/
    public static boolean startsWithNonNumber(String string)
    {
        try
        {
            return !Character.isDigit(string.charAt(0));
        }
        catch (Exception e)
        {
            Log.e(TAG, "startsWithNonNumber: exception while validating starts with non number:\n");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * no special characters
     * this method will validate a string for no special characters
     *
     * @param string - string to be validated
     * @return this method will return true or false
     *         if it returns true then there are no special characters in the string
     *         else if it returns false then the string has special characters in the string
    **/
    public static boolean noSpecialCharacters(String string)
    {
        try
        {
            return string.matches("[A-Za-z0-9]+");
        }
        catch (Exception e)
        {
            Log.e(TAG, "noSpecialCharacters: exception while validating no special characters\n");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * at least one special characters
     * this method will validate a string for at least one special characters
     *
     * @param string - string to be validated
     * @return this method will return true or false
     *         if it returns true then there are special characters in the string
     *         else if it returns false then the string has no special characters in the string
     **/
    public static boolean atLeastOneSpecialCharacters(String string)
    {
        try
        {
            return !string.matches("[A-Za-z0-9]+");
        }
        catch (Exception e)
        {
            Log.e(TAG, "atLeastOneSpecialCharacters: exception while validating for at least one special characters.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * is valid password
     * this method will check if the password is valid for the following conditions:
     *
     * 1. It should of minimum 8 in length.
     * 2. It should have at least one upper case.
     * 3. It should have at least one lower case.
     * 4. It should have at least one special character.
     * 5. It should have at least one numeric value.
     *
     * @return true or false.
    **/
    private static boolean isValidPassword(String password)
    {
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * password strength method
     * this method determines the strength of the string entered.
     *
     * @return int value with strength of the password.
    **/
    private int passwordStrength(String password)
    {
        int strength = 0;

        if (atLeastOneUpperCase(password))
        {
            strength += 15;
        }

        if (atLeastOneUpperCase(password))
        {
            strength += 15;
        }

        if (atLeastOneSpecialCharacters(password))
        {
            strength += 20;
        }

        if (atLeastOneNumber(password))
        {
            strength += 20;
        }

        if (password.length() >= 8)
        {
            strength += 10;
        }

        if (password.length() > 15)
        {
            strength += 20;
        }

        return strength;
    }

    private boolean spellCheck(String string)
    {
        try
        {
            return UserDictionary.Words.WORD.contains(string);
        }
        catch (Exception e)
        {
            Log.e(TAG, "spellCheck: exception while checking for spelling.");
            e.printStackTrace();
            return false;
        }
    }
}
