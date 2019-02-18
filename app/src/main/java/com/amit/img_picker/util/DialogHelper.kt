package com.amit.img_picker.util

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.amit.R
import com.amit.img_picker.constant.ImageProvider
import com.amit.img_picker.listener.ResultListener
import kotlinx.android.synthetic.main.dialog_choose_app.view.*

/**
 * Created by AMIT JANGID on 18/02/2019.
**/
internal object DialogHelper
{
    /**
     * Show Image Provide Picker Dialog. This will streamline the code to pick/capture image
     **/
    fun showChooseAppDialog(context: Context, listener: ResultListener<ImageProvider>)
    {
        val layoutInflater = LayoutInflater.from(context)
        val customView = layoutInflater.inflate(R.layout.dialog_choose_app, null)

        val dialog = AlertDialog.Builder(context)
                .setTitle(R.string.title_choose_image_provider)
                .setView(customView)
                .setNegativeButton(R.string.action_cancel) { _, _ -> }
                .show()

        //Handle Camera option click
        customView.lytCameraPick.setOnClickListener {
            listener.onResult(ImageProvider.CAMERA)
            dialog.dismiss()
        }

        //Handle Gallery option click
        customView.lytGalleryPick.setOnClickListener {
            listener.onResult(ImageProvider.GALLERY)
            dialog.dismiss()
        }
    }
}
