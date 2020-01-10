package com.amit.img_picker.util

import android.graphics.*
import android.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by AMIT JANGID on 18/02/2019.
**/
@Suppress("DEPRECATION")
object ImageUtil
{
    @Throws(IOException::class)
    fun compressImage(
            imageFile: File,
            reqWidth: Float,
            reqHeight: Float,
            compressFormat: Bitmap.CompressFormat,
            quality: Int,
            destinationPath: String
    ): File
    {
        var fileOutputStream: FileOutputStream? = null
        val file = File(destinationPath).parentFile

        if (!file.exists())
        {
            file.mkdirs()
        }

        try
        {
            fileOutputStream = FileOutputStream(destinationPath)

            // write the compressed bitmap at the destination specified by destinationPath.
            decodeSampledBitmapFromFile(imageFile, reqWidth, reqHeight)!!.compress(
                    compressFormat, quality, fileOutputStream)
        }
        finally
        {
            if (fileOutputStream != null)
            {
                fileOutputStream.flush()
                fileOutputStream.close()
            }
        }

        return File(destinationPath)
    }

    @Throws(IOException::class)
    private fun decodeSampledBitmapFromFile(imageFile: File, reqWidth: Float, reqHeight: Float): Bitmap?
    {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        var bmp: Bitmap? = BitmapFactory.decodeFile(imageFile.absolutePath, options)

        var actualWidth = options.outWidth
        var actualHeight = options.outHeight

        val maxRatio = reqWidth / reqHeight
        var imgRatio = actualWidth.toFloat() / actualHeight.toFloat()

        if (actualHeight > reqHeight || actualWidth > reqWidth)
        {
            // if Height is greater
            if (imgRatio < maxRatio)
            {
                actualHeight = reqHeight.toInt()
                imgRatio = reqHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
            }
            // else if Width is greater
            else if (imgRatio > maxRatio)
            {
                actualWidth = reqWidth.toInt()
                imgRatio = reqWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
            }
            else
            {
                actualWidth = reqWidth.toInt()
                actualHeight = reqHeight.toInt()
            }
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        options.inDither = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        try
        {
            bmp = BitmapFactory.decodeFile(imageFile.absolutePath, options)
        }
        catch (e: OutOfMemoryError)
        {
            e.printStackTrace()
        }

        var scaledBitmap: Bitmap? = null

        try
        {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

        val scaleMatrix = Matrix()
        val canvas = Canvas(scaledBitmap!!)

        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()

        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        canvas.setMatrix(scaleMatrix)
        // canvas.matrix = scaleMatrix

        canvas.drawBitmap(bmp!!, middleX - bmp.width / 2,
                middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))

        bmp.recycle()
        val exif: ExifInterface

        try
        {
            exif = ExifInterface(imageFile.absolutePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()

            if (orientation == 6)
            {
                matrix.postRotate(90f)
            }
            else if (orientation == 3)
            {
                matrix.postRotate(180f)
            }
            else if (orientation == 8)
            {
                matrix.postRotate(270f)
            }

            scaledBitmap = Bitmap.createBitmap(
                    scaledBitmap, 0, 0, scaledBitmap.width,
                    scaledBitmap.height, matrix, true)
        }
        catch (e: IOException)
        {
            e.printStackTrace()
        }

        return scaledBitmap
    }

    private fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int,
            reqHeight: Int): Int
    {
        var inSampleSize = 1

        // Raw height and width of image
        val width = options.outWidth
        val height = options.outHeight

        if (height > reqHeight || width > reqWidth)
        {
            inSampleSize *= 2

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth)
            {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}
