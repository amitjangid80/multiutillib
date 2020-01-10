package com.amit.views;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by AMIT JANGID on 29-Oct-18.
**/
@Deprecated
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{
    private static final String TAG = TimePickerFragment.class.getSimpleName();

    private static Calendar calendar;
    private OnTimeSelected onTimeSelected;
    private OnTimeSelectedListener mOnTimeSelectedListener;

    private static int year, month, day;
    private static boolean mIs24HourView;
    private static String mSelectedTimeFormat;
    
    /**
     * 2018 October 24 - Wednesday - 03:34 PM
     * show time picker dialog method
     *
     * this method will show the time picker dialog fragment
     *
     * @param context               - context of the application
     * @param timeSelected          - interface of time picker fragment for getting the selected time value
     * @param selectedTimeFormat    - format in which you want the time.
     *                                Example: hh:mm
    **/
    public void showTimePickerDialog(Context context, OnTimeSelected timeSelected,
                                     String selectedTimeFormat, boolean is24HourView)
    {
        mIs24HourView = is24HourView;
        mSelectedTimeFormat = selectedTimeFormat;

        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.initializeInterface(timeSelected);
        timePickerFragment.show(((Activity) context).getFragmentManager(), "TimePicker");
    }
    
    /**
     * 2018 October 24 - Wednesday - 03:34 PM
     * show time picker dialog method
     *
     * this method will show the time picker dialog fragment
     *
     * @param context                           - context of the application
     * @param onTimeSelectedListener            - interface of time picker fragment for getting the selected time value
     * @param selectedTimeFormat                - format in which you want the time.
     *                                            Example: hh:mm
    **/
    public void showTimePickerDialog(Context context, OnTimeSelectedListener onTimeSelectedListener,
                                     String selectedTimeFormat, boolean is24HourView)
    {
        mIs24HourView = is24HourView;
        mSelectedTimeFormat = selectedTimeFormat;
        
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.initializeInterface(onTimeSelectedListener);
        timePickerFragment.show(((Activity) context).getFragmentManager(), "TimePicker");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, mIs24HourView);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        calendar.set(year, month, day, hourOfDay, minute);
        SimpleDateFormat sdf = new SimpleDateFormat(mSelectedTimeFormat, Locale.getDefault());

        String selectedTime = sdf.format(calendar.getTime());
        Log.e(TAG, "onTimeSet: selected time is: " + selectedTime);

        if (onTimeSelected != null)
        {
            onTimeSelected.selectedTime(selectedTime);
        }
        
        if (mOnTimeSelectedListener != null)
        {
            mOnTimeSelectedListener.onTimeSelected(selectedTime);
        }
    }

    private void initializeInterface(OnTimeSelected timeSelected)
    {
        this.onTimeSelected = timeSelected;
    }
    
    private void initializeInterface(OnTimeSelectedListener onTimeSelectedListener)
    {
        this.mOnTimeSelectedListener = onTimeSelectedListener;
    }

    @Deprecated
    public interface OnTimeSelected
    {
        void selectedTime(String selectedTime);
    }
    
    public interface OnTimeSelectedListener
    {
        void onTimeSelected(String selectedTime);
    }
}
