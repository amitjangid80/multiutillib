package com.amit.img_picker.provider

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.support.v4.app.ActivityCompat
import com.amit.R
import com.amit.img_picker.ImagePickerActivity
import com.amit.img_picker.util.IntentUtils
import com.amit.img_picker.util.PermissionUtil
import com.yalantis.ucrop.util.FileUtils
import java.io.File

/**
 * Created by AMIT JANGID on 18/02/2019.
**/
class GalleryProvider(activity: ImagePickerActivity) : BaseProvider(activity)
{
    companion object
    {
        /**
         * Permission Require for Image Pick, For image pick just storage permission is need but
         * to crop or compress image write permission is also required. as both permission is in
         * same group, we have used write permission here.
        **/
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        private const val GALLERY_INTENT_REQ_CODE = 4261
        private const val PERMISSION_INTENT_REQ_CODE = 4262
    }

    /**
     * Start Gallery Capture Intent
     */
    fun startIntent() {
        checkPermission()
    }

    /**
     * Check Require permission for Picking Gallery Image.
     *
     * If permission is not granted request Permission, Else start gallery Intent
    **/
    private fun checkPermission()
    {
        if (!PermissionUtil.isPermissionGranted(this, REQUIRED_PERMISSIONS))
        {
            ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS, PERMISSION_INTENT_REQ_CODE)
        }
        else
        {
            startGalleryIntent()
        }
    }

    /**
     * Start Gallery Intent
    **/
    private fun startGalleryIntent()
    {
        val galleryIntent = IntentUtils.getGalleryIntent()
        activity.startActivityForResult(galleryIntent, GALLERY_INTENT_REQ_CODE)
    }

    /**
     * Handle Requested Permission Result
    **/
    fun onRequestPermissionsResult(requestCode: Int)
    {
        if (requestCode == PERMISSION_INTENT_REQ_CODE)
        {
            // Check again if permission is granted
            if (PermissionUtil.isPermissionGranted(this, REQUIRED_PERMISSIONS))
            {
                // Permission is granted, Start Camera Intent
                startGalleryIntent()
            }
            else
            {
                // Exit with error message
                setError(getString(R.string.permission_gallery_denied))
            }
        }
    }

    /**
     * Handle Camera Intent Activity Result
     *
     * @param requestCode  It must be {@link CameraProvider#GALLERY_INTENT_REQ_CODE}
     * @param resultCode  For success it should be {@link Activity#RESULT_OK}
     * @param data Result Intent
    **/
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode == GALLERY_INTENT_REQ_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                handleResult(data)
            }
            else
            {
                setResultCancel()
            }
        }
    }

    /**
     * This method will be called when final result fot this provider is enabled.
    **/
    private fun handleResult(data: Intent?)
    {
        val uri = data?.data

        if (uri != null)
        {
            val filePath: String? = FileUtils.getPath(activity, uri)

            if (!filePath.isNullOrEmpty())
            {
                activity.setImage(File(filePath))
            }
            else
            {
                setError(R.string.error_failed_pick_gallery_image)
            }
        }
        else
        {
            setError(R.string.error_failed_pick_gallery_image)
        }
    }
}
