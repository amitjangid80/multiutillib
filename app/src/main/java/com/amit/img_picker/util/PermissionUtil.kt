package com.amit.img_picker.util

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

/**
 * Created by AMIT JANGID on 18/02/2019.
**/
object PermissionUtil
{
    /**
     * Check if Camera Permission is granted
     *
     * @return true if specified permission is granted
    **/
    private fun hasPermission(context: Context, permission: String): Boolean
    {
        val selfPermission = ContextCompat.checkSelfPermission(context, permission)
        return selfPermission == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Check if Specified Permissions are granted or not. If single permission is denied then
     * function will return false.
     *
     * @param context Application Context
     * @param permissions Array of Permission to Check
     *
     * @return true if all specified permission is granted
    **/
    fun isPermissionGranted(context: Context, permissions: Array<String>): Boolean
    {
        return permissions.filter { hasPermission(context, it) }.size == permissions.size
    }
}
