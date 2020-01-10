package com.amit.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Created by AMIT JANGID on 20/02/2019.
 *
 * this class will help users in requesting for permissions
**/
@Suppress("unused", "MemberVisibilityCanBePrivate")
class PermissionHelper
{
    // Companion object is for compatibility between java and kotlin
    companion object
    {
        /**
         * get all permissions method
         *
         * this method will get all permissions mentioned in 'Manifest.xml' file
        **/
        private fun getAllPermissions(context: Context) : Array<String>
        {
            return context.packageManager.getPackageInfo(
                    context.packageName, PackageManager.GET_PERMISSIONS)
                    .requestedPermissions
        }

        /**
         * request all permissions method
         *
         * this method will request for all permissions mentioned in 'Manifest.xml' file
        **/
        fun requestAllPermissions(activity: Activity, requestCode: Int)
        {
            val permissions = getAllPermissions(activity)
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
        }

        /**
         * request permission method
         *
         * this method will handle single permission request
        **/
        fun requestPermission(activity: Activity, requestCode: Int, permission: String)
        {
            val permissions = arrayOf(permission)
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
        }

        /**
         * request permission method
         *
         * this method will handle multiple permissions added in string array
        **/
        fun requestPermission(activity: Activity, requestCode: Int, permissions: Array<String>)
        {
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
        }

        /**
         * get permissions not granted method
         *
         * this method will get all the permissions not granted by the end user
        **/
        fun getPermissionsNotGranted(activity: Activity, permissions: Array<String>): Array<String>
        {
            val list = ArrayList<String>()

            for (permission in permissions)
            {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
                {
                    list.add(permission)
                }
            }

            return list.toTypedArray()
        }

        /**
         * get permissions granted method
         *
         * this method will get all the permissions granted by the end user
        **/
        fun getPermissionsGranted(activity: Activity, permissions: Array<String>): Array<String>
        {
            val list = ArrayList<String>()

            for (p in permissions)
            {
                if (ActivityCompat.checkSelfPermission(activity, p) == PackageManager.PERMISSION_GRANTED)
                {
                    list.add(p)
                }
            }

            return list.toTypedArray()
        }

        /**
         * parse permissions method
        **/
        fun parsePermissions(activity: Activity, requestCode: Int, permissions: Array<String>, listener: PermissionListener)
        {
            listener.onGranted(requestCode, getPermissionsGranted(activity, permissions))
            listener.onDenied(requestCode, getPermissionsNotGranted(activity, permissions))
        }
    }
}
