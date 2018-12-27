package com.amit.iv;

import android.content.Context;
import android.graphics.Bitmap;

import com.amit.utilities.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;

/**
 * Created by AMIT JANGID on 27/12/2018.
**/
@SuppressWarnings({"unused", "WeakerAccess"})
public class ImageCompressor
{
    // max width and height values of the compressed image is taken as 612 x 816
    private int mQuality = 80;
    private int mMaxWidth = 612;
    private int mMaxHeight = 816;

    private String mDestinationDirectoryPath;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;

    public ImageCompressor(Context context)
    {
        mDestinationDirectoryPath = context.getCacheDir().getPath() + File.separator + "images";
    }

    public ImageCompressor setMaxWidth(int maxWidth)
    {
        this.mMaxWidth = maxWidth;
        return this;
    }

    public ImageCompressor setMaxHeight(int maxHeight)
    {
        this.mMaxHeight = maxHeight;
        return this;
    }

    public ImageCompressor setCompressFormat(Bitmap.CompressFormat compressFormat)
    {
        this.mCompressFormat = compressFormat;
        return this;
    }

    public ImageCompressor setQuality(int quality)
    {
        this.mQuality = quality;
        return this;
    }

    public ImageCompressor setDestinationDirectoryPath(String destinationDirectoryPath)
    {
        this.mDestinationDirectoryPath = destinationDirectoryPath;
        return this;
    }

    public File compressToFile(File imageFile) throws IOException
    {
        return compressToFile( imageFile, imageFile.getName() );
    }

    public File compressToFile(File imageFile, String compressedFileName) throws IOException
    {
        return ImageUtils.compressImage( imageFile, mMaxWidth, mMaxHeight, mCompressFormat, mQuality,
                mDestinationDirectoryPath + File.separator + compressedFileName );
    }

    public Bitmap compressToBitmap(File imageFile) throws IOException
    {
        return ImageUtils.decodeSampledBitmapFromFile( imageFile, mMaxWidth, mMaxHeight );
    }

    public Flowable<File> compressToFileAsFlowable(final File imageFile)
    {
        return compressToFileAsFlowable( imageFile, imageFile.getName() );
    }

    public Flowable<File> compressToFileAsFlowable(final File imageFile, final String compressedFileName)
    {
        return Flowable.defer((Callable<Flowable<File>>) () ->
        {
            try
            {
                return Flowable.just( compressToFile( imageFile, compressedFileName ) );
            }
            catch (IOException e)
            {
                return Flowable.error( e );
            }
        });
    }

    public Flowable<Bitmap> compressToBitmapAsFlowable(final File imageFile)
    {
        return Flowable.defer((Callable<Flowable<Bitmap>>) () ->
        {
            try
            {
                return Flowable.just( compressToBitmap( imageFile ) );
            }
            catch (IOException e)
            {
                return Flowable.error( e );
            }
        });
    }
}
