package com.amit.img_picker.provider

import android.content.ContextWrapper
import android.widget.Toast
import com.amit.img_picker.ImagePickerActivity

/**
 * Created by AMIT JANGID on 18/02/2019.
**/
abstract class BaseProvider(protected val activity: ImagePickerActivity) : ContextWrapper(activity)
{
    /**
     * Cancel operation and Set Error Message
     *
     * @param error Error Message
    **/
    protected fun setError(error: String)
    {
        onFailure()
        activity.setError(error)
    }

    /**
     * Cancel operation and Set Error Message
     *
     * @param errorRes Error Message
    **/
    protected fun setError(errorRes: Int)
    {
        setError(getString(errorRes))
    }

    /**
     * Show Short Toast Message
     *
     * @param messageRes String message resource
    **/
    protected fun showToast(messageRes: Int)
    {
        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
    }

    /**
     * Call this method when task is cancel in between the operation.
     * E.g. user hit back-press
    **/
    protected fun setResultCancel()
    {
        onFailure()
        activity.setResultCancel()
    }

    /**
     * This method will be Call on Error, It can be used for clean up Tasks
    **/
    protected open fun onFailure()
    {

    }
}
