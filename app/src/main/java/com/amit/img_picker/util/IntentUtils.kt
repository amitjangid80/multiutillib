package com.amit.img_picker.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.amit.R
import java.io.File

/**
 * Created by AMIT JANGID on 18/02/2019.
**/
object IntentUtils
{
    /**
     * @return Intent Gallery Intent
    **/
    fun getGalleryIntent(): Intent
    {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            //Show Document Intent
            Intent(Intent.ACTION_OPEN_DOCUMENT)
        }
        else
        {
            //Show Gallery Intent, Will open google photos
            Intent(Intent.ACTION_PICK)
        }

        intent.addCategory(Intent.CATEGORY_OPENABLE)

        //Apply filter to show image only in intent
        intent.type = "image/*"
        return intent
    }

    /**
     * @return Intent Camera Intent
    **/
    fun getCameraIntent(context: Context, file: File): Intent?
    {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            val authority = context.packageName + context.getString(R.string.image_picker_provider_authority_suffix)
            val photoURI = FileProvider.getUriForFile(context, authority, file)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        }
        else
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
        }

        return intent
    }
}
