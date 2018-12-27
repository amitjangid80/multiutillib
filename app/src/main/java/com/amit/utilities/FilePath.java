package com.amit.utilities;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by AmitS on 15-05-2017.
**/
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class FilePath
{
    private static final String TAG = FilePath.class.getSimpleName();
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static final int EOF = -1;

    public static File from(Context context, Uri uri) throws IOException
    {
        String fileName = getFileName(context, uri);
        String[] splitName = splitFileName(fileName);

        File tempFile = File.createTempFile(splitName[0], splitName[1]);
        tempFile = rename(tempFile, fileName);
        tempFile.deleteOnExit();

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = context.getContentResolver().openInputStream(uri);

        try
        {
            fileOutputStream = new FileOutputStream(tempFile);
        }
        catch (Exception e)
        {
            Log.e(TAG, "from: exception while getting file:\n");
            e.printStackTrace();
        }

        if (inputStream != null)
        {
            copy(inputStream, fileOutputStream);
            inputStream.close();
        }

        if (fileOutputStream != null)
        {
            fileOutputStream.close();
        }

        return tempFile;
    }

    private static String[] splitFileName(String fileName)
    {
        String name = fileName;
        String extension = "";

        int i = fileName.lastIndexOf(".");

        if (i != -1)
        {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }

        return new String[]{name, extension};
    }

    private static String getFileName(Context context, Uri uri)
    {
        String result = null;

        if (uri.getScheme().equalsIgnoreCase("content"))
        {

            try (Cursor cursor = context.getContentResolver().query(
                    uri, null, null, null, null))
            {
                if (cursor != null && cursor.moveToFirst())
                {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
            catch (Exception e)
            {
                Log.e(TAG, "getFileName: exception while getting file name:\n");
                e.printStackTrace();
            }
        }

        if (result == null)
        {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);

            if (cut != -1)
            {
                result = result.substring(cut + 1);
            }
        }

        return result;
    }

    private static File rename(File file, String newName)
    {
        File newFile = new File(file.getParent(), newName);

        if (!newFile.equals(file))
        {
            if (newFile.exists() && newFile.delete())
            {
                Log.e(TAG, "rename: Deleted old + " + newName + " file");
            }

            if (file.renameTo(newFile))
            {
                Log.e(TAG, "rename: Renamed file to " + newFile);
            }
        }

        return newFile;
    }

    private static long copy(InputStream inputStream, OutputStream outputStream) throws IOException
    {
        int n;
        long count = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

        while (EOF != (n = inputStream.read(buffer)))
        {
            outputStream.write(buffer, 0, n);
            count += n;
        }

        return count;
    }

    /**
     * Method for return file path of Gallery image/ Document / Video / Audio
     *
     * @param context   - context of the application or class
     * @param uri       - uri to get the path
     * @return          - path of the selected image file from gallery
    **/
    public static String getPath(final Context context, final Uri uri)
    {
        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri))
        {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type))
                {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri))
            {
                final String id = DocumentsContract.getDocumentId(uri);

                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;

                if ("image".equals(type))
                {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("video".equals(type))
                {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("audio".equals(type))
                {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme()))
        {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme()))
        {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context           - The context.
     * @param uri               - The Uri to query.
     * @param selection         - (Optional) Filter used in the query.
     * @param selectionArgs     - (Optional) Selection arguments used in the query.
     * @return                  - The value of the _data column, which is typically a file path.
    **/
    private static String getDataColumn(Context context, Uri uri,
                                        String selection, String[] selectionArgs)
    {
        final String column = "_data";
        final String[] projection = { column };

        try (Cursor cursor = context.getContentResolver().query(uri, projection,
                selection, selectionArgs, null))
        {
            if (cursor != null && cursor.moveToFirst())
            {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }

        return null;
    }

    /**
     * @param uri      - The Uri to check.
     * @return         - Whether the Uri authority is ExternalStorageProvider.
    **/
    private static boolean isExternalStorageDocument(Uri uri)
    {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri       - The Uri to check.
     * @return          - Whether the Uri authority is DownloadsProvider.
    **/
    private static boolean isDownloadsDocument(Uri uri)
    {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri       - The Uri to check.
     * @return          - Whether the Uri authority is MediaProvider.
    **/
    private static boolean isMediaDocument(Uri uri)
    {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri       - The Uri to check.
     * @return          - Whether the Uri authority is Google Photos.
    **/
    private static boolean isGooglePhotosUri(Uri uri)
    {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
