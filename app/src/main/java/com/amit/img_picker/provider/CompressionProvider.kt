package com.amit.img_picker.provider

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import com.amit.R
import com.amit.img_picker.ImagePicker
import com.amit.img_picker.ImagePickerActivity
import com.amit.img_picker.util.FileUtil
import com.amit.img_picker.util.ImageUtil
import java.io.File

/**
 * Created by AMIT JANGID on 18/02/2019.
**/
class CompressionProvider(activity: ImagePickerActivity) : BaseProvider(activity)
{
    companion object
    {
        private val TAG = CompressionProvider::class.java.simpleName
    }

    private val mMaxWidth: Int
    private val mMaxHeight: Int
    private val mMaxFileSize: Long

    private var mOriginalFile: File? = null

    init
    {
        val bundle = activity.intent.extras!!

        // Get Max Width/Height parameter from Intent
        mMaxWidth = bundle.getInt(ImagePicker.EXTRA_MAX_WIDTH, 0)
        mMaxHeight = bundle.getInt(ImagePicker.EXTRA_MAX_HEIGHT, 0)

        // Get Maximum Allowed file size
        mMaxFileSize = bundle.getLong(ImagePicker.EXTRA_IMAGE_MAX_SIZE, 0)
    }

    /**
     * Check if compression should be enabled or not
     *
     * @return Boolean. True if Compression should be enabled else false.
    **/
    fun isCompressEnabled(): Boolean
    {
        return mMaxFileSize > 0L
    }

    /**
     * Check if compression is required
     * @param file File object to apply Compression
    **/
    fun isCompressionRequired(file: File): Boolean
    {
        val status = isCompressEnabled() && file.length() > mMaxFileSize

        if (!status && mMaxWidth > 0 && mMaxHeight > 0)
        {
            //Check image resolution
            val sizes = getImageSize(file)
            return sizes[0] > mMaxWidth || sizes[1] > mMaxHeight
        }

        return status
    }

    /**
     * Compress given file if enabled.
     *
     * @param file File to compress
    **/
    fun compress(file: File)
    {
        startCompressionWorker(file)
    }

    /**
     * Start Compression in Background
    **/
    @SuppressLint("StaticFieldLeak")
    private fun startCompressionWorker(file: File)
    {
        mOriginalFile = file

        object : AsyncTask<File, Void, File>()
        {
            override fun doInBackground(vararg params: File): File?
            {
                // Perform operation in background
                return startCompression(params[0])
            }

            override fun onPostExecute(file: File?)
            {
                super.onPostExecute(file)

                if (file != null)
                {
                    // Post Result
                    handleResult(file)
                }
                else
                {
                    // Post Error
                    setError(R.string.error_failed_to_compress_image)
                }
            }
        }.execute(file)
    }

    /**
     * Check if compression required, And Apply compression until file size reach below Max Size.
    **/
    private fun startCompression(file: File): File?
    {
        var attempt = 0
        var newFile: File? = null

        do
        {
            // Delete file if exist, fill will be exist in second loop.
            newFile?.delete()
            newFile = applyCompression(file, attempt)

            if (newFile == null)
            {
                return if (attempt > 0)
                {
                    applyCompression(file, attempt - 1)
                }
                else
                {
                    null
                }
            }

            attempt++
        }
        while (isCompressionRequired(newFile!!))

        return newFile
    }

    /**
     * Compress the file
    **/
    private fun applyCompression(file: File, attempt: Int): File?
    {
        val resList = resolutionList()

        if (attempt >= resList.size)
        {
            return null
        }

        // Apply logic to get scaled bitmap resolution.
        val resolution = resList[attempt]
        var maxWidth = resolution[0]
        var maxHeight = resolution[1]

        if (mMaxWidth > 0 && mMaxHeight > 0)
        {
            if (maxWidth > mMaxWidth || maxHeight > mMaxHeight)
            {
                maxHeight = mMaxHeight
                maxWidth = mMaxWidth
            }
        }

        Log.d(TAG, "maxWidth:$maxWidth, maxHeight:$maxHeight")
        var quality = 90

        // Check file format
        var format = Bitmap.CompressFormat.JPEG

        if (file.absolutePath.endsWith(".png"))
        {
            format = Bitmap.CompressFormat.PNG
            quality = 100
        }

        val compressFile: File? = FileUtil.getCameraFile()

        return if (compressFile != null)
        {
            ImageUtil.compressImage(
                    file, maxWidth.toFloat(), maxHeight.toFloat(),
                    format, quality, compressFile.absolutePath
            )
        }
        else
        {
            null
        }
    }

    /**
     * Image Resolution will be reduce with below parameters.
     *
    **/
    private fun resolutionList(): List<Array<Int>>
    {
        return listOf(
                arrayOf(2580, 1944),        //5.0 Megapixel
                arrayOf(2080, 1542),        //3.3 Megapixel
                arrayOf(1600, 1200),        //2.0 Megapixel
                arrayOf(1392, 1024),        //1.3 Megapixel
                arrayOf(640, 480),          //0.3 Megapixel
                arrayOf(320, 240),          //0.15 Megapixel
                arrayOf(160, 120),          //0.08 Megapixel
                arrayOf(80, 60),             //0.04 Megapixel
                arrayOf(40, 30)             //0.02 Megapixel
        )
    }

    /**
     * This method will be called when final result fot this provider is enabled.
    **/
    private fun handleResult(file: File)
    {
        activity.setCompressedImage(file)
    }

    /**
     *
     * @param file File to get Image Size
     * @return Int Array, Index 0 has width and Index 1 has height
    **/
    private fun getImageSize(file: File): Array<Int>
    {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        BitmapFactory.decodeFile(file.absolutePath, options)
        return arrayOf(options.outWidth, options.outHeight)
    }
}
