package com.amit.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by AMIT JANGID on 27/12/2018.
**/
@SuppressWarnings({"ResultOfMethodCallIgnored", "unused", "WeakerAccess"})
public class ImageUtils
{
    private static Matrix matrix;

    private ImageUtils()
    {

    }

    public static File compressImage(File imageFile, int reqWidth,
                              int reqHeight, Bitmap.CompressFormat compressFormat,
                              int quality, String destinationPath) throws IOException
    {
        FileOutputStream fileOutputStream = null;
        File file = new File(destinationPath).getParentFile();

        if (!file.exists())
        {
            file.mkdirs();
        }

        try
        {
            fileOutputStream = new FileOutputStream(destinationPath);
            // write the compressed bitmap at the destination specified by destinationPath.
            decodeSampledBitmapFromFile(imageFile, reqWidth, reqHeight).compress(compressFormat, quality, fileOutputStream);
        }
        finally
        {
            if (fileOutputStream != null)
            {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        return new File(destinationPath);
    }

    public static Bitmap decodeSampledBitmapFromFile(File imageFile, int reqWidth,
                                                     int reqHeight) throws IOException
    {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap scaledBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        rotateImage(imageFile);

        scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                scaledBitmap.getWidth(), scaledBitmap.getHeight(),
                matrix, true);

        return scaledBitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        int inSampleSize = 1;
        final int width = options.outWidth;
        final int height = options.outHeight;

        if (height > reqHeight || width > reqWidth)
        {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth)
            {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void rotateImage(File imageFile) throws IOException
    {
        //check the rotation of the image and display it properly
        ExifInterface exif;
        exif = new ExifInterface(imageFile.getAbsolutePath());
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
        matrix = new Matrix();

        if (orientation == 6)
        {
            matrix.postRotate(90);
        }
        else if (orientation == 3)
        {
            matrix.postRotate(180);
        }
        else if (orientation == 8)
        {
            matrix.postRotate(270);
        }
    }
}
