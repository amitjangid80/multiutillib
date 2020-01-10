package com.amit.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

/**
 * Created by AMIT JANGID on 17-Sep-18.
 *
 * this class is an progress dialog class which will show progress
 * and text while performing any long operations
 *
 * EX: making many api calls at once.
**/
@SuppressWarnings("unused")
public class CustomProgressBar
{
    private final Context mContext;

    private int currentProgress = 0;
    private int progressBarMaxSize = 100;

    private String titleMessage;
    private String updateMessage;

    private boolean cancelable = true;
    private ProgressDialog progressDialog;

    public CustomProgressBar(@NonNull Context context)
    {
        this.mContext = context;
    }

    public void setProgressBarMaxSize(int progressBarMaxSize)
    {
        this.progressBarMaxSize = progressBarMaxSize;
    }

    public void setCurrentProgress(int currentProgress)
    {
        this.currentProgress = currentProgress;
    }

    public void setUpdateMessage(String updateMessage)
    {
        this.updateMessage = updateMessage;
    }

    public void setTitleMessage(String titleMessage)
    {
        this.titleMessage = titleMessage;
    }

    public void setCancelable(boolean cancelable)
    {
        this.cancelable = cancelable;
    }

    public Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 1:

                    progressDialog = new ProgressDialog(mContext);
                    progressDialog.setTitle(titleMessage);
                    progressDialog.setMessage(updateMessage);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setCancelable(true);
                    progressDialog.setProgress(currentProgress);
                    progressDialog.setMax(progressBarMaxSize);
                    progressDialog.show();

                    break;

                case 2:

                    progressDialog.setTitle(titleMessage);
                    progressDialog.setMessage(updateMessage);
                    progressDialog.setProgress(currentProgress);
                    progressDialog.setMax(progressBarMaxSize);
                    progressDialog.setCancelable(cancelable);

                    break;

                case 3:

                    progressDialog.dismiss();

                    break;

                case 4:

                    break;
            }

            return false;
        }
    });
}
