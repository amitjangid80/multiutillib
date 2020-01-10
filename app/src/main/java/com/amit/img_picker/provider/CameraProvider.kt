package com.amit.img_picker.provider

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityCompat
import com.amit.R
import com.amit.img_picker.ImagePickerActivity
import com.amit.img_picker.util.FileUtil
import com.amit.img_picker.util.IntentUtils
import com.amit.permission.PermissionUtil
import com.amit.permission.PermissionUtil.isPermissionGranted
import java.io.File

/**
 * Created by AMIT JANGID on 18/02/2019.
**/
@Suppress("UNUSED_PARAMETER")
class CameraProvider(activity: ImagePickerActivity) : BaseProvider(activity)
{
    companion object
    {
        /**
         * Permission Require for Image Capture using Camera
        **/
        private val REQUIRED_PERMISSIONS = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        private const val CAMERA_INTENT_REQ_CODE = 4281
        private const val PERMISSION_INTENT_REQ_CODE = 4282
    }

    /**
     * Temp Camera File
    **/
    private var mCameraFile: File? = null

    /**
     * Start Camera Capture Intent
    **/
    fun startIntent()
    {
        checkPermission()
    }

    /**
     * Check Require permission for Taking Picture.
     *
     * If permission is not granted request Permission, Else start Camera Intent
    **/
    private fun checkPermission()
    {
        if (!PermissionUtil.isPermissionGranted(this, REQUIRED_PERMISSIONS))
        {
            ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS, PERMISSION_INTENT_REQ_CODE)
        }
        else
        {
            startCameraIntent()
        }
    }

    /**
     * Start Camera Intent
     *
     * Create Temporary File object and Pass it to Camera Intent
    **/
    private fun startCameraIntent()
    {
        // Create and get empty file to store capture image content
        mCameraFile = FileUtil.getCameraFile()

        // Check if file exists
        if (mCameraFile != null && mCameraFile!!.exists())
        {
            val cameraIntent = IntentUtils.getCameraIntent(this, mCameraFile!!)
            activity.startActivityForResult(cameraIntent, CAMERA_INTENT_REQ_CODE)
        }
        else
        {
            setError(R.string.error_failed_to_create_camera_image_file)
        }
    }

    /**
     * Handle Requested Permission Result
    **/
    fun onRequestPermissionsResult(requestCode: Int)
    {
        if (requestCode == PERMISSION_INTENT_REQ_CODE)
        {
            // Check again if permission is granted
            if (isPermissionGranted(this, REQUIRED_PERMISSIONS))
            {
                //Permission is granted, Start Camera Intent
                startCameraIntent()
            }
            else
            {
                //Exit with error message
                val error = getString(R.string.permission_camera_denied)
                setError(error)
            }
        }
    }

    /**
     * Handle Camera Intent Activity Result
     *
     * @param requestCode  It must be {@link CameraProvider#CAMERA_INTENT_REQ_CODE}
     * @param resultCode  For success it should be {@link Activity#RESULT_OK}
     * @param data Result Intent
    **/
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode == CAMERA_INTENT_REQ_CODE)
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
        activity.setImage(mCameraFile!!)
    }

    /**
     * Delete Camera file is exists
    **/
    override fun onFailure()
    {
        mCameraFile?.delete()
    }
}
