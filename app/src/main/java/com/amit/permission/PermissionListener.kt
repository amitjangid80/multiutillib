package com.amit.permission

/**
 * Created by AMIT JANGID on 20/02/2019.
**/
interface PermissionListener
{
    fun onGranted(requestCode: Int, permissions: Array<String>)
    fun onDenied(requestCode: Int, permissions: Array<String>)
}